package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.view.ImageGalleryView;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.VideoView;
import com.axinxuandroid.activity.view.VideoView.VideoPlayFinishListener;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.sina.weibo.sdk.utils.MD5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 展示 视频
 * @author Administrator
 *
 */
public class PlayVideoActivity  extends NcpZsActivity{
  	private String path;
  	private WebView videoweb;
  	private VideoView videoview;
  	private ImageView videoimg;
 	private ProgressDialog processDialog;
 	private Handler handler=new Handler();
 	private static final String VIDEO_HTML ="file:///android_asset/video.html";
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.playvideo);
 		path= this.getIntent().getStringExtra("path");
   		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
  		videoweb=(WebView) this.findViewById(R.id.playvideo_videoweb);
  		videoview=(VideoView) this.findViewById(R.id.playvideo_videolocal);
  		videoimg=(ImageView) this.findViewById(R.id.playvideo_videoshowimg);
  		videoimg.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(videoview.isPlaying())
 					videoview.pauseOrContinue();
 				else videoview.startPlay(path);
			}
		});
  		if(!isLocalUrl(path)){
  			((RelativeLayout)videoweb.getParent()).setVisibility(View.VISIBLE);
  			startShow();
  		}else{
  			 videoimg.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.videobg));
  			((RelativeLayout)videoview.getParent()).setVisibility(View.VISIBLE);
  		}
   		
	}
	
	private void startShow(){
		final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				processDialog = (ProgressDialog) ((Map) result).get("process");
  					showVideo();
    			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","加载中"));
	}
	 
	/**
	  * 展示视频
	 */
	public void showVideo(){
 		 
 		new Thread() {
			@Override
			public void run() {
//				if (!checkFlashPlayer()) {
//					final NcpzsHandler handler = Gloable.getInstance()
//							.getCurHandler();
//					handler.excuteMethod(new MessageDialogHandlerMethod("提醒",
//							"手机未安装flash插件，无法播放视频"));
//				} else {
					// 设置WebView属性,能够执行Javascript脚本
					videoweb.getSettings().setJavaScriptEnabled(true);
					WebSettings settings = videoweb.getSettings();
					settings.setUseWideViewPort(true);
					settings.setLoadWithOverviewMode(true);
					//settings.setPluginsEnabled(true);
					settings.setJavaScriptEnabled(true);
					settings.setPluginState(PluginState.ON);
					settings.setJavaScriptCanOpenWindowsAutomatically(true);
					settings.setAllowFileAccess(true);
					//settings.setPluginsEnabled(true);
					settings.setDefaultTextEncodingName("UTF-8");
					videoweb.setBackgroundColor(0);
					// 加载指定url的网页
					videoweb.setWebViewClient(new WebViewClient() {
						public boolean shouldOverrideUrlLoading(WebView view,
								String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
							view.loadUrl(url);
							return true;
						}

						@Override
						public void onPageFinished(WebView view, String url) {
 							super.onPageFinished(view, url);
							finishShow();
						}

					});
					videoweb.addJavascriptInterface(
							new YukuJavaScriptInterface(), "YukuAndroid");
					videoweb.loadUrl(VIDEO_HTML);
				}

			//}

		}.start();
 		   
 	}
	 public class YukuJavaScriptInterface {
   	        public void startInitPlayer() {
 	        	//UNIX时间戳 
                final String unixstamp=Long.toString(System.currentTimeMillis());
                //SIGNATURE 为 md5(VID_TIMESTAMP_CLIENT-SECRET)，
                final String signa=MD5.hexdigest(path+"_"+unixstamp+"_"+YukuOAuth.CLIENT_SECRET);
                LogUtil.logInfo(getClass(),YukuOAuth.CLIENT_ID+","+path+","+ unixstamp+","+signa);
         
  	        	handler.post(new Runnable() {
	                @Override
	                public void run() {
	                    // This gets executed on the UI thread so it can safely modify Views
	                	//执行javascript方法
 	                	videoweb.loadUrl("javascript:initPlayer('"+YukuOAuth.CLIENT_ID+"','" + path + "','"+unixstamp+"','"+signa+"')");
	                }
	            });
	        }
   	     public void play() {
               LogUtil.logInfo(getClass(), "play...");
	        	handler.post(new Runnable() {
	                @Override
	                public void run() {
	                    // This gets executed on the UI thread so it can safely modify Views
	                	//执行javascript方法
	                	videoweb.loadUrl("javascript:playVideo()");
	                }
	            });
	        }
	    }
	/**
	 * 结束加载过程
	 */
	private void finishShow(){
		handler.post(new Runnable() {
 			@Override
			public void run() {
				if(processDialog!=null&&processDialog.isShowing())
					processDialog.dismiss();
			}
		});
	
	}
	/**
	 * 检测系统中是否已经安装了adobe flash player插件，插件的packageName是com.adobe.flashplayer
	 * @return
	 */
	private boolean checkFlashPlayer() {  
        PackageManager pm = getPackageManager();  
        List<PackageInfo> infoList = pm  
                .getInstalledPackages(PackageManager.GET_SERVICES);  
        for (PackageInfo info : infoList) {  
            if ("com.adobe.flashplayer".equals(info.packageName)) {  
                return true;  
            }  
        }  
        return false;  
    }  
	
	private boolean isLocalUrl(String url){
		if(url!=null&&url.indexOf(AppConstant.VIDEO_DIR)>=0)
			return true;
		return false;
	}

	 
}
