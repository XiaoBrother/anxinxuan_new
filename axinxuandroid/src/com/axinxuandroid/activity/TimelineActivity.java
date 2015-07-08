package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.LoadImageQueue;
import com.axinxuandroid.activity.net.LoadNetImgThread;
import com.axinxuandroid.activity.net.LoadRecordThread;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.NetThread;
import com.axinxuandroid.activity.net.LoadImageQueue.ImageItem;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.RefreshableView;
import com.axinxuandroid.activity.view.MoreWindow;
import com.axinxuandroid.activity.view.TimeLineView;
import com.axinxuandroid.activity.view.MoreWindow.EventListener;
import com.axinxuandroid.activity.view.RefreshableView.PullToRefreshListener;
import com.axinxuandroid.activity.view.TimeLineView.DataChangeListener;
import com.axinxuandroid.activity.view.TimeLineView.TimeLineLoadNextDatasListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.TimeOrderTool;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordTime;
import com.axinxuandroid.data.UserFavorite.FavoriteType;
import com.axinxuandroid.data.UserVisitHistory.UserVisitType;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.NetUpdateRecordService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVisitHistoryService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.NetUtil;
import com.ncpzs.util.SendShareInfo;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.stay.pull.lib.PullToRefreshBase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableLayout.LayoutParams;

@SuppressLint("HandlerLeak")
public class TimelineActivity extends NcpZsActivity  {
	public static final int FROM_OTHERS=0;
	public static final int FROM_ADD_RECORD=1;
   	private String batch_code;
   	private CommonTopView topview;
    private TimeLineView lineview;
    private Button saleBtn;
    private Button customBtn;
    private Batch batch;
    private BatchService bservice;
    private RecordService recordservice;
    private ProgressDialog processDialog; 
    private MoreWindow bottomwindow;
    private boolean isshow=false;
    private int loadType;//生产者，消费者
    private User user;
    private UserService uservice;
    private UserFovoriteService favoriteservice;
    private UserVisitHistoryService visiteservice;
    private int from=FROM_OTHERS;
    private NetUpdateRecordService netupdateservice;
    private NetUpdateRecord updaterecord;
    private int type=-1;//上一次加载的类型
    private boolean ispulldown=true;//是否是下拉刷新
    private List<Record> datas;
    private boolean isfirstinto=true;//是否是首次进入该页面
   	@Override
	public void onCreate(Bundle savedInstanceState) {
		// index=0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
    	batch_code = this.getIntent().getStringExtra("id");
    	from= this.getIntent().getIntExtra("from", FROM_OTHERS);
    	loadType= this.getIntent().getIntExtra("loadtype",Record.BATCHRECORD_USERTYPE_OF_CREATOR);
 		SharedPreferenceService.saveLastVistBatch(batch_code);
	 
		saleBtn = (Button) this.findViewById(R.id.timeline_salebtn);
		customBtn = (Button) this.findViewById(R.id.timeline_custombtn);
		topview = (CommonTopView) this.findViewById(R.id.timeline_topview);
		lineview=(TimeLineView) this.findViewById(R.id.timeline_timelineview);
		bservice=new BatchService();
		uservice=new UserService();
		favoriteservice=new UserFovoriteService();
		visiteservice=new UserVisitHistoryService();
		recordservice=new RecordService();
		user=uservice.getLastLoginUser();
		netupdateservice=new NetUpdateRecordService();
		bottomwindow=new MoreWindow(TimelineActivity.this,new int[]{MoreWindow.EDIT_RECORD_FUN,
					MoreWindow.SHARE_WEIBO_FUN,MoreWindow.SHARE_COMMENTS_FUN,MoreWindow.COLLECT_FUN,
					MoreWindow.BUY_LINK_FUN,MoreWindow.COPY_LINK_FUN}); 
		bottomwindow.addData(MoreWindow.DATA_USER, user);
		bottomwindow.setEventListener(new EventListener() {
 			@Override
			public void beforeEventDeal() {
 				//释放资源
 			    finish();
			}
			
			@Override
			public void afterEventDeal() {
 				
			}
		});
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(from==FROM_ADD_RECORD){
 					Intent inte=new Intent(TimelineActivity.this,ScanCodeActivity.class);
 					startActivity(inte);
  				}
  				TimelineActivity.this.finish();
			}
		});
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(bottomwindow!=null){
 					if(isshow){
 	 					bottomwindow.dismiss();
 	 					isshow=false;
 	 				}
 	 				else {
 	 					bottomwindow.showAsDropDown(topview.getRightimg(),-DensityUtil.dip2px(150),-DensityUtil.dip2px(20));
 	 					isshow=true;
 	 				}
 				}
 			}
		});
		lineview.setTimeLineLoadNextDatasListener(new TimeLineLoadNextDatasListener() {
 			@Override
			public void loadDatas(int direct) {
 				  if(batch!=null){
   					if(direct==PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH){
   						ispulldown=true;
   	 				}else if(direct==PullToRefreshBase.MODE_PULL_UP_TO_REFRESH){
   	 					ispulldown=false;
   	 				}
  					new Thread(){
 						@Override
						public void run() {
							prepareloadData(false);
						}
   					}.start();
 				}
			}
		});
		lineview.setDataChangeListener(new DataChangeListener() {
 			@Override
			public void onDataChange(int type) {
 				//批次记录发生变化时,重新初始状态
 				batch=bservice.getByBatchCode(batch_code);
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.post(new Runnable() {
 					@Override
					public void run() {
 						 if(loadType==Record.BATCHRECORD_USERTYPE_OF_CONSUMER)
 	 	 			          initMenuState(customBtn);
 	 	 			     else initMenuState(saleBtn);
					}
				});
 			}
		});
		saleBtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				loadSaleRecord();
			}
		});
		customBtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				loadCustomRecord();
 			}
		});
		
		new Thread(){
			@Override
			public void run() {
				if(batch_code!=null){
 				   //加载批次信息
		 			prepareLoad();
 				}
			}
		}.start();
   		
	}
    private void initMenuState(Button selectbtn){
    	 ((LinearLayout)saleBtn.getParent()).setBackgroundResource(R.drawable.tabbg);
    	 saleBtn.setTextColor(Color.rgb(64, 64, 64));//可用状态颜色值
    	 customBtn.setTextColor(Color.rgb(64, 64, 64));//
    	 //saleBtn.setTextColor(Color.rgb(182, 182, 182));//不可用状态颜色值
    	 //customBtn.setTextColor(Color.rgb(182, 182, 182));//
    	 ((LinearLayout)customBtn.getParent()).setBackgroundResource(R.drawable.tabbg); 
	   if(batch!=null){
		   //LogUtil.logInfo(getClass(), batch.getProducecount()+":"+batch.getConsumercount()+":"+(selectbtn==saleBtn)+":"+(selectbtn==customBtn));
  		   if(selectbtn==saleBtn){
				 saleBtn.setTextColor(Color.rgb(93, 69, 64));//当前选择颜色值
				 ((LinearLayout)saleBtn.getParent()).setBackgroundColor(Color.rgb(255, 255, 255));
 		   }else if(selectbtn==customBtn){
				   customBtn.setTextColor(Color.rgb(93, 69, 64));//当前选择颜色值
				   ((LinearLayout)customBtn.getParent()).setBackgroundColor(Color.rgb(255, 255, 255));
		   }
  	   }
   }
   public void updateTitleView(Batch batch){
	   if(batch!=null){
 		   String varietyname=batch.getVariety()==null?"":batch.getVariety();
 		   topview.setTitle(varietyname);
 	   } 
	   topview.setSubTitle(" NO." + batch_code);
   }
 
 

	 

	/**
 	 * 加载生产者记录
 	 */
 	private void loadSaleRecord(){
 		if(batch!=null){
 			//if(batch.getProducecount()>0){
 				initMenuState(saleBtn);
  				loadBatchRecords(batch,Record.BATCHRECORD_USERTYPE_OF_CREATOR,true);
 			//}else{
 			//	NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 			//	handler.excuteMethod(new MessageDialogHandlerMethod("","该批次没有生产者记录!"));
 			//}
 			
 		}
  	}
 	/**
 	 * 加载消费者记录
 	 */
 	private void loadCustomRecord(){
 		if(batch!=null){
 			//if(batch.getConsumercount()>0){
 	 	 	    initMenuState(customBtn);
  	 	 	    loadBatchRecords(batch,Record.BATCHRECORD_USERTYPE_OF_CONSUMER,true);
 			//}else{
 			//	NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 			//	handler.excuteMethod(new MessageDialogHandlerMethod("","该批次没有消费者记录!"));
 			//}
  		}
  	}
 	

	private void prepareLoad(){
		NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
	    processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if(result!=null)
								processDialog=(ProgressDialog) ((Map)result).get("process");
							loadBatch();
						}
					});
		 processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "数据加载中...."));
	}
	//加载批次信息
 	private void loadBatch(){
 		 batch=bservice.getByBatchCode(batch_code);
 		 if(batch==null){
  				 LoadBatchByCodeOrIdThread th=new LoadBatchByCodeOrIdThread(batch_code,null);
	  		  	 th.setLiserner(new NetFinishListener() {
	   					@Override
	 					public void onfinish(NetResult data) {
	   		 				if(data.result== NetResult.RESULT_OF_SUCCESS){
	  		 					 batch=(Batch)data.returnData;
	  		   					 if(batch!=null){
	  		   						//bservice.tmpSave(batch);
	  		   						bservice.saveOrUpdateBatch(batch);
	  		  					 }
	  		    			} 
	  		 				loadBatchFinish();
	 					}
	  			  });
	  		 	  th.start();
  			 
 		 }else{
 		   new Thread(){
 				@Override
				public void run() {
 					loadBatchFinish();
				}
  		   }.start();
  		 }
 	  
 	}
 	 
 	private void loadBatchFinish(){
 		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		if(batch!=null){
			UserFavorite uf=favoriteservice.selectbyUserFavoriteTypeWithId((int)batch.getBatch_id(), FavoriteType.FavoriteType_OF_BATCH);
			if(uf!=null){
					bottomwindow.addData(MoreWindow.DATA_USER_FAVORITE, uf);
					bottomwindow.setCollected(true);
			}
			bottomwindow.addData(MoreWindow.DATA_BATCH, batch);
			if(batch.getBuylink()==null||"".equals(batch.getBuylink())||"null".equals(batch.getBuylink()))//隐藏购买链接
				bottomwindow.hiddenSystemItem(MoreWindow.BUY_LINK_FUN);
			handler.post(new Runnable() {
					@Override
					public void run() {
						updateTitleView(batch);
					    if(loadType==Record.BATCHRECORD_USERTYPE_OF_CONSUMER)
 	 			          initMenuState(customBtn);
 	 			        else initMenuState(saleBtn);
					}
			  });
			 if(user!=null)
			  visiteservice.addVisitory(user.getUser_id(), (int)batch.getBatch_id(), UserVisitType.VISIT_TYPE_BATCH);
			  loadBatchRecords(batch,loadType,false);
			   
		}else{
			 handler.post(new Runnable() {
 					@Override
					public void run() {
 						 if(processDialog!=null&&processDialog.isShowing())
 							processDialog.dismiss();
					}
			 });
		 }
 	}
 	/**
 	 * 加载批次记录,切换标签项调用
 	 * @param batch
 	 * @param type
 	 * @param showdialog
 	 */
 	public void loadBatchRecords(Batch batch,int type,boolean showdialog){
 		if(this.type!=type&&batch!=null){
			ispulldown=true;
 			if(updaterecord==null||batch.getBatch_id()!=updaterecord.getObjid())
				   updaterecord=netupdateservice.getByTypeWithObjId(NetUpdateRecord.TYPE_RECORD,(int)batch.getBatch_id());
			this.batch=batch;
			this.type=type;
   			if(this.batch!=null){
  				prepareloadData(showdialog);
			}
		}
  	}

 	 /**
     *准备 加载数据
    */
	private void prepareloadData(boolean showdialog){
		if(batch==null) return ;
		if(isfirstinto){
 			isfirstinto=false;
  		}
 		if(showdialog){
 			NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
 			processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
 						public void onHandlerFinish(Object result) {
 							if(result!=null)
 								processDialog=(ProgressDialog) ((Map)result).get("process");
 							startLoadData();
 						}
 					});
 		  	  processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "数据加载中...."));
 		}else{
 			startLoadData();
 		}
		
	}
	
	/**
	 * 开始 加载数据
	 */
	private void startLoadData(){
		String starttime=null,endtime=null;
   		if(ispulldown){
   			starttime=updaterecord==null?null:updaterecord.getMaxTime(type);
    	}else{
   			NetUpdateRecordTime time=updaterecord==null?null:updaterecord.findNeedToLoadTime(type);
   			if(time!=null){
   				starttime=time.stime;
   	   			endtime=time.etime;
   			}
    	}
   		final String loadmaxtime=endtime;
   		final String loadmintime=starttime;
       	LoadRecordThread th=new LoadRecordThread(batch,type,starttime,endtime);
    	th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
 				 if(data.result==NetResult.RESULT_OF_SUCCESS){
 					 Map rdata=(Map) data.returnData;
					 List<Record> records= (List<Record>)rdata.get("datas");
					 if(records!=null&&records.size()>0){
						 for(Record rec:records){//保存记录
							 recordservice.saveOrUpdate(rec);
						 }
 					    String[] tims=TimeOrderTool.getMaxMinTime(records);
						if(updaterecord==null){
								updaterecord=new NetUpdateRecord();
								updaterecord.setObjid((int)batch.getBatch_id());
								updaterecord.setType(NetUpdateRecord.TYPE_RECORD);
						}
						int totalcount=(Integer)rdata.get("datacount");
						if(tims!=null){
							//如果是下拉的刷新，则最大日期和最小日期从数据里获取，如果是上拉更新，则最大日期是查询endtime
							if(ispulldown){
								//如果返回的数据个数大于等于约定的数量，则表示服务器端可能还有未查询的数据
								//否则代表这一时间段的数据已经全部获取到
								if(loadmintime==null){
									updaterecord.saveOrUpdateStatus(type, tims[1], tims[0]);
								}else{
									if(totalcount<=AppConstant.NET_RETURN_MAX_COUNT){
										 updaterecord.saveOrUpdateStatus(type, loadmintime,  tims[0]);
									}else{
										 updaterecord.saveOrUpdateStatus(type, tims[1],  tims[0]);
									}
								}
							}
							else {
								if(loadmaxtime!=null){
									if(loadmintime!=null){
										if(totalcount<=AppConstant.NET_RETURN_MAX_COUNT){
											updaterecord.saveOrUpdateStatus(type, loadmintime, loadmaxtime);
										}else{
											updaterecord.saveOrUpdateStatus(type, tims[1], loadmaxtime);
 										}
									}else{
 										updaterecord.saveOrUpdateStatus(type, tims[1], loadmaxtime);
									}
									
								}
 							}
							 
							netupdateservice.saveOrUpdate(updaterecord);
 						}
 					} 
				 } 
				 datas=recordservice.getLocalDatas(batch.getCode(), type,updaterecord==null?null:updaterecord.getFirstStartTime(type));
				 finishload();
			}
		});
    	th.start();
	}
	  /**
     *  加载完成后处理
     */
    private void finishload(){
     NcpzsHandler handler=Gloable.getInstance().getCurHandler();
     handler.post(new Runnable() {
			@Override
		    public void run() {
				lineview.setShowDatas(datas,type);
 		  		if(processDialog!=null&&processDialog.isShowing()){
					processDialog.dismiss();
				}
 				 
 			}
    	});
     }
 	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode=event.getKeyCode();
          switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
            	if(lineview.shareWindowIsShowing()){
            		lineview.dismissShareWindow();
            		return true;
            	}
            	if(from==FROM_ADD_RECORD){
 					Intent inte=new Intent(TimelineActivity.this,ScanCodeActivity.class);
 					startActivity(inte);
 					return true;
  				}
            	
            	return super.dispatchKeyEvent(event);	
            default:
             break;
        }
        return super.dispatchKeyEvent(event);
 	}
 	@Override
	protected void onDestroy() {
 		lineview.destory();
		if(bottomwindow!=null)
		  bottomwindow.dismiss();
 		super.onDestroy();
	}
 	
  
}