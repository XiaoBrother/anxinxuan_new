package com.axinxuandroid.activity.view;

 

import com.axinxuandroid.activity.R;
import com.ncpzs.util.LogUtil;
  
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommonTopView extends ViewGroup{
    private TextView title,subtitle,lefttext,righttext;
    private ImageButton leftimg;
    private ImageButton rightimg;
    private String titlestr;
    private String subtitlestr;
    private String lefttextstr,righttextstr;
    private Drawable leftbg;
    private Drawable rightbg;
    private View mainLay;
    private View leftlay,rightlay;
	public CommonTopView(Context context) {
		super(context);
		initview(context);
 	}
	public CommonTopView(Context context,AttributeSet sets) {
		super(context,sets);
		 TypedArray tArray = context.obtainStyledAttributes(sets,
	                R.styleable.commontop);
		 titlestr = tArray.getString(R.styleable.commontop_title);
		 subtitlestr = tArray.getString(R.styleable.commontop_subtitle);
		 leftbg=tArray.getDrawable(R.styleable.commontop_leftimgsrc);
		 rightbg=tArray.getDrawable(R.styleable.commontop_rightimgsrc);
		 lefttextstr= tArray.getString(R.styleable.commontop_lefttext);
		 righttextstr= tArray.getString(R.styleable.commontop_righttext);
 		 tArray.recycle();//�ͷ�
  		 initview(context);
  	}
	/**
	 * ������ʾͼƬ
	 * @param context
	 */
	private void initview(Context context){
 		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.common_top, null);
        addView(view);
        title=(TextView) view.findViewById(R.id.commontop_title);
        subtitle=(TextView) view.findViewById(R.id.commontop_subtitle);
        leftimg=(ImageButton) view.findViewById(R.id.commontop_left);
        rightimg=(ImageButton) view.findViewById(R.id.commontop_right);
        leftlay=(LinearLayout) view.findViewById(R.id.commontop_leftlay);
        rightlay=(LinearLayout) view.findViewById(R.id.commontop_rightlay);
        lefttext=(TextView) view.findViewById(R.id.commontop_lefttext);
        righttext=(TextView) view.findViewById(R.id.commontop_righttext);
        if(titlestr!=null)
        	title.setText(titlestr);
        if(leftbg!=null){
        	leftlay.setVisibility(View.VISIBLE);
        	leftimg.setImageDrawable(leftbg);
        }else if(lefttextstr!=null){
        	leftlay.setVisibility(View.VISIBLE);
        	leftimg.setVisibility(View.GONE);
        	lefttext.setText(lefttextstr);
        	((LinearLayout)lefttext.getParent()).setVisibility(View.VISIBLE);
        }
         if(rightbg!=null){
        	 rightlay.setVisibility(View.VISIBLE);
        	 rightimg.setImageDrawable(rightbg);
         }else if(righttextstr!=null){
        	 rightlay.setVisibility(View.VISIBLE);
        	 rightimg.setVisibility(View.GONE);
        	 righttext.setText(righttextstr);
        	 ((LinearLayout)righttext.getParent()).setVisibility(View.VISIBLE);
        }
         if(this.subtitlestr!=null){
        	setSubTitle(subtitlestr);
         }
        mainLay=view.findViewById(R.id.commontop_mainlay);
   	}
	//������view���������ߣ�������onLayout��ʾ����view��measure������������ʾ�ؼ�
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
  		//LogUtil.logInfo(getClass(), "heightSize:"+heightSize);
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize,mainLay.getMeasuredHeight());//����õ�����������Ϊ��λ����ֵ
 	 }
 	//�����Ǽ̳�ViewGroupʱ���ڳ��˹��캯��֮���ṩ������������ǿ��Կ�����
 	//��ViewGroup��Դ�����з�������������ģ�Ҳ���Ǹ���û���ṩ���������ݣ���Ҫ�����Լ�ʵ�֡�
  	//��ViewҪΪ�����Ӷ�������С��λ��ʱ�����ô˷���
 	//��������ʵ���У���Ҫ����ÿһ���ؼ��Ĳ��ַ���Ϊ�䲼�֡�
 	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  		//if (changed) {
  			int childCount = getChildCount();//һ��view��ͨ��xmlʵ�������ģ���һ�����Լ����ӵ�BatchView������ĸ�����2
 	 	    for(int i = 0; i < childCount; i ++){
 	 	         View view = getChildAt(i);
   	             //view.measure(r - l, b - t);  
 	 	        int measureHeight = view.getMeasuredHeight();  
 	 	        int measuredWidth = view.getMeasuredWidth(); 
 	            view.layout(0,0, measuredWidth, measureHeight); 
  	            
  	 	    }
  		//}
  	}
 	public void setRightImage(int recid){
 		rightlay.setVisibility(View.VISIBLE);
   		rightimg.setImageResource(recid);
    	((LinearLayout)righttext.getParent()).setVisibility(View.GONE);
 	}
 	public void setRightText(String text){
 		rightlay.setVisibility(View.VISIBLE);
 		righttext.setText(text);
 		rightimg.setVisibility(View.GONE);
 	}
 	public void setLeftImage(int recid){
 		leftlay.setVisibility(View.VISIBLE);
   		leftimg.setImageResource(recid);
    	((LinearLayout)lefttext.getParent()).setVisibility(View.GONE);
 	}
 	public void setLeftClickListener(OnClickListener listener){
 		if(leftimg!=null){
 			leftimg.setOnClickListener( listener);
 			((LinearLayout)lefttext.getParent()).setOnClickListener(listener);
 		}
 		  
 	}
 	public void setRightClickListener(OnClickListener listener){
 		if(rightimg!=null){
 			 rightimg.setOnClickListener(listener);
 			((LinearLayout)righttext.getParent()).setOnClickListener(listener);
 		}
 			
 	}
 	public void setTitleClickListener(OnClickListener listener){
 		if(title!=null)
 			title.setOnClickListener(listener);
 	}
 	public void setSubTitleClickListener(OnClickListener listener){
 		if(subtitle!=null)
 			subtitle.setOnClickListener(listener);
 	}
 	public void setTitle(String titlestr,int fontsize){
  		if(title!=null){
  			title.setTextSize(fontsize);
  			title.setText(titlestr);
  		}
  	}
 	public void setTitle(String titlestr){
  		if(title!=null){
   			title.setText(titlestr);
  		}
  	}
 	public void setSubTitle(String titlestr){
  		if(subtitle!=null){
  			subtitle.setText(titlestr);
  			subtitle.setVisibility(View.VISIBLE);
  			if(titlestr!=null&&!"".equals(titlestr)&&title!=null){
  				LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) title.getLayoutParams();
  				params.topMargin=0;
  				title.setLayoutParams(params);
  			}
  		}
  	}
 	public void hiddenSubTitle(){
  		if(subtitle!=null){
  			subtitle.setVisibility(View.GONE); 
  		}
  	}
 	public void hiddenLeftRight(){
  		if(rightlay!=null){
  			rightlay.setVisibility(View.GONE);
  		}
  		if(leftlay!=null)
  			leftlay.setVisibility(View.GONE);
  	}
	public ImageButton getLeftimg() {
		return leftimg;
	}
	 
	public ImageButton getRightimg() {
		return rightimg;
	}
	 
 	
}