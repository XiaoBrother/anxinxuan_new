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
	 * 用户注册
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
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("","正在提交中...!"));
		
		}
	}

	private void registe(){
		User ruser = getRegisteUser();
		int resgistetype=0;//默认用户名密码方式注册
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
 					// 保存或更新数据库
					userservice.saveOrUpdate((User)data.returnData);
					SharedPreferenceService.saveLastLoginUser((User) data.returnData);// 记录最后一次登录信息
					//Session.getInstance().setAttribute(SessionAttribute.SESSION_USER,(User) rdata.get(UserRegisteThread.RETURNDATA));// 将用户信息存放到session
					
				}else{
					NcpzsHandler handler = Gloable.getInstance().getCurHandler();
					handler.excuteMethod(new MessageDialogHandlerMethod("注册失败", data.message));
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
	 * 构造需要注册的用户
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
	 * 注册检查
	 * 
	 * @return
	 */
	public boolean check() {
		String pwd = user_pwd.getText().toString();
		String repwd = confirm_pwd.getText().toString();
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();

		if (pwd == null || "".equals(pwd.trim())) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请设置密码"));
			return false;
		}
		if (repwd == null || !(repwd.trim().equals(pwd.trim()))) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示",
					"两次输入密码不一致!"));
			return false;
		}
		return true;
	}

	// 返回上一页
	public void reback() {
		this.finish();
	}
}
