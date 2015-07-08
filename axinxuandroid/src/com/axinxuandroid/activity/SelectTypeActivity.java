package com.axinxuandroid.activity;

 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.VisitHistoryActivity.ViewHolder;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadBatchLabelThread;
import com.axinxuandroid.activity.net.LoadRecordThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.LetterSpacingTextView;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.service.BatchLabelService;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;
 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectTypeActivity extends NcpZsActivity {
	private String batch_id;
	private String trace_code;
	private int variety_id;
	private String variety_name;
	private int categotyid;
	private int villeage_id;
 	private ProgressDialog processDialog;
	private BatchLabelService tlservice;
	private GridView gridview;
	private EditText type_seach_text;
	private ImageButton type_seach;
	private ImageButton type_add;
	private List<String> datas;
	private BatchLabelAdapter labeladapter ;
 	private CommonTopView topview;
  	private Handler handler=new Handler();
 	private int needBack=0;
   	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_type);
		variety_id = this.getIntent().getIntExtra("variety_id", 0);
		villeage_id = this.getIntent().getIntExtra("villeage_id", 0);
 		variety_name = this.getIntent().getStringExtra("variety_name");
		trace_code= this.getIntent().getStringExtra("trace_code");
		batch_id= this.getIntent().getStringExtra("batch_id");
		needBack = this.getIntent().getIntExtra("needback", 0);
		categotyid= this.getIntent().getIntExtra("categoryid", 0);
 		type_seach_text=(EditText) findViewById(R.id.selecttype_seachtext);
		type_seach=(ImageButton) findViewById(R.id.selecttype_seachbtn);
		type_add=(ImageButton) findViewById(R.id.selecttype_addbtn);
		gridview = (GridView) findViewById(R.id.selecttype_gridview);
		gridview.setPadding(5, 5, 5, 5);
		// 添加Item到网格中
		labeladapter = new BatchLabelAdapter(this);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridview.setAdapter(labeladapter);
		tlservice = new BatchLabelService();
 		// 添加点击事件
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {

				TextView text_leixing= (TextView) v.findViewById(R.id.type_button);
				v.setBackgroundResource(R.drawable.btn_select_click);
				 text_leixing.setTextColor(Color.rgb(255, 255, 255));
 				if(needBack==1){
 					backWidthLabel(v);
 				}
 				else toaddRecord(v);
 			}
		});
		gridview.setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 			  
 				int position =gridview.pointToPosition((int)event.getX(), (int)event.getY())-gridview.getFirstVisiblePosition();
 				View selview=gridview.getChildAt(position);
   				if(selview!=null&&event.getAction()==MotionEvent.ACTION_DOWN){
   					selview.setBackgroundResource(R.drawable.btn_select_click);
					((TextView)selview.findViewById(R.id.type_button)).setTextColor(Color.rgb(255, 255, 255));
				} else if(event.getAction()==MotionEvent.ACTION_UP){
					for(int i=0;i<gridview.getChildCount();i++){
	 					View view = gridview.getChildAt(i);
	 					view.setBackgroundResource(R.drawable.btn_select_normal);
	 					((TextView)view.findViewById(R.id.type_button)).setTextColor(Color.rgb(153, 153, 153));
 					}
				} 
				return false;
			}
		});
 		type_seach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectType();
			} 
		});
		type_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addType();
			} 
		});
		
		topview=(CommonTopView) this.findViewById(R.id.selecttype_topview);
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
			 reback();
 			}
		});
		
		type_seach_text.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
 				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(type_seach.getVisibility()==View.GONE){
					type_seach.setVisibility(View.VISIBLE);
					type_add.setVisibility(View.GONE);
				}
				
			}
		});
		startloadBatchTypes();
 	}

	 /**
	  * 准备加载生产环节
	  */
	public void startloadBatchTypes(){
		NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
		processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if(result!=null){
								processDialog=(ProgressDialog) ((Map)result).get("process");
 							}
							loadBatchTypes();
 						}
					});
       processhandler.excuteMethod(new ProcessDialogHandlerMethod("请稍后", "数据加载中...."));
       
	}
	 /**
	  * 加载生产环节
	  */
	public void loadBatchTypes(){
		String lastoptime=tlservice.getLatoptime(variety_id);
       LoadBatchLabelThread th=new LoadBatchLabelThread(variety_id,lastoptime);
       th.setLiserner(new NetFinishListener() {
 			 
			@Override
			public void onfinish(NetResult data) {
  				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					List<BatchLabel> labels=(List<BatchLabel>) data.returnData;
 					if(labels!=null){
 						for(BatchLabel lb:labels){
 							tlservice.saveOrUpdate(lb);
 						}
 					}
 				}
 				datas=tlservice.getLabelsWithVarietyCategory(variety_id,categotyid);
 				loadFinish();
			}
	    });
        th.start();
	}
	  
	/**
	 * 加载完成
	 */
	public void loadFinish(){
		handler.post(new Runnable() {
	 			@Override
				public void run() {
	 				if(processDialog!=null)
	 					 processDialog.dismiss();
	 				labeladapter.notifyDataSetChanged();
				}
		});
	}
    /**
     * 查询环节
     */
	 private void selectType(){
		   if(type_seach.getVisibility()==View.GONE){
				type_seach.setVisibility(View.VISIBLE);
				type_add.setVisibility(View.GONE);
			}
			String lable_name=type_seach_text.getText().toString();
			if(lable_name!=null&&!lable_name.equals("")){
				datas = tlservice.getLabelsWithLablename(variety_id,categotyid, lable_name);
			}else{
				datas = tlservice.getLabelsWithVarietyCategory(variety_id,categotyid);
			}
			if(datas==null||datas.size()<1){
				type_seach.setVisibility(View.GONE);
				type_add.setVisibility(View.VISIBLE);
			}
  			labeladapter.notifyDataSetChanged();
	 }
	 /**
	  * 添加环节
	  */
	 public void addType(){
		 String lable_name=type_seach_text.getText().toString();
		 NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		 if("".equals(lable_name.trim())){
			 handler.excuteMethod(new MessageDialogHandlerMethod("", "输入不能为空"));
		 }
		 BatchLabel label=new BatchLabel();
		 label.setLabel_name(lable_name);
		 label.setVariety_id(variety_id);
		 tlservice.saveOrUpdate(label);
//		 handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
// 			@Override
//			public void onHandlerFinish(Object result) {
// 				selectType();
//			}
//		});
//		 ConfirmDialogHandlerMethod confirm=new ConfirmDialogHandlerMethod("提示","添加成功！");
//		 confirm.displayCancel(false);
// 		 handler.excuteMethod(confirm);
		 selectType();
	 }
	/**
	 * 跳转到添加记录界面
	 */
   public void toaddRecord(View view){
	    TextView l = (TextView) view.findViewById(R.id.type_button);
  		Intent openCameraIntent = new Intent(SelectTypeActivity.this,
				AddRecordActivity.class);
		openCameraIntent.putExtra("trace_code", trace_code);
		openCameraIntent.putExtra("batch_id", batch_id);
		openCameraIntent.putExtra("variety_id", variety_id);
		openCameraIntent.putExtra("villeage_id", villeage_id);
		openCameraIntent.putExtra("label_name", l.getText().toString());
		openCameraIntent.putExtra("variety_name", variety_name);
		startActivityForResult(openCameraIntent, 0);
   }
   
	/**
	 * 返回选择结果
	 */
	public void backWidthLabel(View view) {
		TextView l = (TextView) view.findViewById(R.id.type_button);
		Intent aintent = new Intent();
		aintent.putExtra("labelname", l.getText().toString());
		setResult(RESULT_OK,aintent);
  		SelectTypeActivity.this.finish();
	}
	 
    
  
	// 返回
	public void reback() {
 		SelectTypeActivity.this.finish();
	}
	/**
	 * 批次环节
	 * 
	 * @author Administrator
	 * 
	 */
	class  ViewHolder { 
		LetterSpacingTextView text_leixing; 
 	} 
	public class BatchLabelAdapter extends BaseAdapter {
  		private Context context;
  		// 构造函数
 		public BatchLabelAdapter(Context context) {
			this.context = context;
 		}
 		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int position) {
 			return position;
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
 			LayoutInflater mInflater = LayoutInflater.from(context);
 			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = mInflater.inflate(R.layout.select_type_item, null);
				holder=new ViewHolder();
 				holder.text_leixing=(LetterSpacingTextView) view.findViewById(R.id.type_button);
  				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
 			String labelname = datas.get(position);
 			view.setBackgroundResource(R.drawable.btn_select_normal);
 			holder.text_leixing.setTextColor(Color.rgb(153, 153, 153));
  			 
			if (labelname != null) {
				holder.text_leixing.setLetterSpacing(2);
				holder.text_leixing.setText(labelname);
  			}
			
			return view;
		}
	}
}