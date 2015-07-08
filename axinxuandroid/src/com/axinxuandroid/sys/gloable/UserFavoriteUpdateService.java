package com.axinxuandroid.sys.gloable;

import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.net.LoadUserFavoriteThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.NetThread;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
/**
 *  1. 使用startService()方法启用服务，调用者与服务之间没有关连，即使调用者退出了，服务仍然运行。
    如果打算采用Context.startService()方法启动服务，在服务未被创建时，系统会先调用服务的onCreate()方法，接着调用onStart()方法。
    如果调用startService()方法前服务已经被创建，多次调用startService()方法并不会导致多次创建服务，但会导致多次调用onStart()方法。
    采用startService()方法启动的服务，只能调用Context.stopService()方法结束服务，服务结束时会调用onDestroy()方法。

    2. 使用bindService()方法启用服务，调用者与服务绑定在了一起，调用者一旦退出，服务也就终止，大有“不求同时生，必须同时死”的特点。
    onBind()只有采用Context.bindService()方法启动服务时才会回调该方法。该方法在调用者与服务绑定时被调用，当调用者与服务已经绑定，多次调用Context.bindService()方法并不会导致该方法被多次调用。
    采用Context.bindService()方法启动服务时只能调用onUnbind()方法解除调用者与服务解除，服务结束时会调用onDestroy()方法。
 * @author Administrator
 *
 */
public class UserFavoriteUpdateService extends Service {
    private UserService uservice;
	private UserFovoriteService fovservice;
 	@Override
	public IBinder onBind(Intent intent) {
 		return null;
	}

	@Override
	public void onCreate() {
 		super.onCreate();
 		loadUserFavorite();
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

 	private void loadUserFavorite(){
		uservice=new UserService();
		fovservice=new UserFovoriteService();
		User user=uservice.getLastLoginUser();
		LogUtil.logInfo(getClass(), "start service:"+getClass().getName());
  		if(user!=null){
  			String lastoptime=fovservice.getLatoptime();
   	  		LoadUserFavoriteThread  lth=new LoadUserFavoriteThread(user.getUser_id(), lastoptime);
   	  	    lth.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
  					if(data.result==NetResult.RESULT_OF_SUCCESS){
 						List<UserFavorite> fls=(List<UserFavorite>)data.returnData;
 						if(fls!=null&&fls.size()>0)
  							for(UserFavorite ul:fls)
 								fovservice.saveOrUpdate(ul);
 					}
 					stopSelf();//停止后台服务
				}
			});
   	  	    lth.start();
  		}else stopSelf();//停止后台服务
  		
  	}
	 

}
