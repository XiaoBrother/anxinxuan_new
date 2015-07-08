package com.axinxuandroid.activity.handler;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

/**
 * ��ʾ��Ϣ�ĶԿ��
 * @author Administrator
 *
 */
public class MessageDialogHandlerMethod implements HandlerMethodInterface {
   private String title;
   private String info;
   private CountDownTimer timer;//����ʱ������20���Զ��ر�
    
   public MessageDialogHandlerMethod(String title,String info){
	   this.info=info;
	   this.title=title;
    }
	@Override
	public void method(final OnHandlerFinishListener listenre) {
		 final Map cresult=new HashMap();
		 Context tmpcontext= Gloable.getInstance().getCurContext();
//		 AlertDialog.Builder builder =  new   AlertDialog.Builder(tmpcontext);
//	     builder.setTitle(title) .setMessage(info) .setCancelable(false)  
//                   .setPositiveButton("ȷ��",   new DialogInterface.OnClickListener() {  
//	                    public void onClick(DialogInterface dialog, int id)  
//	                    {   
// 	                    	dialog.cancel(); //ɾ���Ի���   
//	                    	if(listenre!=null){
//	                    		listenre.onHandlerFinish(cresult);
//	                    	}
//	                  }  
//	        }).setOnCancelListener(new OnCancelListener() {
// 				@Override
//				public void onCancel(DialogInterface dialog) {
// 					dialog.cancel(); //ɾ���Ի���   
//                	if(listenre!=null){
//                		listenre.onHandlerFinish(cresult);
//                	}
//				}
//			});  
//	                 
//	        AlertDialog alert = builder.create();//�����Ի���  
//  	        alert.show();//��ʾ�Ի���   
 	        Toast.makeText(tmpcontext, info,Toast.LENGTH_SHORT).show();  
 	        if(listenre!=null){
 	        	timer = new CountDownTimer(3000, 1000) {
 	 				public void onTick(long millisUntilFinished) {
 	 	  			}
 	 				public void onFinish() {
 	 					timer = null;
 	 					if(listenre!=null)
 	 						listenre.onHandlerFinish(cresult);
 	 	 			}
 	 			}.start();
 	        }
 	}

}
