package com.axinxuandroid.activity.menu;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

 
public class ComposerButtonAnimation extends InOutAnimation {

	public static final int		DURATION	= 200;
	private static final int	xOffset		= 16;
	private static final int	yOffset		= -13;

	public ComposerButtonAnimation(Direction direction, long l, View view) {
		super(direction, l, new View[] { view });
	}

	public static void startAnimations(ViewGroup viewgroup,
			InOutAnimation.Direction direction) {
		switch (direction) {
		case IN:
			startAnimationsIn(viewgroup);
			break;
		case OUT:
			startAnimationsOut(viewgroup);
			break;
		}
	}

	private static void startAnimationsIn(ViewGroup viewgroup) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof InOutImageButton) {
				InOutImageButton inoutimagebutton = (InOutImageButton) viewgroup
						.getChildAt(i);
				ComposerButtonAnimation animation = new ComposerButtonAnimation(
						InOutAnimation.Direction.IN, DURATION, inoutimagebutton);
				if(-1 + viewgroup.getChildCount()!=0)
				 animation.setStartOffset((i * 100)/ (-1 + viewgroup.getChildCount()));
				else animation.setStartOffset(0);
				animation.setInterpolator(new OvershootInterpolator(2F));
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	private static void startAnimationsOut(ViewGroup viewgroup) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof InOutImageButton) {
				InOutImageButton inoutimagebutton = (InOutImageButton) viewgroup
						.getChildAt(i);
//				ComposerButtonAnimation animation = new ComposerButtonAnimation(
//						InOutAnimation.Direction.OUT, DURATION,
//						inoutimagebutton);
//				if(-1 + viewgroup.getChildCount()!=0)
//				  animation.setStartOffset((100 * ((-1 + viewgroup.getChildCount()) - i))/ (-1 + viewgroup.getChildCount()));
//				else animation.setStartOffset(0);
//				animation.setInterpolator(new AnticipateInterpolator(2F));
//				inoutimagebutton.startAnimation(animation);
				Animation shrinkOut2 = new ComposerButtonShrinkAnimationOut(300);
				inoutimagebutton.startAnimation(shrinkOut2);
			}
		}
	}
   /**
    * TranslateAnimation是移动的动画效果。
    *   float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
 	　　float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
	
	　　float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
	
	　　float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
	
	　　如果view在A(x,y)点 那么动画就是从B点(x+fromXDelta, y+fromYDelta)点移动到C 点(x+toXDelta,y+toYDelta)点.
    */
	@Override
	protected void addInAnimation(View[] aview) {
		MarginLayoutParams mlp = (MarginLayoutParams) aview[0]
				.getLayoutParams();
		addAnimation(new TranslateAnimation(xOffset + -mlp.leftMargin, 0F,
				yOffset + mlp.bottomMargin, 0F));
	}

	@Override
	protected void addOutAnimation(View[] aview) {
		MarginLayoutParams mlp = (MarginLayoutParams) aview[0]
				.getLayoutParams();
		addAnimation(new TranslateAnimation(0F, xOffset + -mlp.leftMargin, 0F,
				yOffset + mlp.bottomMargin-90));
	}
}
