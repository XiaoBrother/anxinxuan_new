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
import com.ncpzs.util.LogUtil;
    
 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class MenuWindow extends PopupWindow{
    private ViewGroup buttonsContain;
 	private Context context;
	public static final int MENU_WIDTH=155;
	public static final int MENU_HEIGHT=270;
	private View parent;
	private boolean isshowing=false;
	private List<InOutImageButton> menus;
 	public MenuWindow(Context context){
		super(context);
		this.context=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.menu, null);
		this.setContentView(view);
		buttonsContain=(ViewGroup) view.findViewById(R.id.menu_composer_buttons_wrapper);
		menus=new ArrayList<InOutImageButton>();
		// 必须设置宽高
 		this.setWidth(MENU_WIDTH);  
        this.setHeight(MENU_HEIGHT);  
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
 			//	LogUtil.logInfo(getClass(),v.getClass().toString());
    				showMenu(parent);
  			}
		});
        
	}
 
 	public void clear(){
 		buttonsContain.removeAllViews();
 		this.menus.clear();
  	}
 	/**
 	 * 添加菜单
 	 */
	public void addMenu(InOutImageButton menu,final MenuClickListerner lis){
		if(menu==null) return ;
		menu.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				startComposerButtonClickedAnimations(v,lis);
 			}
		});
		menu.setId(menus.size());
		buttonsContain.addView(menu);
 		RelativeLayout.LayoutParams layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
 		menu.setLayoutParams(layout);
 		this.menus.add(menu);
 		resetMenuXY();
 	}
    /**
     * 重设坐标
     */
	private void resetMenuXY(){
		if(this.menus==null||this.menus.size()<1) return ;
		for(int i=0;i<this.menus.size();i++){
			InOutImageButton menu=this.menus.get(i);
			RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams) menu.getLayoutParams();
			float[] xy=this.getXY(i);
 			layout.leftMargin=(int)xy[0];
			layout.bottomMargin=(int)xy[1];
			menu.setLayoutParams(layout);
		}
	}
 
	private void startComposerButtonClickedAnimations(View view,final MenuClickListerner lis) {
 		Animation shrinkOut2 = new ComposerButtonShrinkAnimationOut(300);
		Animation growOut = new ComposerButtonGrowAnimationOut(300);
		growOut.setInterpolator(new AnticipateInterpolator(2.0F));
		growOut.setAnimationListener(new Animation.AnimationListener() {
 			@Override
			public void onAnimationEnd(Animation animation) {
  				if(lis!=null)
 				  lis.clickMenu();
  				dismiss();
			}
 			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		
 		for (int i = 0; i < this.buttonsContain.getChildCount(); i++) {
			final View button = this.buttonsContain.getChildAt(i);
 			if (!(button instanceof InOutImageButton))
				continue;
			if (button.getId() != view.getId())
				// 其他按钮缩小消失
				button.setAnimation(shrinkOut2);
			else {
				// 被点击按钮放大消失
				button.startAnimation(growOut);
			}
		}
 		
	}
	
	public void showMenu(View view) {
		//LogUtil.logInfo(getClass(), "click..."+(this.parent==null)+":"+(this.parent!=view));
		if(view==null) return;
		if(this.parent==null||this.parent!=view){
			dismiss();
 		   this.showAsDropDown(view, 0, -160);
  		   ComposerButtonAnimation.startAnimations(this.buttonsContain, InOutAnimation.Direction.IN);
  		   this.isshowing=true;
		   this.parent=view;
		}else{
 			if(this.isshowing){
  				ComposerButtonAnimation.startAnimations(this.buttonsContain, InOutAnimation.Direction.OUT);
 				this.isshowing=false;
			}
			else {
   				 ComposerButtonAnimation.startAnimations(this.buttonsContain, InOutAnimation.Direction.IN);
				 this.isshowing=true;
 			}
		}
  
	}

	@Override
	public void dismiss() {
		//ComposerButtonAnimation.startAnimations(this.buttonsContain, InOutAnimation.Direction.OUT);
		this.isshowing=false;
		this.parent=null;
  		super.dismiss();
	}
	
	public interface MenuClickListerner{
		public void clickMenu();
	}
	 
	/**
	 * 获取第n个坐标
	 */
	private float[] getXY(int index){
		int radius=90;//半径
		int count=this.menus.size();
 		int start_degree = -90;//开始角度
	    int end_degree = 15;//结束角度
	    int degree=end_degree-start_degree;
	    int start_x=0;
	    int start_y=110;
	    float[] xy=new float[2];
	    double p_degree = 0;
	    if(count-1<=0) p_degree=degree;
	    else p_degree=degree * 1.0 / (count - 1);
 	    float t_degree = (float) Math.toRadians(start_degree+degree - (index * p_degree));
	    float x=(float) (start_x+radius * Math.cos(t_degree) ); 
		float y=(float) (start_y+radius* Math.sin(t_degree));
		xy[0]=x;
		xy[1]=y;
		//LogUtil.logInfo(getClass(), "x:"+x+",y:"+y);
 		return xy;
	}
}
