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
import com.axinxuandroid.activity.net.SaveRecordThread;
import com.axinxuandroid.activity.net.UploadNetMediaThread;
import com.axinxuandroid.activity.net.UploadVideoThread;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DraftActivity extends NcpZsActivity{

	private DragListView listview;
	private CommonTopView topview;
	private List<Record> drafts;
	private DraftDataAdapter adapter;
	private RecordService rservice;
	protected ProgressDialog processDialog;
	protected RecordService recordservice;
	private UserService uservice;
	private BatchService batchservice;
	protected RecordResourceService sourceservice;
	protected  SystemSetService setservice;
	protected  SystemSet systemset;
	private User user;
	private OnRecordCompleteUpload recordCompleteListener;
	private boolean ismulit=false;//是否是批量上传
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.draft);
 		rservice=new RecordService();
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		drafts=rservice.getDraftRecords(user.getUser_id());
 		listview=(DragListView) this.findViewById(R.id.draft_listdatas);
 		topview=(CommonTopView) this.findViewById(R.id.draft_topview);
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
				DraftActivity.this.finish();				
			}
		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
  				NcpzsHandler  hand=Gloable.getInstance().getCurHandler();
  				if(drafts!=null&&drafts.size()>0){
  					hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
  	 					@Override
  						public void onHandlerFinish(Object result) {
  	 						int usersel=(Integer)((Map)result).get("result");
  	 						if(usersel==1){
  	 							  saveMultiRecords();
  	 						}
  						}
  					});
  	 				hand.excuteMethod(new ConfirmDialogHandlerMethod("","确定要批量上传记录吗？"));
  				}else{
  					hand.excuteMethod(new ConfirmDialogHandlerMethod("","没有可上传的记录！"));
  				}
 			}
		});
  		recordservice=new RecordService();
 		sourceservice=new RecordResourceService();
 		setservice=new SystemSetService();
		batchservice=new BatchService();
		systemset=setservice.getByType(SystemSetType.SYSTEMSETTYPE_COMMONTYPE);
		if(systemset==null){
 			systemset=new SystemSet();
 			systemset.setSet(CommonType.DEFAULT_SET);//默认设置
		}
 		adapter=new DraftDataAdapter(this);
 		listview.setAdapter(adapter);
 		listview.setCanMove(false);
 		listview.setRightHiddenLen(DensityUtil.dip2px(70));
 		listview.setDragListener(new DragListener() {
 			@Override
			public void onTouchUp(int position, View view) {
 			}
			
			@Override
			public void onTouchDown(int position, View view) {
 			}
 			@Override
			public void onStartMove(int position, View view, float x, float y) {
 			}
			
			@Override
			public void onFinishMove(int position, View view, float x, float y) {
 			}
			
			@Override
			public void onDragFinsh(int position) {
 			}
			
			@Override
			public void onClick(int position) {
  				Record rec=drafts.get(position);
 				Intent inte=new Intent(DraftActivity.this,EditRecordActivity.class);
 				inte.putExtra("id", rec.getId());
 				startActivity(inte);
			}

			@Override
			public void onBackToInitPosition(View view) {
				// TODO Auto-generated method stub
				
			}

			 
		});
 		recordCompleteListener=new OnRecordCompleteUpload() {
				@Override
			public void onComplete(int status,Record newrec) {
					
					NcpzsHandler  hand=Gloable.getInstance().getCurHandler();
					if(ismulit){
						//批量上传处理
						Record last=drafts.get(drafts.size()-1);
						boolean islast=false;
						if(last.getRecord_id()==newrec.getRecord_id())
							islast=true;
						if(status==UploadRecordManager.SAVE_SUCCESS){
						    	if(!islast)
							      processDialog.setMessage("记录："+newrec.getLabel_name()+"上传成功！开始上传下一条记录....");
						    	else processDialog.setMessage("记录："+newrec.getLabel_name()+"上传成功！");
						       drafts.remove(newrec);
							   hand.post(new Runnable() {
	 							 @Override
								 public void run() {
									adapter.notifyDataSetChanged();
								 }
							   });
							   
						}else{
							if(!islast)
							   processDialog.setMessage("记录"+newrec.getLabel_name()+"，上传失败！开始上传下一条记录....");
							else processDialog.setMessage("记录"+newrec.getLabel_name()+"，上传失败！");
						}
						
						if(islast){
							if(processDialog!=null)
								processDialog.dismiss();
 							 hand.excuteMethod(new MessageDialogHandlerMethod("","批量上传已经完成！"));
 						} 
					}else{//单个记录上传处理
						if(processDialog!=null)
							processDialog.dismiss();
						if(status==UploadRecordManager.SAVE_SUCCESS){
							drafts.remove(newrec);
							   hand.post(new Runnable() {
	 							 @Override
								 public void run() {
									adapter.notifyDataSetChanged();
								 }
							 });
						    hand.excuteMethod(new MessageDialogHandlerMethod("","记录："+newrec.getLabel_name()+"上传成功！"));
 						}
						else hand.excuteMethod(new MessageDialogHandlerMethod("","记录："+newrec.getLabel_name()+"上传失败！"));
 					}
					
 					 
			}
 			@Override
			public boolean notifyWarn() {
 					return false;
			}
		};
	}

	private void deleteRecord(final Record rec){
		final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
					@Override
					public void onHandlerFinish(Object result) {
							if(result!=null){
								Integer rresult=(Integer)((Map)result).get("result");
								if(rresult==1){
									rservice.deleteRecord(rec.getId());
									drafts.remove(rec);
									adapter.notifyDataSetChanged();
 								}
							}
 					 }
		 });
		handler.excuteMethod(new ConfirmDialogHandlerMethod("确认","确实要删除该记录吗？"));
	}
	
	
	/**
	 * 判断要不要传到网路
	 * @param recordtype
	 * @return
	 */
	private boolean uploadToNet(int recordtype){
		boolean iswifi=NetUtil.isWifiNetwork();
		if(iswifi) return true;
 		if((recordtype& Record.BATCHRECORD_TYPE_OF_IMAGE)== Record.BATCHRECORD_TYPE_OF_IMAGE){
			//包含图片
			String imgset=systemset.getSetValue(SystemSetType.CommonType.IMAGE_WIFI);
 			if(imgset!=null&&SystemSetType.TRUE_VALUE.equals(imgset))//仅在wifi下上传
 				return false;
 		}
		if((recordtype& Record.BATCHRECORD_TYPE_OF_AUDIO)== Record.BATCHRECORD_TYPE_OF_AUDIO){
			//包含音频
			String audioset=systemset.getSetValue(SystemSetType.CommonType.AUDIO_WIFI);
 			if(audioset!=null&&SystemSetType.TRUE_VALUE.equals(audioset))//仅在wifi下上传
 				return false;
 		}
		if((recordtype& Record.BATCHRECORD_TYPE_OF_VIDEO)== Record.BATCHRECORD_TYPE_OF_VIDEO){
			//包含视频
			String videoset=systemset.getSetValue(SystemSetType.CommonType.VIDEO_WIFI);
 			if(videoset!=null&&SystemSetType.TRUE_VALUE.equals(videoset))//仅在wifi下上传
 				return false;
 		}
		return true;
	}
 	/**
	 *  保存，单个记录
	 */
	public void  saveSingleRecord(final Record rec) {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		rec.setSavestatus(Record.BATCHRECORD_SAVESTATE_PREPARESAVE);
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null) {
					processDialog = (ProgressDialog) ((Map) result)
							.get("process");
					ismulit=false;
					UploadRecordManager.getInstance().putUploadRecord(rec,recordCompleteListener);
				}
			}
		});
		ProcessDialogHandlerMethod processhand=new ProcessDialogHandlerMethod("", "正在保存批次："+rec.getTrace_code()+"的"+rec.getLabel_name()+"记录...");
		processhand.isAutoClose(false);
		handler.excuteMethod(processhand);
	}
     
	   
	/**
	 *  批量保存记录
	 */
	public void  saveMultiRecords() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				   if (result != null) {
					 processDialog = (ProgressDialog) ((Map) result).get("process");
				   ismulit=true;
 				   for(Record rec:drafts){
 					  rec.setSavestatus(Record.BATCHRECORD_SAVESTATE_PREPARESAVE);
					  UploadRecordManager.getInstance().putUploadRecord(rec,recordCompleteListener);
 				   }
 				}
			}
		});
		ProcessDialogHandlerMethod processhand=new ProcessDialogHandlerMethod("", "正在进行批量保存...");
		processhand.isAutoClose(false);
		handler.excuteMethod(processhand);
	}
	
	
	public class DraftDataAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private Context context;
		private DraftDataAdapter(Context context){
			inflater=LayoutInflater.from(context);
			this.context=context;
		}
		@Override
		public int getCount() {
 			return drafts==null?0:drafts.size();
		}

		@Override
		public Object getItem(int position) {
			if(drafts==null)
 			  return null;
			else return drafts.get(position);
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=inflater.inflate(R.layout.draft_item, null);
 			final Record rec=drafts.get(position);
 			 if((position+1)%2==0){
	 				view.setBackgroundColor(Color.rgb(255,255, 255));
	 		 }else{
	 				view.setBackgroundColor(Color.rgb(249,249, 249));
	 		 }
			if(rec!=null){
				 ImageView upload=(ImageView) view.findViewById(R.id.draft_item_upload);
				 TextView label=(TextView) view.findViewById(R.id.draft_item_recordlabel);
				 TextView time=(TextView) view.findViewById(R.id.draft_item_time);
				 TextView context=(TextView) view.findViewById(R.id.draft_item_context);
				 ImageView delete=(ImageView) view.findViewById(R.id.draft_item_delete);
				 EditTabelView  table=(EditTabelView ) view.findViewById(R.id.draft_item_tablelayout);
				 
				
 				 if(rec.getSave_date()!=null&&!"".equals(rec.getSave_date().trim()))
					time.setText(DateUtil.DateToStr(DateUtil.StrToDate(rec.getSave_date()), "MM/dd hh:mm"));
  				 
 				if(rec.getLabel_name()!=null&&!"".equals(rec.getLabel_name().trim()))
 					label.setText(rec.getLabel_name());
 				if(rec.getContext()!=null&&!"".equals(rec.getContext().trim())){
 					if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEXT)==Record.BATCHRECORD_TYPE_OF_TEXT)
 	 					  context.setText(rec.getContext());
 	 				else if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEMPLATE)==Record.BATCHRECORD_TYPE_OF_TEMPLATE){
 	 					context.setVisibility(View.GONE);
  	 						setTable(table, rec.getContext());
 	 				}
 				}
 				 
 				if(rec.getBatch_id()<=0||rec.getLabel_name()==null||"".equals(rec.getLabel_name())){
 					upload.setImageResource(R.drawable.drat_item_upload);
 				}
 					 
 				delete.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
 						deleteRecord(rec);
					}
				});
 				upload.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
 						if(rec.getBatch_id()<=0||rec.getLabel_name()==null||"".equals(rec.getLabel_name())){
 							NcpzsHandler hanlder=Gloable.getInstance().getCurHandler();
 							hanlder.excuteMethod(new MessageDialogHandlerMethod("","无法上传,请先点击记录进行编辑，选择批次和标签后在进行上传操作！"));
 						}else{
 							saveSingleRecord(rec);
 						}
					}
				});
			}
 			return view;
		}
		
		private void setTable(EditTabelView jsonTable ,String json){
 	 		jsonTable.setVisibility(View.VISIBLE);
 	 		jsonTable.setTextColor(Color.rgb(161, 137, 118));
 	 		jsonTable.setReadOnly();
			try{
				JSONObject   jsonobj   =   new   JSONObject(json);
	  	    	JSONArray answers_array=jsonobj.getJSONArray("answers"); 
	 	    	for(int j = 0; j<answers_array.length(); j++){
		    		 JSONObject joa = answers_array.getJSONObject(j);  
		    		 String joa_name=joa.getString("name");
		    		 String joa_value=joa.getString("value");
		    		 jsonTable.addItem(j, joa_name,joa_value);
		    		 
		    	}
			}catch(Exception ex){
				ex.printStackTrace();
			}
	 	}
		
	}
}
