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
    private static final String SMS_IDENTIFICATION="����ѡ";
    private boolean cansend=false;
	/**
	 * ���ŵ�Uri����һ�¼��֣�
     * content://sms/inbox     �ռ���         
     * content://sms/sent        �ѷ��� 
     * content://sms/draft        �ݸ�           
     * content://sms/outbox    ������           (���ڷ��͵���Ϣ)
     * content://sms/failed      ����ʧ��     
     * content://sms/queued  �������б�  (���翪������ģʽ�󣬸ö��ž��ڴ������б���)
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
       
		// ����ʱ
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
	 * ���·���У����
	 */
	public void resend() {
		NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
		if(!cansend){
			processhandler.excuteMethod(new MessageDialogHandlerMethod("","��ȴ�һ��ʱ����ڷ���!"));
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
		processhandler.excuteMethod(new ProcessDialogHandlerMethod("����",
				"���ڷ����ֻ�У����...."));
		
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
							"���ͳɹ�", "��ע�������֤��!"));
 				} else {
					messagehandler.excuteMethod(new MessageDialogHandlerMethod(
							"����ʧ��", data.message));
				}
 				timer.start();

			}
		});
		th.start();
	}
	/**
	 * ��һ��
	 */
	public void tonext() {
		String usernum = numedit.getText().toString();
		String sysnum = (String) Session.getInstance().getAttribute(
				SessionAttribute.SESSION_PHONE_VALID_NUM);
		NcpzsHandler messagehandler = Gloable.getInstance().getCurHandler();
		if (sysnum == null) {
			messagehandler.excuteMethod(new MessageDialogHandlerMethod("��֤",
					"��֤����ʧЧ�������»�ȡ"));
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
						"��֤", "��֤ʧ��"));
			}
		}
	}

	// ������һҳ
	public void reback() {
		this.finish();
	}

    public void getSmsFromPhone() {  
        ContentResolver cr = getContentResolver();  
        String[] projection = new String[] { "body","date" };//"_id", "address", "person",, "date", "type  
        String where = "  date >  "   + sendtime; //һ����ǰ���Ķ���
        Cursor cur = cr.query(Uri.parse("content://sms/inbox"), projection, where, null, "date desc");  
        if (null == cur)  
            return;  
         while (cur.moveToNext()) {  
            //String number = cur.getString(cur.getColumnIndex("address"));//�ֻ���  
           // String name = cur.getString(cur.getColumnIndex("person"));//��ϵ�������б�  
            String body = cur.getString(cur.getColumnIndex("body"));  
            //LogUtil.logInfo(getClass(), "  sms  message context:"+body);
            if(body.indexOf(SMS_IDENTIFICATION)>=0){
            	//��ȡ��֤��
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
     *  ContentObserver�������ݹ۲��ߣ�Ŀ���ǹ۲�(��׽)�ض�Uri��������ݿ�ı仯���̶���һЩ��Ӧ�Ĵ�����������
     *  ���ݿ⼼���еĴ�����(Trigger)����ContentObserver���۲��Uri�����仯ʱ����ᴥ��������������Ϊ���������д�������
     *  ��Ӧ��ContentObserverҲ��Ϊ����ContentObserver�����С�ContentObserver"
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
           // LogUtil.logInfo(getClass(), "get a sms  message��prepare to parse");
            //ÿ�����¶��ŵ���ʱ��ʹ�����ǻ�ȡ����Ϣ�ķ���  
            getSmsFromPhone();  
        }  
    }  
}
