package com.axinxuandroid.activity;

import java.util.List;

import com.axinxuandroid.activity.view.BatchListView;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.BatchListView.BatchItemListener;
  import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.UserService;
import com.zxing.activity.CaptureActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectBatchActivity  extends NcpZsActivity{

 	public static final int RETURN_FROM_SELECT_LABEL = 1;
 	private CommonTopView topview;
	private Batch selectBatch;
	private LinearLayout produre;//生产按钮
	private LinearLayout sale;//销售按钮
	private LinearLayout saleout;//售罄按钮
	private BatchListView blistview;
	private UserService uservice;
	private User user;
	private int variety_id=-1;
	private int withOutType=-1;
	private List<Villeage> uservilleages;
	private Villeage selectvilleage;
	private int stage=-1;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
  		this.setContentView(R.layout.selectbatch);
  		
  		variety_id=this.getIntent().getIntExtra("varietyid", -1);
  		withOutType=this.getIntent().getIntExtra("withOutType", -1);
        produre=(LinearLayout) this.findViewById(R.id.selectbatch_produre);
        sale=(LinearLayout) this.findViewById(R.id.selectbatch_sale);
        saleout=(LinearLayout) this.findViewById(R.id.selectbatch_saleout);
        blistview=(BatchListView) this.findViewById(R.id.selectbatch_listview);
        topview= (CommonTopView) this.findViewById(R.id.selectbatch_topview);
        uservice=new UserService();
        user=uservice.getLastLoginUser();
        topview.setTitle("选择批次");
        if (user != null) {
			if (user.getVilleages()!= null) {
				uservilleages= user.getVilleages();
				selectvilleage=uservilleages.get(0);
				topview.setSubTitle(selectvilleage.getVilleage_name());
    		}
		}
        produre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	loadProcedure();
            }
        });
         sale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	loadSale();
            }
        });
         saleout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	loadSaleout();
            }
        });
         if( blistview.getDrageListView()!=null){
        	  blistview.getDrageListView().setCanMove(false);
        	  blistview.getDrageListView().setLeftHiddenLen(0);
         }
         
         
         blistview.setBatchItemClickListener(new BatchItemListener() {
  			@Override
 			public void onClickBatchItem(Batch bath) {
  				selectBatch=bath;
  				
  				if(withOutType>0){
  				    Intent inte=new Intent();
  					inte.putExtra("trace_code",  selectBatch.getCode());
  					inte.putExtra("batch_id", selectBatch.getBatch_id()+"");
  					inte.putExtra("variety_id", (int)selectBatch.getVariety_id());
  					inte.putExtra("villeage_id", (int)selectBatch.getVilleage_id());
  					inte.putExtra("variety_name", selectBatch.getVariety());
  					setResult(RESULT_OK,inte);
  					SelectBatchActivity.this.finish();
  				}else{
  					 Intent inte=new Intent(SelectBatchActivity.this,SelectTypeActivity.class);
  	  				inte.putExtra("variety_id", (int)bath.getVariety_id());
  	  				inte.putExtra("villeage_id", (int)bath.getVilleage_id());
  	 				inte.putExtra("variety_name", bath.getVariety());
  	 				inte.putExtra("trace_code", bath.getCode());
  	 				inte.putExtra("batch_id", bath.getBatch_id()+"");
  	 				inte.putExtra("needback", 1);
  	 				startActivityForResult(inte, RETURN_FROM_SELECT_LABEL);
  				}
 			
 			   
 				
 			}
 			@Override
			public void onMoveBatchItem(Batch batch, float x, float y) {
 				
			}

			@Override
			public void onStartMoveBatchItem(Batch batch, float x, float y) {
 				
			}
 		});
        topview.setSubTitleClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				showSelectVilleage();
			}
		});
        topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent openCameraIntent = new Intent(SelectBatchActivity.this,
 						CaptureActivity.class);
 				startActivity(openCameraIntent);
			}
		});
   	    loadProcedure();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		 switch(requestCode){
 		   case RETURN_FROM_SELECT_LABEL://处理添加标签返回的数据
 			  backWithInfo(resultCode,data);
 			  break;
		 }
	}
 
	private void backWithInfo(int resultCode,Intent data){
		if(resultCode==RESULT_OK){
			    String labelname=data.getStringExtra("labelname");
			    Intent inte=new Intent();
				inte.putExtra("trace_code",  selectBatch.getCode());
				inte.putExtra("batch_id", selectBatch.getBatch_id()+"");
				inte.putExtra("variety_id", (int)selectBatch.getVariety_id());
				inte.putExtra("villeage_id", (int)selectBatch.getVilleage_id());
				inte.putExtra("label_name", labelname);
				inte.putExtra("variety_name", selectBatch.getVariety());
				setResult(RESULT_OK,inte);
		}
		this.finish();
	}
	
	//加载生产数据
	public void loadProcedure() {
		produre.setBackgroundColor(Color.rgb(255, 255, 255));
		((TextView)produre.getChildAt(0)).setTextColor(Color.rgb(93, 69, 64));
		sale.setBackgroundResource(R.drawable.tabbg);
		((TextView)sale.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		saleout.setBackgroundResource(R.drawable.tabbg);
		((TextView)saleout.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		if(selectvilleage!=null){
			if(variety_id>0){
				blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_PRODUCE,selectvilleage.getVilleage_id(),true,variety_id);
			}else{
				blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_PRODUCE,selectvilleage.getVilleage_id(),true);
			}
		}
		stage=Batch.Stage.BATCH_STAGE_PRODUCE;
      }  	
	//加载销售数据
	public void loadSale() { 
		produre.setBackgroundResource(R.drawable.tabbg);
		((TextView)produre.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		sale.setBackgroundColor(Color.rgb(255, 255, 255));
		((TextView)sale.getChildAt(0)).setTextColor(Color.rgb(93, 69, 64));
		saleout.setBackgroundResource(R.drawable.tabbg);
		((TextView)saleout.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		if(selectvilleage!=null){
			if(variety_id>0){
				blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALE,selectvilleage.getVilleage_id(),true,variety_id);
			}else{
				blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALE,selectvilleage.getVilleage_id(),true);
			}
		}
	//	blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALE,selectvilleage.getVilleage_id(),true);
		stage=Batch.Stage.BATCH_STAGE_SALE;
    }  
	//加载售罄数据
	public void loadSaleout() { 
		produre.setBackgroundResource(R.drawable.tabbg);
		((TextView)produre.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		sale.setBackgroundResource(R.drawable.tabbg);
		((TextView)sale.getChildAt(0)).setTextColor(Color.rgb(64, 64, 64));
		saleout.setBackgroundColor(Color.rgb(255, 255, 255));
		((TextView)saleout.getChildAt(0)).setTextColor(Color.rgb(93, 69, 64));
		if(selectvilleage!=null){
			if(variety_id>0){
				blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALEOUT,selectvilleage.getVilleage_id(),true,variety_id);
			}else{
				blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALEOUT,selectvilleage.getVilleage_id(),true);
			}
		}
		//blistview.startLoadDatas(Batch.Stage.BATCH_STAGE_SALEOUT,selectvilleage.getVilleage_id(),true);
		stage=Batch.Stage.BATCH_STAGE_SALEOUT;
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
			 
		     AlertDialog dialog= new AlertDialog.Builder(this).setTitle("").setItems(villeagenames,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
		 						 dialog.dismiss();
		 						 selectvilleage=uservilleages.get(which);
		 						 if(stage==Batch.Stage.BATCH_STAGE_PRODUCE)  
		 							 loadProcedure();
		 						 else if(stage==Batch.Stage.BATCH_STAGE_SALE)  
		 							loadSale();
		 						 else if(stage==Batch.Stage.BATCH_STAGE_SALEOUT)  
		 							loadSaleout();
							}
			}).show();
		}
 	   
 		
  		
 	}
}
