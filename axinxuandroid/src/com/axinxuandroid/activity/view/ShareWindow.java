package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.activity.R;

import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.SendShareInfo;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
    
 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ShareWindow extends PopupWindow {
  	private Context context;
 	public static final int MENU_HEIGHT=250;
 	private LinearLayout sinaweibo, webchat,laiwang,moments;
  	public static final int SHARE_SINAWEIBO=1;
 	public static final int SHARE_WEBCAHT=2;
 	public static final int SHARE_LAIWANG=3;
 	public static final int SHARE_MOMENTS=4;
   	private ShareMessageInterface sminf;
   	private	SendShareInfo send;
  	public ShareWindow(Activity context){
		super(context);
		this.context=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.sharewindow, null);
		view.setFocusableInTouchMode(true);
		this.setContentView(view);
  		// 必须设置宽高
 		this.setWidth(WindowManager.LayoutParams.FILL_PARENT);  
        this.setHeight(WindowManager.LayoutParams.FILL_PARENT);  
        ColorDrawable dw = new ColorDrawable(Color.BLACK);
        this.setBackgroundDrawable(dw);
        this.getBackground().setAlpha(180);
  		//this.setFocusable(true); 
		this.setTouchable(true); //设置PopupWindow可触摸  
	    this.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
 		this.setAnimationStyle(R.style.SharePopupAnimation);
 		sinaweibo=(LinearLayout) view.findViewById(R.id.share_sinaweibo);
  		send=new SendShareInfo(context);
 		view.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				ShareWindow.this.dismiss();
			}
		});
 		sinaweibo.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				send.sendSinaShare(getMessage(),getImagePath());
 				//toShare(SHARE_SINAWEIBO);
			}
		});
		webchat=(LinearLayout) view.findViewById(R.id.share_webchat);
		webchat.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
  				send.sendWebchatShare(getMessage(),getImagePath());
 				//toShare(SHARE_WEBCAHT);
			}
		});
		laiwang=(LinearLayout) view.findViewById(R.id.share_laiwang);
		laiwang.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				//toShare(SHARE_LAIWANG);
 			}
		});
		moments=(LinearLayout) view.findViewById(R.id.share_moments);
		moments.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				send.sendMomentsShare(getMessage(),getImagePath());
 				//toShare(SHARE_MOMENTS);
			}
		});
		
 	}
 
  	public void setShareMessageInterface(ShareMessageInterface inf){
  		this.sminf=inf;
  	}
  	 public interface ShareMessageInterface  {
  		 public String getShareMessage();
  		 public String getShareImagePath();
  	 }
  	private String getMessage(){
  		 String message="";
 		 if(this.sminf!=null)
 			 message=this.sminf.getShareMessage();
 		 return message;
 	 }
  	private String getImagePath(){
 		 if(this.sminf!=null)
			 return this.sminf.getShareImagePath();
		 return null;
	}
//   	private void toShare(int type){
// 		 Intent inte=new Intent(context,ShareInfoActivity.class);
// 		 String message="";
// 		 if(this.sminf!=null)
// 			 message=this.sminf.getShareMessage();
// 		inte.putExtra("message", message);
// 		inte.putExtra("type", type);
// 		context.startActivity(inte);
// 	 }

	
  	
  	
}
