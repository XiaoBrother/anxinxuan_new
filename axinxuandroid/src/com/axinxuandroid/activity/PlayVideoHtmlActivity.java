package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadProustThread;
import com.axinxuandroid.activity.view.ImageGalleryView;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.VideoView;
import com.axinxuandroid.activity.view.VideoView.VideoPlayFinishListener;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.sina.weibo.sdk.utils.MD5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
public class PlayVideoHtmlActivity  extends NcpZsActivity{
  	private String path;
  	private WebView videoweb;
  	private VideoView videoview;
  	private ImageView videoimg;
 	private ProgressDialog processDialog;
 	private Handler handler=new Handler();
 	private static final String VIDEO_HTML ="file:///android_asset/html5video.html";
 	private static final String VIDEO_URL_SUBFIX ="http://v.youku.com/v_show/id_";
 	private static final String VIDEO_GET_MP4_URL_SUBFIX ="http://api.3g.youku.com/layout/phone2_1/play?point=1&id=" ;
 	private static final String VIDEO_GET_MP4_URL_POSTFIX ="&pid=352e7f78a0bc479b&format=4&language=guoyu&audiolang=1&guid=c7a0fd9f8f19ea5cbafde16f327f8004&ver=2.3.1&operator=%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A_46001&network=WIFI";
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.playvideo);
 		path= this.getIntent().getStringExtra("path");
   		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
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
  			prepareShow();
//  			Intent intent = new Intent(Intent.ACTION_VIEW);
//  			String url=getMp4Url();
//  			if(url!=null){
//  			  intent.setDataAndType(Uri.parse(url),"video/mp4"); 
//    	      startActivity(intent);
//  			}else{
//  				NcpzsHandler hand=Gloable.getInstance().getCurHandler();
//  				hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
// 					@Override
//					public void onHandlerFinish(Object result) {
// 						PlayVideoHtmlActivity.this.finish();
//					}
//				});
//  				hand.excuteMethod(new MessageDialogHandlerMethod("", "优酷视频地址解析异常！"));
//  			}
   		}else{
  			 videoimg.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.videobg));
  			((RelativeLayout)videoview.getParent()).setVisibility(View.VISIBLE);
  		}
   		
	}
	
   private void prepareShow(){
	   NcpzsHandler handler=Gloable.getInstance().getCurHandler();
	    handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	 			@Override
				public void onHandlerFinish(Object result) {
	 				if(result!=null)
	 					processDialog=(ProgressDialog) ((Map)result).get("process");
	 				showVideo();
				}
			});
	   	handler.excuteMethod(new ProcessDialogHandlerMethod("","加载中..."));
   }
	 
	/**
	  * 展示视频
	 */
	public void showVideo(){
 		 
 		new Thread() {
			@Override
			public void run() {
				 
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
					videoweb.addJavascriptInterface(new VideoHtmlJavaScriptInterface(), "VideoHtmlAndroid");
 					//videoweb.loadUrl(VIDEO_HTML);
 					videoweb.loadUrl(VIDEO_URL_SUBFIX+path);
				}

 		}.start();
 		   
 	}
	
	public String getMp4Url(){
	    	byte[] data = HttpUtil.readStream(VIDEO_GET_MP4_URL_SUBFIX+path+VIDEO_GET_MP4_URL_POSTFIX);
	    	LogUtil.logInfo(getClass(), "get mp4 url:"+VIDEO_GET_MP4_URL_SUBFIX+path+VIDEO_GET_MP4_URL_POSTFIX);
	        if(data!=null){
	        	String json = new String(data);
				try {
					
				JSONObject jsonObject = new JSONObject(json);
				if(jsonObject.has("results")){
					JSONObject results=jsonObject.getJSONObject("results");
					if(results!=null){
						JSONObject mp4=(JSONObject) results.getJSONArray("3gphd").get(0);
						String url=mp4.getString("url");
	                    return url;
					}
				}
			} catch (JSONException e) {
					e.printStackTrace();
			}
	       }
	        return null;
	   }
	 public class VideoHtmlJavaScriptInterface {
   	     
   	     public void play() {
 	        	handler.post(new Runnable() {
	                @Override
	                public void run() {
	                    // This gets executed on the UI thread so it can safely modify Views
	                	String url=getMp4Url();
	                	LogUtil.logInfo(getClass(), "mp4 url :"+url);
	                	//执行javascript方法
	                	videoweb.loadUrl("javascript:addvideo('"+url+"'");
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
	 
	
	private boolean isLocalUrl(String url){
		if(url!=null&&url.indexOf(AppConstant.VIDEO_DIR)>=0)
			return true;
		return false;
	}

	 
}
