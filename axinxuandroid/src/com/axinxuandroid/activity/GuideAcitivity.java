package com.axinxuandroid.activity;

 

import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle; 
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

public class GuideAcitivity extends NcpZsActivity implements OnGestureListener{
    private static final int FLIP_WIDTH=80;
  	private ViewFlipper viewFlipper;
  	private GestureDetector detector;//识别手势
   	private LinearLayout indexlay;
     @Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.guide);
		indexlay=(LinearLayout) findViewById(R.id.guide_index);
 		detector=new GestureDetector(this);
		viewFlipper=(ViewFlipper) findViewById(R.id.guide_viewFlipper);
 		viewFlipper.addView(getImageView(R.drawable.welcome));
		viewFlipper.addView(getImageView(R.drawable.ann1));
		RelativeLayout rel=new RelativeLayout(this);
 		Button btn=new Button(this);
 		btn.setBackgroundResource(R.drawable.shape_corner_button);
 		btn.setText("开始放心之旅");
 		btn.setTextColor(Color.WHITE);
 		btn.setTextSize(DensityUtil.sp2px(15));
 		RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, DensityUtil.dip2px(50));
 		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
 		param.bottomMargin=DensityUtil.dip2px(50);
 		param.leftMargin=DensityUtil.dip2px(20);
 		param.rightMargin=DensityUtil.dip2px(20);
 		btn.setLayoutParams(param);
  		ImageView lastimg=getImageView(R.drawable.app);
 		rel.addView(lastimg,  new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT ));
 		rel.addView(btn);
 		viewFlipper.addView(rel);
 		btn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				GuideAcitivity.this.finish();
 				startActivity(new Intent(GuideAcitivity.this,WelcomeActivity.class));
 				GuideAcitivity.this.finish();
			}
		});
 		createIndexImage();
     }
   	private ImageView getImageView(int id) {
        ImageView iv = new ImageView(this);
        iv.setImageResource(id);
        return iv;
    }
    
   	private void createIndexImage(){
   		int len=viewFlipper.getChildCount();
        for(int i=0;i<len;i++){
   			ImageView indeximage = getImageView(R.drawable.imgindex_normal); 
   	  		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, DensityUtil.dip2px(4));
   	  		//lp.weight=1;
    	   	lp.setMargins(DensityUtil.dip2px(5), 0,DensityUtil.dip2px(5), 0);
    	   	lp.width=DensityUtil.dip2px(15);
    	   	lp.height=DensityUtil.dip2px(15);
   	  		indeximage.setLayoutParams(lp);
    	  	indexlay.addView(indeximage);
   		}
   		if(len>0)
   		 ((ImageView)indexlay.getChildAt(0)).setImageResource(R.drawable.imgindex_selected);
   		 
   			
   	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
         return this.detector.onTouchEvent(event);
    }
    
	@Override
	public boolean onDown(MotionEvent e) {
		 
		return false;
	}
	/**
	 * 滑动手势事件；Touch了滑动一点距离后，在ACTION_UP时才会触发       
	 * 	参数：e1 第1个ACTION_DOWN MotionEvent 并且只有一个；
	 * 	e2 最后一个ACTION_MOVE MotionEvent ；velocityX X轴上的移动速度，像素/秒 ；
	 * 	velocityY Y轴上的移动速度，像素/秒.触发条件：X轴的坐标位移大于FLING_MIN_DISTANCE，
     *      且移动速度大于FLING_MIN_VELOCITY个像素/秒
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
   		if (e1.getX() - e2.getX() > FLIP_WIDTH) {
             this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
             this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
             this.viewFlipper.showNext();
          } else if (e1.getX() - e2.getX() < -FLIP_WIDTH) {
        	 this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
             this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
             this.viewFlipper.showPrevious();
           }
  		if(viewFlipper.getChildCount()>0){
  			 for(int i=0;i<viewFlipper.getChildCount();i++)
  			  ((ImageView)indexlay.getChildAt(i)).setImageResource(R.drawable.imgindex_normal);
  			((ImageView)indexlay.getChildAt(viewFlipper.getDisplayedChild())).setImageResource(R.drawable.imgindex_selected);
  			
  		}
 		
 		 
 		 return true;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		 
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		 
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	 
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		 
		return false;
	}
}
