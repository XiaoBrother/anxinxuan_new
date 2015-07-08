package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.menu.ComposerButtonAnimation;
import com.axinxuandroid.activity.menu.ComposerButtonGrowAnimationIn;
import com.axinxuandroid.activity.menu.ComposerButtonGrowAnimationOut;
import com.axinxuandroid.activity.menu.ComposerButtonShrinkAnimationOut;
import com.axinxuandroid.activity.menu.InOutAnimation;
import com.axinxuandroid.activity.menu.InOutImageButton;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;
    
 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class ImageGalleryWindow extends PopupWindow{
    private ImageGalleryView imgs;
 	private Context context;
 	private TextView text;
	private boolean isshowing=false;
	private List<InOutImageButton> menus;
 	public ImageGalleryWindow(Context context){
		super(context);
		this.context=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.imagegallery_window, null);
		this.setContentView(view);
		imgs=(ImageGalleryView) view.findViewById(R.id.imggallerywindow_img);
		text=(TextView) view.findViewById(R.id.imggallerywindow_text);
 		// 必须设置宽高
 		this.setWidth(Gloable.getInstance().getScreenWeight());  
        this.setHeight(Gloable.getInstance().getScreenHeight());  
      // 如果设置setBackgroundDrawable，onTouch监听事件不启用，
        // OutsideTouchable不启用，OnKey的KEYCODEY_BACK不启用；
        // 如果没有设置，OutsideTouchable启用，OnTouch启用。
        //setBackgroundDrawable(new BitmapDrawable());
        //ColorDrawable dw = new ColorDrawable(Color.BLACK);
        //this.setBackgroundDrawable(dw);
        this.setBackgroundDrawable(new BitmapDrawable());
		//this.setFocusable(true); 
		this.setTouchable(true); //设置PopupWindow可触摸  
		this.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		view.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				dismiss();
			}
		});
		imgs.setImgScaleType(ScaleType.CENTER_INSIDE);
		imgs.setImgWidthWidthHeight(Gloable.getInstance().getScreenWeight(), Gloable.getInstance().getScreenHeight());
		this.setAnimationStyle(R.style.SharePopupAnimation);
	}
 
 	public void addImg(Bitmap btm){
 		imgs.addImage(btm);
 		text.setText(imgs.getCurrentIndex()+"/"+imgs.getImgCount());
 	}
 	
 	public void replaceImg(int postion,Bitmap btm){
 		imgs.replaceImage(btm, postion);
 	}

	 
 	public void clear(){
 		imgs.cleare();
 	}

	@Override
	public void dismiss() {
		clear();
		super.dismiss();
	}
 	
 	
 	
}
