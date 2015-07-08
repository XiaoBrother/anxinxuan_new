package com.axinxuandroid.oauth;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;

/**
 * 优酷上传（登录->）
 * @author Administrator
 *
 */
public class YukuOAuth {

	 public static final String LOGIN_URL="https://openapi.youku.com/v2/oauth2/token";
	 public static final String REFRESH_URL="https://openapi.youku.com/v2/oauth2/token";
	 //public static final String REFRESH_URL=HttpUrlConstant.URL_HEAD+"/anxinxuan/phone/oauthRedirect";
	 public static final String UPLOADVIDEOINFO_URL="https://openapi.youku.com/v2/uploads/create.json";
	 public static final String DEFAULT_TITLE="安心选";
	 public static final String DEFAULT_TAG="安心选";
	 public static final String FILE_EXT="3gp";
 	 public static final String CLIENT_ID      = "9cddb02df7281131";
	 public static final String CLIENT_SECRET      = "d014fe912482493aac678107a0a0fce2";
	 public static final String REDIRECT_URL      = "http://www.anxinxuan.com/soauth/oauth_call/type/4";
	 public static final String SINGLE_VIDEO_INFO      = "https://openapi.youku.com/v2/videos/show_basic.json";
	/**
	 * 第一步，登录到优酷,获取token
	 * https://openapi.youku.com/v2/oauth2/token
	 * client_id：sting 必选参数 appkey
       client_secret：string 必选参数 appsecret
       grant_type：string 必选参数 授权类型。对于登录用户，只支持grant_type=password
       username：string 必选参数 用户名或email
       password：string 必选参数 用户明文密码
	       返回值
		access_token：string 不为空 用来调用其他已授权的API接口的凭证
		expires_in：int 不为空 access_token有效时长，单位：秒
		refresh_token string 不为空 刷新Token，用于获取新的access_token
		token_type string 不为空 使用access_token访问资源的方式，默认为bearer
 	 */
//	public YukuOAuthInfo login(){
//		String params="client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&grant_type=password&username="+USERNAME+"&password="+USERPWD;
//        
//		try{
//			LogUtil.logInfo(getClass(), ",login:"+params);
//			String datas=HttpUtil.readStreamWithPost(LOGIN_URL,params);
// 			if(datas!=null){
//				
// 				JSONObject jsonObject = new JSONObject(new String(datas));
//				YukuOAuthInfo info=new YukuOAuthInfo();
//				info.access_token=jsonObject.getString("access_token");
//				info.expires_in=jsonObject.getInt("expires_in");
//				info.refresh_token=jsonObject.getString("refresh_token");
//				info.token_type=jsonObject.getString("token_type");
//				Session.getInstance().setAttribute(SessionAttribute.SESSION_YUKU_OAUTH, info);
//				SharedPreferenceService.saveYukuRefreshToken(info.refresh_token);
//				return info;
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return null;
//	}
	/**
	 * 根据refresh_token获取token
	 * 参数
		client_id：string 必选参数 appkey
		client_secret：string 必选参数 appsecret
		grant_type：string 必选参数 对应刷新token时，只支持grant_type=refresh_token
		refresh_token：string 必选参数 刷新token（之前登录时调用接口获取的返回字段值），用于获取新的access_token
		返回值
		access_token：string 不为空 新的access_token，用来调用其他已授权的API接口的凭证
		expires_in：int 不为空 access_token有效时长，单位：秒
		refresh_token string 不为空 刷新Token，用于获取新的access_token
		token_type string 不为空 使用access_token访问资源的方式，默认为bearer
	 */
	public YukuOAuthInfo getTokenByRefreshToken(String refresh_token){
 		if(refresh_token==null) return null;
		String params="client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&grant_type=refresh_token&refresh_token="+refresh_token;
		try{
			String datas=HttpUtil.readStreamWithPost(REFRESH_URL, params);
			if(datas!=null){
				LogUtil.logInfo(getClass(), "refresh:"+new String(datas));
				JSONObject jsonObject = new JSONObject(new String(datas));
				YukuOAuthInfo info=new YukuOAuthInfo();
				info.access_token=jsonObject.getString("access_token");
				info.expires_in=jsonObject.getInt("expires_in");
				info.refresh_token=jsonObject.getString("refresh_token");
				info.token_type=jsonObject.getString("token_type");
				Session.getInstance().setAttribute(SessionAttribute.SESSION_YUKU_OAUTH, info);
 				SharedPreferenceService.saveYukuRefreshToken(info.refresh_token);
				return info;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	 
	  
	/**
	 * 域名转ip地址
	 */
	private String GetInetAddress(String host){
	    String IPAddress = "";    
	    InetAddress ReturnStr1 = null;   
	    try {   
	        ReturnStr1 = java.net.InetAddress.getByName(host);   
	        IPAddress = ReturnStr1.getHostAddress();   
	    } catch (UnknownHostException e) {   
 	        e.printStackTrace();   
	        return  IPAddress;   
	    }   
	    return IPAddress;   
	}  


	public class YukuOAuthInfo{
		public String access_token;
		public int expires_in;
		public String refresh_token;
		public String token_type;
	}
	
	public class UploadToken{
		public String upload_token;
		public String instant_upload_ok;
		public String upload_server_uri;
		public String upload_ip;
	}
	
	public class SliceInfo{
		public int slice_task_id;
		public long offset;
		public int length;
		public long transferred;
		public boolean finished;
	}
	public class UploadStatus{
		public int status;
  		public int transferred_percent;
		public int confirmed_percent;
		public int empty_tasks;
		public boolean finished;
		public String upload_server_ip;
	}
	
 
}
