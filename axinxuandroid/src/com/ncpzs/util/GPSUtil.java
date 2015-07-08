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
		// ��ȡλ�ù������
		LocationManager locationManager = (LocationManager) Gloable
				.getInstance().getCurContext().getSystemService(
						Context.LOCATION_SERVICE);
		 boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);    
	    if (!gpsEnabled) {        
	       NcpzsHandler hand=Gloable.getInstance().getCurHandler();
	       hand.excuteMethod(new MessageDialogHandlerMethod("","�뿪��GPS"));
	    }else{
	    	// ���ҵ�������Ϣ
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			// �߾���
			criteria.setAltitudeRequired(false);// ��Ҫ�󺣰�
			criteria.setBearingRequired(false);// ��Ҫ��λ
			criteria.setCostAllowed(false);// �������л���
			criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���
			String provider = locationManager.getBestProvider(criteria, true);
			// ������һ�α仯��λ��
			Location location = locationManager.getLastKnownLocation(provider);
	 		LogUtil.logInfo(GPSUtil.class, "get location");
			if (location != null) {
	 			double latitude = location.getLatitude();
	 			double longitude = location.getLongitude();
	 			LogUtil.logInfo(GPSUtil.class, "ά�ȣ�" + latitude + "\n����" + longitude);
	 		}
	    }
		
	}

	public static boolean isopenGPS() {
		// �õ�ϵͳ���� ��LocationManager ����
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
