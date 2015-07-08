package com.axinxuandroid.activity.net;

 
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ncpzs.util.LogUtil;
 
public abstract class NetThread {
   	protected final static int NET_RETURN_SUCCESS=1;
	protected final static int NET_RETURN_ERROR=0;
	protected final static int HAS_DATA=1;
	protected final static int NOT_HAS_DATA=0;
 	protected NetFinishListener liserner;
	protected NetJsonResponseHandler jsonhand;
	private  Thread  dealthread;
   	public NetThread() {
 		jsonhand=new NetJsonResponseHandler();
  	}

 	public void start() {
		requestHttp();
  	}

	public abstract void requestHttp();
  
	//子类重写处理方法
 	public  NetResult onResponseSuccess(JSONObject json){
 		 return null;
 	}
 	//子类重写处理方法
 	public  NetResult onResponseError(){
 		 return null;
 	}
	protected void finishNet(NetResult result){
 		if(liserner!=null){
       	 liserner.onfinish(result);
       }
	}
	 

	public void setLiserner(NetFinishListener liserner) {
		this.liserner = liserner;
	}
	 
	public class NetJsonResponseHandler  extends JsonHttpResponseHandler {
 		@Override
		public void onSuccess(int arg0, final JSONObject json) {
 			dealthread=new Thread(){
 	 			@Override
 				public void run() {
 	 				NetResult result=onResponseSuccess(json);
 	 	 		    finishNet(result);
 				}
 	  		};
 	  		dealthread.start();
 		}

		@Override
		protected void handleFailureMessage(Throwable arg0, String arg1) {
			LogUtil.logInfo(getClass(), arg1);
			NetResult result=onResponseError();
 		    if(result==null)
 		    	result=new NetResult(NetResult.RESULT_OF_ERROR,"网络连接失败");
 			finishNet(result);
		}

	}
	
	 

}
