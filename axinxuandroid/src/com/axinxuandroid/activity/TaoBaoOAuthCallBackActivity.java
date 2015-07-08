package com.axinxuandroid.activity;

import android.content.Intent;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.OAuthResult;
import com.axinxuandroid.oauth.TaoBaoOAuthListener;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.LogUtil;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AuthActivity;
import com.taobao.top.android.auth.AuthorizeListener;

public class TaoBaoOAuthCallBackActivity extends AuthActivity{

	private TaoBaoOAuthListener listener;
	@Override
	protected AuthorizeListener getAuthorizeListener() {
 		listener=new TaoBaoOAuthListener();
		listener.setFinishListener(new NetFinishListener() {
 			 
			@Override
			public void onfinish(NetResult data) {
				LogUtil.logInfo(getClass(), "top listener  finish....");
				 OAuthResult result=(OAuthResult) data.returnData;
		     	 NcpzsHandler messagehandler=Gloable.getInstance().getCurHandler();
		     	 if(result.getResult()==-1){ //用户自动取消授权
		 			messagehandler.excuteMethod(new MessageDialogHandlerMethod("取消授权",result.getMessage()));
		     	 }else if (result.getResult()==0){//授权出现问题
		 			messagehandler.excuteMethod(new MessageDialogHandlerMethod("授权失败",result.getMessage()));
		     	 }else if(result.getResult()==1){//授权成功 ，获取用户信息成功
		     		 Intent resultIntent = new Intent();
		     		 Session.getInstance().setAttribute(SessionAttribute.SESSION_OAUTH_RESULT, result);
		       		 resultIntent.setClass(TaoBaoOAuthCallBackActivity.this, OAuthLoginActivity.class);
		      		 startActivity(resultIntent);
		     	}	
			}
		});
 		return listener;
	}

	@Override
	protected TopAndroidClient getTopAndroidClient() {
 		return TopAndroidClient.getAndroidClientByAppKey(OAuthConstant.TaoBao.APP_ID);
	}

}
