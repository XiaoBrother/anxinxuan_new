package com.axinxuandroid.service;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.oauth.YukuOAuth.YukuOAuthInfo;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.UserCookie;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;

public class SharedPreferenceService {
	  /**
	   * 保存用户信息
	   * @param user
	   */
	  public static void saveLastLoginUser(User user){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putInt(UserCookie.USER_ID, user.getUser_id());
	      editor.putString(UserCookie.USER_NAME, user.getUser_name());
	      editor.putString(UserCookie.PHONE, user.getPhone());
	      editor.putString(UserCookie.PWD, user.getPwd());
	      editor.putString(UserCookie.DATE, user.getLogin_time());
	      editor.putInt(UserCookie.LOGINTYPE, user.getLogintype());
	      editor.putString(UserCookie.ACCESSTOKEN, user.getAccesstoken());
	      editor.putLong(UserCookie.EXPIREIN, user.getExpirein());
	      editor.putString(UserCookie.OAUTHLOGINTIME, user.getOauthlogintime());
	      editor.commit();
	  }
	  
	  public static User getLastLoginUser(){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
		  if(uiState!=null){
			  User user=new User();
			  user.setUser_id(uiState.getInt(UserCookie.USER_ID, -1));
			  user.setUser_name(uiState.getString(UserCookie.USER_NAME, null));
			  user.setPhone(uiState.getString(UserCookie.PHONE, null));
			  user.setLogin_time(uiState.getString(UserCookie.DATE, null));
			  user.setAccesstoken(uiState.getString(UserCookie.ACCESSTOKEN, null));
			  user.setLogintype(uiState.getInt(UserCookie.LOGINTYPE, 0));
			  user.setExpirein(uiState.getLong(UserCookie.EXPIREIN,0));
			  user.setOauthlogintime(uiState.getString(UserCookie.OAUTHLOGINTIME, null));
			  return user;
		  }
		  return null;
 	  }
	  /**
	   * 保存版本号
	   * @param user
	   */
	  public static void saveVersion(float version){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(AppConstant.SYSTEM_VERSION,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putFloat("version", version);
 	      editor.commit();
	  }
	  /**
	   * 获取版本号
	   * 
	   */
	public static float getVersion() {
		SharedPreferences uiState = Gloable.getInstance().getCurContext()
				.getSharedPreferences(AppConstant.SYSTEM_VERSION,
						Context.MODE_PRIVATE);
		float version = -1;
		if (uiState != null) {
			version = uiState.getFloat("version", -1);
 		}
		try {
		   float appversion = Float.parseFloat(Gloable.getInstance()
						.getCurContext().getPackageManager().getPackageInfo(
								"com.axinxuandroid.activity", 0).versionName);// 获取AndroidManifest.xml定义的android:versionCode
			 if(version<appversion) version=appversion;
		} catch (NameNotFoundException e) {
				// e.printStackTrace();
		}
		LogUtil.logInfo(SharedPreferenceService.class, version + "");
		return version;
	}
	  /**
	   * 保存用户最后一个访问的批次号码
	   * @param user
	   */
	  public static void saveLastVistBatch(String batchcode){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putString(UserCookie.LAST_VISIT_BATCH, batchcode);
 	      editor.commit();
	  }
	  /**
	   * 获取用户最后一个访问的批次号码
	   * @param user
	   */
	  public static String  getLastVistBatch(){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
          return uiState.getString(UserCookie.LAST_VISIT_BATCH, null);
	  }
	  
	  public static void saveYukuRefreshToken(String refreshtoken){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putString(UserCookie.YUKU_REFRESH_TOKEN, refreshtoken);
 	      editor.commit();
	  }
	  
	  public static String getYukuRefreshToken(){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
          return uiState.getString(UserCookie.YUKU_REFRESH_TOKEN, null);
	  }
	  
	  /**
	   * 保存用户最后更新日志时间
	   * @param user
	   */
	  public static void saveLastUpdateTime(Date date){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putString(UserCookie.LAST_UPDATE_TIME, DateUtil.DateToStr(date, "yyyy-MM-dd hh:mm"));
 	      editor.commit();
	  }
	  /**
	   * 获取用户最后更新日志时间
	   * @param user
	   */
	  public static String  getLastUpdateTime(){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
          return uiState.getString(UserCookie.LAST_UPDATE_TIME, null);
	  }
	  
	  private static  final String ERROR_LOG="error_log";
	  private static int logcount=0;
	  public static void saveErrorMsg(String msg){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(ERROR_LOG,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putString(ERROR_LOG+logcount++, msg);
 	      editor.commit();
	  }
	  
	  /**
	   * 保存app启动次数
	   * @param user
	   */
	  public static void saveAppStartCount(int count){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putInt(UserCookie.START_APP_COUNT, count);
	      editor.commit();
	  }
	  /**
	   * 获取app启动次数
	   * @param user
	   */
	  public static int  getAppStartCount(){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
          return uiState.getInt(UserCookie.START_APP_COUNT, 0);
	  }
	  
	  
	  /**
	   * 保存页面引导次数
	   * @param user
	   */
	  public static void saveLeadViewCount(String clzname, int count){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putInt(clzname, count);
	      editor.commit();
	  }
	  /**
	   * 获取页面引导次数
	   * @param user
	   */
	  public static int  getLeadViewCount(String clzname){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
          return uiState.getInt(clzname, 0);
	  }
	  
	  /**
	   * 保存用户位置信息
	   * @param user
	   */
	  public static void saveUserLocation(double lat, double ltu){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor=uiState.edit();
	      editor.putString(UserCookie.USER_LOCATION_LAT, lat+"");
	      editor.putString(UserCookie.USER_LOCATION_LTU, ltu+"");
	      editor.commit();
	  }
	  /**
	   * 获取用户位置信息
	   * @param user
	   */
	  public static double[]  getUserLocation(){
		  SharedPreferences uiState=Gloable.getInstance().getCurContext().getSharedPreferences(UserCookie.USER_COOKIE,Context.MODE_PRIVATE);
          double[] pos=new double[2];
          pos[0]=Double.parseDouble(uiState.getString(UserCookie.USER_LOCATION_LAT, "0"));
          pos[1]=Double.parseDouble(uiState.getString(UserCookie.USER_LOCATION_LTU, "0"));
          return pos;
	  }
}
