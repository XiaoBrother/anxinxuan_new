package com.xmpp.client.connect;


 import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;
import com.xmpp.client.connect.task.TaskFinishEventHandler;
import com.xmpp.client.connect.task.TaskResult;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
public class ConnectionService extends Service{
 	private TelephonyManager telephonyManager;
	private PhoneStateListener phoneStateListener;
	private  ConnectivityReceiver connectivityReceiver;
	public ConnectionService(){
 		phoneStateListener=new PhoneStateChangeListener(); 
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
 		super.onCreate();
 		telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
 		LogUtil.logInfo(ConnectionService.class, "start service...");
 		//ע�����
 		registerConnectivityReceiver();
// 		
 		//�����������������״̬��������Զ�̷������������ڼ��������������״̬��һ�������������ӷ�����
 		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		 if (networkInfo != null) {
		      if (networkInfo.isConnected()) {
		    	  new ConnectThread().start();
 		    }
		}  
 		 
	}
    
 	private void registerConnectivityReceiver() {
         telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);//ͨ���绰״̬��������
        IntentFilter filter = new IntentFilter();
        // filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);//ͨ���㲥��������״̬
        registerReceiver(connectivityReceiver, filter);
    }
	private class ConnectThread extends Thread{
		public void run() {
			 ServerConnection.getInstance().connectServer();
 			//����Զ�̷�����
 		}
	}
    
}
