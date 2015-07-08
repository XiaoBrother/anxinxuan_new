package com.axinxuandroid.sys.gloable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.axinxuandroid.activity.SystemUpdateActivity;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.net.CheckVersionThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.NetThread;
import com.axinxuandroid.activity.net.TemplateCheckThread;
import com.axinxuandroid.activity.net.UploadSystemErrorThread;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;

import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemLogService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UploadSystemErrorService extends Service {
	private SystemLogService logservice;
 	@Override
	public IBinder onBind(Intent intent) {
 		return null;
	}

	@Override
	public void onCreate() {
 		super.onCreate();
 		uploadlog();
	}

	@Override
	public void onDestroy() {
		LogUtil.logInfo(getClass(), "destory service:"+getClass().getName());
 		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
 		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
 		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
 		return super.onStartCommand(intent, flags, startId);
	}

	private void uploadlog(){
		LogUtil.logInfo(getClass(), "start service:"+getClass().getName());
		logservice=new SystemLogService();
		List<SystemLog> logs=logservice.getAllLog();
		if(logs!=null&&logs.size()>0){
			UploadSystemErrorThread th=new UploadSystemErrorThread(logs,"phone");
	 		th.setLiserner(new NetFinishListener() {
  				@Override
				public void onfinish(NetResult data) {
 	 				if(data.result==NetResult.RESULT_OF_SUCCESS){
	 					List<SystemLog> hasuploads=(List<SystemLog>) data.returnData;
	 					if(hasuploads!=null)
	 						for(SystemLog log:hasuploads)
	 							logservice.deleteById(log.getId());
	 				}
	 				stopSelf();
				}
			});
			th.start();
		}else stopSelf();
		
	}

}
