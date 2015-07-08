package com.ncpzs.util;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.sys.gloable.Gloable;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GPSUtil {

	public static void getLocation() {
		// 获取位置管理服务
		LocationManager locationManager = (LocationManager) Gloable
				.getInstance().getCurContext().getSystemService(
						Context.LOCATION_SERVICE);
		 boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);    
	    if (!gpsEnabled) {        
	       NcpzsHandler hand=Gloable.getInstance().getCurHandler();
	       hand.excuteMethod(new MessageDialogHandlerMethod("","请开启GPS"));
	    }else{
	    	// 查找到服务信息
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			// 高精度
			criteria.setAltitudeRequired(false);// 不要求海拔
			criteria.setBearingRequired(false);// 不要求方位
			criteria.setCostAllowed(false);// 不允许有花费
			criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
			String provider = locationManager.getBestProvider(criteria, true);
			// 获得最后一次变化的位置
			Location location = locationManager.getLastKnownLocation(provider);
	 		LogUtil.logInfo(GPSUtil.class, "get location");
			if (location != null) {
	 			double latitude = location.getLatitude();
	 			double longitude = location.getLongitude();
	 			LogUtil.logInfo(GPSUtil.class, "维度：" + latitude + "\n经度" + longitude);
	 		}
	    }
		
	}

	public static boolean isopenGPS() {
		// 得到系统服务 的LocationManager 对象
		LocationManager loctionManager = (LocationManager) Gloable
				.getInstance().getCurContext().getSystemService(
						Context.LOCATION_SERVICE);
		if (loctionManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}
	
 
}
