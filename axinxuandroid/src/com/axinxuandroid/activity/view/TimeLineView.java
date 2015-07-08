package com.axinxuandroid.activity.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 
import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.CommentActivity;
import com.axinxuandroid.activity.EditRecordActivity;
import com.axinxuandroid.activity.R;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.menu.InOutImageButton;
import com.axinxuandroid.activity.net.AdvocateThread;
import com.axinxuandroid.activity.net.DeleteRecordThread;
import com.axinxuandroid.activity.net.LoadImageQueue;
import com.axinxuandroid.activity.net.LoadRecordThread;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.LoadImageQueue.ImageItem;
import com.axinxuandroid.activity.view.GetMoreView.GetMoreListener;
import com.axinxuandroid.activity.view.MenuWindow.MenuClickListerner;
import com.axinxuandroid.activity.view.RefreshableView.PullToRefreshListener;
import com.axinxuandroid.activity.view.ShareWindow.ShareMessageInterface;
import com.axinxuandroid.activity.view.TimeLineItemView.AudioPlayListener;
import com.axinxuandroid.activity.view.TimeLineItemView.LoadImgListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.TimeOrderTool;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordTime;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.NetUpdateRecordService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
 import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeLineView extends ViewGroup {
	public static int TIME_LINE_ITEM_STYLE_COMMON=1;
	public static int TIME_LINE_ITEM_STYLE_USERRECORD=2;
	public static int DATA_CHANGE_ADD=1;
	public static int DATA_CHANGE_DELETE=-1;
	private Context context;
 	private List<Record> datas;
 	private LoadImageQueue loadqueue;
 	private RecordService tlservice;
 	private BatchService batchservice;
 	private UserService uservice;
 	private User user;
   	private ProgressDialog processDialog; 
  	private TimeLineAdapter tdapter;
	private RecordResourceService resourceservice;
 	private List<TimeLineItemView> items;
 	private ShareWindow sharewindow;
 	private int type=-1;
    private AudioPlayListener audiolistener;
   	private LoadImgListener loadimglistener;
  	private MediaPlayer   player;
	private volatile boolean isPlaying=false;
	private PullToRefreshListView pullRefreshListView;
	private ListView listview;
	private TextView warn_msg_textview;
    private TimeLineItemView currentplayview;
    private TimeLineLoadNextDatasListener loaddatalistener;
    private int showstyle=TIME_LINE_ITEM_STYLE_COMMON;
    private DataChangeListener datachangelistener;
  
    public TimeLineView(Context context) {
		super(context);
 		initview(context);
	}

	public TimeLineView(Context context, AttributeSet sets) {
		super(context, sets);
		initview(context);
	}
    
	private void initview(final Context context) {
		this.context=context;
		items=new ArrayList<TimeLineItemView>();
    	LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.timelineview, null);
		addView(view);
		warn_msg_textview=(TextView) view.findViewById(R.id.timelineview_warninfo);
    	loadqueue=new LoadImageQueue();
 		loadqueue.setLiserner(new NetFinishListener() {
 			 
			@Override
			public void onfinish(NetResult data) {
				final ImageItem item=(ImageItem) data.returnData;
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.post(new Runnable() {
 					@Override
					public void run() {
 						updateViewResourceData((RecordResource)item.res,item.view,item.index);
					}
				});
			}
		});
		tlservice = new RecordService();
		uservice=new UserService();
		resourceservice=new RecordResourceService();
		batchservice=new BatchService();
 		user=uservice.getLastLoginUser();
		pullRefreshListView =  (PullToRefreshListView) view.findViewById(R.id.timelineview_refresh_list);
		pullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(int direct) {
    			if(loaddatalistener!=null){
   					loaddatalistener.loadDatas(direct);
   				}
  			}
		});
		listview = pullRefreshListView.getRefreshableView();
		tdapter= new TimeLineAdapter(context); //  
		listview.setPadding(0, 0, 0, 0);
		//listview.setBackgroundResource(R.drawable.timeline_bg);
 		listview.setAdapter(tdapter);
 		
 		audiolistener=new AudioPlayListener() {
 			@Override
			public void playAudio( TimeLineItemView view,String url) {
  				try{
 					if (player != null&&isPlaying) {
 						stopPlay(currentplayview);
 	 		        }
 		 			if(!isPlaying){
 			  			isPlaying=true;
 			  			currentplayview=view;
 			  			currentplayview.setPlayAudioStatus(isPlaying);
 			  			player=new MediaPlayer();
  			  	 		player.setOnCompletionListener(new OnCompletionListener() {
 			  				@Override
 			  				public void onCompletion(MediaPlayer mp) {
 			  						if(isPlaying){
 			  							if(player!=null)
 			  								player.release();
 			  							player=null;
 			  							isPlaying=false;
 			  							currentplayview.setPlayAudioStatus(isPlaying);
 			  						}
 			  					}
 			  			});
  		  	  			FileInputStream fis = new FileInputStream(new File(url)); 
 			  			player.setDataSource(fis.getFD());
 			  			player.prepare();
 			  			player.start();
 		 	  		 
 		 	   		 }
 				}catch(Exception ex){
 					ex.printStackTrace();
 				}
			}

			@Override
			public void stopPlay(TimeLineItemView view) {
				if(currentplayview!=null&&(currentplayview.record.getRecord_id()==view.record.getRecord_id())){
					 if (player != null&&isPlaying) {
		 		     		player.release();
		 		     		isPlaying = false;
		 		     		player=null;
		 		     		currentplayview.setPlayAudioStatus(isPlaying);
		 		     }
				}
 			}

			@Override
			public boolean viewIsPlay(TimeLineItemView view) {
				if(currentplayview!=null&&(currentplayview.record.getRecord_id()==view.record.getRecord_id()))
					return isPlaying;
 				return false;
			}
		};
		loadimglistener=new LoadImgListener() {
 			@Override
			public void load(RecordResource resource, View view) {
   				ImageItem item=loadqueue.getFinishItem(resource,view);
				if (item== null)// 如果没加载过，则进行加载
					loadqueue.addItem(resource, view,type);
				else ((TimeLineItemView)view).setImageWithPath((RecordResource)item.res);
			}

			@Override
			public void reload(RecordResource resource, View view) {
				loadqueue.reloadItem(resource, view,type);	//强制重新加载
			}
 			 
		};
 		sharewindow=new ShareWindow((Activity)context);
 		
 		
 		
 	}

	/**
	 * 显示记录数据
	 * @param records
	 * @param type
	 */
	public void setShowDatas(List<Record> records,int type){
		if(records!=null&&records.size()>0){
			warn_msg_textview.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
 			this.datas=records;
			this.type=type;
			this.tdapter.notifyDataSetChanged();
 		}else{
 			if(type==Record.BATCHRECORD_USERTYPE_OF_CREATOR)
			    showWarnMessage("该批次没有生产者记录!");
 			else showWarnMessage("该批次没有消费者记录!");
		}
		if(pullRefreshListView!=null)
			  pullRefreshListView.onRefreshComplete();
	}
	/**
	 * 加载更多数据的接口
	 * @author Administrator
	 *
	 */
	public interface TimeLineLoadNextDatasListener{
		public void loadDatas(int direct);
	}
	
	public void setTimeLineLoadNextDatasListener(TimeLineLoadNextDatasListener lis){
		this.loaddatalistener=lis;
	}
	/**
	 * 数据发生变化的接口
	 * @author Administrator
	 *
	 */
	public interface  DataChangeListener{
		public void onDataChange(int type);//-1:删除，1：添加
 	}
	public void setDataChangeListener(DataChangeListener lis){
		this.datachangelistener=lis;
	}
	// 遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);
	}

 	// 在这个类的实现中，需要调用每一个控件的布局方法为其布局。
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  			for (int i = 0; i < this.getChildCount(); i++) {
				View view = getChildAt(i);
  				view.layout(0, 0, this.getWidth(),this.getHeight());
 			}
  			//bottomwindow.showAtLocation(this, Gravity.BOTTOM|Gravity.LEFT, -10,0);
  	}
	
	/**
	 * 更新图片资源信息
	 * 
	 * @param url
	 * @param viewindex
	 */
    private void updateViewResourceData(RecordResource resource,View view,int type) {
  		if (resource != null) {
			try {
 				//LogUtil.logInfo(getClass(), "start set img "+this.type+":"+type);
 				if(this.type==type){//如果页面没切换,则进行以下操作
					tlservice.updateResourceLocalUrl(resource.getId(),
							(String) resource.getLocalurl());
					if(view!=null){
						TimeLineItemView item=(TimeLineItemView) view;
						item.setImageWithPath(resource);
	 					//LogUtil.logInfo(getClass(), "set finish");
						tdapter.notifyDataSetChanged();
	  				} 
				}
   			  
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
     
	/**
	 * 时间轴
	 * 
	 * @author Administrator
	 * 
	 */
	public class TimeLineAdapter extends BaseAdapter {
  		private Context context;
 
		// 构造函数

		public TimeLineAdapter(Context context) {
			this.context = context;
 		}

 		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}
 		

		@Override
		public Object getItem(int position) {
 			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			Record rec = datas.get(position);
			if (rec != null)
				return rec.getRecord_id();
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
  			Record rec = datas.get(position);
   			TimeLineItemView view =null;
  			if(convertView!=null){
  				view=(TimeLineItemView) convertView;
  				view.destory();
  			}
   			else {
   				view = new TimeLineItemView(context,TimeLineView.this);
   				items.add(view);
   			}
  			view.setLoadImgListener(loadimglistener);
  			view.setAudioPlayListener(audiolistener);
   			view.setData(rec,user);
  			return view;
  		}
	}
  
	/**
	 * 删除确认
	 * @param currecord
	 */
	public void prepareDelete( final Record currecord){
		final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
					@Override
					public void onHandlerFinish(Object result) {
							if(result!=null){
								Integer rresult=(Integer)((Map)result).get("result");
								if(rresult==1){
 									handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 										@Override
										public void onHandlerFinish(Object result) {
 											if(result!=null)
 												processDialog=(ProgressDialog) ((Map)result).get("process");
 											new Thread(){
		 										@Override
												public void run() {
 		 											deleteRecord(currecord);
												}
												
											}.start();
										}
									});
									handler.excuteMethod(new ProcessDialogHandlerMethod("","删除中...."));
 								}
							}
 					 }
		 });
		handler.excuteMethod(new ConfirmDialogHandlerMethod("确认","确实要删除该记录吗？"));
	}
	private void deleteRecord(final Record currecord){
		//LogUtil.logInfo(getClass(), "state:"+currecord.getState());
		if(currecord.getState()!=Record.BATCHRECORD_STATE_UNSAVED){
			//同步服务器删除
			DeleteRecordThread th=new DeleteRecordThread(currecord.getRecord_id(),user);
			th.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
  					if(data.result==NetResult.RESULT_OF_SUCCESS){
 						deleteLocalRecord(currecord);
 					}else{
 						deleteFinish(0,data.message);
 					}
				}
			});
			th.start();
		}else{
			deleteLocalRecord(currecord);
 		}
 	}
	
 
 
	/**
	 * 分享窗口
	 * @param rec
	 */
	public void showShareWindow(final Record rec){
 		if(sharewindow!=null){
			sharewindow.setShareMessageInterface(new ShareMessageInterface() {
	 			@Override
				public String getShareMessage() {
	 				String message="";
	 				if(user!=null&&rec.getUser_id().equals(user.getUser_id()+"")){
 	 					if((rec.getSave_type() & Record.BATCHRECORD_TYPE_OF_TEXT) == Record.BATCHRECORD_TYPE_OF_TEXT){
 	 						if(rec.getContext()!=null&&rec.getContext().length()>100)
 	 					    	message+=rec.getContext().substring(0,99)+"...";
 	 						else message+=rec.getContext()+".";
	 					}
 	 				}else{
	 					Batch batch=batchservice.getByBatchId(rec.getBatch_id());
		 				if(batch!=null)
		 					message= "我在安心选看了"+HttpUrlConstant.URL_HEAD+"发现了"+batch.getVilleage_name()+"的"+rec.getVariety_name()+",看上去很不错 ，快来看看吧。";
		 				else  message= "我在安心选看了"+HttpUrlConstant.URL_HEAD+"发现了"+rec.getVariety_name()+",看上去很不错 ，快来看看吧。";
 
	 				}
	 				message+=HttpUrlConstant.URL_HEAD+"/no/"+rec.getTrace_code();
	 				return message;
  	 			}

				@Override
				public String getShareImagePath() {
					List<RecordResource> res=rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
					if(res!=null&&res.size()>0){
						return res.get(0).getLocalurl();
					}
 					return null;
				}
			});
	  		sharewindow.showAtLocation(this, Gravity.CENTER_VERTICAL|Gravity.LEFT, -10,0);
		}
 	}
	
	/**
	 * 删除本地资源
	 * @param currecord
	 */
	private void deleteLocalRecord(Record currecord){
 		resourceservice.deleteByRecord(currecord.getId());
		//删除记录
		tlservice.deleteRecord(currecord.getId());
		batchservice.updateBatchRecordCount(-1, currecord.getUser_type(), currecord.getBatch_id());
		this.datas.remove(currecord);
		notifyDataChange(DATA_CHANGE_DELETE);
  		deleteFinish(1,"删除成功！");
 	}
	private void deleteFinish(int status,String message){
		if(processDialog!=null)
			processDialog.dismiss();
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 	    handler.excuteMethod(new MessageDialogHandlerMethod("",message));
 	    if(status==1){
 	    	handler.post(new Runnable() {
 				@Override
				public void run() {
					tdapter.notifyDataSetChanged();
 				}
			});
 	    	
 	    }
 	}
	 
	/**
	 * 销毁图片，释放内存
	 */
	public void destory() {
 		 if(this.items!=null&&this.items.size()>0){
			 int count=items.size();  
			 TimeLineItemView view=null;
			 for(int i=0;i<count;i++){
				 view= items.get(i);
 				if(view!=null){
					view.destory();
 				}
 			 }
		 }
		 loadqueue.stopLoad();//停止加载图片
		 pullRefreshListView=null;
 		 if(player!=null)
			 player.release();
		 if(sharewindow!=null)
			 sharewindow.dismiss();
 //		 if (bottomwindow.isShowing()) 
//			 bottomwindow.dismiss();
	}

	
	public boolean shareWindowIsShowing(){
		return this.sharewindow.isShowing();
	}
   
	public void dismissShareWindow(){
		 if(sharewindow!=null)
			 sharewindow.dismiss();
	}
	/**
	 * 获取显示样式
	 * @param showstyle
	 */
	public int getShowstyle() {
		return showstyle;
	}
	/**
	 * 设置显示样式
	 * @param showstyle
	 */
	public void setShowstyle(int showstyle) {
		this.showstyle = showstyle;
	}
	/**
	 * 通知数据发生变化
	 * @param type
	 */
	public void notifyDataChange(int type){
		if(datachangelistener!=null)
			datachangelistener.onDataChange(type);
	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
// 		if (event.getAction() == MotionEvent.ACTION_DOWN) {
// 		}else if (event.getAction() == MotionEvent.ACTION_UP) {
//			if (!bottomwindow.isShowing())
//				bottomwindow.showAtLocation(this,
//						Gravity.BOTTOM | Gravity.LEFT, -10, 0);
// 		}  
// 		return super.dispatchTouchEvent(event);
//	}
   /**
    * 显示提示信息，当没有记录的时候显示
    * @param message
    */
	public void showWarnMessage(String message){
		listview.setVisibility(View.GONE);
		warn_msg_textview.setVisibility(View.VISIBLE);
 		warn_msg_textview.setText(message);
	}
	
}
