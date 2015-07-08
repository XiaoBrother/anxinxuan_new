package com.axinxuandroid.activity;

import java.util.Map;

import android.os.CountDownTimer;
import android.os.Handler;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SendPhoneValidNumThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.User;
import com.axinxuandroid.db.UserDB;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.SIMCardInfo;
import com.ncpzs.util.ValidPattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * ��֤�ֻ�����
 * @author Administrator
 *
 */
public class SendPhoneNumActivity extends  NcpZsActivity{
	private EditText phone_code; //����
	private Button tonext_btn;
	private ProgressDialog processDialog; 
	private CommonTopView topview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_phonenum);
        phone_code=(EditText) this.findViewById(R.id.sendphonenum_phone_edit);
        phone_code.setInputType(InputType.TYPE_CLASS_NUMBER);
        tonext_btn=(Button) this.findViewById(R.id.sendphonenum_tonext);
        tonext_btn.setOnClickListener(new OnClickListener(){
 			@Override
			public void onClick(View v) {
  				tonext();
 			}
         });
        topview=(CommonTopView) this.findViewById(R.id.sendphonenum_topview);
        topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				reback();
			}
		});
     }
    /**
     * ��һ��
     */
    public void tonext(){
    	//����֤�û�������ֻ�����
    	String phone=phone_code.getText().toString();
     	if(!ValidPattern.isMobileNO(phone)){
    		//��ʾ�û��������
    		NcpzsHandler messagehandler=Gloable.getInstance().getCurHandler();
 		    messagehandler.excuteMethod(new MessageDialogHandlerMethod("��ʾ","�ֻ������ʽ����ȷ"));
     	}else{
     		sendValidNum(phone);
     	}
    }
    /**
     * ���ֻ�������֤��
     */
    public void sendValidNum(final String phone){
    	NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
    	processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
  				if(result!=null)
  					processDialog=(ProgressDialog) ((Map)result).get("process");
			}
		});
    	processhandler.excuteMethod(new ProcessDialogHandlerMethod("����", "���ڷ����ֻ�У����...."));
    	SendPhoneValidNumThread th=new SendPhoneValidNumThread(phone);
    	th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
				if(processDialog!=null)
 					processDialog.dismiss();
    			if(data.result==NetResult.RESULT_OF_SUCCESS){
 					Session.getInstance().setAttribute(SessionAttribute.SESSION_PHONE_VALID_NUM, (String)data.returnData);
 					Session.getInstance().setAttribute(SessionAttribute.SESSION_PHONE_NUM, phone);
 					Intent resultIntent = new Intent();
 		    		resultIntent.setClass(SendPhoneNumActivity.this, ValidPhoneNumActivity.class);
 		    		startActivity(resultIntent);
  				}else{
  					NcpzsHandler messagehandler=Gloable.getInstance().getCurHandler();
  					messagehandler.excuteMethod(new MessageDialogHandlerMethod("����ʧ��",data.message));
  				}
 				
			}
		});
    	th.start();
     }
   
  //������һҳ
   public void reback() {     
      	this.finish();
   }  
 
    
}
