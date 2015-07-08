package com.axinxuandroid.activity.handler;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Text;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Window;
import android.widget.TextView;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.view.XRTextView;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

/**
 * ���ӷ�����ʧ�ܷ���������ʾһ���Ի������û�ѡ���Ƿ��������
 * 
 * @author Administrator
 * 
 */
public class ConfirmDialogHandlerMethod implements HandlerMethodInterface {
	private String title;
	private String info;
	private String okmsg = "ȷ��";
	private String cancelmsg = "ȡ��";
	private boolean displayCancel = true;

	public ConfirmDialogHandlerMethod(String title, String info) {
		this(title, info, "ȷ��", "ȡ��");
	}

	public ConfirmDialogHandlerMethod(String title, String info, String okmsg,
			String cancelmsg) {
		this.info = info;
		this.title = title;
		this.okmsg = okmsg;
		this.cancelmsg = cancelmsg;
	}

	/**
	 * �Ƿ���ʾȡ����ť
	 * 
	 * @param display
	 */
	public void displayCancel(boolean display) {
		displayCancel = display;
	}

	@Override
	public void method(final OnHandlerFinishListener listenre) {
		final Map cresult = new HashMap();
		Context tmpcontext = Gloable.getInstance().getCurContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(tmpcontext);
//		 LogUtil.logInfo(getClass(), "start...");
		builder.setTitle(title).setMessage(info).setCancelable(false)
				.setPositiveButton(okmsg,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								cresult.put("result", 1);
								dialog.dismiss(); // ɾ���Ի���
 								if (listenre != null) {
									listenre.onHandlerFinish(cresult);
								}
							}
						});
		if (displayCancel)
			builder.setNegativeButton(cancelmsg,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							cresult.put("result", 0);
							dialog.dismiss(); // ɾ���Ի���
							if (listenre != null) {
								listenre.onHandlerFinish(cresult);
							}
						}
					});
		AlertDialog alert = builder.create();// �����Ի���
//		if(info!=null){
//			XRTextView tv=new XRTextView(tmpcontext, 18,Color.rgb(255, 0, 122), 2, 2);
//			tv.setText(info);
//			alert.setView(tv,0,0,0,0);
//		}
		
		alert.show();// ��ʾ�Ի���
//		if(info!=null){
//			Window win=alert.getWindow();
//  			win.setContentView(R.layout.dialog);
// 			TextView tv=(TextView) win.findViewById(R.id.dialog_text);
//			tv.setText(info);
//		}
//		
		// if(tmpcontext==Gloable.getInstance().getCurContext())
		
		// else method(listenre);

	}

}
