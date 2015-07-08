package com.axinxuandroid.sys.gloable;

/**
 * @author 存放最近一次登陆的用户信息
 * 
 */
public class UserCookie {
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String PHONE = "phone";
	public static final String PWD = "pwd";
	public static final String DATE = "date";// 保存日期
	public static final String LOGINTYPE = "logintype";// 登录方式
	public static final String ACCESSTOKEN = "accesstoken";//  访问令牌
	public static final String EXPIREIN = "expirein";//  过期时间
	public static final String OAUTHLOGINTIME = "oauthlogintime";//  oauth登录时间
	public static final String LAST_VISIT_BATCH = "last_visit_batch";// 用户最后访问的一条批次
	public static final String USER_COOKIE = "user_cookie";
	
	public static final String YUKU_REFRESH_TOKEN = "user_refresh_token";
	
	public static final String LAST_UPDATE_TIME = "last_update_time";// 用户最后更新日期
	
	public static final String START_APP_COUNT = "start_app_count";// 启动app次数
	
	public static final String USER_LOCATION_LAT = "user_location_lat";// 用户坐标:纬度
	public static final String USER_LOCATION_LTU = "user_location_ltu";// 用户坐标:经度
}
