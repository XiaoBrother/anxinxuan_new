package com.axinxuandroid.activity.view;

import com.axinxuandroid.activity.AddRecordActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GetMoreView extends ViewGroup {
	private Context context;
	private TextView message;
	private LinearLayout progresslay;
	private GetMoreListener listener;
	public GetMoreView(Context context) {
		super(context);
		initview(context);
 	}
 	public GetMoreView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}
    public void initview(Context context){
    	this.context=context;
  		LayoutInflater inflater = LayoutInflater.from(context);
	    View  view=inflater.inflate(R.layout.loadmore, null);
		addView(view);
		message=(TextView) view.findViewById(R.id.loadmore_messagetext);
		progresslay= (LinearLayout) view.findViewById(R.id.loadmore_progresslay);
		message.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				message.setVisibility(View.GONE);
 				progresslay.setVisibility(View.VISIBLE);
 				if(listener!=null)
 					listener.startGetMore();
			}
		});
     }
  //遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
    	measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize, DensityUtil.dip2px(50));  
  	 }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  		for (int i = 0; i < this.getChildCount(); i++) {
			View view = getChildAt(i);
   			view.layout(0, 0, this.getWidth(),this.getHeight());
 		}
 	}
 
	public void finishGetMore(){
		message.setVisibility(View.VISIBLE);
		progresslay.setVisibility(View.GONE);
	}
	
	public void setGetMoreListener(GetMoreListener listener){
		this.listener=listener;
	}
	
	public interface GetMoreListener {
		public void startGetMore();
	}
 

}
