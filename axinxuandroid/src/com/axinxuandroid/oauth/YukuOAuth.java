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
 * �ſ��ϴ�����¼->��
 * @author Administrator
 *
 */
public class YukuOAuth {

	 public static final String LOGIN_URL="https://openapi.youku.com/v2/oauth2/token";
	 public static final String REFRESH_URL="https://openapi.youku.com/v2/oauth2/token";
	 //public static final String REFRESH_URL=HttpUrlConstant.URL_HEAD+"/anxinxuan/phone/oauthRedirect";
	 public static final String UPLOADVIDEOINFO_URL="https://openapi.youku.com/v2/uploads/create.json";
	 public static final String DEFAULT_TITLE="����ѡ";
	 public static final String DEFAULT_TAG="����ѡ";
	 public static final String FILE_EXT="3gp";
 	 public static final String CLIENT_ID      = "9cddb02df7281131";
	 public static final String CLIENT_SECRET      = "d014fe912482493aac678107a0a0fce2";
	 public static final String REDIRECT_URL      = "http://www.anxinxuan.com/soauth/oauth_call/type/4";
	 public static final String SINGLE_VIDEO_INFO      = "https://openapi.youku.com/v2/videos/show_basic.json";
	/**
	 * ��һ������¼���ſ�,��ȡtoken
	 * https://openapi.youku.com/v2/oauth2/token
	 * client_id��sting ��ѡ���� appkey
       client_secret��string ��ѡ���� appsecret
       grant_type��string ��ѡ���� ��Ȩ���͡����ڵ�¼�û���ֻ֧��grant_type=password
       username��string ��ѡ���� �û�����email
       password��string ��ѡ���� �û���������
	       ����ֵ
		access_token��string ��Ϊ�� ����������������Ȩ��API�ӿڵ�ƾ֤
		expires_in��int ��Ϊ�� access_token��Чʱ������λ����
		refresh_token string ��Ϊ�� ˢ��Token�����ڻ�ȡ�µ�access_token
		token_type string ��Ϊ�� ʹ��access_token������Դ�ķ�ʽ��Ĭ��Ϊbearer
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
	 * ����refresh_token��ȡtoken
	 * ����
		client_id��string ��ѡ���� appkey
		client_secret��string ��ѡ���� appsecret
		grant_type��string ��ѡ���� ��Ӧˢ��tokenʱ��ֻ֧��grant_type=refresh_token
		refresh_token��string ��ѡ���� ˢ��token��֮ǰ��¼ʱ���ýӿڻ�ȡ�ķ����ֶ�ֵ�������ڻ�ȡ�µ�access_token
		����ֵ
		access_token��string ��Ϊ�� �µ�access_token������������������Ȩ��API�ӿڵ�ƾ֤
		expires_in��int ��Ϊ�� access_token��Чʱ������λ����
		refresh_token string ��Ϊ�� ˢ��Token�����ڻ�ȡ�µ�access_token
		token_type string ��Ϊ�� ʹ��access_token������Դ�ķ�ʽ��Ĭ��Ϊbearer
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
	 * ����תip��ַ
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
