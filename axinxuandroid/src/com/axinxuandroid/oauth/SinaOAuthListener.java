package com.axinxuandroid.oauth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.Toast;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
 

public class SinaOAuthListener implements WeiboAuthListener {
	 private Oauth2AccessToken mAccessToken;
	 private String access_token;
	 private NetFinishListener listener;
	 private OAuthResult result;
 	 public SinaOAuthListener(){
		 result=new OAuthResult();
		 result.setType(OAuthConstant.SINA_OAUTH_TYPE);
	 }
	/**
	 * 用户取消授权后调用该方法
	 */
	@Override
	public void onCancel() {
  		result.setResult(OAuthResult.RESULT_OF_CANCEL);
		result.setMessage("已经取消授权!");
 		this.onfinish(result);
	}

	/**
	 * 用户授权成功后调用该方法
	 */
	@Override
	public void onComplete(Bundle values) {
       mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        if (mAccessToken.isSessionValid()) {
              access_token= mAccessToken.getToken();
              result.setAccesstoken(access_token);
              result.setExpirein(mAccessToken.getExpiresTime());
              result.setOauthlogintime(DateUtil.dateToStrWithPattern(new Date(), "yyyy-MM-dd HH:mm:ss"));
             // LogUtil.logInfo(getClass(), "access_token:"+access_token);
              getSinaUserInfo( mAccessToken.getUid());
        }else{
 	   		result.setResult(OAuthResult.RESULT_OF_ERROR);
	   		result.setMessage("新浪授权失败!");
	    	this.onfinish(result);
        }

	}
 

	/**
	 * 读取sina用户信息
	 */
	private void getSinaUserInfo(String uid){
  		if(uid!=null){
			String url=OAuthConstant.Sina.GET_USER_INFO+"?access_token="+access_token+"&uid="+uid;
			byte[] data=HttpUtil.readStream(url);
			LogUtil.logInfo(SinaOAuthListener.class,url);
 			if(data!=null){
				try {
					JSONObject jsonObject = new JSONObject(new String(data,"utf-8"));
 				   	result.setResult(OAuthResult.RESULT_OF_SUCCESS);
				   	result.setUserid(uid);
				   	result.setImgurl(jsonObject.getString("profile_image_url"));
				   	result.setName(jsonObject.getString("name"));
				   	LogUtil.logInfo(SinaOAuthListener.class,jsonObject.getString("profile_image_url"));
				    
  				} catch (Exception e) {
 					e.printStackTrace();
				}
			}else{
				result.setResult(OAuthResult.RESULT_OF_ERROR);
 				result.setMessage("获取用户信息失败");
			}
 		}else{
 			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("获取用户信息失败");
 		}
		this.onfinish(result);
	}
//	/**
//	 * OAuth授权之后，获取授权用户的UID
//	 * @return
//	 */
//	private String getUserId(){
//		if(access_token!=null){
//			String url=OAuthConstant.Sina.GET_USER_ID+"?access_token="+access_token;
//			byte[] data=HttpUtil.readStream(url);
//			if(data!=null){
//				try {
//					JSONObject jsonObject = new JSONObject(new String(data));
//					return jsonObject.getString("uid");
//				} catch (JSONException e) {
// 					e.printStackTrace();
//				}
//			}
//		}
//		return null;
//	}
	
	public void setFinishListener(NetFinishListener listener){
		this.listener=listener;
	}
	private void  onfinish(OAuthResult result){
  		if(this.listener!=null)
  			this.listener.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, null, result));
 	}
	@Override
	public void onWeiboException(WeiboException arg0) {
 		 result.setResult(OAuthResult.RESULT_OF_ERROR);
	   	 result.setMessage("新浪授权出现异常:"+arg0.getMessage());
	     this.onfinish(result);
	}
}
