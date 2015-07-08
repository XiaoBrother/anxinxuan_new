package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveRecordThread;
import com.axinxuandroid.activity.net.UploadNetMediaThread;
import com.axinxuandroid.activity.net.UploadVideoThread;
import com.axinxuandroid.activity.net.ValidSecurityCodeResultThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.DragListView;
import com.axinxuandroid.activity.view.EditTabelView;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.TimeLineView;
import com.axinxuandroid.activity.view.DragListView.DragListener;
import com.axinxuandroid.activity.view.ResourcesView.Resource;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.SystemSet;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.SystemSet.SystemSetType;
import com.axinxuandroid.data.SystemSet.SystemSetType.CommonType;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SystemSetService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.UploadRecordManager;
import com.axinxuandroid.sys.gloable.UploadRecordManager.OnRecordCompleteUpload;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.NetUtil;
import com.zxing.activity.CaptureActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ValidSecurityCodeResultActivity extends NcpZsActivity{

   private ProgressDialog progress;
   private TextView qcounttxt,qinfotxt;
   private EditTabelView tableview;
   private Button contbtn,qzsbtn;
   private String vscode,safecode;
   private String batchcode;
   private CommonTopView topview;
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.valid_securitycode_result);
 		qcounttxt=(TextView) this.findViewById(R.id.valid_sd_result_qcount);
 		qinfotxt=(TextView) this.findViewById(R.id.valid_sd_result_qinfo);
  		contbtn=(Button) this.findViewById(R.id.valid_sd_result_cont);
  		qzsbtn=(Button) this.findViewById(R.id.valid_sd_result_qzs);
  		tableview=(EditTabelView) this.findViewById(R.id.valid_sd_result_edittable);
  		tableview.setReadOnly();
  		topview=(CommonTopView) this.findViewById(R.id.valid_s_result_topview);
  		vscode=getIntent().getStringExtra("vscode");
  		safecode=getIntent().getStringExtra("safecode");
  		contbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				Intent openCameraIntent = new Intent(ValidSecurityCodeResultActivity.this,
 						CaptureActivity.class);
 				startActivity(openCameraIntent);
			}
		});
  		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				Intent openCameraIntent = new Intent(ValidSecurityCodeResultActivity.this,
 						ScanCodeActivity.class);
 				startActivity(openCameraIntent);
			}
		});
  		//批次时间轴
  		qzsbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				if(batchcode!=null){
 					Intent totime = new Intent(ValidSecurityCodeResultActivity.this,TimelineActivity.class);
 					totime.putExtra("id", batchcode);
 	 	 			startActivity(totime);
 				}else{
 					NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 					hand.excuteMethod(new MessageDialogHandlerMethod("","没有对应的追溯信息!"));
 				}
			}
		});
  		prepareLoad();
	}

  	private void prepareLoad(){
  		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  		hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			
			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
					progress=(ProgressDialog) ((Map)result).get("process");
 				validCode();
			}
		});
  		hand.excuteMethod(new ProcessDialogHandlerMethod("","正在查询...."));
  	}
	 
  	private void validCode(){
  		ValidSecurityCodeResultThread th=new ValidSecurityCodeResultThread(vscode, safecode);
  		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
 				updateView(data);
 				LogUtil.logInfo(ValidSecurityCodeResultActivity.class,"finish" );
			}
		});
  		th.start();
  	}
  	private void updateView(final NetResult data){
  		final int result=data.result;
  		final NcpzsHandler hand=Gloable.getInstance().getCurHandler();
   		hand.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				if(result==NetResult.RESULT_OF_SUCCESS){
 					try{
 						//获取code信息
 	 					Map rdata=(Map) data.returnData;
 	  					if(rdata.containsKey("scode")){
 	  						JSONObject scode=(JSONObject) rdata.get("scode");
 	  						int qcount=scode.getInt("querycount");
 	  						qcounttxt.setText("本商品已被查询"+qcount+"次");
 	  						if(qcount<2){
 	  							qinfotxt.setText("您查询的商品为正品，请放心使用!");
 	  						}else qinfotxt.setText("您查询的商品已经被查询过!");
 	  					}
 	  					if(rdata.containsKey("batch")){
 	  						JSONObject batch=(JSONObject) rdata.get("batch");
 	  						if(batch.has("batch_code"))
 	  						  batchcode=batch.getString("batch_code");
 	  					}
 	  					if(rdata.containsKey("keyarr")){
  	  						JSONArray  keyarr=(JSONArray) rdata.get("keyarr");
  	  						if(keyarr!=null){
  	  						   JSONObject obj=null;
   	  						   for(int i=0;i<keyarr.length();i++){
  	  							obj=keyarr.getJSONObject(i);
  	  							tableview.addItem(i, obj.getString("key"),obj.getString("value"));
  	  						   }
  	  						 ((View)tableview.getParent()).setVisibility(View.VISIBLE);
  	  						}
  	  					}
 					}catch(Exception ex){
 						ex.printStackTrace();
 					}
 					
 				}else{
 					qcounttxt.setText("防伪码查询失败");
 					hand.excuteMethod(new MessageDialogHandlerMethod("",data.message));
 				}
			}
		});
  		 
  	}
}
