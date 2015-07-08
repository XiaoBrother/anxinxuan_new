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
 * 提示信息的对框框
 * @author Administrator
 *
 */
public class MessageDialogHandlerMethod implements HandlerMethodInterface {
   private String title;
   private String info;
   private CountDownTimer timer;//倒计时，超过20秒自动关闭
    
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
//                   .setPositiveButton("确定",   new DialogInterface.OnClickListener() {  
//	                    public void onClick(DialogInterface dialog, int id)  
//	                    {   
// 	                    	dialog.cancel(); //删除对话框   
//	                    	if(listenre!=null){
//	                    		listenre.onHandlerFinish(cresult);
//	                    	}
//	                  }  
//	        }).setOnCancelListener(new OnCancelListener() {
// 				@Override
//				public void onCancel(DialogInterface dialog) {
// 					dialog.cancel(); //删除对话框   
//                	if(listenre!=null){
//                		listenre.onHandlerFinish(cresult);
//                	}
//				}
//			});  
//	                 
//	        AlertDialog alert = builder.create();//创建对话框  
//  	        alert.show();//显示对话框   
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
