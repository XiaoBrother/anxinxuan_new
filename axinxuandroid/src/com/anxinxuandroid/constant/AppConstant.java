package com.anxinxuandroid.constant;

import android.os.Environment;

public class AppConstant {
	public static final int NET_RETURN_MAX_COUNT=10;
	public static final String VIDEO_DIR = Environment.getExternalStorageDirectory()+ "/AXX/Video/";
 	
 	public static final String MEDIA_DIR = Environment.getExternalStorageDirectory() + "/AXX/MEDIA/";
	public static final String PHOTO_DIR = Environment.getExternalStorageDirectory() + "/AXX/Picture/";
	//public static final String PHOTO_THUMB_DIR = Environment.getExternalStorageDirectory() + "/AXX/Picture/thumb/";
	public static final String CAMERA_DIR = Environment.getExternalStorageDirectory() + "/AXX/Camera/";  
	
	
	public static final String SYSTEM_VERSION_PATH = Environment.getExternalStorageDirectory() + "/AXX/version/"; 
	public static final String SYSTEM_VERSION =  "system_version";  
	
	public static final String BANNER_DIR = Environment.getExternalStorageDirectory()+ "/AXX/banner/";
	public static final String USERIMG_DIR = Environment.getExternalStorageDirectory()+ "/AXX/userimg/";
	
	public static final String ROOT_DIR = Environment.getExternalStorageDirectory()+ "/AXX/";
	
	/**
	 * 联网状况
	 * @author Administrator
	 *
	 */
	 public class NetStatus{
	    	public static final int CONNECT=1;
	    	public static final int UNCONNECT=0;
	 }
	 /**
	  * 联网最长时间，超过自动断开
	  */
	 public static final int AUTO_CLOSE_OUTTIME=20;

}
