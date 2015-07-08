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
 * ����sd��״̬
 * @author Administrator
 *
 */
public class SDCardListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Environment.getExternalStorageState().equals(    
                Environment.MEDIA_MOUNTED)) {    
            //sd card ����    
			
 		}else {    
		    //��ǰ������    ����ʾ�û������˳�Ӧ��
 			NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 			hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
				 Gloable.getInstance().exitApplication();
 				}
			});
 			hand.excuteMethod(new MessageDialogHandlerMethod("","δ����SD�洢�������Ȱ�װSD������ʹ�ã�"));
		}    
	}

}
