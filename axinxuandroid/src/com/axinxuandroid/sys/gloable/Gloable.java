package com.axinxuandroid.sys.gloable;

import java.lang.reflect.Field;
import java.util.Stack;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ncpzs.util.LogUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

/**
 * �����Ŀ��ص����г�ʼ������
 * @author Administrator
 *
 */
public class Gloable {
	
    private   Context curcontext;//��ǰ���ڻ��activity
    private NcpzsHandler curhandler;
    private  static Gloable factory;
    private boolean hasinit=false;
    private int screenHeight=0;
    private int screenWeight=0;
    public static final String EXIT_APP_SIGN="AXX_EXIT_APP";
 
    
    public static Gloable getInstance(){
    	if(factory==null)
    		factory=new Gloable();
    	return factory;
    }
    public boolean hasInit(){
    	return hasinit;
    }
    public void initAll(){
      	//initAndroidUUID();
    	hasinit=true;
    }
    
    public Context getCurContext(){
     	return this.curcontext;
    }
    public void setCurContext(Context context){
    	  this.curcontext=context;
    	  //LogUtil.logInfo(Gloable.class,context.hashCode()+"");
    }
    public NcpzsHandler getCurHandler(){
    	if(this.curhandler!=null)
    		this.curhandler.setOnHandlerFinishListener(null);
    	return this.curhandler;
    }
    public void setCurHandler(NcpzsHandler handler){
    	this.curhandler=handler;
    }
     
	public int getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	public int getScreenWeight() {
		return screenWeight;
	}
	public void setScreenWeight(int screenWeight) {
		this.screenWeight = screenWeight;
	}
	 
	public void exitApplication(){
		 
 		Intent intent = new Intent();  
	    intent.setAction(EXIT_APP_SIGN);  
	    curcontext.sendBroadcast(intent);  
	    this.curcontext=null;
	    //System.exit(0);
 	}
	 
	/**

     * @author sky

     * Email vipa1888@163.com

 
     * ��ȡ��ǰ������״̬  -1��û������  1��WIFI����2��wap����3��net����

     * @param context

     * @return

     */ 

    public  int getAPNType(){ 
        int netType = AppConstant.NetStatus.UNCONNECT;  
        ConnectivityManager connMgr = (ConnectivityManager) curcontext.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 
        if(networkInfo==null){ 
             return netType; 
         } 
        int nType = networkInfo.getType(); 
        if(nType==ConnectivityManager.TYPE_MOBILE){ 
        	netType = AppConstant.NetStatus.CONNECT;   
        } 
        else if(nType==ConnectivityManager.TYPE_WIFI){ 
        	netType = AppConstant.NetStatus.CONNECT;  
        } 
         return netType; 
     } 
    /**
	 * ״̬���߶�
	 * @param activity
	 * @return
	 */
	public   int getStatusbarheight(){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
		    c = Class.forName("com.android.internal.R$dimen");
		    obj = c.newInstance();
		    field = c.getField("status_bar_height");
		    x = Integer.parseInt(field.get(obj).toString());
		    sbar = curcontext.getResources().getDimensionPixelSize(x);
		} catch(Exception e1) {
 		    e1.printStackTrace();
		}
        return sbar;
    }
    
}
