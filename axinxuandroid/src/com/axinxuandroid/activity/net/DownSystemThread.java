package com.axinxuandroid.activity.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;

/**
 * 下载最新版本
 * 
 * @author Administrator
 * 
 */
public class DownSystemThread extends Thread {
	private String url;
	private int process;//进度
	private boolean stop=false;//暂停
	private DownLoadProcessListener listener;
	public static int DOWN_RESULT_SUCCESS=1;
	public static int DOWN_RESULT_STOP=2;
	/* 下载包安装名称 */
   	private static final String SAVEFILENAME="anxinxuan.apk";
	public DownSystemThread(String url) {
		this.url = url;
	}
 	@Override
	public void run() {
 		try {
			URL downurl = new URL(url);
 			HttpURLConnection conn = (HttpURLConnection) downurl.openConnection();
			conn.connect();
			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();
            FileUtil.createDirectory(AppConstant.SYSTEM_VERSION_PATH);
			File file = new File(AppConstant.SYSTEM_VERSION_PATH+SAVEFILENAME);
  			FileOutputStream fos = new FileOutputStream(file);
 			int count = 0;
			byte buf[] = new byte[1024];
 			do {
				int numread = is.read(buf);
				count += numread;
				process = (int) (((float) count / length) * 100);
				if(listener!=null)
					listener.currentProcess(process);
 				if (numread <= 0) {
					// 下载完成通知安装
 					break;
 				}
				fos.write(buf, 0, numread);
			} while (!stop);// 点击取消就停止下载.
 			fos.close();
			is.close();
			if(!stop){
				listener.onfinish(DOWN_RESULT_SUCCESS,AppConstant.SYSTEM_VERSION_PATH+SAVEFILENAME);
  			}else{
  				listener.onfinish(DOWN_RESULT_STOP,null);
			}
			
 		} catch (Exception e) {
			e.printStackTrace();
 		}  
  	}
	
	public void stopdown(){
		stop=true;
	}
   
	public void setProcessListenere(DownLoadProcessListener listener){
		this.listener=listener;
	}
	public interface DownLoadProcessListener{
		public void currentProcess(int process);
		public void onfinish(int result,String savepath);
	}
}
