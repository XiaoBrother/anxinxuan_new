package com.axinxuandroid.activity;

import java.util.Map;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.OAuthLoginThread;
 import com.axinxuandroid.data.User;
import com.axinxuandroid.oauth.OAuthResult;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OAuthLoginActivity extends NcpZsActivity{
	private ProgressDialog processDialog; 
	private OAuthResult result;
	private UserService userservice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		userservice=new UserService();
 	    result=(OAuthResult) Session.getInstance().getAttribute(SessionAttribute.SESSION_OAUTH_RESULT);
 	    NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
        processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
 					processDialog=(ProgressDialog) ((Map)result).get("process");
			}
		});
        processhandler.excuteMethod(new ProcessDialogHandlerMethod("��¼", "��¼��...."));
        oauthlogin();
	}
   /**
    * OAuth��¼
    */
	public void oauthlogin(){
		if(result!=null){
			OAuthLoginThread th=new OAuthLoginThread(result.getType()+"",result.getUserid());
 	    	th.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
					    if(processDialog!=null)
		 				   processDialog.dismiss();
  		 				NcpzsHandler messagehandler=Gloable.getInstance().getCurHandler();
		  				if(data.result==NetResult.RESULT_OF_SUCCESS){
		 					//��¼�ɹ�
	 	 		    		//�����������ݿ�
		  					User loginuser=(User)data.returnData;
		  					loginuser.setLogintype(result.getType());
		  					loginuser.setAccesstoken(result.getAccesstoken());
		  					loginuser.setExpirein(result.getExpirein());
		  					loginuser.setOauthlogintime(result.getOauthlogintime());
		 		    		userservice.saveOrUpdate(loginuser);
		 		    		SharedPreferenceService.saveLastLoginUser(loginuser);//��¼���һ�ε�¼��Ϣ
		 		    		//Session.getInstance().setAttribute(SessionAttribute.SESSION_USER, rdata.get(OAuthLoginThread.RETURNDATA));//���û���Ϣ��ŵ�session
		 		    		redirect(ScanCodeActivity.class);
	 	 				}else if(data.result==NetResult.RESULT_OF_OTHER){//��ʾ�û����а�
	 	 					messagehandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	 							@Override
								public void onHandlerFinish(Object result) {
	 								Map userselect=(Map) result;
	 								if((Integer)userselect.get("result")==1){
	 									//��ת����ҳ��
	 									redirect(SendPhoneNumActivity.class);
	 								}else{
	 									//��ת����¼ҳ��
	 									redirect(LoginActivity.class);
	 								}
								}
							});
	  	 					messagehandler.excuteMethod(new ConfirmDialogHandlerMethod("ȷ��",data.message));
	 	  				}else{
		 					messagehandler.excuteMethod(new MessageDialogHandlerMethod("��¼ʧ��",data.message));
	 	  				}
				}
  				 
			});
 	    	th.start();
		}
 	}
	
	public void redirect(Class clz){
		Intent resultIntent = new Intent();
 		resultIntent.setClass(OAuthLoginActivity.this, clz);
 		startActivity(resultIntent);
	}
}
