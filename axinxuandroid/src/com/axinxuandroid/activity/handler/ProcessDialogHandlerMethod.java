package com.axinxuandroid.activity.handler;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.CountDownTimer;
import android.view.KeyEvent;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.view.RecordAudioViewStyleF;
import com.axinxuandroid.sys.gloable.Gloable;

/**
 * 进度框
 * @author Administrator
 *
 */
public class ProcessDialogHandlerMethod implements HandlerMethodInterface {
   private String title;
   private String info;
   private CountDownTimer timer;//倒计时，超过20秒自动关闭
   private boolean isautocolse=true;

   public ProcessDialogHandlerMethod(String title,String info){
	   this.info=info;
	   this.title=title;
    }
	@Override
	public void method(final OnHandlerFinishListener listenre) {
	    Map cresult=new HashMap();
		final ProgressDialog showDialog = new ProgressDialog(Gloable.getInstance().getCurContext());
		showDialog.setTitle("");
		//showDialog.setTitle(title);
		showDialog.setMessage(info);
		showDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		showDialog.setOnKeyListener(new OnKeyListener() {
 			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
 				//return true 不响应任何触碰操作
 				return true;
			}
		});
 		cresult.put("process", showDialog);
 		if(listenre!=null){
    		listenre.onHandlerFinish(cresult);
    	}
 		if(this.isautocolse){
 			timer = new CountDownTimer((AppConstant.AUTO_CLOSE_OUTTIME+5)*1000, 1000) {
 				public void onTick(long millisUntilFinished) {
 	  			}
 				public void onFinish() {
 					timer = null;
 					//if(showDialog!=null)
 					//	showDialog.dismiss();
 	 			}
 			}.start();
 		}
  		showDialog.setOnDismissListener(new OnDismissListener() {
 			@Override
			public void onDismiss(DialogInterface dialog) {
 				if(timer!=null){
 					timer.cancel();
 				}
			}
		});
		showDialog.show();
 	}
    
	public void isAutoClose(boolean autoclose){
		this.isautocolse=autoclose;
	}
}
