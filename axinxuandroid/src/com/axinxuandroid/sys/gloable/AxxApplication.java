package com.axinxuandroid.sys.gloable;

import java.io.File;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.oauth.OAuthConstant;
import com.ncpzs.util.FileUtil;
import com.taobao.top.android.TopAndroidClient;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yixia.weibo.sdk.VCamera;
 

import cn.jpush.android.api.JPushInterface;
import android.app.Application;
import android.os.Environment;

public class AxxApplication extends Application {

	@Override
	public void onCreate() {
 		super.onCreate();
	    JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        GloableExceptionHandler crashHandler = GloableExceptionHandler.getInstance();    
        //注册GloableExceptionHandler    
        crashHandler.init(getApplicationContext());    
        //先注册，后续才能授权
        TopAndroidClient.registerAndroidClient(getApplicationContext(), OAuthConstant.TaoBao.APP_ID, OAuthConstant.TaoBao.APP_KEY, OAuthConstant.TaoBao.REDIRECT_URL);
 	    //微信注册
        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), null);
  		api.registerApp(OAuthConstant.WebChat.APP_ID);

		// 设置拍摄视频缓存路径
//		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//		if (DeviceUtils.isZte()) {
//			if (dcim.exists()) {
//				VCamera.setVideoCachePath(dcim + "/Camera/AxxVideoCache/");
//			} else {
//				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Camera/AxxVideoCache/");
//			}
//		} else {
//			VCamera.setVideoCachePath(dcim + "/Camera/AxxVideoCache/");
//		}
  		File videopath = new File(AppConstant.VIDEO_DIR);
  		FileUtil.createDirectory(AppConstant.VIDEO_DIR);
  		if(videopath.exists()){
  			VCamera.setVideoCachePath(AppConstant.VIDEO_DIR);
  		// 开启log输出,ffmpeg输出到logcat
  			VCamera.setDebugMode(true);
  			// 初始化拍摄SDK，必须
  			VCamera.initialize(this);
  		}
		
	}
   
}