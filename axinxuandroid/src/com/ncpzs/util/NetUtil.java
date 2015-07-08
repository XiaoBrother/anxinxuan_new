package com.ncpzs.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.axinxuandroid.sys.gloable.Gloable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

public class NetUtil {
  public static  String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
 				NetworkInterface intf = en.nextElement();
 				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {

					InetAddress inetAddress = enumIpAddr.nextElement();
 					if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) {
 						return inetAddress.getHostAddress().toString();
					}
 				}
 			}
 		} catch (SocketException ex) {
            ex.printStackTrace() ;
 		}
 		return null;
 	}

//	/**
//	 * 
//	 * ��ȡmac
//	 * 
//	 * 
//	 * 
//	 * @return
//	 */
//
//	private String getLocalMacAddress() {
//
//		WifiManager wifi = (WifiManager) this.getBaseContext()
//				.getSystemService(Context.WIFI_SERVICE);
//
//		WifiInfo info = wifi.getConnectionInfo();
//		Log.e(TAG, "WIFI MAC Address: " + info.getMacAddress());
//		Log.e(TAG, "WIFI First IP Address: " + info.getIpAddress());
//		String ip = intToIp(info.getIpAddress());
//		Log.e(TAG, "WIFI Normal IP Address: " + ip);
//		return info.getMacAddress();
//	}
//
//	private String intToIp(int i) {
//
//		return (i & 0xFF) + "." +
//
//		((i >> 8) & 0xFF) + "." +
//
//		((i >> 16) & 0xFF) + "." +
//
//		(i >> 24 & 0xFF);
//
//	}
  
  /**
   * ��������Ƿ�����
   * @return
   */
  public static boolean isInNetworkState() {
          boolean flag = false;
          //�õ�����������Ϣ
          ConnectivityManager manager =  (ConnectivityManager)   Gloable.getInstance().getCurContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
          //�ж������Ƿ�����
          if (manager.getActiveNetworkInfo() != null) {
                  flag = manager.getActiveNetworkInfo().isAvailable();
          }
          return flag;
  }
  

//  /**
//   * ����δ����ʱ���������÷���
//   */
//  private void setNetwork(){
//          Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();
//         
//          AlertDialog.Builder builder = new AlertDialog.Builder(this);
//          builder.setIcon(R.drawable.ic_launcher);
//          builder.setTitle("������ʾ��Ϣ");
//          builder.setMessage("���粻���ã���������������������磡");
//          builder.setPositiveButton("����", new OnClickListener() {
//                  @Override
//                  public void onClick(DialogInterface dialog, int which) {
//                          Intent intent = null;
//                          /**
//                           * �ж��ֻ�ϵͳ�İ汾�����API����10 ����3.0+
//                           * ��Ϊ3.0���ϵİ汾�����ú�3.0���µ����ò�һ�������õķ�����ͬ
//                           */
//                          if (android.os.Build.VERSION.SDK_INT > 10) {
//                                  intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
//                          } else {
//                                  intent = new Intent();
//                                  ComponentName component = new ComponentName(
//                                                  "com.android.settings",
//                                                  "com.android.settings.WirelessSettings");
//                                  intent.setComponent(component);
//                                  intent.setAction("android.intent.action.VIEW");
//                          }
//                          startActivity(intent);
//                  }
//          });
//
//          builder.setNegativeButton("ȡ��", new OnClickListener() {
//                  @Override
//                  public void onClick(DialogInterface dialog, int which) {
//
//                  }
//          });
//          builder.create();
//          builder.show();
//  }
  
  /**
   * �ж�wifi�Ƿ�����
   * ����һЩ�Լ����߼�����
   */
  public static boolean isWifiNetwork(){
 	  ConnectivityManager manager = (ConnectivityManager)   Gloable.getInstance().getCurContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
      State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
      if(wifi == State.CONNECTED || wifi == State.CONNECTING){
              return true;
      }
       return false;  
  }
}
