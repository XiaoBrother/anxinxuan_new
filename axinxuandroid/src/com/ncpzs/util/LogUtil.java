package com.ncpzs.util;

import android.util.Log;

public class LogUtil {
    private static final String subfix="anxinxuan_";
	public static void logInfo(Class clz,String info){
		if(clz!=null)
		Log.e(clz.getName(), info==null?"":info);
	}
	
	public static void logWarn(Class clz,String info){
		if(clz!=null)
		Log.e(clz.getName(), info==null?"":info);
	}
	
	public static void logError(Class clz,String info){
		if(clz!=null)
		Log.e(clz.getName(), info==null?"":info);
	}
}
