package com.axinxuandroid.sys.gloable;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.ScanCodeActivity;
import com.axinxuandroid.activity.SystemUpdateActivity;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.net.CheckVersionThread;
import com.axinxuandroid.activity.net.DownSystemThread;
 import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetThread;
import com.axinxuandroid.activity.net.DownSystemThread.DownLoadProcessListener;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
 import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

public class SystemUpdateService extends Service {
    private double version = 0;
    private SystemNoticeService noticeservice;
    //通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
    private int oldprocess=0;
    private RemoteViews remoteViews;//状态栏通知显示的view
    private static final int NOTIFY_ID = 9090;  
    private DownLoadProcessListener dlistener;
	@Override
	public IBinder onBind(Intent intent) {
 		return null;
	}

	@Override
	public void onCreate() {
 		super.onCreate();
 		//getLogs();
	}

	@Override
	public void onDestroy() {
  		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
 		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		startDownLoad(intent);
 		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
  		return super.onStartCommand(intent, flags, startId);
	}
 
	//通知自定义视图
	  
	  
	 
	public void startDownLoad(Intent intent){
		noticeservice=new SystemNoticeService();
		 //获取传值
        String url = intent.getStringExtra("downurl");
        version = intent.getDoubleExtra("version", 0);
        this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        this.updateNotification = new Notification();

        //设置下载过程中，点击通知栏，回到主界面
        updateIntent = new Intent(this, ScanCodeActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        //设置通知栏显示内容
        
        updateNotification.icon = R.drawable.xiazai; 
        updateNotification.tickerText = "正在下载安心选客户端";//设置通知消息的标题
        remoteViews=new RemoteViews(getPackageName(),R.layout.progressbar);
        remoteViews.setProgressBar(R.id.systemupdate_progressbar, 100, 0, false);
        updateNotification.contentView = remoteViews;
        updateNotification.contentIntent = updatePendingIntent;//这个pengdingIntent很重要，必须要设置
         
        updateNotificationManager.notify(NOTIFY_ID,updateNotification);

        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        final DownSystemThread th=new DownSystemThread(url);
        final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
        dlistener=new DownLoadProcessListener() {
 			@Override
			public void currentProcess(final int process) {
 				if(Gloable.getInstance()!=null&&Gloable.getInstance().getCurContext()!=null){
 				  if(process-oldprocess>2){
  						oldprocess=process;
  	 	 				handler.post(new Runnable() {
 	 	 					@Override
 	 						public void run() {
 	 	 						remoteViews.setTextViewText(R.id.systemupdate_progresstext, process + "%");  
 	  	                        remoteViews.setProgressBar(R.id.systemupdate_progressbar, 100, process,false);
 	   	                        updateNotificationManager.notify(NOTIFY_ID, updateNotification);
 	   						}
 	 					});
   				 }
 				}else{
 					 th.stopdown();
   				}
			}
 			@Override
			public void onfinish(int result, String savepath) {
				remoteViews.setTextViewText(R.id.systemupdate_progresstext, "");  
   				if(result==DownSystemThread.DOWN_RESULT_SUCCESS){
 					remoteViews.setTextViewText(R.id.systemupdate_progressinfo, "下载完成，点击安装！");  
  	                remoteViews.setProgressBar(R.id.systemupdate_progressbar, 100, 100,false);
 					noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_VERSION);
 					installApk(savepath);
 					updateNotificationManager.notify(NOTIFY_ID, updateNotification);
  			    }else{
   			    	updateNotificationManager.cancel(NOTIFY_ID);
    			}
                
 				LogUtil.logInfo(getClass(), "has down load");
 				stopSelf();//停止服务
			}
		};
		th.setProcessListenere(dlistener);
		th.start();
	}
	 
	 /**
     * 安装apk
     * @param url
     */
	private void installApk(String filepath){
 		//SharedPreferenceService.saveVersion((float)version);
		//更新成最新版本
		File apkfile = new File(filepath);
        if (!apkfile.exists()) {
            return;
        }   
        
        updateNotification.flags = Notification.FLAG_AUTO_CANCEL;  // 点击后，通知消失
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        
        updatePendingIntent = PendingIntent.getActivity(SystemUpdateService.this, 0, installIntent, 0);
        //updateNotification.contentIntent = updatePendingIntent;
        updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒 
        updateNotification.setLatestEventInfo(SystemUpdateService.this, "安心选下载完成,点击安装。", "", updatePendingIntent);
        updateNotificationManager.notify(NOTIFY_ID, updateNotification);
  	}
}
