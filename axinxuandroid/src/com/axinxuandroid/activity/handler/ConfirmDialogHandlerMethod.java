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
 * 连接服务器失败方法处理，显示一个对话框让用户选择是否继续连接
 * 
 * @author Administrator
 * 
 */
public class ConfirmDialogHandlerMethod implements HandlerMethodInterface {
	private String title;
	private String info;
	private String okmsg = "确定";
	private String cancelmsg = "取消";
	private boolean displayCancel = true;

	public ConfirmDialogHandlerMethod(String title, String info) {
		this(title, info, "确定", "取消");
	}

	public ConfirmDialogHandlerMethod(String title, String info, String okmsg,
			String cancelmsg) {
		this.info = info;
		this.title = title;
		this.okmsg = okmsg;
		this.cancelmsg = cancelmsg;
	}

	/**
	 * 是否显示取消按钮
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
								dialog.dismiss(); // 删除对话框
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
							dialog.dismiss(); // 删除对话框
							if (listenre != null) {
								listenre.onHandlerFinish(cresult);
							}
						}
					});
		AlertDialog alert = builder.create();// 创建对话框
//		if(info!=null){
//			XRTextView tv=new XRTextView(tmpcontext, 18,Color.rgb(255, 0, 122), 2, 2);
//			tv.setText(info);
//			alert.setView(tv,0,0,0,0);
//		}
		
		alert.show();// 显示对话框
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
