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
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
 
 

public class WeiXinOAuthListener implements IWXAPIEventHandler {
 	 private String access_token;
	 private NetFinishListener oauthlistener;
	 private NetFinishListener sendmsglistener;
	 private String userid;
	 private OAuthResult result;
	 public static final int RESPONSE_OAUTH=1;
	 public static final int RESPONSE_SEND_MSG=2;
	 public WeiXinOAuthListener(){
		 result=new OAuthResult();
		 result.setType(OAuthConstant.WEIXIN_OAUTH_TYPE);
	 }

	@Override
	public void onReq(BaseReq req) {
 		
	}

	@Override
	public void onResp(BaseResp resp) {
  		if(resp.getType()==ConstantsAPI.COMMAND_SENDAUTH){//微信oauth登陆返回处理
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					getToken(resp);
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					result.setResult(OAuthResult.RESULT_OF_CANCEL);
					result.setMessage("已经取消授权!");
					break;
		 		default:
					result.setResult(OAuthResult.RESULT_OF_CANCEL);
					result.setMessage("授权失败！");
					break;
			}	
		}else if(resp.getType()==ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){//分享信息到微博返回处理
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					result.setMessage("分享成功!");
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
 					result.setMessage("已取消分享!");
					break;
		 		default:
 					result.setMessage("分享失败！");
					break;
			}
			this.onfinish(result,RESPONSE_SEND_MSG);
		}
  	}
 
	public void getToken(BaseResp resp){
		if(resp!=null){
			SendAuth.Resp res= (SendAuth.Resp) resp;
			String url=OAuthConstant.WebChat.GET_ACCESS_TOKEN+"?appid="+OAuthConstant.WebChat.APP_ID+"&secret="+OAuthConstant.WebChat.APP_KEY+"&code="+res.code+"&grant_type=authorization_code";
			byte[] data=HttpUtil.readStream(url);
			LogUtil.logInfo(WeiXinOAuthListener.class,url);
 			if(data!=null){
				try {
					JSONObject jsonObject = new JSONObject(new String(data));
					access_token=jsonObject.getString("access_token");
					userid=jsonObject.getString("openid");
					getUserInfo();
 				    
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
		this.onfinish(result,RESPONSE_OAUTH);
	}
	
	public void getUserInfo(){
		if(access_token!=null&&userid!=null){
 			String url=OAuthConstant.WebChat.GET_USER_INFO+"?access_token="+access_token+"&openid="+userid;
			byte[] data=HttpUtil.readStream(url);
			LogUtil.logInfo(WeiXinOAuthListener.class,url);
 			if(data!=null){
				try {
					JSONObject jsonObject = new JSONObject(new String(data));
					result.setResult(OAuthResult.RESULT_OF_SUCCESS);
				   	result.setUserid(userid);
				   	result.setImgurl(jsonObject.getString("headimgurl"));
				   	result.setName(jsonObject.getString("nickname"));
				   	LogUtil.logInfo(WeiXinOAuthListener.class,userid);
				    
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
		this.onfinish(result,RESPONSE_OAUTH);
	}
	private void  onfinish(OAuthResult result,int type){
  		if(type==RESPONSE_OAUTH&&this.oauthlistener!=null)
  			this.oauthlistener.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, null, result));
  		if(type==RESPONSE_SEND_MSG&&this.sendmsglistener!=null)
  			this.sendmsglistener.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, null, result));
 	}
	public void setOAuthFinishListener(NetFinishListener listener){
		this.oauthlistener=listener;
	}
	public void setSendMsgFinishListener(NetFinishListener listener){
		this.sendmsglistener=listener;
	}
}
