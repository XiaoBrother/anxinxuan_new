package com.axinxuandroid.broadcast;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.sys.gloable.Gloable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
/**
 * 监听sd卡状态
 * @author Administrator
 *
 */
public class SDCardListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Environment.getExternalStorageState().equals(    
                Environment.MEDIA_MOUNTED)) {    
            //sd card 可用    
			
 		}else {    
		    //当前不可用    ，提示用户，并退出应用
 			NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 			hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
				 Gloable.getInstance().exitApplication();
 				}
			});
 			hand.excuteMethod(new MessageDialogHandlerMethod("","未发现SD存储卡，请先安装SD卡后再使用！"));
		}    
	}

}
