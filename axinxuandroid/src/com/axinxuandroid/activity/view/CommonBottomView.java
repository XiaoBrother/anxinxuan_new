package com.axinxuandroid.activity.view;

 

import java.util.List;

import com.axinxuandroid.activity.AddRecordActivity;
import com.axinxuandroid.activity.BatchManageActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.ScanCodeActivity;
import com.axinxuandroid.activity.SettingActivity;
import com.axinxuandroid.activity.UserPageActivity;
import com.axinxuandroid.activity.UserFovoriteActivity;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
  
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommonBottomView extends ViewGroup{
	public static final int BOTTOM_ITEM_INDEX_OF_USERCOLLECT=1;
	public static final int BOTTOM_ITEM_INDEX_OF_BATCHMANAGE=2;
	public static final int BOTTOM_ITEM_INDEX_OF_EDITRECORD=3;
	public static final int BOTTOM_ITEM_INDEX_OF_SCANCODE=4;
	public static final int BOTTOM_ITEM_INDEX_OF_MORE=5;
    private Context context;
    //private TriangleView triangleview;������
 	private ImageView batchImg,recordImg,scancodeImg,moreImg,collectImg;
	private TextView batchText,recordText,scancodeText,moreText,collectText;
 	private int currentindex=-1;
 	private LinearLayout  parent=null;
	public CommonBottomView(Context context) {
		super(context);
		initview(context);
 	}
	public CommonBottomView(Context context,AttributeSet sets) {
		super(context,sets);
   		initview(context);
  	}
	
	public void setCurrentItem(int index){
		 currentindex=index;
		 int textcolor=Color.rgb(133, 185, 28);
 		 if(index==BOTTOM_ITEM_INDEX_OF_USERCOLLECT){
 			    parent=(LinearLayout) collectImg.getParent();
			    collectImg.setImageDrawable(getResources().getDrawable(R.drawable.user_collect_click));
			    collectText.setTextColor(textcolor);  
			    
 			 } else if(index==BOTTOM_ITEM_INDEX_OF_BATCHMANAGE){
		    	parent=(LinearLayout) batchImg.getParent();
 	 	    	batchImg.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed_no));
				batchText.setTextColor(textcolor);
		    }else if(index==BOTTOM_ITEM_INDEX_OF_EDITRECORD){
		    	parent=(LinearLayout) recordImg.getParent();
		    	recordImg.setImageDrawable(getResources().getDrawable(R.drawable.quik_record_click));
		    	recordText.setTextColor(textcolor);
  			 }else if(index==BOTTOM_ITEM_INDEX_OF_SCANCODE){
 			    parent=(LinearLayout) scancodeImg.getParent();
	  	    	scancodeImg.setImageDrawable(getResources().getDrawable(R.drawable.bar_serach_click));
		    	scancodeText.setTextColor(textcolor);
		    }else if(index==BOTTOM_ITEM_INDEX_OF_MORE){
 		    	parent=(LinearLayout) moreImg.getParent();
		    	moreImg.setImageDrawable(getResources().getDrawable(R.drawable.mycion_click));
		    	moreText.setTextColor(textcolor);
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
		View  view=inflater.inflate(R.layout.common_bottom, null);
        addView(view);
        batchImg = (ImageView) findViewById(R.id.commonbtm_img_pici);
        recordImg = (ImageView) findViewById(R.id.commonbtm_img_jilu);
        scancodeImg = (ImageView) findViewById(R.id.commonbtm_img_saoma);
        moreImg = (ImageView) findViewById(R.id.commonbtm_img_more);
        collectImg=(ImageView) findViewById(R.id.commonbtm_img_collect);
        batchText = (TextView) findViewById(R.id.commonbtm_text_pici);
        recordText = (TextView) findViewById(R.id.commonbtm_text_jilu);
        scancodeText = (TextView) findViewById(R.id.commonbtm_text_saoma);
        moreText = (TextView) findViewById(R.id.commonbtm_text_more);
        collectText = (TextView) findViewById(R.id.commonbtm_text_collect);
       // triangleview=(TriangleView) findViewById(R.id.commonbtm_triangleview);
        ((LinearLayout) collectImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_USERCOLLECT);
 			}
		});
         ((LinearLayout) batchImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_BATCHMANAGE);
 			}
		});
         ((LinearLayout)  recordImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_EDITRECORD);
 			}
		});
         ((LinearLayout) scancodeImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_SCANCODE);
 			}
		});
         ((LinearLayout) moreImg.getParent()).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				onClickItem(BOTTOM_ITEM_INDEX_OF_MORE);
 			}
		});
        UserService userservice=new UserService();
        User user=userservice.getLastLoginUser();
        UserVilleageService uvilservice=new UserVilleageService();
        if(user!=null){
        	List<Villeage> vils= uvilservice.getVilleageByUser(user.getUser_id());
        	if(vils!=null&&vils.size()>0)
        		((LinearLayout) batchImg.getParent().getParent()).setVisibility(View.VISIBLE);
        	else ((LinearLayout) collectImg.getParent().getParent()).setVisibility(View.VISIBLE);
         }
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
		 
		if (index == BOTTOM_ITEM_INDEX_OF_USERCOLLECT) {
			tousercollect();
		} else if (index == BOTTOM_ITEM_INDEX_OF_BATCHMANAGE) {
			tobatchmanage();
		} else if (index == BOTTOM_ITEM_INDEX_OF_EDITRECORD) {
			toeditrecord();
		} else if (index == BOTTOM_ITEM_INDEX_OF_SCANCODE) {
			toscancode();
		} else if (index == BOTTOM_ITEM_INDEX_OF_MORE) {
			tomore();
		}
	 
	}
 	/**
 	 * �û��ղ�
 	 */
 	private void tousercollect(){
 		Intent inte = new Intent(context,UserFovoriteActivity.class);
  		context.startActivity(inte);
 	}
	/**
	 * ���ι���
	 */
 	private void tobatchmanage(){
 		Intent inte = new Intent(context,BatchManageActivity.class);
  		context.startActivity(inte);
 	}
 	/**
 	 * �༭��¼
 	 */
 	private void toeditrecord(){
 		Intent inte = new Intent(context,AddRecordActivity.class);
  		context.startActivity(inte);
 	}
 	/**
 	 * ����
 	 */
 	private void tomore(){
 		Intent inte = new Intent(context,SettingActivity.class);
  		context.startActivity(inte);
 	}
 	/**
 	 * ɨ��
 	 */
 	private void toscancode(){
 		Intent inte = new Intent(context,ScanCodeActivity.class);
  		context.startActivity(inte);
 	}
 	
 
 	 
}
