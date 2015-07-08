package com.axinxuandroid.oauth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.Toast;

import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.NetUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.taobao.top.android.TopAndroidClient;
import com.tencent.weibo.sdk.android.api.UserAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
 

public class QQWeiBoOAuthListener implements OnAuthListener {
 	 private String access_token,openid,clientip;
	 private NetFinishListener listener;
	 private OAuthResult result;
	    public QQWeiBoOAuthListener() {
			result = new OAuthResult();
			result.setType(OAuthConstant.QQWEIBO_OAUTH_TYPE);
			clientip=Util.getLocalIPAddress(Gloable.getInstance().getCurContext());
 		}
	  //如果当前设备没有安装腾讯微博客户端，走这里
		@Override
		public void onWeiBoNotInstalled() {
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("请先安装QQ微博客户端!");
	 		this.onfinish(result);
		}

		//如果当前设备没安装指定版本的微博客户端，走这里
		@Override
		public void onWeiboVersionMisMatch() {
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("微博客户端版本过低，请升级版本!");
	 		this.onfinish(result);
		}

		//如果授权失败，走这里
		@Override
		public void onAuthFail(int resultstatus, String err) {
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("授权失败:"+err);
	 		this.onfinish(result);
 		}

		//授权成功，走这里
		//授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
		//在这里，存放到了applicationcontext中
		@Override
		public void onAuthPassed(String name, WeiboToken token) {
			access_token=token.accessToken;
 			result.setAccesstoken(access_token);
 			result.setExpirein(token.expiresIn);
 			result.setOauthlogintime(DateUtil.dateToStrWithPattern(new Date(), "yyyy-MM-dd HH:mm:ss"));
			openid=token.openID;
			result.setUserid(openid);
			getUserInfo();
		}
		
		public void getUserInfo(){
			if(access_token!=null){
  				String url=OAuthConstant.QQWeiBo.GET_USER_INFO+"?format=json&oauth_consumer_key="+OAuthConstant.QQWeiBo.APP_KEY+"&access_token="+access_token+"&openid="+openid+"&clientip="+clientip+"&oauth_version=2.a&scope=all";
				byte[] data=HttpUtil.readStream(url);
 	 			if(data!=null){
 	 				LogUtil.logInfo(QQWeiBoOAuthListener.class,url+":"+new String(data));
					try {
						JSONObject jsonObject = new JSONObject(new String(data,"utf-8"));
						if(jsonObject.getInt("errcode")==0){
							result.setResult(OAuthResult.RESULT_OF_SUCCESS);
							JSONObject datas=jsonObject.getJSONObject("data");
	 					   	result.setImgurl(datas.getString("head"));
						   	result.setName(datas.getString("name"));
 						}
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
		private void  onfinish(OAuthResult result){
	  		if(this.listener!=null)
				this.listener.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, null, result));
		}
		public void setFinishListener(NetFinishListener listener){
			this.listener=listener;
		}
}
