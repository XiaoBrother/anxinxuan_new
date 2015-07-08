package com.axinxuandroid.activity;

import java.io.File;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.CheckVersionThread;
import com.axinxuandroid.activity.net.DownSystemThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.DownSystemThread.DownLoadProcessListener;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.FileUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class SystemUpdateActivity extends NcpZsActivity{
 	private ProgressDialog progress;
 	private ProgressBar pbar;
 	public static final int RETURN_FROM_INSTALL = 1;
    private Dialog dialog;
    private View barview;
    private Version version;
    private SystemNoticeService noticeservice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 	    barview=LayoutInflater.from(this).inflate(R.layout.progressbar, null);
 	    pbar=(ProgressBar) barview.findViewById(R.id.systemupdate_progressbar);
 	    noticeservice=new SystemNoticeService();
 	    SystemNotice notice=noticeservice.getByType(SystemNoticeType.NOTICE_TYPE_VERSION);
 	    if(notice!=null){
 	    	 try {
			   JSONObject jsonObject = new JSONObject(notice.getJsondata());
			   String url=jsonObject.getString("downurl");
			   version=new Version();
			   version.setDownurl(jsonObject.getString("downurl"));
			   version.setVersion((float)jsonObject.getDouble("newversion"));
 	 		   download(url);
 			} catch (Exception e) {
 				e.printStackTrace();
			}
  	    }else{
  	    	NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  	    	handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					SystemUpdateActivity.this.finish();
				}
			});
  	    	handler.excuteMethod(new MessageDialogHandlerMethod("", "没有可更新版本!"));
  	    }
 	   
	}
  
	 
	
	 
	
	 
	
	/**
	 * 下载最新版本
	 */
	private void download(String url){
		final DownSystemThread th=new DownSystemThread(url);
 		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	    builder.setTitle("软件版本更新");  
	    builder.setView(barview);  
	    builder.setNegativeButton("取消", new OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	                th.stopdown();
 	            }  
	        });  
	    dialog = builder.create();  
	    dialog.show();
 		th.setProcessListenere(new DownLoadProcessListener() {
 			@Override
			public void currentProcess(final int process) {
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.post(new Runnable() {
 					@Override
					public void run() {
  						pbar.setProgress(process);
					}
				});
			}

			@Override
			public void onfinish(int result, String savepath) {
				if(dialog!=null)
 					dialog.dismiss();
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  				String path=savepath;
 				if(result==DownSystemThread.DOWN_RESULT_SUCCESS){
  					noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_VERSION);
    				installApk(path);
 				}else{
 					FileUtil.deleteFile(path);
 					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
						public void onHandlerFinish(Object result) {
							SystemUpdateActivity.this.finish();
 						}
					});
 					handler.excuteMethod(new MessageDialogHandlerMethod("软件版本更新","更新失败"));
 					
 				}
			}
		});
		th.start();
	}
	 /**
     * 安装apk
     * @param url
     */
	private void installApk(String filepath){
		if(version!=null)
		  SharedPreferenceService.saveVersion(version.getVersion());
		//更新成最新版本
		File apkfile = new File(filepath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent inte = new Intent(Intent.ACTION_VIEW);
        inte.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        startActivityForResult(inte, RETURN_FROM_INSTALL);
 	}
	
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case RETURN_FROM_INSTALL:
//			dealInstall();
//			break;
//		 
//  		}
//	}
//	private void dealInstall(){
//		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
//		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
// 			@Override
//			public void onHandlerFinish(Object result) {
// 				//重启应用
// 				Intent i = getBaseContext().getPackageManager()  
// 		        .getLaunchIntentForPackage(getBaseContext().getPackageName());  
//		 		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//		 		startActivity(i); 
//			}
//		});
//		handler.excuteMethod(new MessageDialogHandlerMethod("软件版本更新","系统更新完成，重新启动！"));
//	}

	private void finishCheckVersion(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
			}
		});
	}
}
