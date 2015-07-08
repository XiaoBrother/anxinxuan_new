package com.axinxuandroid.activity;

import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.UserRegisteThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.User;

import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.OAuthResult;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;

import android.os.Bundle;
import android.os.Handler;

import android.app.ProgressDialog;
import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegistActivity extends NcpZsActivity {
	private EditText user_pwd;
	private EditText confirm_pwd;
	private Button submit;
	private ProgressDialog processDialog;
	private UserService userservice;
	private CommonTopView topview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_registered);
		user_pwd = (EditText) this.findViewById(R.id.resite_user_pwd);
		confirm_pwd = (EditText) this.findViewById(R.id.resite_confirm_pwd);
		submit = (Button) this.findViewById(R.id.resite_submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prepareregiste();
			}
		});
		userservice = new UserService();
		topview = (CommonTopView) this.findViewById(R.id.registe_topview);
		topview.setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reback();
			}
		});
	}

	/**
	 * �û�ע��
	 */
	public void prepareregiste() {
		if (check()) {
			NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
			processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if (result != null)
								processDialog = (ProgressDialog) ((Map) result).get("process");
							registe();
						}
					});
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("","�����ύ��...!"));
		
		}
	}

	private void registe(){
		User ruser = getRegisteUser();
		int resgistetype=0;//Ĭ���û������뷽ʽע��
		String oauthid=null;//
		OAuthResult oauth = (OAuthResult) Session.getInstance().getAttribute(SessionAttribute.SESSION_OAUTH_RESULT);
		if (oauth != null) {
			resgistetype=oauth.getType();
			oauthid=oauth.getUserid();
 			ruser.setUser_name(oauth.getName());
			ruser.setPerson_imageurl(oauth.getImgurl());
		}
		UserRegisteThread th = new UserRegisteThread(ruser,resgistetype,oauthid);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				if (data.result == NetResult.RESULT_OF_SUCCESS) {
 					// �����������ݿ�
					userservice.saveOrUpdate((User)data.returnData);
					SharedPreferenceService.saveLastLoginUser((User) data.returnData);// ��¼���һ�ε�¼��Ϣ
					//Session.getInstance().setAttribute(SessionAttribute.SESSION_USER,(User) rdata.get(UserRegisteThread.RETURNDATA));// ���û���Ϣ��ŵ�session
					
				}else{
					NcpzsHandler handler = Gloable.getInstance().getCurHandler();
					handler.excuteMethod(new MessageDialogHandlerMethod("ע��ʧ��", data.message));
				}
				finishRegiste(data.result);
			}
		});
		th.start();
	}
	private void finishRegiste(final int result){
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if (processDialog != null)
					processDialog.dismiss();
 				if (result == NetResult.RESULT_OF_SUCCESS){
 					Intent resultIntent = new Intent();
					resultIntent.setClass(RegistActivity.this,ScanCodeActivity.class);
					startActivity(resultIntent);
 				} 
			}
		});
	}
	/**
	 * ������Ҫע����û�
	 * 
	 * @return
	 */
	public User getRegisteUser() {
		User user = new User();
		String pwd = user_pwd.getText().toString();
		String phone = (String) Session.getInstance().getAttribute(
				SessionAttribute.SESSION_PHONE_NUM);
		user.setPhone(phone);
		user.setPwd(pwd);
 		return user;
	}

	/**
	 * ע����
	 * 
	 * @return
	 */
	public boolean check() {
		String pwd = user_pwd.getText().toString();
		String repwd = confirm_pwd.getText().toString();
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();

		if (pwd == null || "".equals(pwd.trim())) {
			handler.excuteMethod(new MessageDialogHandlerMethod("��ʾ", "����������"));
			return false;
		}
		if (repwd == null || !(repwd.trim().equals(pwd.trim()))) {
			handler.excuteMethod(new MessageDialogHandlerMethod("��ʾ",
					"�����������벻һ��!"));
			return false;
		}
		return true;
	}

	// ������һҳ
	public void reback() {
		this.finish();
	}
}
