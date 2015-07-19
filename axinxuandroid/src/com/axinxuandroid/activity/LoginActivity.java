package com.axinxuandroid.activity;


import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadUserVilleageThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.UserLoginThread;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.OAuthResult;
import com.axinxuandroid.oauth.QQWeiBoOAuthListener;
import com.axinxuandroid.oauth.SinaOAuthListener;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.service.VilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.taobao.top.android.TopAndroidClient;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends NcpZsActivity {
	private EditText login_user; // 帐号编辑框
	private EditText login_pwd; // 密码编辑框
	private Button loginbtn,usertestbtn;
    private Button findpwdbtn;
    private Button registebtn;
    private ProgressDialog processDialog; 
    private UserService userservice;
	private UserVilleageService uservilleageservice;
	private VilleageService villeageservice;
    private ImageButton sinalogin,taobaologin,weixinlogin;
    //新浪授权
    /** 微博API接口类，提供登陆等功能  */
    private AuthInfo mWeibo;
    /** 注意：SsoHandler 仅当sdk支持sso时有效 */
    private SsoHandler mSsoHandler;
    private SinaOAuthListener listener;
     //淘宝授权
    private TopAndroidClient topclient;
    //qq微博
    private QQWeiBoOAuthListener qqweibolistener;
    //微信 
    private IWXAPI wxapi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_new);
        mWeibo = new AuthInfo(this,OAuthConstant.Sina.APP_KEY, OAuthConstant.Sina.REDIRECT_URL, OAuthConstant.Sina.SCOPE);
        wxapi= WXAPIFactory.createWXAPI(this, OAuthConstant.WebChat.APP_ID,true);
        wxapi.registerApp(OAuthConstant.WebChat.APP_ID);
        userservice=new UserService();
        login_user = (EditText)findViewById(R.id.login_user);
        login_pwd = (EditText)findViewById(R.id.login_pwd);
        loginbtn=(Button)findViewById(R.id.login_tologin);
        findpwdbtn=(Button)findViewById(R.id.login_findpwd);
        registebtn=(Button)findViewById(R.id.login_registe);
        sinalogin=(ImageButton)findViewById(R.id.login_sinaoauth);
        taobaologin=(ImageButton) findViewById(R.id.login_taooauth);
        weixinlogin=(ImageButton) findViewById(R.id.login_weixinoauth);
        usertestbtn=(Button)findViewById(R.id.login_usertest);
        loginbtn.setOnClickListener(new OnClickListener(){
 			@Override
			public void onClick(View v) {
 				preparelogin();
 			}
         });
         registebtn.setOnClickListener(new OnClickListener(){
 			@Override
			public void onClick(View v) {
  				registe();
 			}
         });
        sinalogin.setOnClickListener(new OnClickListener(){
 			@Override
			public void onClick(View v) {
 				sinaoauth();
 			}
         });
        weixinlogin.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				//qqweibologin();
 				final SendAuth.Req req = new SendAuth.Req();
 				req.scope = "snsapi_userinfo";
 				req.state = "wechat_sdk_demo_test";
 				wxapi.sendReq(req);
			}
		});
        listener=new SinaOAuthListener();
        listener.setFinishListener(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
				onOAouthBack(data.returnData);
			}
		});
        usertestbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent openCameraIntent = new Intent(LoginActivity.this,TimelineActivity.class);
 				openCameraIntent.putExtra("id", "100000141140830001");
  	 			startActivity(openCameraIntent);
			}
		});
        topclient=TopAndroidClient.getAndroidClientByAppKey(OAuthConstant.TaoBao.APP_ID);
        taobaologin.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
   				topclient.authorize(LoginActivity.this);
			}
		});
        qqweibolistener=new QQWeiBoOAuthListener();
        qqweibolistener.setFinishListener(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
				onOAouthBack(data.returnData);
			}
		});
        findpwdbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 				hand.excuteMethod(new MessageDialogHandlerMethod("","请使用合作网站账号登录后，重新设置密码。"));
			}
		});
    }
    /**
     * 用户登录
     */
    public void preparelogin()  {
         NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
        processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
  				if(result!=null)
  					processDialog=(ProgressDialog) ((Map)result).get("process");
  				login();
			}
		});
        processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "登录中...."));
    	
      }  
    private void login(){
    	String user=login_user.getText().toString();
    	String pwd=login_pwd.getText().toString();
    	UserLoginThread th=new UserLoginThread(user,pwd);
    	th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.post(new Runnable() {
 					@Override
					public void run() {
						if(processDialog!=null)
			 				   processDialog.dismiss();
					}
				});
   	  			if(data.result==NetResult.RESULT_OF_SUCCESS){
 					Intent resultIntent = new Intent();
 		    		resultIntent.setClass(LoginActivity.this, ScanCodeActivity.class);
 		    		//保存或更新数据库
 		    		userservice.saveOrUpdate((User)data.returnData);
 		    		SharedPreferenceService.saveLastLoginUser((User)data.returnData);//记录最后一次登录信息
 		    		updateUserVilleage();
 		    		//Session.getInstance().setAttribute(SessionAttribute.SESSION_USER, (User)rdata.get(UserLoginThread.RETURNDATA));//将用户信息存放到session
 					startActivity(resultIntent);
  				}else{
   					handler.excuteMethod(new MessageDialogHandlerMethod("",data.message));
  				}
			}
		});
    	th.start();
    }
    
	/**
	 * 更新用户农场信息
	 */
	private void updateUserVilleage(){
		final User user=userservice.getLastLoginUser();
		if(user!=null){
			LoadUserVilleageThread uvith=new LoadUserVilleageThread(user.getUser_id());
			uvith.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
  						if (data.result == NetResult.RESULT_OF_SUCCESS) {
 							List<UserVilleage> tuservils = (List<UserVilleage>) data.returnData;
 							uservilleageservice=new UserVilleageService();
 							villeageservice=new VilleageService();
 							
 							if (tuservils != null) {
 								for (UserVilleage vil : tuservils) {
 										uservilleageservice.saveOrUpdate(vil);
 									if(vil.getVilleage()!=null)
 									   villeageservice.saveOrUpdate(vil.getVilleage());
 								}
 							  }  
 						} 
 				}
			});
			uvith.start();
		}else{
		}
		
	}
     
     /**
      * 用户注册
      */
     public void registe(){
    	 Intent resultIntent = new Intent();
  		 resultIntent.setClass(LoginActivity.this, SendPhoneNumActivity.class);
  		 startActivity(resultIntent);
     }
    
     /**
      * 新浪授权
      */
     public void sinaoauth(){
    	 //sso单点登录方式
    	 mSsoHandler = new SsoHandler(LoginActivity.this, mWeibo);
         mSsoHandler.authorize(listener);
    	 //oauth2方式
         // mWeibo.anthorize(listener);
     }
     /**
      * 授权后处理
      * @param data
      */
     private void onOAouthBack(Object data){
     	 OAuthResult result=(OAuthResult) data;
     	 NcpzsHandler messagehandler=Gloable.getInstance().getCurHandler();
      	 if(result.getResult()==-1){ //用户自动取消授权
 			messagehandler.excuteMethod(new MessageDialogHandlerMethod("取消授权",result.getMessage()));
     	 }else if (result.getResult()==0){//授权出现问题
 			messagehandler.excuteMethod(new MessageDialogHandlerMethod("授权失败",result.getMessage()));
     	 }else if(result.getResult()==1){//授权成功 ，获取用户信息成功
     		 Intent resultIntent = new Intent();
     		 Session.getInstance().setAttribute(SessionAttribute.SESSION_OAUTH_RESULT, result);
       		 resultIntent.setClass(LoginActivity.this, OAuthLoginActivity.class);
      		 startActivity(resultIntent);
     	 }
     }
     
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
          // SSO 授权回调
         // 重要：发起 SSO 登陆的Activity必须重写onActivityResult
         if (mSsoHandler != null) {
             mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
         }
     }
	 
     
	
	public void qqweibologin(){
		/**
		 * 跳转到授权
		 */
 		AuthHelper.register(this,Long.parseLong(OAuthConstant.QQWeiBo.APP_KEY),  OAuthConstant.QQWeiBo.APP_SECRET, qqweibolistener);
 		AuthHelper.auth(this, "");
	}
	@Override
	protected boolean isExitActivity() {
 		return true;
	}
     
     
}
