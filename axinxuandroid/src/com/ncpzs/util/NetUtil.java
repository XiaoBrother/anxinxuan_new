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
//	 * 获取mac
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
   * 检测网络是否连接
   * @return
   */
  public static boolean isInNetworkState() {
          boolean flag = false;
          //得到网络连接信息
          ConnectivityManager manager =  (ConnectivityManager)   Gloable.getInstance().getCurContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
          //判断网络是否连接
          if (manager.getActiveNetworkInfo() != null) {
                  flag = manager.getActiveNetworkInfo().isAvailable();
          }
          return flag;
  }
  

//  /**
//   * 网络未连接时，调用设置方法
//   */
//  private void setNetwork(){
//          Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();
//         
//          AlertDialog.Builder builder = new AlertDialog.Builder(this);
//          builder.setIcon(R.drawable.ic_launcher);
//          builder.setTitle("网络提示信息");
//          builder.setMessage("网络不可用，如果继续，请先设置网络！");
//          builder.setPositiveButton("设置", new OnClickListener() {
//                  @Override
//                  public void onClick(DialogInterface dialog, int which) {
//                          Intent intent = null;
//                          /**
//                           * 判断手机系统的版本！如果API大于10 就是3.0+
//                           * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
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
//          builder.setNegativeButton("取消", new OnClickListener() {
//                  @Override
//                  public void onClick(DialogInterface dialog, int which) {
//
//                  }
//          });
//          builder.create();
//          builder.show();
//  }
  
  /**
   * 判断wifi是否连接
   * 设置一些自己的逻辑调用
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
