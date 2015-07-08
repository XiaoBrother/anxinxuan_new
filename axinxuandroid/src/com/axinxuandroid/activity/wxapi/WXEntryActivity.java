package com.axinxuandroid.activity.wxapi;


import java.util.Map;

import com.axinxuandroid.activity.LoginActivity;
import com.axinxuandroid.activity.NcpZsActivity;
import com.axinxuandroid.activity.OAuthLoginActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.OAuthResult;
import com.axinxuandroid.oauth.WeiXinOAuthListener;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
 
import com.ncpzs.util.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
 


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WXEntryActivity extends NcpZsActivity  {
	private NcpzsHandler  handler;
    private IWXAPI api;
	private WeiXinOAuthListener lis;
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.weixinresp);
         handler=Gloable.getInstance().getCurHandler();
         // 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this, OAuthConstant.WebChat.APP_ID, false);
    	lis= new WeiXinOAuthListener();
    	lis.setOAuthFinishListener(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
 				 
 				 OAuthResult result=(OAuthResult) data.returnData;
 				if(result.getResult()==-1){ //用户自动取消授权
 					handler.excuteMethod(new MessageDialogHandlerMethod("取消授权",result.getMessage()));
 					WXEntryActivity.this.finish();
 		     	 }else if (result.getResult()==0){//授权出现问题
 		     		handler.excuteMethod(new MessageDialogHandlerMethod("授权失败",result.getMessage()));
 		     		WXEntryActivity.this.finish();
 		     	 }else if(result.getResult()==1){//授权成功 ，获取用户信息成功
  	 				 Intent resultIntent = new Intent();
 	 	     		 Session.getInstance().setAttribute(SessionAttribute.SESSION_OAUTH_RESULT, result);
 	 	       		 resultIntent.setClass(WXEntryActivity.this, OAuthLoginActivity.class);
 	 	      		 startActivity(resultIntent);
 		     	 }
 			}
		});
    	lis.setSendMsgFinishListener(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
 				OAuthResult result=(OAuthResult) data.returnData;
 				handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 					@Override
					public void onHandlerFinish(Object result) {
 						WXEntryActivity.this.finish();
					}
				});
 				handler.excuteMethod(new MessageDialogHandlerMethod("",result.getMessage()));
 				 
 			}
		});
    	api.handleIntent(getIntent(),lis);
     }
 
 
	 
}