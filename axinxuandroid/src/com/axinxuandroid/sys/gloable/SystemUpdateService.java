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
    //֪ͨ��
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    //֪ͨ����תIntent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
    private int oldprocess=0;
    private RemoteViews remoteViews;//״̬��֪ͨ��ʾ��view
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
 
	//֪ͨ�Զ�����ͼ
	  
	  
	 
	public void startDownLoad(Intent intent){
		noticeservice=new SystemNoticeService();
		 //��ȡ��ֵ
        String url = intent.getStringExtra("downurl");
        version = intent.getDoubleExtra("version", 0);
        this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        this.updateNotification = new Notification();

        //�������ع����У����֪ͨ�����ص�������
        updateIntent = new Intent(this, ScanCodeActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        //����֪ͨ����ʾ����
        
        updateNotification.icon = R.drawable.xiazai; 
        updateNotification.tickerText = "�������ذ���ѡ�ͻ���";//����֪ͨ��Ϣ�ı���
        remoteViews=new RemoteViews(getPackageName(),R.layout.progressbar);
        remoteViews.setProgressBar(R.id.systemupdate_progressbar, 100, 0, false);
        updateNotification.contentView = remoteViews;
        updateNotification.contentIntent = updatePendingIntent;//���pengdingIntent����Ҫ������Ҫ����
         
        updateNotificationManager.notify(NOTIFY_ID,updateNotification);

        //����һ���µ��߳����أ����ʹ��Serviceͬ�����أ��ᵼ��ANR���⣬Service����Ҳ������
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
 					remoteViews.setTextViewText(R.id.systemupdate_progressinfo, "������ɣ������װ��");  
  	                remoteViews.setProgressBar(R.id.systemupdate_progressbar, 100, 100,false);
 					noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_VERSION);
 					installApk(savepath);
 					updateNotificationManager.notify(NOTIFY_ID, updateNotification);
  			    }else{
   			    	updateNotificationManager.cancel(NOTIFY_ID);
    			}
                
 				LogUtil.logInfo(getClass(), "has down load");
 				stopSelf();//ֹͣ����
			}
		};
		th.setProcessListenere(dlistener);
		th.start();
	}
	 
	 /**
     * ��װapk
     * @param url
     */
	private void installApk(String filepath){
 		//SharedPreferenceService.saveVersion((float)version);
		//���³����°汾
		File apkfile = new File(filepath);
        if (!apkfile.exists()) {
            return;
        }   
        
        updateNotification.flags = Notification.FLAG_AUTO_CANCEL;  // �����֪ͨ��ʧ
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        
        updatePendingIntent = PendingIntent.getActivity(SystemUpdateService.this, 0, installIntent, 0);
        //updateNotification.contentIntent = updatePendingIntent;
        updateNotification.defaults = Notification.DEFAULT_SOUND;//�������� 
        updateNotification.setLatestEventInfo(SystemUpdateService.this, "����ѡ�������,�����װ��", "", updatePendingIntent);
        updateNotificationManager.notify(NOTIFY_ID, updateNotification);
  	}
}
