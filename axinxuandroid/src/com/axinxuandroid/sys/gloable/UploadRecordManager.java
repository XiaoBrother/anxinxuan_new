package com.axinxuandroid.sys.gloable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.axinxuandroid.activity.DraftActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.ScanCodeActivity;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveRecordThread;
import com.axinxuandroid.activity.net.UploadNetImgThread;
import com.axinxuandroid.activity.net.UploadNetMediaThread;
import com.axinxuandroid.activity.net.UploadVideoThread;
import com.axinxuandroid.activity.net.UploadNetImgThread.UploadImgFinishListener;
import com.axinxuandroid.activity.net.UploadNetMediaThread.UploadMediaFinishListener;
import com.axinxuandroid.activity.net.UploadVideoThread.UploadVideoFinishListener;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;
import com.youku.uploader.YoukuUploader;

public class UploadRecordManager {
  public static final int SAVE_SUCCESS = 1;//全部保存完成
  public static final int SAVE_FAILED_BY_RECORDINFO = -1;//记录基本信息保存失败
  public static final int SAVE_FAILED_BY_IMAGES = -2;//图片保存失败
  public static final int SAVE_FAILED_BY_VIDEO = -3;//视频保存失败
  public static final int SAVE_FAILED_BY_AUDIO = -4;//音频保存失败
  private static UploadRecordManager manager;
  private List<UploadRecord> uprecords;
  private volatile boolean issaving=false;
  private RecordService recordservice;
  private UserService uservice;
  private RecordResourceService sourceservice;
  private BatchService batchservice;
  private User user;
  private Context context;
  //private YoukuUploader uploader;
  //通知栏
  private NotificationManager updateNotificationManager = null;
  private Notification updateNotification = null;
  private PendingIntent updatePendingIntent = null;
  private static final int NOTIFY_ID = 99;  
   private UploadRecordManager(){
	  context=Gloable.getInstance().getCurContext().getApplicationContext();
	  updateNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	  uprecords=new ArrayList<UploadRecord>();
	//  uploader = YoukuUploader.getInstance(YukuOAuth.CLIENT_ID, YukuOAuth.CLIENT_SECRET, Gloable.getInstance().getCurContext().getApplicationContext());
   }
  public static UploadRecordManager  getInstance(){
	  if(manager==null)
		  manager=new UploadRecordManager();
	  return manager;
  }
 
  public void putUploadRecord(Record red,OnRecordCompleteUpload listener){
	  if(red==null||red.getSavestatus()!=Record.BATCHRECORD_SAVESTATE_PREPARESAVE) return;
	  if(hasContain(red))
		  return ;
	  LogUtil.logInfo(getClass(), "add record :"+red.getId());
	  UploadRecord newur=new UploadRecord(red,listener);
	  uprecords.add(newur);
  	  saveNext();
  }
  public void saveNext(){
	  if(!issaving&&uprecords.size()>0){
		  LogUtil.logInfo(getClass(), "start upload service :");
		  issaving=true;
		  new UploadRecordThread(uprecords.get(0).record,uprecords.get(0).listener).start();
	  }
   }
  private void finish(Record rec){
  	  if(rec!=null){
  		 uprecords.remove(getById(rec.getId()));
 	  }
 	  issaving=false;
	  saveNext();
  }
  private boolean hasContain(Record red){
	  if(red==null) return true;
 	  return !(getById(red.getId())==null);
  }
  
  private UploadRecord getById(long id){
 	  if(uprecords!=null&&uprecords.size()>0){
		  for(UploadRecord r:uprecords)
			  if(r.record.getId()==id)
				  return r;
	  }
 	  return null;
  }
  public interface OnRecordCompleteUpload{
	  public void onComplete(int status,Record rec);
	  public boolean notifyWarn();//是否需要通知提醒
  }
  public class  UploadRecord{
	  public Record record;
	  public OnRecordCompleteUpload listener;
	  public UploadRecord(Record red,OnRecordCompleteUpload listener){
		  this.record=red;
		  this.listener=listener;
	  }
  }
  public class UploadRecordThread extends Thread {
	 
  	    private Record record;
	 	private OnRecordCompleteUpload listener;

 		public UploadRecordThread(Record record,OnRecordCompleteUpload listener) {
 			this.record=record;
			uservice=new UserService();
			recordservice=new RecordService();
			sourceservice=new RecordResourceService();
			user=uservice.getLastLoginUser();
			batchservice=new BatchService();
			this.listener=listener;
   		}
  		@Override
		public void run() {
//  			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
			if(record!=null) 
				saveRecordInfo();
		}
 
		/**
		 * 上传到网络，基本信息
		 */
		public void saveRecordInfo() {
	 		SaveRecordThread th = new SaveRecordThread(record);
			th.setLiserner(new NetFinishListener() {
  				@Override
				public void onfinish(NetResult data) {
  					if (data.result == NetResult.RESULT_OF_SUCCESS) {
						if((record.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEXT)==Record.BATCHRECORD_TYPE_OF_TEXT)
							record.setState(record.getState()+Record.BATCHRECORD_STATE_TEXTSAVED);
						if((record.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEMPLATE)==Record.BATCHRECORD_TYPE_OF_TEMPLATE)
							record.setState(record.getState()+Record.BATCHRECORD_STATE_TEMPLATESAVED);
						recordservice.update(record);//更新本地数据库
						uploadImgs();
					} else {
						finishSave(SAVE_FAILED_BY_RECORDINFO);
					}
				}
			});
			th.start();
			LogUtil.logInfo(getClass(), "save record...");
		}

		/**
		 * 上传图片
		 * 
		 * @param res
		 */
		private void uploadImgs() {
			LogUtil.logInfo(getClass(), "upload image...");
			List<RecordResource> imgs =  record.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
			if (imgs != null && imgs.size() > 0) {
				UploadNetImgThread upthread = new UploadNetImgThread(imgs,
						record.getBatch_id()+"", record.getRecord_id()+"");
				upthread.setUploadImgFinishListener(new UploadImgFinishListener() {
 					@Override
					public void onfinish(int resultcode, Object resultdata) {
 						if (resultcode == NetResult.RESULT_OF_SUCCESS) {
							//获取上传成功的图片,更新本地数据库
							List<RecordResource> sucessimgs=(List<RecordResource>) resultdata;
							sourceservice.updateResources(sucessimgs);
							record.setState(record.getState()+Record.BATCHRECORD_STATE_IMAGESAVED);
							recordservice.update(record);//更新本地数据库
							uploadAudio();// 上传音频
						} else {
							finishSave(SAVE_FAILED_BY_IMAGES);
						}
					}
				});
			 
				upthread.start();

			} else {
	 			uploadAudio();// 上传音频
			}
			// 上传图片
			
		}

		/**
		 * 上传音频
		 * 
		 * @param res
		 */
		private void uploadAudio() {
			LogUtil.logInfo(getClass(), "upload audio...");
			List<RecordResource> audios =  record.getResourceByType(Record.BATCHRECORD_TYPE_OF_AUDIO);
			if (audios != null && audios.size() > 0) {
				UploadNetMediaThread upmediathread = new UploadNetMediaThread(audios,
						record.getBatch_id()+"", record.getRecord_id()+"");
				upmediathread.setUploadMediaFinishListener(new UploadMediaFinishListener() {
 					@Override
					public void onfinish(int resultcode, Object resultdata) {
  						if (resultcode == NetResult.RESULT_OF_SUCCESS) {
							//获取上传成功的音频,更新本地数据库
							List<RecordResource> sucessimgs=(List<RecordResource>) resultdata;
							sourceservice.updateResources(sucessimgs);
							record.setState(record.getState()+Record.BATCHRECORD_STATE_AUDIOSAVED);
							recordservice.update(record);//更新本地数据库
							uploadVideo();// 上传视频
						} else {
							finishSave(SAVE_FAILED_BY_AUDIO);
						}
					}
				});
				 
				upmediathread.start();
			} else {
	 			uploadVideo();
			}
			
		}

		/**
		 * 上传视频
		 * 
		 * @param res
		 */
		private void uploadVideo() {
			LogUtil.logInfo(getClass(), "upload video...");
	 		final List<RecordResource> videos =  record.getResourceByType(Record.BATCHRECORD_TYPE_OF_VIDEO);
			if (videos != null && videos.size() > 0) {
	 			UploadVideoThread th=new UploadVideoThread(record.getBatch_id()+"",record.getRecord_id()+"",record.getLabel_name(),record.getLabel_name(),videos,user);
	 			th.setUploadVideoFinishListener(new UploadVideoFinishListener() {
 					@Override
					public void onfinish(int resultcode, Object resultdata) {
 						if (resultcode == NetResult.RESULT_OF_SUCCESS) {
	 						sourceservice.updateResources(videos);
	 						record.setState(record.getState()+Record.BATCHRECORD_STATE_VIDEOSAVED);
							recordservice.update(record);//更新本地数据库
	 						finishSave(SAVE_SUCCESS);
						}else{
							finishSave(SAVE_FAILED_BY_VIDEO);
						}
					}
				});
 			  th.start();
			} else {
	 			finishSave(SAVE_SUCCESS);
			}
			
		}
		
		
		 /**
		   * 保存处理
		   * @param status
		   */
		private void finishSave(final int status) {
			LogUtil.logInfo(getClass(), "record:"+record.getId()+",state:"+status);
			if(status==SAVE_SUCCESS){
				batchservice.updateBatchRecordCount(1, record.getUser_type(), record.getBatch_id());
				record.setSavestatus(Record.BATCHRECORD_SAVESTATE_NET);
 			}else record.setSavestatus(Record.BATCHRECORD_SAVESTATE_DRAFT);
			recordservice.update(record);//更新本地数据库
	 	    if(this.listener!=null){
	 	    	 NcpzsHandler handler=Gloable.getInstance().getCurHandler();
	 	    	 handler.post(new Runnable() {
					@Override
					public void run() {
						 listener.onComplete(status,record);
					}
				});
	 	    	if(this.listener.notifyWarn()){
	 	    		updateNotification = new Notification();
	 		 	    //设置通知栏显示内容
	 		 	    updateNotification.flags = Notification.FLAG_AUTO_CANCEL;  // 点击后，通知消失
	 		 	    updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒 
	 		 	    if(status==SAVE_SUCCESS){
	 		 	    	// 点击通知栏，回到时间轴面
	 			 	     Intent  notifyIntent = new Intent(context, TimelineActivity.class);
	 			 	     notifyIntent.putExtra("id", record.getTrace_code());
	 			 	     updatePendingIntent = PendingIntent.getActivity(context,0,notifyIntent,0);
	 			 	     updateNotification.icon = R.drawable.transparentbg; 
	 			         updateNotification.tickerText="√ "+record.getLabel_name()+"记录上传完毕";
	 	 	  	         updateNotification.setLatestEventInfo(context, record.getLabel_name()+"记录上传完毕，点击进行查看！", "", updatePendingIntent);

	 		 	    }else{
	 		 	    // 点击通知栏，回到草稿箱界面
	 		 	    	 Intent  draftIntent = new Intent(context, DraftActivity.class);
	 		 	    	 updatePendingIntent = PendingIntent.getActivity(context,0,draftIntent,0);
	 			 	     updateNotification.icon = R.drawable.transparentbg; 
	 			 	      updateNotification.tickerText="× "+record.getLabel_name()+"记录上传失败!";
	 	 	  	         updateNotification.setLatestEventInfo(context, record.getLabel_name()+"记录上传失败，点击进行查看！", "", updatePendingIntent);
	 	 	 	    }
	 	 	        updateNotificationManager.notify(NOTIFY_ID,updateNotification);
	 	    	}
	 	     }
	 	    
 	        finish(record);
 	 		LogUtil.logInfo(getClass(), "Thread is finish upload");
 		}
	}
}
