package com.axinxuandroid.activity;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.UpdateBatchStageThread;
import com.axinxuandroid.activity.view.BatchListView;
import com.axinxuandroid.activity.view.CommonBottomView;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.BatchListView.BatchItemListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.EANValidate;
import com.zxing.activity.CaptureActivity;
 /**
 * 入口：扫码页，更多页,编辑记录
 * 出口：添加批次，时间轴，选择批次标签,扫码，更多，编辑记录
 * @author hubobo
 *
 */
public class BatchManageActivity extends NcpZsActivity{
 	private LinearLayout produre;//生产按钮
	private LinearLayout sale;//销售按钮
	private LinearLayout saleout;//售罄按钮
	private BatchListView blistview;
	private CommonTopView topview;
	private ProgressDialog processDialog;
	private BatchService batchservice;
	private UserService uservice;
	private User user;
	private int stage=-1;
	private List<Villeage> uservilleages;
	private Villeage selectvilleage;
	private CommonBottomView bottomview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.batchlistmanager);
	    topview=(CommonTopView) this.findViewById(R.id.batchlistmanager_topview);
        produre= (LinearLayout) this.findViewById(R.id.batchlistmanager_produre);
        sale= (LinearLayout) this.findViewById(R.id.batchlistmanager_sale);
        saleout= (LinearLayout) this.findViewById(R.id.batchlistmanager_saleout);
        blistview=(BatchListView) this.findViewById(R.id.batchlistmanager_listview);
        bottomview=(CommonBottomView) this.findViewById(R.id.batchlistmanager_bottom);
        bottomview.setCurrentItem(CommonBottomView.BOTTOM_ITEM_INDEX_OF_BATCHMANAGE);
        batchservice=new BatchService();
        uservice=new UserService();
        user=uservice.getLastLoginUser();
        if (user != null) {
			if (user.getVilleages() != null) {
				uservilleages= user.getVilleages();
				selectvilleage=uservilleages.get(0);
				topview.setSubTitle(selectvilleage.getVilleage_name());
  			}
		}
        produre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	loadProcedure(true);
            }
        });
         sale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	loadSale(true);
            }
        });
         saleout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	loadSaleout(true);
            }
        });
        topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent openCameraIntent = new Intent(BatchManageActivity.this,
 						CaptureActivity.class);
 				startActivity(openCameraIntent);
			}
		});
        blistview.setBatchItemClickListener(new BatchItemListener() {
  			@Override
 			public void onClickBatchItem(final Batch bath) {
  				if(bath.getConsumercount()+bath.getProducecount()==0){
  					NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
						public void onHandlerFinish(Object result) {
 							int sel=(Integer)((Map)result).get("result");
 							if(sel==1){
 								toEditRecord(bath);
 							} 
						}
					});
  					handler.excuteMethod(new ConfirmDialogHandlerMethod("", "该批次没有记录信息，是否创建新记录？"));
  				}else{
  					Intent openCameraIntent = new Intent(BatchManageActivity.this,TimelineActivity.class);
  	   				openCameraIntent.putExtra("id", bath.getCode());
  	   				startActivity(openCameraIntent);
  				}
   			}

			@Override
			public void onMoveBatchItem(final Batch batch, float x, float y) {
				produre.getChildAt(1).setVisibility(View.GONE);
				sale.getChildAt(1).setVisibility(View.GONE);
				saleout.getChildAt(1).setVisibility(View.GONE);
				if(batch==null) return ;
   			    int movestage=-1;
  				int[] location = new int[2];
  				String stagename="";
  				produre.getLocationOnScreen(location);
   				if(x>location[0]&&y>location[1]&&x<(location[0]+produre.getMeasuredWidth())&&y<(location[1]+produre.getMeasuredHeight())){
   					movestage=Batch.Stage.BATCH_STAGE_PRODUCE;
   					stagename="生产";
  				}else{
  					sale.getLocationOnScreen(location);
   					if(x>location[0]&&y>location[1]&&x<(location[0]+sale.getMeasuredWidth())&&y<(location[1]+sale.getMeasuredHeight())){
   						movestage=Batch.Stage.BATCH_STAGE_SALE;
   						stagename="销售";
  	  				}else{
  	  				    saleout.getLocationOnScreen(location);
 	  	  				if(x>location[0]&&y>location[1]&&x<(location[0]+saleout.getMeasuredWidth())&&y<(location[1]+saleout.getMeasuredHeight())){
 	  	  				  movestage=Batch.Stage.BATCH_STAGE_SALEOUT;
 	  	  				  stagename="售罄";
	  	  				} 
  	  				}
  				}
   				final int newstage=movestage;
   				if(movestage!=-1&&movestage!=batch.getStage()){
   					NcpzsHandler  handler = Gloable.getInstance().getCurHandler();
   					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
						public void onHandlerFinish(Object result) {
 							int select=(Integer)((Map)result).get("result");
 			 				if(select==1){
 			 					prepareUpdate(batch,newstage);
  			 				}
 						}
					});
   					handler.excuteMethod(new ConfirmDialogHandlerMethod("","确实要移动到"+stagename+"吗？"));
    			}
				
			}

			@Override
			public void onStartMoveBatchItem(Batch batch, float x, float y) {
				produre.getChildAt(1).setVisibility(View.GONE);
				sale.getChildAt(1).setVisibility(View.GONE);
				saleout.getChildAt(1).setVisibility(View.GONE);
 				if(stage==Batch.Stage.BATCH_STAGE_PRODUCE){
  					sale.getChildAt(1).setVisibility(View.VISIBLE);
 					saleout.getChildAt(1).setVisibility(View.VISIBLE);
  				}else if(stage==Batch.Stage.BATCH_STAGE_SALE){
  					produre.getChildAt(1).setVisibility(View.VISIBLE);
  					saleout.getChildAt(1).setVisibility(View.VISIBLE);
  				}else if(stage==Batch.Stage.BATCH_STAGE_SALEOUT){
 					sale.getChildAt(1).setVisibility(View.VISIBLE);
 					produre.getChildAt(1).setVisibility(View.VISIBLE);
 				}
			}
 		});
        
        topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent addbatchIntent = new Intent(BatchManageActivity.this,AddBatchActivity.class);
    			startActivity(addbatchIntent);
			}
		});
        
        topview.setSubTitleClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				showSelectVilleage();
			}
		});
        loadProcedure(false);
	}
	
	private void toEditRecord(Batch bath) {
		if (bath != null) {
			Intent openCameraIntent = new Intent(BatchManageActivity.this,
					SelectTypeActivity.class);
			openCameraIntent.putExtra("batch_id", bath.getBatch_id() + "");
			openCameraIntent.putExtra("villeage_id", (int) bath
					.getVilleage_id());
			openCameraIntent.putExtra("trace_code", bath.getCode());
			openCameraIntent.putExtra("variety_name", bath.getVariety());
			openCameraIntent
					.putExtra("variety_id", (int) bath.getVariety_id());
			startActivity(openCameraIntent);
		}
	}
	
	//加载生产数据
	public void loadProcedure(boolean force) {
		produre.setBackgroundColor(Color.rgb(255, 255, 255));
		((TextView)produre.getChildAt(0)).setTextColor(Color.rgb(93, 69, 64));
		sale.setBackgroundResource(R.drawable.tabbg);
		((TextView)sale.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		saleout.setBackgroundResource(R.drawable.tabbg);
		((TextView)saleout.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		if(selectvilleage!=null)
		blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_PRODUCE,selectvilleage.getVilleage_id(),force);
		stage=Batch.Stage.BATCH_STAGE_PRODUCE;
      }  	
	//加载销售数据
	public void loadSale(boolean force) { 
		produre.setBackgroundResource(R.drawable.tabbg);
		((TextView)produre.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		sale.setBackgroundColor(Color.rgb(255, 255, 255));
		((TextView)sale.getChildAt(0)).setTextColor(Color.rgb(93, 69, 64));
		saleout.setBackgroundResource(R.drawable.tabbg);
		((TextView)saleout.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		if(selectvilleage!=null)
		blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALE,selectvilleage.getVilleage_id(),force);
		stage=Batch.Stage.BATCH_STAGE_SALE;
    }  
	//加载售罄数据
	public void loadSaleout(boolean force) { 
		produre.setBackgroundResource(R.drawable.tabbg);
		((TextView)produre.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		sale.setBackgroundResource(R.drawable.tabbg);
		((TextView)sale.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		saleout.setBackgroundColor(Color.rgb(255, 255, 255));
		((TextView)saleout.getChildAt(0)).setTextColor(Color.rgb(93, 69, 64));
		if(selectvilleage!=null)
		blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALEOUT,selectvilleage.getVilleage_id(),force);
		stage=Batch.Stage.BATCH_STAGE_SALEOUT;
    }  	
	 
 	 
	 
	private void prepareUpdate(final Batch batch,final int stage){
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null) {
					processDialog = (ProgressDialog) ((Map) result)
							.get("process");
				}
				startUpdate(batch,stage);
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("", "正在更新...."));
 				 
   	}
    private void startUpdate(final Batch batch,final int stage){
		UpdateBatchStageThread th=new UpdateBatchStageThread(batch,stage,user);
		th.setLiserner(new NetFinishListener() {
 			 
 			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
 				if (data.result== NetResult.RESULT_OF_SUCCESS) {
 					batch.setStage(stage);
 					batchservice.saveOrUpdateBatch(batch);
 					blistview.getShowbatchs().remove(batch);
 					messagehandler.excuteMethod(new MessageDialogHandlerMethod("","更新成功！"));
				} else {
					messagehandler.excuteMethod(new MessageDialogHandlerMethod("","更新失败:"+ data.message));
				}
 				finishUpdate();
			}
		});
		th.start();
	}
    private void finishUpdate(){
    	NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
    	processhandler.post(new Runnable() {
 			@Override
			public void run() {
 				if(processDialog!=null)
 					processDialog.dismiss();
 				blistview.notifyDataChange();
			}
		});
	}
 	 
 	 
    
    
    /**
	 * 显示选择农场界面
	 */
	private void showSelectVilleage(){
		if(this.uservilleages!=null){
			 String[] villeagenames=new String[this.uservilleages.size()];
			 int i=0;
			 for( Villeage vil:uservilleages)
				   villeagenames[i++]=vil.getVilleage_name();
			 
		     AlertDialog dialog= new AlertDialog.Builder(BatchManageActivity.this).setTitle("").setItems(villeagenames,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
		 						 dialog.dismiss();
 		 						 selectvilleage=uservilleages.get(which);
		 						 topview.setSubTitle(selectvilleage.getVilleage_name());
		 						 if(stage==Batch.Stage.BATCH_STAGE_PRODUCE)  
		 							 loadProcedure(true);
		 						 else if(stage==Batch.Stage.BATCH_STAGE_SALE)  
		 							loadSale(true);
		 						 else if(stage==Batch.Stage.BATCH_STAGE_SALEOUT)  
		 							loadSaleout(true);
							}
			}).show();
		}
 	   
 		
  		
 	}
	 
	@Override
	protected void onResume() {
 	    loadProcedure(true);
 		super.onResume();
	}

	@Override
	protected void onDestroy() {
		blistview.destory();
 		super.onDestroy();
	}

}
