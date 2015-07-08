package com.axinxuandroid.activity;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SendPhoneValidNumThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ValidPhoneNumActivity extends NcpZsActivity {

	private Button resendBtn;
	private Button tonextBtn;
	private EditText numedit;
	private ProgressDialog processDialog;
 	private CountDownTimer timer;
	private CommonTopView topview;
    private SmsObserver smsObserver;
    private long sendtime;
    private static final String SMS_IDENTIFICATION="安心选";
    private boolean cansend=false;
	/**
	 * 短信的Uri共有一下几种：
     * content://sms/inbox     收件箱         
     * content://sms/sent        已发送 
     * content://sms/draft        草稿           
     * content://sms/outbox    发件箱           (正在发送的信息)
     * content://sms/failed      发送失败     
     * content://sms/queued  待发送列表  (比如开启飞行模式后，该短信就在待发送列表里)
	 */
	private Uri SMS_INBOX = Uri.parse("content://sms");  
	private String resendWarnMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.valid_phonenum);
		numedit = (EditText) this.findViewById(R.id.validphonenum_num_edit);
		resendBtn = (Button) this.findViewById(R.id.validphonenum_resend);
		tonextBtn = (Button) this.findViewById(R.id.validphonenum_tonext);
		resendWarnMsg=getString(R.string.resend_phone_num_text);
		resendBtn.setText(resendWarnMsg.format(resendWarnMsg, "(60)"));
 		resendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resend();
			}
		});
		tonextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tonext();
			}
		});
		topview = (CommonTopView) this.findViewById(R.id.validphonenum_topview);
		topview.setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reback();
			}
		});
		sendtime= (System.currentTimeMillis() -  60 * 1000);
		smsObserver = new SmsObserver(this, Gloable.getInstance().getCurHandler());  
        getContentResolver().registerContentObserver(SMS_INBOX, true,    smsObserver);  
       
		// 倒计时
		timer = new CountDownTimer(60000, 1000) {
			
			public void onTick(long millisUntilFinished) {
 				resendBtn.setText(resendWarnMsg.format(resendWarnMsg, "("+(millisUntilFinished / 1000 -1)+")"));
			}

			public void onFinish() {
				cansend=true;
				resendBtn.setText(resendWarnMsg.format(resendWarnMsg,""));
				resendBtn.setTextColor(Color.rgb(89, 89, 89));
				Session.getInstance().removeAttribute(SessionAttribute.SESSION_PHONE_VALID_NUM);
			} 
		}.start();

	}

	/**
	 * 重新发送校验码
	 */
	public void resend() {
		NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
		if(!cansend){
			processhandler.excuteMethod(new MessageDialogHandlerMethod("","请等待一段时间后在发送!"));
			return ;
		}
 		cansend=false;
 		resendBtn.setTextColor(Color.rgb(160, 160, 160));
  		processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
					@Override
					public void onHandlerFinish(Object result) {
						if (result != null)
							processDialog = (ProgressDialog) ((Map) result).get("process");
						sendNum();
					}
				});
		processhandler.excuteMethod(new ProcessDialogHandlerMethod("发送",
				"正在发送手机校验码...."));
		
	}

	private void sendNum(){
		String phone = (String) Session.getInstance().getAttribute(
				SessionAttribute.SESSION_PHONE_NUM);
		SendPhoneValidNumThread th = new SendPhoneValidNumThread(phone);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
				if (processDialog != null)
					processDialog.dismiss();
 				NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
 				if (data.result == NetResult.RESULT_OF_SUCCESS) {
					Session.getInstance().setAttribute(
							SessionAttribute.SESSION_PHONE_VALID_NUM,(String)data.returnData);
					messagehandler.excuteMethod(new MessageDialogHandlerMethod(
							"发送成功", "请注意查收验证码!"));
 				} else {
					messagehandler.excuteMethod(new MessageDialogHandlerMethod(
							"发送失败", data.message));
				}
 				timer.start();

			}
		});
		th.start();
	}
	/**
	 * 下一步
	 */
	public void tonext() {
		String usernum = numedit.getText().toString();
		String sysnum = (String) Session.getInstance().getAttribute(
				SessionAttribute.SESSION_PHONE_VALID_NUM);
		NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
		if (sysnum == null) {
			messagehandler.excuteMethod(new MessageDialogHandlerMethod("验证",
					"验证码已失效，请重新获取"));
		} else {
			LogUtil
					.logInfo(ValidPhoneNumActivity.class, sysnum + ":"
							+ usernum);
			if (sysnum.equals(usernum.trim())) {
				Intent resultIntent = new Intent();
				resultIntent.setClass(ValidPhoneNumActivity.this,
						RegistActivity.class);
				startActivity(resultIntent);
			} else {
				messagehandler.excuteMethod(new MessageDialogHandlerMethod(
						"验证", "验证失败"));
			}
		}
	}

	// 返回上一页
	public void reback() {
		this.finish();
	}

    public void getSmsFromPhone() {  
        ContentResolver cr = getContentResolver();  
        String[] projection = new String[] { "body","date" };//"_id", "address", "person",, "date", "type  
        String where = "  date >  "   + sendtime; //一分钟前发的短信
        Cursor cur = cr.query(Uri.parse("content://sms/inbox"), projection, where, null, "date desc");  
        if (null == cur)  
            return;  
         while (cur.moveToNext()) {  
            //String number = cur.getString(cur.getColumnIndex("address"));//手机号  
           // String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表  
            String body = cur.getString(cur.getColumnIndex("body"));  
            //LogUtil.logInfo(getClass(), "  sms  message context:"+body);
            if(body.indexOf(SMS_IDENTIFICATION)>=0){
            	//获取验证码
                Pattern pattern = Pattern.compile("[0-9]{6}");  
                Matcher matcher = pattern.matcher(body);  
                if (matcher.find()) {  
                    String res = matcher.group().substring(0,6);  
                    numedit.setText(res);  
                }  
            }
            
        }  
    }  
    /**
     *  ContentObserver——内容观察者，目的是观察(捕捉)特定Uri引起的数据库的变化，继而做一些相应的处理，它类似于
     *  数据库技术中的触发器(Trigger)，当ContentObserver所观察的Uri发生变化时，便会触发它。触发器分为表触发器、行触发器，
     *  相应地ContentObserver也分为“表“ContentObserver、“行”ContentObserver"
     * @author Administrator
     *
     */
	class SmsObserver extends ContentObserver {  
         public SmsObserver(Context context, Handler handler) {  
            super(handler);  
        }  
  
        @Override  
        public void onChange(boolean selfChange) {  
            super.onChange(selfChange);  
           // LogUtil.logInfo(getClass(), "get a sms  message，prepare to parse");
            //每当有新短信到来时，使用我们获取短消息的方法  
            getSmsFromPhone();  
        }  
    }  
}
