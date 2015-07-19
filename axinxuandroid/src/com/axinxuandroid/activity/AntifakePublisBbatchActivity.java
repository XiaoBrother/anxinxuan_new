package com.axinxuandroid.activity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.AntifakeBatchListActivity.AntiFakeBatchListAdapter;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveAntiFakePublishBatchThread;
import com.axinxuandroid.activity.view.AntifakeView;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.AntiFakePublishBatch;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.AntiFakePublishBatchService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DateTimePickDialogUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AntifakePublisBbatchActivity extends NcpZsActivity{
    private CommonTopView topview;
    private RelativeLayout selectvariety;
    private RelativeLayout selectbatch;
    private TextView batch_name_text,variety_name_text;
    private EditText antifake_batchtime,antifake_tcount,antifake_bdesc;
    private ProgressDialog progress;
    private AntifakeView bottomview;
    public static final int RETURN_FROM_SELECT_BATCH = 1;
    public static final int RETURN_FROM_SELECT_VARIETY = 2;
    public static final int RETURN_FROM_SELECT_WITHOUT_TYPE = 3;
    private Integer batchid=-1;
    private Integer villeageid=-1;
    private Integer varietyid=-1;
    private UserService uservice;
	private AntiFakePublishBatchService publiccbatchservier;
    private User user;
    private Villeage vil;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.antifake_publishbatch);
 		topview=(CommonTopView) this.findViewById(R.id.antifake_publishbath_top);
 		selectbatch=(RelativeLayout) this.findViewById(R.id.antifake_selectbatch);
 		selectvariety=(RelativeLayout) this.findViewById(R.id.antifake_selectvariety);
 		variety_name_text=(TextView) this.findViewById(R.id.antifake_varietyname);
 		batch_name_text=(TextView) this.findViewById(R.id.antifake_batch);
 		antifake_batchtime=(EditText) this.findViewById(R.id.antifake_batchtime);
 		antifake_tcount=(EditText) this.findViewById(R.id.antifake_tcount);
 		antifake_bdesc=(EditText) this.findViewById(R.id.antifake_bdesc);
 
 		 bottomview=(AntifakeView) this.findViewById(R.id.antifake_Publicbatch_bottom);
         bottomview.setCurrentItem(AntifakeView.BOTTOM_ITEM_INDEX_OF_PUBLICBATCH);
        Format format = new SimpleDateFormat("yyyy-MM-dd");
        antifake_batchtime.setText(format.format(new Date()));
 		
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		
 		
 		vil=uservice.getUserCreateVilleage(user.getUser_id());
 		
  		selectvariety.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 					Intent inte=new Intent(AntifakePublisBbatchActivity.this,SelectVarietyActivity.class);
 		 			 if(vil!=null){
 		 						inte.putExtra("villeageid", vil.getVilleage_id());
 		 	 	 				startActivityForResult(inte, RETURN_FROM_SELECT_VARIETY);
 		 			 }
 				
 				
			}
		});
  		
 		selectbatch.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(varietyid==-1){
 					NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 					handler.excuteMethod(new MessageDialogHandlerMethod("","请先选择品种"));
 				}else{
 					Intent inte=new Intent(AntifakePublisBbatchActivity.this,SelectBatchActivity.class);
 					inte.putExtra("varietyid", varietyid);
 					inte.putExtra("withOutType", RETURN_FROM_SELECT_WITHOUT_TYPE);
 					startActivityForResult(inte, RETURN_FROM_SELECT_BATCH);
 				}
			}
		});
 		antifake_batchtime.setOnClickListener(new OnClickListener() {  
             public void onClick(View v) {  
                 DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(  
                		 AntifakePublisBbatchActivity.this, antifake_batchtime.getText().toString());  
                 dateTimePicKDialog.dateTimePicKDialog(antifake_batchtime);  
             }  
         });  
 		antifake_batchtime.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                           setbdesc();   
                        }
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							// TODO Auto-generated method stub
							
						}
		});

 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				AntifakePublisBbatchActivity.this.finish();
			}
		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				prepareSave();
			}
		});
 		
 		setbdesc();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RETURN_FROM_SELECT_BATCH:// 处理添加标签返回的数据
			dealSelectBacth(resultCode, data);
			break;
		case RETURN_FROM_SELECT_VARIETY: 
			dealSelectVariety(resultCode, data);
			break;
  		}
 	}

   	private void dealSelectBacth( int resultCode, Intent data){
   		if(resultCode==RESULT_OK){
	        String batch_code = data.getStringExtra("trace_code");
	        String bid = data.getStringExtra("batch_id");
	        
	        batchid = Integer.parseInt(bid);
 		    batch_name_text.setText(batch_code);
  		}
   	}
   	private void dealSelectVariety( int resultCode, Intent data){
   		if(resultCode==RESULT_OK){
	        String villeage_name = data.getStringExtra("variety_name");
	        varietyid = data.getIntExtra("variety_id",-1);
	        variety_name_text.setText(villeage_name);
	        setbdesc();
  		}
   	}
   	
   	private void prepareSave(){
 	   if(check()){	
   		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
   			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
   	 			@Override
   				public void onHandlerFinish(Object result) {
   	 				if(result!=null)
   	 					progress=(ProgressDialog) ((Map)result).get("process");
   	 			    startPublishBatch();
   				}
   			});
   	   		handler.excuteMethod(new ProcessDialogHandlerMethod("","保存中..."));
 	   }
   }
   private void startPublishBatch(){
	 AntiFakePublishBatch pbatch=new AntiFakePublishBatch() ;
	   String tcount=   antifake_tcount.getText().toString();
			   	pbatch.setBatchid(this.batchid);
			   	pbatch.setVariety_id(this.varietyid);
				pbatch.setUser_id(this.user.getUser_id());
				pbatch.setBatchtime(antifake_batchtime.getText().toString());
				pbatch.setBdesc(antifake_bdesc.getText().toString());
				pbatch.setTcount( Integer.parseInt(tcount));
				
				
				SaveAntiFakePublishBatchThread resetth=new SaveAntiFakePublishBatchThread(pbatch);
				resetth.setLiserner(new NetFinishListener() {
					@Override
					public void onfinish(NetResult data) {
		  				NcpzsHandler handler = Gloable.getInstance().getCurHandler();
						if (data.result == NetResult.RESULT_OF_SUCCESS) {
							publiccbatchservier =new AntiFakePublishBatchService();
							AntiFakePublishBatch pbatch = (AntiFakePublishBatch) data.returnData;
							publiccbatchservier.saveOrUpdateBatch(pbatch);
							handler.excuteMethod(new MessageDialogHandlerMethod("","保存成功！"));
							
							Intent intent = new Intent (AntifakePublisBbatchActivity.this,AntifakeBatchListActivity.class);	
		 			  		startActivity(intent);
		 					AntifakePublisBbatchActivity.this.finish();
						}else{
							handler.excuteMethod(new MessageDialogHandlerMethod("","保存失败！"));
						}
						//saveFinish();		
						}
				});
				resetth.start();
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
 					Intent intent = new Intent (AntifakePublisBbatchActivity.this,AntifakeBatchListActivity.class);	
 			  		startActivity(intent);
 					AntifakePublisBbatchActivity.this.finish();
				}
			});
 			handler.excuteMethod(new MessageDialogHandlerMethod("","保存成功!"));
		}
	   });
   }
   private void setbdesc(){
	   antifake_bdesc.setText("商品名称:"+variety_name_text.getText().toString()+"<br/>\n包装日期:"+antifake_batchtime.getText().toString()+"<br/>\n生产商:"+vil.getVilleage_name());
   }
   
	private boolean check(){
		
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		
		String  tcount,bdesc;
		tcount=antifake_tcount.getText().toString();
		bdesc=antifake_bdesc.getText().toString();
		if("".equals(tcount.trim())){
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请填写正确的生成数量!"));
			return false;
		}else {
			if(Integer.parseInt(tcount)<=0){
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请填写正确的生成数量!"));
			return false;
			}
		}
		if (("".equals(bdesc.trim()))) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示",	"请输入详细描述!"));
			return false;
		}
		return true;
	}
 

 
}
