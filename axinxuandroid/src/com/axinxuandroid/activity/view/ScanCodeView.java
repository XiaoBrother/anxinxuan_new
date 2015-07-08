package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;
 
 import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.EANValidate;
import com.ncpzs.util.LogUtil;
import com.zxing.activity.CaptureActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
 import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
 
public class ScanCodeView extends ViewGroup {
 	private Context context;
 	private View mview;
 	private CommonTopView topview;
 	private   EditText code ;
  	private List<Bitmap> bmps;
  	private View serachlay;
 	public ScanCodeView(Context context) {
		super(context);
		initview(context);
  	}
	public ScanCodeView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}
	private void initview(Context context){
		 this.context=context;
		 bmps=new ArrayList<Bitmap>();
 		LayoutInflater inflater = LayoutInflater.from(context);
 	    mview=inflater.inflate(R.layout.main_scancode, null);
        addView(mview);
        code = (EditText)mview.findViewById(R.id.code);
        code.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        topview  = (CommonTopView) mview.findViewById(R.id.scan_code_topview);
        Button seachCodeButton = (Button) mview.findViewById(R.id.seach_code);
        serachlay=mview.findViewById(R.id.scan_code_searchlay);
        int[] imgwidthheight=BitmapUtils.getImageWidthHeight(R.drawable.scancodeimg);
        if(imgwidthheight!=null){
        	RelativeLayout.LayoutParams pars=(android.widget.RelativeLayout.LayoutParams) serachlay.getLayoutParams();
        	if(Gloable.getInstance().getScreenWeight()<imgwidthheight[0]){
        		int margin=(int) ((Gloable.getInstance().getScreenWeight()-imgwidthheight[0])*1.0/imgwidthheight[0]*imgwidthheight[1]);
             	pars.topMargin=margin;
            	//LogUtil.logInfo(getClass(), "margin:"+margin);
          	}else {
          		pars.topMargin=DensityUtil.dip2px(8);
         	}
        	serachlay.setLayoutParams(pars);
        }
        
        topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				scancodeButtonClick(v);
			}
		});
        
        seachCodeButton.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				seachcodeButtonClick();
			}
		});
        
  	}
	//ɨ��
	private void scancodeButtonClick(View v){
		Intent openCameraIntent = new Intent(context,
				CaptureActivity.class);
		context.startActivity(openCameraIntent);
	}
	//����
    private void seachcodeButtonClick(){
 			String seachcode=code.getText().toString().trim();
 			if(seachcode==null||"".equals(seachcode))
 				seachcode="100000141140315007";
 			if(EANValidate.validateCode(seachcode)){
 				Intent openCameraIntent = new Intent(context,TimelineActivity.class);
 				openCameraIntent.putExtra("id", seachcode);
 	 			context.startActivity(openCameraIntent);
 			}else{
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.excuteMethod(new MessageDialogHandlerMethod("","��ѯ�벻�Ϸ���"));
 			}
  	}
   
	//������view��������ߣ�������onLayout��ʾ����view��measure������������ʾ�ؼ�
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
   	    setMeasuredDimension(widthSize, heightSize);  
 	 }
 	//�����Ǽ̳�ViewGroupʱ���ڳ��˹��캯��֮���ṩ������������ǿ��Կ�����
 	//��ViewGroup��Դ�����з�������������ģ�Ҳ���Ǹ���û���ṩ���������ݣ���Ҫ�����Լ�ʵ�֡�
  	//��ViewҪΪ�����Ӷ�������С��λ��ʱ�����ô˷���
 	//��������ʵ���У���Ҫ����ÿһ���ؼ��Ĳ��ַ���Ϊ�䲼�֡�
 	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  		if (changed) {
  			int childCount = getChildCount();//��Ϊview��ͨ��xmlʵ�������ģ�����ĸ�����1
 	 	    for(int i = 0; i < childCount; i ++){
 	 	         View view = getChildAt(i);
   	             //view.measure(r - l, b - t);  
 	 	        int measuredWidth = view.getMeasuredWidth(); 
 	            view.layout(0,0, measuredWidth, this.getHeight()); 
   	 	    }
  		}
  	}
 	/**
	 * ����ͼƬ���ͷ��ڴ�
	 */
	public void destory() {
		if (this.bmps != null && this.bmps.size() > 0) {
			for (Bitmap bm : bmps) {
				if (bm != null && !bm.isRecycled()) {
					//LogUtil.logInfo(getClass(), "recycle...");
					bm.recycle();
				}
			}
		}
  	}
}
