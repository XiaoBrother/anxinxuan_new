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
	  //�����ǰ�豸û�а�װ��Ѷ΢���ͻ��ˣ�������
		@Override
		public void onWeiBoNotInstalled() {
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("���Ȱ�װQQ΢���ͻ���!");
	 		this.onfinish(result);
		}

		//�����ǰ�豸û��װָ���汾��΢���ͻ��ˣ�������
		@Override
		public void onWeiboVersionMisMatch() {
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("΢���ͻ��˰汾���ͣ��������汾!");
	 		this.onfinish(result);
		}

		//�����Ȩʧ�ܣ�������
		@Override
		public void onAuthFail(int resultstatus, String err) {
			result.setResult(OAuthResult.RESULT_OF_ERROR);
			result.setMessage("��Ȩʧ��:"+err);
	 		this.onfinish(result);
 		}

		//��Ȩ�ɹ���������
		//��Ȩ�ɹ������е���Ȩ��Ϣ�Ǵ����WeiboToken��������ģ����Ը��ݾ����ʹ�ó���������Ȩ��Ϣ��ŵ��Լ�������λ�ã�
		//�������ŵ���applicationcontext��
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
	 				result.setMessage("��ȡ�û���Ϣʧ��");
				}
	 		}else{
	 			result.setResult(OAuthResult.RESULT_OF_ERROR);
				result.setMessage("��ȡ�û���Ϣʧ��");
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
