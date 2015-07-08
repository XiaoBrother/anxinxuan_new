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
 *  1. ʹ��startService()�������÷��񣬵����������֮��û�й�������ʹ�������˳��ˣ�������Ȼ���С�
    ����������Context.startService()�������������ڷ���δ������ʱ��ϵͳ���ȵ��÷����onCreate()���������ŵ���onStart()������
    �������startService()����ǰ�����Ѿ�����������ε���startService()���������ᵼ�¶�δ������񣬵��ᵼ�¶�ε���onStart()������
    ����startService()���������ķ���ֻ�ܵ���Context.stopService()�����������񣬷������ʱ�����onDestroy()������

    2. ʹ��bindService()�������÷��񣬵���������������һ�𣬵�����һ���˳�������Ҳ����ֹ�����С�����ͬʱ��������ͬʱ�������ص㡣
    onBind()ֻ�в���Context.bindService()������������ʱ�Ż�ص��÷������÷����ڵ�����������ʱ�����ã���������������Ѿ��󶨣���ε���Context.bindService()���������ᵼ�¸÷�������ε��á�
    ����Context.bindService()������������ʱֻ�ܵ���onUnbind()�����������������������������ʱ�����onDestroy()������
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
 					stopSelf();//ֹͣ��̨����
				}
			});
   	  	    lth.start();
  		}else stopSelf();//ֹͣ��̨����
  		
  	}
	 

}
