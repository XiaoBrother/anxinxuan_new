package com.ncpzs.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.service.SharedPreferenceService;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
 
public class SendShareInfo {
 	private IWeiboShareAPI iweibo;
	private IWXAPI iweixin;
    private Activity context;
    private static final int IMAGE_WIDTH=100;
    private static final int IMAGE_HEIGHT=100;
	public SendShareInfo(Activity context){
		this.context=context;
  	}
  	private void initWeibo(){
		if(iweibo==null){
			iweibo=WeiboShareSDK.createWeiboAPI(context, OAuthConstant.Sina.APP_KEY);
			iweibo.registerApp();
  		}
 	}
	private void initWeiXin(){
		if(iweixin==null){
  		   iweixin=WXAPIFactory.createWXAPI(context, OAuthConstant.WebChat.APP_ID,true);
  		   iweixin.registerApp(OAuthConstant.WebChat.APP_ID);
  		}
 	}
	
	public IWeiboShareAPI getWeiboAPI(){
		return iweibo;
	}
	public void sendSinaShare(String message,String imgurl) {
		if(iweibo==null)
			initWeibo();
  		//if(iweibo.isWeiboAppInstalled()){
			//如果没安装微博
// 			iweibo.registerWeiboDownloadListener(new IWeiboDownloadListener() {
// 				@Override
//				public void onCancel() {
// 					
//				}
//			});
 	//		return ;
	//	}
	    //if(iweibo.checkEnvironment(true)){
	    	 
	    //}
  	    WeiboMultiMessage wmsg = new WeiboMultiMessage();
		TextObject text = new TextObject();
		text.text =message;
 		//smr.message = wmsg;
		wmsg.textObject = text;
		
		if(imgurl!=null){
			ImageObject imageObject = new ImageObject();
			Bitmap bitmap =BitmapUtils.getCompressImage(imgurl);
 	        Bitmap thbitmap =ThumbnailUtils.extractThumbnail(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT);
 	        bitmap.recycle();
	        imageObject.setImageObject(thbitmap);
	        wmsg.imageObject = imageObject;
		}
		SendMultiMessageToWeiboRequest smr = new SendMultiMessageToWeiboRequest();
		smr.transaction = String.valueOf(System.currentTimeMillis());// 唯一标识
		smr.multiMessage = wmsg;
		iweibo.sendRequest(context,smr);
  	}

//	public void sendWebchatShare(String message) {
//  		 WXTextObject text=new WXTextObject();
// 		 text.text=message;
// 		 WXMediaMessage msg=new WXMediaMessage();
// 		 msg.mediaObject=text;
// 		 msg.description=message;
// 		 SendMessageToWX.Req req=new SendMessageToWX.Req();
// 		 req.transaction ="webchat"+ String.valueOf(System.currentTimeMillis());// 唯一标识
// 		 req.message=msg;
// 		 iweixin.sendReq(req);
//	} 
	public void sendWebchatShare(String message,String imgurl) {
		if(iweixin==null)
			initWeiXin();
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = HttpUrlConstant.URL_HEAD;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title =message;
 		if(imgurl!=null){
			Bitmap bitmap =BitmapUtils.getCompressImage(imgurl);
			Bitmap thbitmap =ThumbnailUtils.extractThumbnail(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT);
 	        bitmap.recycle();
 	        LogUtil.logInfo(getClass(), imgurl);
 	        msg.thumbData = Util.bmpToByteArray(thbitmap, true);
		}
 		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "webchat"+ String.valueOf(System.currentTimeMillis());// 唯一标识
		req.message = msg;
   		 iweixin.sendReq(req);
   	} 
//	public boolean sendMomentsShare(String message) {
// 		//检查微信版本
//		if(iweixin.getWXAppSupportAPI()>=0x21020001){
//			 WXTextObject text=new WXTextObject();
//	 		 text.text=message;
//	 		 WXMediaMessage msg=new WXMediaMessage();
//	 		 msg.mediaObject=text;
//	 		 msg.description=message;
//	 		 SendMessageToWX.Req req=new SendMessageToWX.Req();
//	 		 req.transaction ="moments"+String.valueOf(System.currentTimeMillis());// 唯一标识
//	 		 req.message=msg;
//	 		 req.scene=SendMessageToWX.Req.WXSceneTimeline;
//	 		 iweixin.sendReq(req);
//	 		 return true;
//		}
//		return false;
//	}
	public boolean sendMomentsShare(String message,String imgurl) {
		if(iweixin==null)
			initWeiXin();
 		//检查微信版本
		if(iweixin.getWXAppSupportAPI()>=0x21020001){
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = HttpUrlConstant.URL_HEAD;
			WXMediaMessage msg = new WXMediaMessage(webpage);
			msg.title =message;
	 		if(imgurl!=null){
				Bitmap bitmap =BitmapUtils.getCompressImage(imgurl);
				Bitmap thbitmap =ThumbnailUtils.extractThumbnail(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT);
	 	        bitmap.recycle();
	 	        //LogUtil.logInfo(getClass(), imgurl);
	 	        msg.thumbData = Util.bmpToByteArray(thbitmap, true);
			}
	 		SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = "webchat"+ String.valueOf(System.currentTimeMillis());// 唯一标识
			req.message = msg;
			req.scene=SendMessageToWX.Req.WXSceneTimeline;
	   		iweixin.sendReq(req); 
 	 		return true;
		}
		return false;
	}
//	public void sendWebchatShare(String message) {
//		Intent intent = new Intent();
//		ComponentName comp = new ComponentName("com.tencent.mm",
//				"com.tencent.mm.ui.tools.ShareImgUI");
//		intent.setComponent(comp);
//		intent.setAction("android.intent.action.SEND");
//		intent.setType("text/plain");
//		//intent.setType("image/*");
//		//intent.setFlags(0x3000001);
//		//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//		intent.putExtra(Intent.EXTRA_TEXT,message);
//		context.startActivity(intent);
//	}
//
//	public void sendMomentsShare(String message) {
//		Intent intent = new Intent();
//		ComponentName comp = new ComponentName("com.tencent.mm",
//				"com.tencent.mm.ui.tools.ShareToTimeLineUI");
//		intent.setComponent(comp);
//		intent.setAction("android.intent.action.SEND");
//		//intent.setType("image/*");
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_TEXT,message);
//		//intent.setFlags(0x3000001);
//		//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//		context.startActivity(intent);
//	}
}
