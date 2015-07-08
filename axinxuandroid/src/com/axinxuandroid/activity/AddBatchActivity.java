package com.axinxuandroid.activity;

import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.SlipButton;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddBatchActivity extends NcpZsActivity{
    private CommonTopView topview;
    private RelativeLayout selectvilleage;
    private RelativeLayout selectvariety;
    private TextView villeage_name_text;
    private TextView variety_name_text;
     private SlipButton statusbtn,stagebtn;
    private BatchService bservice;
    private ProgressDialog progress;
    public static final int RETURN_FROM_SELECT_VILLEAGE = 1;
    public static final int RETURN_FROM_SELECT_VARIETY = 2;
    private int villeageid=-1;
    private int varietyid=-1;
    private UserService uservice;
    private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.addbatch);
 		topview=(CommonTopView) this.findViewById(R.id.addbatch_topview);
 		selectvilleage=(RelativeLayout) this.findViewById(R.id.addbatch_selectvilleage);
 		selectvariety=(RelativeLayout) this.findViewById(R.id.addbatch_selectvariety);
 		villeage_name_text=(TextView) this.findViewById(R.id.addbatch_villeagename);
 		variety_name_text=(TextView) this.findViewById(R.id.addbatch_varietyname);
 		statusbtn=(SlipButton) this.findViewById(R.id.addbatch_status);
 		stagebtn=(SlipButton) this.findViewById(R.id.addbatch_stage);
  		bservice=new BatchService();
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		stagebtn.setCheck(true);
  		if(user!=null){
 			 List<Villeage> villeages=user.getVilleages();
 			 if(villeages!=null&&villeages.size()>0){
 				Villeage fvilleage=villeages.get(0);
 				villeageid=fvilleage.getVilleage_id();
 				villeage_name_text.setText(fvilleage.getVilleage_name());
 			 }
 		} 
   		selectvilleage.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent inte=new Intent(AddBatchActivity.this,SelectVilleageActivity.class);
 				startActivityForResult(inte, RETURN_FROM_SELECT_VILLEAGE);
			}
		});
  		selectvariety.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(villeageid==-1){
 					NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 					handler.excuteMethod(new MessageDialogHandlerMethod("","请先选择农场"));
 				}else{
 					Intent inte=new Intent(AddBatchActivity.this,SelectVarietyActivity.class);
 	 				inte.putExtra("villeageid", villeageid);
 	 				startActivityForResult(inte, RETURN_FROM_SELECT_VARIETY);
 				}
 				
			}
		});
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				AddBatchActivity.this.finish();
			}
		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				prepareSave();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RETURN_FROM_SELECT_VILLEAGE:// 处理添加标签返回的数据
			dealSelectVilleage(resultCode, data);
			break;
		case RETURN_FROM_SELECT_VARIETY: 
			dealSelectVariety(resultCode, data);
			break;
  		}
 	}

   	private void dealSelectVilleage( int resultCode, Intent data){
   		if(resultCode==RESULT_OK){
	        String villeage_name = data.getStringExtra("villeage_name");
	        villeageid = data.getIntExtra("villeage_id",-1);
 		    villeage_name_text.setText(villeage_name);
  		}
   	}
   	private void dealSelectVariety( int resultCode, Intent data){
   		if(resultCode==RESULT_OK){
	        String villeage_name = data.getStringExtra("variety_name");
	        varietyid = data.getIntExtra("variety_id",-1);
	        variety_name_text.setText(villeage_name);
  		}
   	}
   	
   	private void prepareSave(){
   		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
   		if(villeageid==-1){
   			handler.excuteMethod(new MessageDialogHandlerMethod("","请选择农场！"));
   		}else if(varietyid==-1){
   			handler.excuteMethod(new MessageDialogHandlerMethod("","请选择品种！"));
   		}else{
   			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
   	 			@Override
   				public void onHandlerFinish(Object result) {
   	 				if(result!=null)
   	 					progress=(ProgressDialog) ((Map)result).get("process");
   	 			    startSave();
   				}
   			});
   	   		handler.excuteMethod(new ProcessDialogHandlerMethod("","保存中..."));
   		}
   }
   private void startSave(){
	   int staus=Batch.Status.BATCH_STATUS_OPEN;
	   if(statusbtn.getPosition()==SlipButton.RIGHT_POSITION){
		   staus=Batch.Status.BATCH_STATUS_HIDDEN;
	   }
	   int stage=Batch.Stage.BATCH_STAGE_SALE;
	   if(stagebtn.getPosition()==SlipButton.RIGHT_POSITION){
		   stage=Batch.Stage.BATCH_STAGE_PRODUCE;
	   }
	   AddBatchThread th=new AddBatchThread(villeageid, varietyid,stage,staus,user);
	   th.setLiserner(new NetFinishListener() {
 		 
 		@Override
		public void onfinish(NetResult data) {
  			NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 			if(data.result==NetResult.RESULT_OF_SUCCESS){
  				Batch batch=(Batch) data.returnData;
 				bservice.saveOrUpdateBatch(batch);
  			}else{
 				handler.excuteMethod(new MessageDialogHandlerMethod("",data.message));
 			}
 			saveFinish();
		}
	   });
	  th.start();
   }
   private void saveFinish(){
	   NcpzsHandler handler=Gloable.getInstance().getCurHandler();
	   handler.post(new Runnable() {
 		@Override
		public void run() {
 			if(progress!=null)
 				progress.dismiss();
 			NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					AddBatchActivity.this.finish();
				}
			});
 			handler.excuteMethod(new MessageDialogHandlerMethod("","保存成功!"));
		}
	   });
   }

 
}
