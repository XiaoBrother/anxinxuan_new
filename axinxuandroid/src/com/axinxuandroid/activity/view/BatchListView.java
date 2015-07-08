package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.RecordDemoActivity;
import com.axinxuandroid.activity.SelectTypeActivity;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.DeleteBatchThread;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.UpdateBatchStageThread;
import com.axinxuandroid.activity.net.UpdateBatchStatusThread;
import com.axinxuandroid.activity.view.DragListView.DragListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.TimeOrderInterface;
import com.axinxuandroid.data.TimeOrderTool;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordStatus;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordTime;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.NetUpdateRecordService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.ValidPattern;
import com.sina.weibo.sdk.net.NetStateManager.NetStateReceive;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BatchListView extends ViewGroup {
	private Context context;
	private ProgressDialog processDialog;
 	private List<Batch> showbatchs;
	private BatchAdapter adapter;
	private BatchService batchservice;
	private UserService uservice;
	private User user;
	private BatchItemListener listerner;
	private int villeage_id;
	private int stage;
	private PullToRefreshDragListView refresh_view;
	private DragListView listview;
    private ImageButton serach;
    private EditText serachText;
    private List<Batch> allbatchs;
    private boolean ispulldown=true;//是否是下拉刷新
    private NetUpdateRecordService netupdateservice;
    private NetUpdateRecord updaterecord;
    private volatile boolean isloading=false;//防止多次加载
    private UserVilleageService uvilservice;
    private int uvilleagerole;//用户在农场的角色
 	public BatchListView(Context context) {
		super(context);
		initview(context);
	}

	public BatchListView(Context context, AttributeSet sets) {
		super(context, sets);
		initview(context);
	}

	private void initview(Context context) {
 		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.batchlistview, null);
		addView(view);
 		
		batchservice = new BatchService();
		uservice=new UserService();
		user = uservice.getLastLoginUser();
		netupdateservice=new NetUpdateRecordService();
		uvilservice=new UserVilleageService();
 		
 		refresh_view= (PullToRefreshDragListView) view.findViewById(R.id.batchlistview_refresh_list);
		refresh_view.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(int direct) {
				if(direct==PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH){
					ispulldown=true;
 				}else if(direct==PullToRefreshBase.MODE_PULL_UP_TO_REFRESH){
 					ispulldown=false;
 				}
				
   				new Thread(){
 					@Override
					public void run() {
 						isloading=true;
 						loadData(stage);
					}
   				}.start();
  				
  			}
		});
		listview = refresh_view.getRefreshableView();
		adapter = new BatchAdapter(context);
		 
 		serach=(ImageButton) view.findViewById(R.id.batchlistview_seachbtn);
		serachText= (EditText) view.findViewById(R.id.batchlistview_seachtext);
 		listview.setLeftHiddenLen(DensityUtil.dip2px(95));
 		listview.setAdapter(adapter);
		listview.setDragListener(new DragListener() {
 			@Override
			public void onFinishMove(int position,View view, float x, float y) {
  				 Batch batch=showbatchs.get(position);
 				 if(listerner!=null){
 					listerner.onMoveBatchItem(batch, x, y);
 				 }
// 				if(batch!=null&&batch.getStatus()==Batch.Status.BATCH_STATUS_HIDDEN)
//					 view.findViewById(R.id.batchlist_bglayout).setBackgroundResource(R.drawable.batchlist_hui);
//			   else view.findViewById(R.id.batchlist_bglayout).setBackgroundResource(R.drawable.batchlist_normalbg);
   			}
 			@Override
			public void onDragFinsh(int position) {
 				
 			}
			
			@Override
			public void onClick(int position) {
        		if (listerner != null&&showbatchs!=null&&showbatchs.size()>=position) {
 						listerner.onClickBatchItem(showbatchs.get(position));
				}
			}
			@Override
			public void onTouchDown(int postion,View view) {
   				 if(view!=null){
// 					view.findViewById(R.id.batchlist_bglayout).setBackgroundColor(Color.rgb(255, 255, 255));
// 					((TextView)view.findViewById(R.id.batchlist_variety)).setTextColor(Color.rgb(138, 188, 29));
// 					((ImageView)view.findViewById(R.id.batchlist_repeatimg)).setBackgroundResource(R.drawable.batch_bg_click);
  				 }
			}
			@Override
			public void onTouchUp(int postion,View view) {
   				if(view==null) return ;
   				if(showbatchs!=null&&showbatchs.size()>postion){
   					Batch batch=showbatchs.get(postion);
//    				 if(batch!=null&&batch.getStatus()==Batch.Status.BATCH_STATUS_HIDDEN)
//   					   view.findViewById(R.id.batchlist_bglayout).setBackgroundResource(R.drawable.batchlist_hui);
//   				      else {
//   					   view.findViewById(R.id.batchlist_bglayout).setBackgroundResource(R.drawable.batchlist_normalbg);
//    				 }
     			}
				
 			}
			@Override
			public void onStartMove(int position, View view, float x, float y) {
				 Batch batch=showbatchs.get(position);
 				 if(listerner!=null){
 					listerner.onStartMoveBatchItem(batch, x, y);
 				 }
			}
			@Override
			public void onBackToInitPosition(View view) {
//				view.findViewById(R.id.batchlist_bglayout).setBackgroundColor(Color.rgb(246,246,246));
//				((TextView)view.findViewById(R.id.batchlist_variety)).setTextColor(Color.rgb(102, 102, 102));
//				((ImageView)view.findViewById(R.id.batchlist_repeatimg)).setBackgroundResource(R.drawable.batch_bg_normal);
			}
			 
			 
		});
		serach.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {	
  				serach();
			}
		});
	 
		serachText.addTextChangedListener(new TextWatcher() {
 			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
 			}
 			@Override
			public void afterTextChanged(Editable s) {
 				filterBatch(serachText.getText().toString());
   				adapter.notifyDataSetChanged();
 			}
		});
		 
	}
	 
	// 遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);
	}

	// 在我们继承ViewGroup时会在除了构造函数之外提供这个方法，我们可以看到，
	// 在ViewGroup的源代码中方法是这样定义的，也就是父类没有提供方法的内容，需要我们自己实现。
	// 当View要为所有子对象分配大小和位置时，调用此方法
	// 在这个类的实现中，需要调用每一个控件的布局方法为其布局。
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int mTotalHeight = 0;
		int childCount = getChildCount();// 一个view是通过xml实例化来的，另一个是自己添加的BatchView，这里的个数是2
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			// view.measure(r - l, b - t);
			int measureHeight = view.getMeasuredHeight();
			int measuredWidth = view.getMeasuredWidth();
			view.layout(0, mTotalHeight, measuredWidth, this.getHeight());
			mTotalHeight += measureHeight;

		}
	}

	public void serach(){
		String text=serachText.getText().toString();
		if(text!=null&&!"".equals(text)){
			//batchs=batchservice.getByVilleageid(vid, stage)
			if(ValidPattern.isAllNumber(text)){
				allbatchs=showbatchs=batchservice.serachByCode(text,stage);
			}else{
				allbatchs=showbatchs=batchservice.serachByVariety(text,stage);
			}
 		}else allbatchs=showbatchs = batchservice.getByVilleageid(villeage_id, stage);
		adapter.notifyDataSetChanged();
 	}
	/**
	 * 准备加载数据
	 * force:强制刷新
	 */
	public void startLoadDatas(final int stage,int villeage_id,boolean force) {
		if(!isloading){
		  if (this.stage != stage||force) {
			    this.villeage_id=villeage_id;
			    uvilleagerole=uvilservice.getUserRoleInVilleage(user.getUser_id(), villeage_id);
				isloading=true;
				if(updaterecord==null||villeage_id!=updaterecord.getObjid())
				   updaterecord=netupdateservice.getByTypeWithObjId(NetUpdateRecord.TYPE_VILLEAGE_BATCH, villeage_id);

				ispulldown=true;
				this.stage = stage;
				if (this.allbatchs != null && allbatchs.size() > 0)
					allbatchs.clear();
 				serachText.setText("");
				NcpzsHandler processhandler = Gloable.getInstance()
						.getCurHandler();
				processhandler
						.setOnHandlerFinishListener(new OnHandlerFinishListener() {
							@Override
							public void onHandlerFinish(Object result) {
								if (result != null) {
									processDialog = (ProgressDialog) ((Map) result)
											.get("process");
								}
								loadData(stage);
							}
						});
				processhandler.excuteMethod(new ProcessDialogHandlerMethod(
						"", "数据加载中...."));
 	 		}
		}
		
  	}

	/**
	 * 加载数据
	 */
	private void loadData(final int stage) {
   		String starttime=null,endtime=null;
   		if(ispulldown){
   			//starttime=batchservice.getLatoptime(villeage_id, stage);
   			starttime=updaterecord==null?null:updaterecord.getMaxTime(stage);
   		}else{
   			NetUpdateRecordTime time=updaterecord==null?null:updaterecord.findNeedToLoadTime(stage);
   			if(time!=null){
   				starttime=time.stime;
   	   			endtime=time.etime;
   			}
    	}
   		final String loadmaxtime=endtime;
   		final String loadmintime=starttime;
 		LoadVilleageBatchThread th = new LoadVilleageBatchThread(villeage_id+"", stage,starttime,endtime);
		th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
  				if (data.result == NetResult.RESULT_OF_SUCCESS) {
  					Map rdata=(Map) data.returnData;
					List<Batch>  nbts= (List<Batch>)rdata.get("datas");
 					if (nbts != null && nbts.size() > 0) {
						for (Batch batch : nbts) {
							batchservice.saveOrUpdateBatch(batch);
 						}
						String[] tims=TimeOrderTool.getMaxMinTime(nbts);
						if(updaterecord==null){
							updaterecord=new NetUpdateRecord();
							updaterecord.setObjid(villeage_id);
							updaterecord.setType(NetUpdateRecord.TYPE_VILLEAGE_BATCH);
						}
						int totalcount=(Integer)rdata.get("datacount");
						if(tims!=null){
							//如果是下拉的刷新，则最大日期和最小日期从数据里获取，如果是上拉更新，则最大日期是查询endtime
							if(ispulldown){
								//如果返回的数据大小大于等于约定的数量，则表示服务器端可能还有未查询的数据
								//否则代表这一时间段的数据已经全部获取到
								if(loadmintime==null){
									updaterecord.saveOrUpdateStatus(stage, tims[1], tims[0]);
								}else{
									if(totalcount<=AppConstant.NET_RETURN_MAX_COUNT){
										 updaterecord.saveOrUpdateStatus(stage, loadmintime,  tims[0]);
									}else{
										 updaterecord.saveOrUpdateStatus(stage, tims[1],  tims[0]);
									}
								}
 							}
 							else {
 								if(loadmaxtime!=null){
 									if(loadmintime!=null){
 										if(totalcount<=AppConstant.NET_RETURN_MAX_COUNT){
 											updaterecord.saveOrUpdateStatus(stage, loadmintime, loadmaxtime);
 										}else{
 											updaterecord.saveOrUpdateStatus(stage, tims[1], loadmaxtime);
  										}
 									}else{
  										updaterecord.saveOrUpdateStatus(stage, tims[1], loadmaxtime);
 									}
 									
 								}
  							}
							netupdateservice.saveOrUpdate(updaterecord);
						}
     				} 
				}  
 				allbatchs=batchservice.getByVilleageid(villeage_id, stage);
				loadFinish();
			}
		});
		th.start();
	}

	/**
	 * 加载完成
	 */
	private void loadFinish() {
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
    	handler.post(new Runnable() {
			@Override
			public void run() {
				if (processDialog != null ) {
					processDialog.dismiss();
				}
				showbatchs=allbatchs;
				adapter.notifyDataSetChanged();
				if(refresh_view!=null)
				  refresh_view.onRefreshComplete();
				isloading=false;
 			}
		});
	}

    private void prepareDelete(final Batch batch){
    	final NcpzsHandler handler = Gloable.getInstance().getCurHandler();
    	handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			
			@Override
			public void onHandlerFinish(Object result) {
 				int select=(Integer)((Map)result).get("result");
 				if(select==1){
 					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
 						public void onHandlerFinish(Object result) {
 							if (result != null) {
 								processDialog = (ProgressDialog) ((Map) result)
 										.get("process");
 							}
 							startDelete(batch);
 						}
 					});
 					handler.excuteMethod(new ProcessDialogHandlerMethod("",
 					"正在删除...."));
 				}
			}
		});
    	handler.excuteMethod(new ConfirmDialogHandlerMethod("","确实要删除吗？"));
 	}
    private void startDelete(final Batch batch){
		DeleteBatchThread th=new DeleteBatchThread(batch,user);
		th.setLiserner(new NetFinishListener() {
 			 
			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
 				if (data.result == NetResult.RESULT_OF_SUCCESS) {
 					batchservice.deleteByBatchId((int)batch.getBatch_id());
 					showbatchs.remove(batch);
 					messagehandler.excuteMethod(new MessageDialogHandlerMethod("","删除成功！"));
				} else {
					messagehandler.excuteMethod(new MessageDialogHandlerMethod("","删除失败:"+ data.message));
				}
 				finishDelete();
			}
		});
		th.start();
	}
    private void finishDelete(){
    	NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
    	processhandler.post(new Runnable() {
 			@Override
			public void run() {
 				if(processDialog!=null)
 					processDialog.dismiss();
 				adapter.notifyDataSetChanged();
			}
		});
	}

    /**
     * 置顶
     * @param curBatch
     * @param topBatch
     */
    public void toTop(Batch curBatch){
    	Batch  topBatch=batchservice.getTopBatch();
    	if(topBatch!=null&&topBatch.getId()!=curBatch.getId()){
    		curBatch.setOrder_date(topBatch.getOrder_date());
    		curBatch.setOrder_index(topBatch.getOrder_index()+1);
    		batchservice.saveOrUpdateBatch(curBatch);
    		showbatchs.remove(curBatch);
    		showbatchs.add(0, curBatch);
      		adapter.notifyDataSetChanged();
     	} 
    }
	public class BatchAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public BatchAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return showbatchs == null ? 0 : showbatchs.size();
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
		    Batch cbatch = null;
			if(showbatchs!=null){
				cbatch=showbatchs.get(position);
			}
 			final Batch batch=cbatch;
			
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.batchlistview_item, null);
				holder=new ViewHolder();
				holder.bglayout=view.findViewById(R.id.batchlist_bglayout);
				holder.showhidden=(ImageButton)view.findViewById(R.id.batchlist_showhidden);
				holder.code=(TextView) view.findViewById(R.id.batchlist_batchcode);
				holder.variety=(TextView) view.findViewById(R.id.batchlist_variety);
				holder.timetext=(TextView) view.findViewById(R.id.batchlist_date);
				holder.recordcount=(TextView) view.findViewById(R.id.batchlist_recordcount);
 				holder.del=(ImageButton) view.findViewById(R.id.batchlist_del);
				holder.edit=(ImageButton) view.findViewById(R.id.batchlist_edit);
				holder.totop=(ImageButton) view.findViewById(R.id.batchlist_totop);
				holder.img=(ImageView) view.findViewById(R.id.batchlist_repeatimg);
				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
			final View pview=view;
		 
   			if(batch!=null){
    			if((position+1)%2==0){
    				holder.bglayout.setBackgroundColor(Color.rgb(255, 255, 255));
   					holder.variety.setTextColor(Color.rgb(138, 188, 29));
   					holder.img.setBackgroundResource(R.drawable.batch_bg_click);
   				}else{
   					holder.bglayout.setBackgroundColor(Color.rgb(246,246,246));
   					holder.variety.setTextColor(Color.rgb(102, 102, 102));
   					holder.img.setBackgroundResource(R.drawable.batch_bg_normal);
   				}
    				 
   				
  				if(batch.getStatus()==Batch.Status.BATCH_STATUS_HIDDEN){
  					//holder.bglayout.setBackgroundResource(R.drawable.batchlist_hui);
  					holder.showhidden.setImageResource(R.drawable.batchlist_show);
				}
  				holder.code.setText(batch.getCode());
  				holder.variety.setText(batch.getVariety());
 				if(batch.getCreate_time()!=null){
					holder.timetext.setText( DateUtil.DateToStr(DateUtil.StrToDate(batch.getLastoptime()), "yyyy/MM/dd HH:mm"));
				}
				
 				holder.recordcount.setText(batch.getProducecount() + "条生产者记录/"+batch.getConsumercount()+"条消费者记录");
 				holder.del.setVisibility(View.GONE);
 				//只有农产主和创建人才能删除批次。
 				if(uvilleagerole==UserVilleage.UserVilleageRole.VILLEAGE_ROLE_MASTER||cbatch.getUserid()==user.getUser_id())
 					holder.del.setVisibility(View.VISIBLE);
 				holder.del.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						prepareDelete(batch);
					}
				});
				holder.edit.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
  						Intent openCameraIntent = new Intent(context,
 								SelectTypeActivity.class);
 						openCameraIntent.putExtra("batch_id", batch.getBatch_id()+"");
 						openCameraIntent.putExtra("villeage_id", villeage_id);
 						openCameraIntent.putExtra("trace_code", batch.getCode());
 						openCameraIntent.putExtra("variety_name", batch.getVariety());
 						openCameraIntent.putExtra("variety_id", (int)batch.getVariety_id());
 						context.startActivity(openCameraIntent);
 					}
				});
				holder.totop.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
 						toTop(batch);
 					}
				});
				holder.showhidden.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
 						prepareUpdateStatus(batch,pview);
 					}
				});
 
			}
			
			return view;
		}
	}
	class  ViewHolder { 
	       View bglayout; 
	       ImageButton showhidden,del,edit,totop;
	       TextView code,variety,timetext,recordcount;
	       ImageView img;
	} 
   /**
    * 更新状态
    * @param batch
    * @param stage
    */
	private void prepareUpdateStatus(final Batch batch,final View updateview){
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null) {
					processDialog = (ProgressDialog) ((Map) result)
							.get("process");
				}
				startUpdateStatus(batch,updateview);
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("", "正在更新...."));
 				 
   	}
    private void startUpdateStatus(final Batch batch,final View updateview){
    	int status=Batch.Status.BATCH_STATUS_OPEN;
    	if(batch.getStatus()==Batch.Status.BATCH_STATUS_OPEN)
    		status=Batch.Status.BATCH_STATUS_HIDDEN;
		UpdateBatchStatusThread th=new UpdateBatchStatusThread(batch,status,user);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(final NetResult data) {
  				NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
 				if (data.result == NetResult.RESULT_OF_SUCCESS) {
 					if(batch.getStatus()==Batch.Status.BATCH_STATUS_OPEN)
 						batch.setStatus(Batch.Status.BATCH_STATUS_HIDDEN);
 					else batch.setStatus(Batch.Status.BATCH_STATUS_OPEN);
 					batchservice.saveOrUpdateBatch(batch);
  					messagehandler.excuteMethod(new MessageDialogHandlerMethod("","更新成功！"));
				} else {
					messagehandler.excuteMethod(new MessageDialogHandlerMethod("","更新失败:"+ data.message));
				}
 				messagehandler.post(new Runnable() {
 		 			@Override
 					public void run() {
 		 				if(processDialog!=null)
 		 					processDialog.dismiss();
 		 				if (data.result== NetResult.RESULT_OF_SUCCESS) {
 		 					if(batch.getStatus()==Batch.Status.BATCH_STATUS_OPEN){
 		 						updateview.findViewById(R.id.batchlist_bglayout).setBackgroundResource(R.drawable.batchlist_normalbg);
 		 						((ImageButton)updateview.findViewById(R.id.batchlist_showhidden)).setImageResource(R.drawable.batchlist_hidden);
 		 					}else{
 		 						updateview.findViewById(R.id.batchlist_bglayout).setBackgroundResource(R.drawable.batchlist_hui);
 		 						((ImageButton)updateview.findViewById(R.id.batchlist_showhidden)).setImageResource(R.drawable.batchlist_show);
 		 					}
 		 				}
 		 			}
 				});
			}
		});
		th.start();
	}
    
 	 
	public void setBatchItemClickListener(BatchItemListener lis) {
		this.listerner = lis;
	}

	/**
	 * 过滤批次
	 */
	public void  filterBatch(String text){
		if(text!=null&&this.allbatchs!=null&&this.allbatchs.size()>0){
 			if("".equals(text.trim())) this.showbatchs=allbatchs;
 			else{
 				List<Batch> nbatchs=new ArrayList<Batch>();
 	  			for(Batch batch:this.allbatchs){
 					if(ValidPattern.isAllNumber(text)&&batch.getCode().indexOf(text)>=0){
 						nbatchs.add(batch);
 					}else if(batch.getVariety().indexOf(text)>=0){
 						nbatchs.add(batch);
 					}
 				}
 	  			this.showbatchs=nbatchs;
 			}
			
  		}
  	}
	/**
	 * 点击监听
	 * 
	 * @author Administrator
	 * 
	 */
	public interface BatchItemListener {
		public void onClickBatchItem(Batch bath);
		public void onMoveBatchItem(Batch batch,float x, float y);
		public void onStartMoveBatchItem(Batch batch,float x, float y);
	}
	public List<Batch> getShowbatchs() {
		return showbatchs;
	}

	public void setShowbatchs(List<Batch> showbatchs) {
		this.showbatchs = showbatchs;
		
 	}
	
	public DragListView getDrageListView(){
		return this.listview;
	}
	
	 
	 public void notifyDataChange(){
		 NcpzsHandler  handler = Gloable.getInstance().getCurHandler();
			handler.post(new Runnable() {
	 			@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
	 }

	 
	 public void destory(){
		 refresh_view=null;
		 processDialog=null;
	 }
}
