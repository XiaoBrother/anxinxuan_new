package com.axinxuandroid.activity;




import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

 
import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.WeiBoFollowAxxThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.MenuWindow;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.OAuthResult;
import com.axinxuandroid.oauth.QQWeiBoOAuthListener;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.GPSUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
 
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Intent;
 
public class AboutAppActivity extends NcpZsActivity{
	private UserService userservice;
	private SystemNoticeService noticeservice;
	private SystemNotice versionnotice;
	private Button updatebtn;
	private LinearLayout weibobtn,weixinbtn;
	private User user;
	private ProgressDialog progress;
	private CommonTopView topview;
  	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.aboutapp);
		updatebtn=(Button) this.findViewById(R.id.aboutapp_update);
		weibobtn=(LinearLayout) this.findViewById(R.id.aboutapp_weibo);
		weixinbtn=(LinearLayout) this.findViewById(R.id.aboutapp_weixin);
		topview=(CommonTopView)this.findViewById(R.id.aboutapp_topview);
		userservice = new UserService();
		noticeservice=new SystemNoticeService();
		versionnotice=noticeservice.getByType(SystemNoticeType.NOTICE_TYPE_VERSION);
		user=SharedPreferenceService.getLastLoginUser();
		float curversion=SharedPreferenceService.getVersion();
		topview.setTitle("当前版本"+curversion);
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				AboutAppActivity.this.finish();
			}
		});
		if(versionnotice!=null){
         	 try{
        		 JSONObject jsonObject = new JSONObject(versionnotice.getJsondata());
     			 double version=jsonObject.getDouble("newversion");
     			 if(curversion<version){
     				updatebtn.setBackgroundResource(R.drawable.btn_org);
     				updatebtn.setEnabled(true);
     				updatebtn.setText("升级版本至"+version);
     				updatebtn.setOnClickListener(new OnClickListener() {
     	 				@Override
     					public void onClick(View v) {
     	 					systemUpdate();
     					}
     				});
     			 }
          	 }catch(Exception ex){
        		 ex.printStackTrace();
        	 }
 		}
		weibobtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				weibo();				
			}
		});
		weixinbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				//weixin();		
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.excuteMethod(new MessageDialogHandlerMethod("","暂时不支持微信关注功能！"));
			}
		});
    }

	
  	public void systemUpdate(){
 		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
					int usersel=(Integer)((Map)result).get("result");
					if(usersel==1){
						try{
							 JSONObject jsonObject = new JSONObject(versionnotice.getJsondata());
							 String url=jsonObject.getString("downurl");
							 double version=jsonObject.getDouble("newversion");
	 						 Intent upint=new Intent("com.axinxuandroid.backservice.SystemUpdateService");
							 upint.putExtra("downurl", url);
							 upint.putExtra("version", version);
							 startService(upint);//下载系统
						}catch(Exception ex){
							ex.printStackTrace();
						}
 					}
					
				}
			});
 			handler.excuteMethod(new ConfirmDialogHandlerMethod("", "是否下载最新版本？"));
  	}
  	/**
  	 * 微博关注
  	 */
  	public void weibo(){
     	//判断用户登录方式
  		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  		 if(user.getLogintype()!=OAuthConstant.SINA_OAUTH_TYPE){
   			 hand.excuteMethod(new MessageDialogHandlerMethod("","请先使用微博账号登录！"));
  		 }else{
  			Date curdate=new Date();
			Date logindate=DateUtil.StrToDate(user.getOauthlogintime());
			if(curdate.getTime()- logindate.getTime()>user.getExpirein()){
				//登录过期
 	  			hand.excuteMethod(new MessageDialogHandlerMethod("","登录已过期，请重新使用微博账号登录！"));
			}else{
				hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	   	 			@Override
	   				public void onHandlerFinish(Object result) {
	   	 				if(result!=null)
	   	 					progress=(ProgressDialog) ((Map)result).get("process");
	   	 		    	weibofollow();
	   				}
	   			});
				hand.excuteMethod(new ProcessDialogHandlerMethod("","请稍候..."));
			}
   		 }
  	}
  	private void weibofollow(){
  		WeiBoFollowAxxThread th=new WeiBoFollowAxxThread(user.getAccesstoken());
  		th.setLiserner(new NetFinishListener() {
 			 

			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  	 			if(data.result==NetResult.RESULT_OF_SUCCESS){
  	 				hand.excuteMethod(new MessageDialogHandlerMethod("","关注成功！"));
  	 			}else{
  	 				hand.excuteMethod(new MessageDialogHandlerMethod("","关注失败,"+data.message));
  	 			}
  	 			hand.post(new Runnable() {
 					@Override
					public void run() {
 						if(progress!=null)
 							progress.dismiss();
					}
				});
			}
		});
  		th.start();
  	}
  	
  	
  	/**
  	 * 微信关注
  	 */
  	public void weixin(){
  		IWXAPI api=WXAPIFactory.createWXAPI(AboutAppActivity.this, OAuthConstant.WebChat.APP_ID,true);
  		SendAuth.Req req = new SendAuth.Req();
  		
   		req.scope = "snsapi_userinfo";
  		req.state = "wechat_sdk_anxinxuan";
  		api.sendReq(req);
   		//NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  		//hand.excuteMethod(new MessageDialogHandlerMethod("","暂不支持！"));
   	}

 
}