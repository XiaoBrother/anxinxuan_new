package com.axinxuandroid.activity;




import java.util.Date;
import java.util.Map;

 
import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.ResetPwdThread;
import com.axinxuandroid.activity.net.UserRegisteThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.MenuWindow;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.User;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.oauth.YukuOAuth.YukuOAuthInfo;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.GPSUtil;
import com.ncpzs.util.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

  
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
 
public class ResetPwdActivity extends NcpZsActivity{
	private EditText pwd,repwd;
	private CommonTopView topview;
	private User user;
	private UserService uservice;
	private ProgressDialog processDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.resetpwd);
		pwd=(EditText) this.findViewById(R.id.resetpwd_pwd);
		repwd=(EditText) this.findViewById(R.id.resetpwd_repwd);
 		topview=(CommonTopView) this.findViewById(R.id.resetpwd_topview);
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				prepare();
			}
		});
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				ResetPwdActivity.this.finish();
			}
		});
    }
  	
	
	private void prepare() {
		if (check()) {
			NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
			processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if (result != null)
								processDialog = (ProgressDialog) ((Map) result).get("process");
							resetPwd();
						}
					});
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("","正在保存...!"));
		
		}
	}
  	
	private void resetPwd(){
		int userid=user.getUser_id();
		String pwdstr=pwd.getText().toString();
		ResetPwdThread resetth=new ResetPwdThread(userid, pwdstr);
		resetth.setLiserner(new NetFinishListener() {
 			 
			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler = Gloable.getInstance().getCurHandler();
				if (data.result == NetResult.RESULT_OF_SUCCESS) {
					handler.excuteMethod(new MessageDialogHandlerMethod("","设置成功！"));
				}else{
					handler.excuteMethod(new MessageDialogHandlerMethod("","设置失败！"));
				}
				resetPwdFinish();			}
		});
		resetth.start();
	}
	
	private void resetPwdFinish(){
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new  Runnable() {
			public void run() {
				if(processDialog!=null)
					processDialog.dismiss();
				pwd.setText("");
				repwd.setText("");
			}
		});
	}
 
	private boolean check(){
		
		String pwdstr = pwd.getText().toString();
		String repwdstr = repwd.getText().toString();
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		if(user==null){
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请先登录在设置密码！"));
			return false;
		}
 		if (pwdstr == null || "".equals(pwdstr.trim())) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请输入密码"));
			return false;
		}
		if (repwdstr == null || !(repwdstr.trim().equals(pwdstr.trim()))) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示",
					"两次输入密码不一致!"));
			return false;
		}
		return true;
	}
 
}