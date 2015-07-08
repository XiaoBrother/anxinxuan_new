package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddBatchThread;
import com.axinxuandroid.activity.net.AddVarietyThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.service.CategoryService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

 

public class AddVarietyActivity extends NcpZsActivity{
    
	private CommonTopView topview;
	private Spinner fcategory,scategory,tcategory;
	private List<Category> fdatas,sdatas,tdatas;
	private CategoryService cservice;
	private VarietyService vservice;
	private UserService uservice;
	private User user;
	private ArrayAdapter fadapter,sadapter,tadapter;
	private ProgressDialog progress;
	private EditText variety_name,variety_desc;
	private int villeageid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.addvariety);
 		villeageid=this.getIntent().getIntExtra("villeageid", -1);
 		fcategory=(Spinner) this.findViewById(R.id.addvariety_fcategory);
 		scategory=(Spinner) this.findViewById(R.id.addvariety_scategory);
 		tcategory=(Spinner) this.findViewById(R.id.addvariety_tcategory);
 		topview=(CommonTopView) this.findViewById(R.id.addvariety_topview);
 		variety_name=(EditText) this.findViewById(R.id.addvariety_varietyname);
 		variety_desc=(EditText) this.findViewById(R.id.addvariety_varietydesc);
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				AddVarietyActivity.this.finish();
			}
		});
 		fcategory.setPrompt("选择品类");
 		scategory.setPrompt("选择品类");
 		tcategory.setPrompt("选择品类");
 		cservice=new CategoryService();
 		vservice=new VarietyService();
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		fdatas=cservice.selectbyParentId(0);
 		if(fdatas!=null&&fdatas.size()>0){
 			fadapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item, getSpinnerDatas(fdatas));
 	 		fadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  	   		fcategory.setAdapter(fadapter);
  	     	loadSCategory(fdatas.get(0).getId());
 		}
 		fcategory.setOnItemSelectedListener(new OnItemSelectedListener() {
 			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
 				loadSCategory(fdatas.get(position).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
 				
			}
		});
 		scategory.setOnItemSelectedListener(new OnItemSelectedListener() {
 			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
 				loadTCategory(sdatas.get(position).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
 				
			}
		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				prepareSave();
			}
		});
	}
	
	
	private void loadSCategory(long parentid){
		sdatas=cservice.selectbyParentId(parentid);
		sadapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item, getSpinnerDatas(sdatas));
 	    sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scategory.setAdapter(sadapter);
		if(sdatas!=null&&sdatas.size()>0){
 	        loadTCategory(sdatas.get(0).getId());
 		} else loadTCategory(-1);
		sadapter.notifyDataSetChanged();
	}
	private void loadTCategory(long parentid){
		tdatas=cservice.selectbyParentId(parentid);
 		tadapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item, getSpinnerDatas(tdatas));
 		tadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tcategory.setAdapter(tadapter);
		tadapter.notifyDataSetChanged();
	}
	
	private List<String> getSpinnerDatas(List<Category> cates){
		List<String> rdata=new ArrayList<String>();
		if(cates!=null&&cates.size()>0){
 			for(Category cat:cates)
				rdata.add(cat.getCategory_name());
		}
	   return rdata;	
	}
	
	private void prepareSave(){
		String name=variety_name.getText().toString();
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		if(name==null||"".equals(name.trim())){
			 handler.excuteMethod(new MessageDialogHandlerMethod("","未填写品种名称！"));
 		}else{
  			if(user==null){
 				handler.excuteMethod(new MessageDialogHandlerMethod("","请先登录！"));
 			}else{
 				final Variety var=new Variety();
 				var.setUser_id(user.getUser_id());
 				var.setCategory_id((int)getCategoryId());
 				var.setVariety_name(name);
 				var.setVilleage_id(villeageid);
 				var.setVariety_desc(variety_desc.getText().toString());
 				handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 					@Override
					public void onHandlerFinish(Object result) {
 						if(result!=null)
 	   	 					progress=(ProgressDialog) ((Map)result).get("process");
 	   	 			    startSave(var);
					}
				});
 				handler.excuteMethod(new ProcessDialogHandlerMethod("","保存中...."));
  			}
  		}
	}
 
	private void startSave(Variety var){
		AddVarietyThread th=new AddVarietyThread(var,user);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(final NetResult data) {
				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					Variety variety=(Variety) data.returnData;
 					vservice.saveOrUpdate(variety);
 					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
						public void onHandlerFinish(Object ret) {
 							saveFinish(data.result);
						}
					});
 	 				handler.excuteMethod(new MessageDialogHandlerMethod("","保存成功!"));
 				}else{
 					handler.excuteMethod(new MessageDialogHandlerMethod("",data.message));
 					saveFinish(data.result);
 				}
			}
		});
		th.start();
	}
	private void saveFinish(final int result){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				if(result==NetResult.RESULT_OF_SUCCESS)
 					backWithData();
			}
		});
	}
	
	private void backWithData(){
		 Intent aintent = new Intent(AddVarietyActivity.this, SelectVarietyActivity.class);
		 aintent.putExtra("variety_name",variety_name.getText().toString());
 		 setResult(RESULT_OK,aintent);
 		AddVarietyActivity.this.finish();
	}
	private long getCategoryId(){
		int position=tcategory.getSelectedItemPosition();
		if(position!=-1){
			return tdatas.get(position).getId();
		}else {
			position=scategory.getSelectedItemPosition();
			if(position!=-1){
				return sdatas.get(position).getId();
			}else{
				position=fcategory.getSelectedItemPosition();
				return fdatas.get(position).getId();
			}
		}
 	}
	 
}
