package com.axinxuandroid.sys.gloable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
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
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;

import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class VersionUpdateService extends Service {
	private SystemNoticeService noticeservice;
 	@Override
	public IBinder onBind(Intent intent) {
 		return null;
	}

	@Override
	public void onCreate() {
 		super.onCreate();
 		checkversion();
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

	private void checkversion(){
		LogUtil.logInfo(getClass(), "start service:"+getClass().getName());
		CheckVersionThread th=new CheckVersionThread();
		noticeservice=new SystemNoticeService();
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					float oldversion=SharedPreferenceService.getVersion();
 					final Version version=(Version) data.returnData;
 					if(oldversion<version.getVersion()){
 						boolean isold=version.isOldVersion(oldversion);
 	 					if(version.getImportant()==Version.IMPORTANT_TRUE||isold){
 	 						String  message="";
 	 						if(version.getImportant()==Version.IMPORTANT_TRUE)
 	 							message="发现重要的更新版本，请更新最新版本！\n";
 	 						else message="当前版本太低，请更新最新版本！\n";
 	 						String  versiondesc=version.getVersiondesc();
 	 						if(versiondesc!=null&&!"".equals(versiondesc)){
 	 							JSONArray descs=JSONObject.parseArray(versiondesc);
 	 							if(descs!=null){
  	 								for(Object obj:descs){
 	 									JSONObject jsonobj=(JSONObject) obj;
 	 									message+=jsonobj.getString("desc")+"\n";
 	 								}
 	 							}
 	 						}
 	 						handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 	 							@Override
 								public void onHandlerFinish(Object result) {
 	 								 Intent upint=new Intent("com.axinxuandroid.backservice.SystemUpdateService");
 	 								 upint.putExtra("downurl", version.getDownurl());
 	 								 upint.putExtra("version",(double) version.getVersion());
 	 								 startService(upint);//下载系统
 								}
 							});
 	 						handler.excuteMethod(new MessageDialogHandlerMethod("",message));
 	 					}else{
 	  	 						SystemNotice notice=new SystemNotice();
 	 						    notice.setNotice(version.getVersion()+"");
 	 							notice.setType(SystemNoticeType.NOTICE_TYPE_VERSION);
 	 							 Map map = new HashMap();  
 	 						     map.put( "newversion", version.getVersion() );  
 	 						     map.put( "versiondesc", version.getVersiondesc() ); 
 	 						     map.put( "downurl", version.getDownurl());  
 	 						     notice.setJsondata(JSONObject.toJSON(map).toString());
 	 							 noticeservice.saveOrUpdateNotice(notice);
 	  					}
 					}else{
 						noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_VERSION);
 					}
  				} 
 				stopSelf();
			}
		});
		th.start();
	}

}
