package com.axinxuandroid.activity.handler;

import com.ncpzs.util.LogUtil;

import android.os.Handler;
import android.os.Message;

public class NcpzsHandler extends Handler {

	private OnHandlerFinishListener finishEventlistener;
	private HandlerMethodInterface method;
	private static final int NCPZSHANDLER_MESSAGE=-202;
	@Override
	public void handleMessage(Message msg) {
		 //LogUtil.logInfo(getClass(), "getMessage...:"+msg.what);
		 if(msg.what==NCPZSHANDLER_MESSAGE&&this.method!=null){
  			 method.method(finishEventlistener);
  			//finishEventlistener=null;
		 }
 		 //if(finishEventlistener!=null)
 		//	finishEventlistener.onHandlerFinish(result);
	}
	public void setOnHandlerFinishListener(OnHandlerFinishListener listener){
		this.finishEventlistener=listener;
	}
 
	public void excuteMethod(HandlerMethodInterface method){
 	   this.method=method;
 	   Message msg=this.obtainMessage();
 	   msg.what=NCPZSHANDLER_MESSAGE;
 	  //LogUtil.logInfo(getClass(), "sendMessage...:"+msg.what);
	   this.sendMessage(msg);
	  
	}
    
}
