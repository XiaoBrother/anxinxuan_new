package com.axinxuandroid.broadcast;
 

import java.util.Random;

 

import cn.jpush.android.api.JPushInterface;

import com.axinxuandroid.activity.Appstart;
import com.axinxuandroid.activity.NotifyActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.WelcomeActivity;
import com.ncpzs.util.LogUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifyInfoReceiver extends BroadcastReceiver {
	  private static final int NOTIFY_ID = 999;  
	@Override
	public void onReceive(Context context, Intent intent) {
		 String action = intent.getAction();
		 LogUtil.logInfo(getClass(), "action:"+action);
		 if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
			  LogUtil.logInfo(getClass(), "get message");
//  	            String notificationTitle = intent.getStringExtra(JPushInterface.EXTRA_TITLE);
//	            String notificationMessage = intent .getStringExtra(JPushInterface.EXTRA_MESSAGE);
// 
//	            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//	            Notification notification = new Notification();
//	            notification.icon = R.drawable.notification;
//	            notification.defaults = Notification.DEFAULT_SOUND;
//  	           // notification.defaults |= Notification.DEFAULT_VIBRATE;
//	             
//	            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//	           // notification.when = System.currentTimeMillis();
//	            notification.tickerText = notificationMessage;
//
//	             
//	            Intent tointent = new Intent(context,
//	            		WelcomeActivity.class);
//   	           // tointent.putExtra("title", notificationTitle);
//	            //tointent.putExtra("message", notificationMessage);
//	            tointent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////	            tointent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
////	            tointent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////	            tointent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
////	            tointent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//	            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//	            		tointent, PendingIntent.FLAG_CANCEL_CURRENT);
// 	            notification.setLatestEventInfo(context, notificationTitle, notificationMessage,
//	                    contentIntent);
// 	            notificationManager.notify(NOTIFY_ID, notification);
	        }else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
	        	Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            context.startActivity(i);
	        	  LogUtil.logInfo(getClass(), "user click push");
  	          }

	}

}
