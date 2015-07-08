package com.axinxuandroid.activity.view;

import java.util.Date;

import com.axinxuandroid.activity.R;
import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;

public class DragListView extends ListView implements
		android.widget.AbsListView.OnScrollListener {
	public interface DragListener {
		public void onDragFinsh(int position);
		public void onClick(int position);
		public void onStartMove(int position,View view,float x,float y);
 		public void onFinishMove(int position,View view,float x,float y);
 		public void onTouchDown(int position,View view);
        public void onTouchUp(int position,View view);
        public void onBackToInitPosition(View view);
 	}

	private final int STATE_NORMAL = 0;
	private final int STATE_SCROLL_VERTICAL = 1;//纵向滑动
	private final int STATE_SCROLL_HORIZONTAL = 2;//横向滑动
	private final int STATE_SNAP_OUT = 3;
	private final int DIRECT_NONE=0;//未滑动
	private final int DIRECT_LEFT=1;//向左
	private final int DIRECT_RIGHT=2;//向右
	private int state;
	private Context mContext;
	private int mTouchSlop;
	private DragListener listener;
	private int leftHiddenLen=0;
	private int rightHiddenLen=0;
	private boolean canmove=true;
 	private int h_direct;//水平方向
	// 实现View平滑滚动的一个Helper类,设置mScroller滚动的位置时，
	//并不会导致View的滚动，通常是用mScroller记录/计算View滚动的位置
	//，再重写View的computeScroll()，完成实际的滚动。 
	//mScroller.getCurrX() //获取mScroller当前水平滚动的位置  
	//mScroller.getCurrY() //获取mScroller当前竖直滚动的位置  
	//mScroller.getFinalX() //获取mScroller最终停止的水平位置  
	//mScroller.getFinalY() //获取mScroller最终停止的竖直位置  
	//mScroller.setFinalX(int newX) //设置mScroller最终停留的水平位置，没有动画效果，直接跳到目标位置  
	//mScroller.setFinalY(int newY) //设置mScroller最终停留的竖直位置，没有动画效果，直接跳到目标位置  
	//滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量, duration为完成滚动的时间  
	//mScroller.startScroll(int startX, int startY, int dx, int dy) //使用默认完成时间250ms  
	//mScroller.startScroll(int startX, int startY, int dx, int dy, int duration)  
	//mScroller.computeScrollOffset() //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。 
	private Scroller mScroller;
	private View snapView,downView;
	private int firstVisibleItem = 0;
	private int listItem = 0;
	private float alpha;
 	private boolean ismove=false;
 	private View moveView=null;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;
	private Bitmap mDragBitmap;
	private ImageView mDragView;
	private int preheight=0;
  	private static final int DOWN_MOVE_TIMESTAMP=800;//时间差
	public DragListView(Context context) {
		this(context, null, 0);
	}

	public DragListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray tArray = context.obtainStyledAttributes(attrs,
                R.styleable.draglistview);
		leftHiddenLen= tArray.getInt(R.styleable.draglistview_lefthiddenlen, 0);
		rightHiddenLen= tArray.getInt(R.styleable.draglistview_righthiddenlen, 0);
		setOnScrollListener(this);
		mScroller = new Scroller(context);
		this.mContext = context;
		this.snapView = null;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//获取一个距离，这个距离表示滑动的时候，手的移动要大于这个距离才开始移动控件
	    
	}

	public void setDragListener(DragListener listener) {
		this.listener = listener;
	}

	private void snapToDestination() {
		// if(listItem > this.getChildCount() || listItem < 0)
		// return;
		//System.out.println("snapToDestination:"+(snapView==null) );
		if (snapView == null)
			return;
		int width = this.getMeasuredWidth();
		int scroll = snapView.getScrollX();
		//System.out.println("scroll:" + scroll);
		if (-scroll > leftHiddenLen / 3||scroll>rightHiddenLen/3) {
 			snapToOut();
		} else {
			snapToBack();
		}

	}
   //调用startScroll()是不会有滚动效果的,只有在computeScroll()获取滚动情况，做出滚动的响应
	//恢复到初始位置
	private void snapToBack() {
		if (snapView == null)
			return;
		//System.out.println("snapToBack");
		int delta = snapView.getScrollX();
		//System.out.println("snapToBack:" + delta);
 		mScroller.startScroll(delta, 0, -delta, 0, Math.abs(delta) );
 	}

	private void snapToOut() {
		if (snapView == null)
			return;
		state = STATE_SNAP_OUT;
		// listener.onDragFinsh(listItem);
		//System.out.println("snapToOut");
		final int delta = (int) (getMeasuredWidth() - snapView.getScrollX());
		if(this.listener!=null)
		  mHandler.sendEmptyMessageDelayed(0, (long) (Math.abs(delta) * 2 / 3.0));
		//System.out.println("snapToOut:" + delta);
		int flag = snapView.getScrollX() > 0 ? 1 : -1;
		int dx=0;
		if(h_direct==DIRECT_RIGHT){
			dx=snapView.getScrollX()+leftHiddenLen;
 		}
 		else if(h_direct==DIRECT_LEFT){
 			dx=snapView.getScrollX()-rightHiddenLen;
  		}
			
		//System.out.println("snapToOut:"+snapView.getScrollX());
 		mScroller.startScroll(snapView.getScrollX(), 0, -dx, 0, Math
				.abs(delta) );
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (listener != null)
				listener.onDragFinsh(listItem);
		};
	};

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset() && snapView != null) {// 如果返回true，表示动画还没有结束
			//System.out.println("computeScroll");
			snapView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
 			if(mScroller.getCurrX()==0){
  				if(this.listener!=null)
 				  this.listener.onBackToInitPosition(snapView);
  			} 
 			postInvalidate();
		} else {
			//System.out.println("compute");
			if (state == STATE_SNAP_OUT && listener != null) {
				state = STATE_NORMAL;
				// listener.onDragFinsh(listItem);
				snapView = null;
			}
		}
	}

	int downx,downy, downpositon,x, y = 0;
 	VelocityTracker mVelocityTracker;//这个类是用来得到手势在屏幕上滑动的速度
    long down_time;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			// 使用obtain方法得到VelocityTracker的一个对象
			mVelocityTracker = VelocityTracker.obtain();
		}
		// 将当前的触摸事件传递给VelocityTracker对象,对那个MotionEvent 进行监控（也就是说得到在那个MotionEvent上的速度） 
		mVelocityTracker.addMovement(event);
		if(event.getAction()==MotionEvent.ACTION_DOWN){
 			state = STATE_NORMAL;
			downx=x = (int) event.getX();
			downy=y = (int) event.getY();
 			snapView = null;
			mScroller.setFinalX(0);
			down_time=(new Date()).getTime();
 		    downpositon=pointToPosition(downx, downy);
			downView=getChildAt( downpositon - firstVisibleItem);
			if(this.listener!=null){
  				this.listener.onTouchDown(downpositon,downView);
			}
 		}else if(event.getAction()==MotionEvent.ACTION_MOVE){
   			int dx = (int) (event.getX() - x);
			int dy = (int) (event.getY() - y);
			alpha = 255 - Math.abs(dx / 2);
			if(ismove&&canmove){
				int[] location = new  int[2] ;
				getLocationOnScreen(location); //获取在当前窗口内的绝对坐标
    			dragView((int)(event.getX()-moveView.getMeasuredWidth()/2), (int)(location[1]+ event.getY()-preheight/2));
				return true;
 			}else{
 				long timedis=(new Date()).getTime()-down_time;
 				if(canmove&&timedis>DOWN_MOVE_TIMESTAMP&&Math.abs(dy)<mTouchSlop&&Math.abs(dx)< mTouchSlop){
   					ismove=true;
  					int[] location = new  int[2] ;
   					getLocationOnScreen(location); //获取在当前窗口内的绝对坐标
					listItem = pointToPosition(x, (int) event.getY());
					moveView=  getChildAt(listItem - firstVisibleItem);
 					moveView.scrollTo(0, 0);
					moveView.setDrawingCacheEnabled(true);
					preheight=moveView.getMeasuredHeight();
   					Bitmap bitmap = Bitmap.createBitmap(moveView.getDrawingCache());
     				startDragging(bitmap,(int)(event.getX()-moveView.getMeasuredWidth()/2), (int)(location[1]+ event.getY()-preheight/2));
   					moveView.setVisibility(View.GONE);
   					ViewGroup.LayoutParams params = moveView.getLayoutParams();
   					params.height = 1;
   					moveView.setLayoutParams(params);
   					if(this.listener!=null){
  						listener.onStartMove(listItem,moveView, location[0]+event.getX(),location[1]+ event.getY());
   					}
    				//LogUtil.logInfo(getClass(), "start move:"+event.getY()+","+moveView.getMeasuredHeight());
 				}else{
 					//判断用户是横向滑动还是纵向滑动
 		 			if (Math.abs(dy) > mTouchSlop) {// 进行纵向滑动
 						state = STATE_SCROLL_VERTICAL;
 					} else if (Math.abs(dx) > mTouchSlop){
 						state = STATE_SCROLL_HORIZONTAL;
 					} 
					if (state == STATE_SCROLL_HORIZONTAL) {
		 				//System.out.println("ACTION_MOVE:" + (event.getX()-downx));
		  				if((event.getX()-downx)>0){//向右
		  				  h_direct=DIRECT_RIGHT;
						  listItem = pointToPosition(x-leftHiddenLen, (int) event.getY());//根据xy坐标，获取当前用户滑动的第几个item
		  				  if(this.leftHiddenLen<=0)//如果右边没有隐藏元素，则禁止移动
		  					  dx=0;
		  				}else {
		  					h_direct=DIRECT_LEFT;
		  					listItem = pointToPosition(x, (int) event.getY());
		  				    if(this.rightHiddenLen<=0)//如果左边没有隐藏元素，则禁止移动
			  					  dx=0;
		  				}
		 				// position为visibleItem
						snapView = getChildAt(listItem - firstVisibleItem);
						//System.out.println("ACTION_MOVE:" + listItem+","+firstVisibleItem);
						//computeScrollOffset:true说明滚动尚未完成，false说明滚动已经完成
						if (snapView != null && !mScroller.computeScrollOffset()) {
							snapView.scrollBy(-dx, 0);
							// if(android.os.Build.VERSION.SDK_INT > 10)
							// snapView.setAlpha(alpha);
							//System.out.println("onTouchEvent:" + snapView.getScrollX());
						}

						x = (int) event.getX();
					} 
  				}
  			}
 		}else if(event.getAction()==MotionEvent.ACTION_UP){
   			long timedis=(new Date()).getTime()-down_time;
 			int dx = (int) (event.getX() - downx);
			int dy = (int) (event.getY() - downy);
  			if(this.listener!=null){
 				this.listener.onTouchUp(downpositon,downView);
 			}
 			if(timedis<DOWN_MOVE_TIMESTAMP&&Math.abs(dy)<mTouchSlop&&Math.abs(dx)< mTouchSlop){
 				if(this.listener!=null){
 					listItem = pointToPosition(x, (int) event.getY());
 					this.listener.onClick(listItem);
 				}
  			} 
			if(snapView!=null||moveView!=null){
				if(ismove){
					ismove=false;
					stopDragging();
	 				ViewGroup.LayoutParams params = moveView.getLayoutParams();
					params.height = preheight;
					moveView.setLayoutParams(params);
					moveView.setVisibility(View.VISIBLE);
					if (listener != null){
						int[] location = new int[2];
						this.getLocationOnScreen(location);
 						listener.onFinishMove(listItem,moveView, location[0]+event.getX(),location[1]+ event.getY());
					}
 	 			}else{
					// if (mTouchState == TOUCH_STATE_SCROLLING) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					// 计算当前的速度,使用computeCurrentVelocity(int)初始化速率的
					//单位，并获得当前的事件的速率，然后使用getXVelocity() 或getXVelocity()获得横向和竖向的速率
					//units: &nbsp;你使用的速率单位,1000表示 一秒时间内运动了多少个像素
					velocityTracker.computeCurrentVelocity(1000);
					// 获得当前的速度
					int velocityX = (int) velocityTracker.getXVelocity();
 					//System.out.println("velocityX:" + velocityX);
					if (velocityX > 600) {
						// Fling enough to move left
						snapToOut();
					} else if (velocityX < -600) {
						// Fling enough to move right
						snapToOut();
						//snapToBack();
					} else {
						snapToDestination();
					}
	
					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
				}
			}
  		}
		 
		return super.onTouchEvent(event);
	}

	
	 
	/**
	 * ，scrollState有三种状态，分别是开始滚动（SCROLL_STATE_FLING），
	 *  正在滚动(SCROLL_STATE_TOUCH_SCROLL), 已经停止（SCROLL_STATE_IDLE）
	 *  //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1； 
     *   //由于用户的操作，屏幕产生惯性滑动时为2 
 	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	/**
	 *   firstVisibleItem表示当前显示屏幕第一个ListItem(部分显示的ListItem也算)在整个ListView的位置（下标从0开始）     
	 *   visibleItemCount表示当前显示屏幕可以见到的ListItem(部分显示的ListItem也算)总数  
	 *   totalItemCount表示ListView的ListItem总数  
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
 	}
	private void startDragging(Bitmap bm,int x, int y) {
		stopDragging();
 		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP| Gravity.LEFT;
		mWindowParams.x = x;
		mWindowParams.y = y ;
 		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;//可以拖动到屏幕外
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		ImageView v = new ImageView(getContext());
		// int backGroundColor =
		// getContext().getResources().getColor(R.color.dragndrop_background);
		v.setBackgroundColor(0x00000000);
		v.setImageBitmap(bm);
		mDragBitmap = bm;

		mWindowManager = (WindowManager) getContext()
				.getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
	}

	private void stopDragging() {
		if (mDragView != null) {
			WindowManager wm = (WindowManager) getContext().getSystemService(
					"window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}

	public void setLeftHiddenLen(int len){
		this.leftHiddenLen=len;
	}
	public void setRightHiddenLen(int len){
		this.rightHiddenLen=len;
	}
	 
	public void setCanMove(boolean move){
		this.canmove=move;
	}
	private void dragView(int x, int y) {
		float alpha = 1.0f;
		mWindowParams.alpha = alpha;
 		mWindowParams.y = y ;
 		mWindowParams.x=x;
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
		//LogUtil.logInfo(getClass(), "x:"+x+",y:"+y);
 	}
	 
	 public boolean isMoving(){
		 return ismove;
	 }
	 
	 
}
