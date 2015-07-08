package com.axinxuandroid.oauth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

public class TaoBaoOAuthListener implements AuthorizeListener {
	private OAuthResult result;
    private NetFinishListener listener;
	private String access_token;
	private String userid;
	private TopAndroidClient client;
	public TaoBaoOAuthListener() {
		result = new OAuthResult();
		result.setType(OAuthConstant.TAOBAO_OAUTH_TYPE);
		client=TopAndroidClient.getAndroidClientByAppKey(OAuthConstant.TaoBao.APP_ID);
	}

	@Override
	public void onAuthException(AuthException arg0) {
		LogUtil.logInfo(getClass(), "taobao oauth exception :"+arg0.getMessage());
		result.setResult(OAuthResult.RESULT_OF_ERROR);
		result.setMessage("淘宝授权异常:!"+arg0.getMessage());
 		this.onfinish(result);
	}

	@Override
	public void onComplete(AccessToken accessToken) {
		LogUtil.logInfo(getClass(), "taobao oauth complete ");
		access_token=accessToken.getAdditionalInformation().get(
				AccessToken.KEY_ACCESS_TOKEN);
		result.setAccesstoken(access_token);
		result.setExpirein(accessToken.getExpiresIn());
		result.setOauthlogintime(DateUtil.dateToStrWithPattern(new Date(), "yyyy-MM-dd HH:mm:ss"));
		userid = accessToken.getAdditionalInformation().get(
				AccessToken.KEY_SUB_TAOBAO_USER_ID);
		if (userid == null) {
			userid = accessToken.getAdditionalInformation().get(
					AccessToken.KEY_TAOBAO_USER_ID);
		}
		String nick=accessToken.getAdditionalInformation().get(AccessToken.KEY_SUB_TAOBAO_USER_NICK);
		if(nick==null){
			nick=accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_NICK);
			result.setName(nick);
		}
		result.setUserid(userid);
		//getTaobaoUserInfo();
		LogUtil.logInfo(getClass(), "taobao id:"+userid);
		result.setResult(OAuthResult.RESULT_OF_SUCCESS);
		this.onfinish(result);
	}
	/**
	 * 读取用户信息
	 */
	private void getTaobaoUserInfo(){
		TopParameters params=new TopParameters();
		params.setMethod("taobao.user.buyer.get");
		params.addFields("nick","avatar");//返回字段
		if(userid==null){
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("淘宝授权异常!");
	 		this.onfinish(result);
			return;
		}
		client.api(params, Long.parseLong(userid), new TopApiListener(){
 			@Override
			public void onComplete(JSONObject json) {
  				try {
 					result.setResult(OAuthResult.RESULT_OF_SUCCESS);
					result.setName(json.getString("nick"));
					result.setImgurl(json.getString("avatar"));
					onfinish(result);
 				} catch (JSONException e) {
 					e.printStackTrace();
 					result.setResult(OAuthResult.RESULT_OF_ERROR);
 					result.setMessage("获取淘宝关联用户信息失败:!");
 					onfinish(result);
 				}
  			}
 			@Override
			public void onError(ApiError error) {
 				result.setResult(OAuthResult.RESULT_OF_ERROR);
				result.setMessage("获取淘宝关联用户信息失败:!");
				onfinish(result);
			}

			@Override
			public void onException(Exception e) {
				result.setResult(OAuthResult.RESULT_OF_ERROR);
			    result.setMessage("获取淘宝关联用户信息失败:!");
			    onfinish(result);
				
	   }}, false);
	}
	@Override
	public void onError(AuthError arg0) {
		LogUtil.logInfo(getClass(), "taobao oauth error: "+arg0.getError());
		result.setResult(OAuthResult.RESULT_OF_ERROR);
		result.setMessage("淘宝授权异常:!"+arg0.getError());
 		this.onfinish(result);
	}
	public void setFinishListener(NetFinishListener listener){
		this.listener=listener;
	}
	private void  onfinish(OAuthResult result){
  		if(this.listener!=null)
			this.listener.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, null, result));
	}
}
