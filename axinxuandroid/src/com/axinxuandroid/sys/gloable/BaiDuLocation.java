package com.axinxuandroid.sys.gloable;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiDuLocation {
	 //百度定位，获取用户位置信息
    private  LocationClient lc;
     private GetBaiduLocationPositionListener lis;
    public static final int GET_POSITION_RESULT_SUCCESS=1;
    public static final int GET_POSITION_RESULT_FAILD=-1;
    public BaiDuLocation(Context context){
    	lc = new LocationClient(context.getApplicationContext());     //声明LocationClient类
    	lc.registerLocationListener( new LocationListener() );    //注册监听函数
   	 	LocationClientOption option = new LocationClientOption();
  	 	option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
  	 	option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
  	 	//当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。
  	 	//调用requestLocation( )后，每隔设定的时间，定位SDK就会进行一次定位。
  	 	//如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
  	 	//返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
  	 	//定时定位时，调用一次requestLocation，会定时监听到定位结果。
   	 	//当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。每调用一次requestLocation( )，定位SDK会发起一次定位。请求定位与监听结果一一对应。
   	 	//设定了定时定位后，可以热切换成一次定位，需要重新设置时间间隔小于1000（ms）即可。
  	 	//locationClient对象stop后，将不再进行定位。如果设定了定时定位模式后，多次调用requestLocation（），
  	 	//则是每隔一段时间进行一次定位，同时额外的定位请求也会进行定位，但频率不会超过1秒一次。
  	 	option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
   	 	lc.setLocOption(option);
  	 	
  	  
    }
   
    
    
    public void start(GetBaiduLocationPositionListener lis){
    	this.lis=lis;
    	if(lc!=null&&!lc.isStarted()){
    		 lc.start();//启动定位
    	}
    	lc.requestLocation();
     }
      
    public void stop(){
    	if(lc!=null&&lc.isStarted())
    	  lc.stop();//停止定位
    	lc=null;
    }
    
     
    
    public class LocationListener implements BDLocationListener {
    	@Override
    	public void onReceiveLocation(BDLocation location) {
    		stop();//停止定位 
    		double lat=-1;
    		double ltu=-1;
    		int result=GET_POSITION_RESULT_FAILD;
    		if (location != null){
    			result=GET_POSITION_RESULT_SUCCESS;
    			lat=location.getLatitude();
    			ltu=location.getLongitude();
    		}
      		if(lis!=null)
      			lis.position(result, lat, ltu);
    		 
     	}
     }

    public interface GetBaiduLocationPositionListener{
    	public void position(int result,double lat,double litu);
    }
}
