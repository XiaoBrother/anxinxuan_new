package com.axinxuandroid.activity;

import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.SelectTypeActivity.ViewHolder;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadVarietyThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.LetterSpacingTextView;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectVarietyActivity extends NcpZsActivity{

	private CommonTopView topview;
	private ListView list;
	private List<Variety> varieties;
	private ProgressDialog progress;
	private VarietyService vservice;
	private SelectVarietyAdapter adapter;
	private int villeageid;
	private EditText serach_text;
	private ImageButton serach_btn;
	public static final int RETURN_FROM_ADD_VARIETY = 1;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.selectvariety);
 		villeageid=this.getIntent().getIntExtra("villeageid", -1);
 		vservice=new VarietyService();
 		list=(ListView) this.findViewById(R.id.selectvariety_list);
 		topview=(CommonTopView) this.findViewById(R.id.selectvariety_topview);
 		serach_text=(EditText) this.findViewById(R.id.selectvariety_seachtext);
  		serach_btn=(ImageButton) this.findViewById(R.id.selectvariety_seachbtn);
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				SelectVarietyActivity.this.finish();
			}
		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent inte=new Intent(SelectVarietyActivity.this,AddVarietyActivity.class);
 				inte.putExtra("villeageid", villeageid);
 				startActivityForResult(inte,RETURN_FROM_ADD_VARIETY);
			}
		});
  		adapter=new SelectVarietyAdapter(this);
 		list.setAdapter(adapter);
 		serach_btn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				serach();
			}
		});
 		new Thread(){
 			@Override
			public void run() {
 				prepareLoad();
			}
 			
 		}.start();
   		
	}
    /**
     * 查询
     */
	private void serach(){
		String text=serach_text.getText().toString();
		if(text==null||"".equals(text))
			varieties=vservice.selectbyVilleage(villeageid);
		else varieties=vservice.queryByVilleageWithName(villeageid, text);
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(varieties!=null&&varieties.size()>0){
 					list.setVisibility(View.VISIBLE);
 				}else list.setVisibility(View.GONE);
 				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void prepareLoad(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
 					progress=(ProgressDialog) ((Map)result).get("process");
  				startLoad();
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","加载中..."));
	}
	
	private void startLoad(){
   		String lastoptime=vservice.getLatoptime(villeageid);
 		LoadVarietyThread  th=new LoadVarietyThread(villeageid,lastoptime);
		th.setLiserner(new NetFinishListener() {
  				@Override
				public void onfinish(NetResult data) {
 	                if(data.result==NetResult.RESULT_OF_SUCCESS){
	                List<Variety>   tvarieties= (List<Variety>) data.returnData;
 	            	   if(tvarieties!=null&&tvarieties.size()>0)
	            		   for(Variety va:tvarieties){
	            			   vservice.saveOrUpdate(va);
 	            		}
 	               } 
	               varieties=vservice.selectbyVilleage(villeageid);
	               loadFinish();
				}
			});
			th.start();
 	}
	
	private void loadFinish(){
  		
 		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 				@Override
				public void run() {
 					if(progress!=null){
 			  			progress.dismiss();
 					}
 					adapter.notifyDataSetChanged();
 					if(varieties!=null&&varieties.size()>0)
 						list.setVisibility(View.VISIBLE);
				}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RETURN_FROM_ADD_VARIETY:// 处理添加标签返回的数据
			dealAddVariety(resultCode, data);
			break;
   		}
 	}
	private void dealAddVariety( int resultCode, Intent data){
   		if(resultCode==RESULT_OK){
	        String variety_name = data.getStringExtra("variety_name");
	        serach_text.setText(variety_name);
	        serach();
  		}
   	}
	public void backWithResult(Variety va){
		 Intent aintent = new Intent(SelectVarietyActivity.this, AddBatchActivity.class);
		 aintent.putExtra("variety_name", va.getVariety_name());
		 aintent.putExtra("variety_id", va.getVariety_id());
		 setResult(RESULT_OK,aintent);
		 SelectVarietyActivity.this.finish();
	}
	
	
	class  ViewHolder { 
		TextView text; 
 	} 
	public class SelectVarietyAdapter extends BaseAdapter{
        private LayoutInflater inflater;
		public SelectVarietyAdapter(Context context){
			inflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
 			return varieties==null?0:varieties.size();
		}

		@Override
		public Object getItem(int arg0) {
 			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
 			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			final Variety va=varieties.get(position);
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.selectvariety_item, null);
				holder=new ViewHolder();
 				holder.text=(TextView) view.findViewById(R.id.selectvariety_item_name);
  				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
			if(va!=null){
				holder.text.setText(va.getCategorynames()+va.getVariety_name());
 				view.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
  		 				backWithResult(va);
					}
				});
				return view;
			}
 			return null;
		}
		
	}
}
