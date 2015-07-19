package com.axinxuandroid.activity.view;

 

import com.axinxuandroid.activity.AntiFakeActivity;
import com.axinxuandroid.activity.AntifakeBatchListActivity;
import com.axinxuandroid.activity.AntifakePublisBbatchActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.SettingActivity;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DensityUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AntifakeView extends ViewGroup{
	public static final int BOTTOM_ITEM_INDEX_OF_QUERYSET=1;
	public static final int BOTTOM_ITEM_INDEX_OF_PUBLICBATCH=2;
	public static final int BOTTOM_ITEM_INDEX_OF_BATCHLIST=3;
	public static final int BOTTOM_ITEM_INDEX_OF_MORE=4;
    private Context context;
    //private TriangleView triangleview;������
 	private ImageView querySetImg,publishBatchImg,bathListImg,moreImg;
	private TextView querySetText,publishBatchText,bathListText,moreText;
 	private int currentindex=-1;
 	private LinearLayout  parent=null;
	public AntifakeView(Context context) {
		super(context);
		initview(context);
 	}
	public AntifakeView(Context context,AttributeSet sets) {
		super(context,sets);
   		initview(context);
  	}
	
	public void setCurrentItem(int index){
		 currentindex=index;
		 int textcolor=Color.rgb(133, 185, 28);
 		 if(index==BOTTOM_ITEM_INDEX_OF_QUERYSET){
 			    parent=(LinearLayout) querySetImg.getParent();
 			   querySetImg.setImageDrawable(getResources().getDrawable(R.drawable.user_collect_click));
 			  querySetText.setTextColor(textcolor);  
			    
 			 } else if(index==BOTTOM_ITEM_INDEX_OF_PUBLICBATCH){
		    	parent=(LinearLayout) publishBatchImg.getParent();
		    	publishBatchImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed_no));
		    	publishBatchText.setTextColor(textcolor);
		    }else if(index==BOTTOM_ITEM_INDEX_OF_BATCHLIST){
		    	parent=(LinearLayout) bathListImg.getParent();
		    	bathListImg.setImageDrawable(getResources().getDrawable(R.drawable.quik_record_click));
		    	bathListText.setTextColor(textcolor);
  			 }
		 if(parent!=null){
			// parent.setBackgroundResource(R.drawable.appfootbg_hover);
 		 }
	    	
	}
	/**
	 *  
	 * @param context
	 */
	private void initview(Context context){
		this.context=context;
 		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.antifake_bottom, null);
        addView(view);
        querySetImg = (ImageView) findViewById(R.id.antifake_queryset);
        publishBatchImg = (ImageView) findViewById(R.id.antifake_publishbatch);
        bathListImg = (ImageView) findViewById(R.id.antifake_batchlist);
        moreImg = (ImageView) findViewById(R.id.antifake_img_more);
        querySetText = (TextView) findViewById(R.id.antifake_queryset_value);
        publishBatchText = (TextView) findViewById(R.id.antifake_publishbatch_value);
        bathListText = (TextView) findViewById(R.id.antifake_batchlist_value);
        moreText = (TextView) findViewById(R.id.antifake_text_more);
       // triangleview=(TriangleView) findViewById(R.id.commonbtm_triangleview);
        ((LinearLayout) querySetImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_QUERYSET);
 			}
		});
         ((LinearLayout) publishBatchImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_PUBLICBATCH);
 			}
		});
         ((LinearLayout)  bathListImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_BATCHLIST);
 			}
		});
         ((LinearLayout)  moreImg.getParent()).setOnClickListener(new OnClickListener() {
        	 @Override
        	 public void onClick(View v) {
        		 onClickItem(BOTTOM_ITEM_INDEX_OF_MORE);
        	 }
         });
   	}
	//������view��������ߣ�������onLayout��ʾ����view��measure������������ʾ�ؼ�
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
        measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize,DensityUtil.dip2px(65));//����õ�����������Ϊ��λ����ֵ
 	 }
 	//�����Ǽ̳�ViewGroupʱ���ڳ��˹��캯��֮���ṩ������������ǿ��Կ�����
 	//��ViewGroup��Դ�����з�������������ģ�Ҳ���Ǹ���û���ṩ���������ݣ���Ҫ�����Լ�ʵ�֡�
  	//��ViewҪΪ�����Ӷ�������С��λ��ʱ�����ô˷���
 	//��������ʵ���У���Ҫ����ÿһ���ؼ��Ĳ��ַ���Ϊ�䲼�֡�
 	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  		//if (changed) {
  			int childCount = getChildCount();//һ��view��ͨ��xmlʵ�������ģ� 
 	 	    for(int i = 0; i < childCount; i ++){
 	 	         View view = getChildAt(i);
   	             //view.measure(r - l, b - t);  
 	 	        int measureHeight = view.getMeasuredHeight();  
 	 	        int measuredWidth = view.getMeasuredWidth(); 
 	            view.layout(0,0, Gloable.getInstance().getScreenWeight(), measureHeight); 
  	            
  	 	    }
  		//}
// 	 	 if(parent!=null){
// 	 		 int[] xy=new int[2];
//			 parent.getLocationInWindow(xy);
//			 int width=parent.getWidth();
//			 triangleview.setPoints(new Point(xy[0]+width/2,0), new Point(xy[0],DensityUtil.dip2px(6)), new Point(xy[0]+width,DensityUtil.dip2px(6)));
//  	 	 }
 	 	 
   	}
 	 
	private void onClickItem(int index) {
		if(index==currentindex) return ;
		 
		if (index == BOTTOM_ITEM_INDEX_OF_QUERYSET) {
			toqueryset();
		} else if (index == BOTTOM_ITEM_INDEX_OF_PUBLICBATCH) {
			topublicbatch();
		} else if (index == BOTTOM_ITEM_INDEX_OF_BATCHLIST) {
			tobatchlist();
		} else if (index == BOTTOM_ITEM_INDEX_OF_MORE) {
			tomore();
		} 
	 
	}
 	/**
 	 * ��������
 	 */
 	private void toqueryset(){
 		Intent inte = new Intent(context,AntiFakeActivity.class);
  		context.startActivity(inte);
 	}
	/**
	 * ��α����
	 */
 	private void topublicbatch(){
 		Intent inte = new Intent(context,AntifakePublisBbatchActivity.class);
  		context.startActivity(inte);
 	}
 	/**
 	 * �鿴��¼
 	 */
 	private void tobatchlist(){
 		Intent inte = new Intent(context,AntifakeBatchListActivity.class);
  		context.startActivity(inte);
 	}
 	/**
 	 * ����
 	 */
 	private void tomore(){
 		Intent inte = new Intent(context,SettingActivity.class);
  		context.startActivity(inte);
 	}
 
 	 
}
