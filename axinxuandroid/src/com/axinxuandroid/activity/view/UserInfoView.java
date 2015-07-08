package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.ProustActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadUserInfoThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
 import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UserInfoView extends ViewGroup{
	private Context context;
 	private TextView setting_grxx_text;
	private TextView setting_grxx_sex_text,setting_age_text,setting_address_text,setting_desc_text;
	private View proustview;
 	private ImageView userimg;
 	private User user;
  	public UserInfoView(Context context) {
		super(context);
		initview(context);
 	}
	
	public UserInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview(context);
 	}
	 
	private void initview(Context scontext){
		this.context=scontext;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.main_tab_user, null);
		addView(view);
   		setting_grxx_text=(TextView) this.findViewById(R.id.userinfo_grxx_text);
   		setting_grxx_sex_text=(TextView) this.findViewById(R.id.userinfo_grxx_sex_text);
		setting_age_text=(TextView) this.findViewById(R.id.userinfo_grxx_age_text);
		setting_address_text=(TextView) this.findViewById(R.id.userinfo_grxx_address_text);
		setting_desc_text=(TextView) this.findViewById(R.id.userinfo_grxx_desc_text);
 		userimg= (ImageView) this.findViewById(R.id.userinfo_userimg);
 		proustview=this.findViewById(R.id.userinfo_grxx_proust);
 		proustview.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
  		 		if(user!=null){
 					Intent inte=new Intent(context,ProustActivity.class);
 					inte.putExtra("user_id", user.getUser_id());
 					context.startActivity(inte);
 				}
			}
		});
   	}
	

	
	public void setUserInfo(User queryuser){
		if(queryuser!=null){
			this.user=queryuser;
 			if(queryuser.getUser_name()==null||queryuser.getUser_name().equals("")||queryuser.getUser_name().equals("null")){
				setting_grxx_text.setText("未填写");
			}else{
				setting_grxx_text.setText(queryuser.getUser_name());
			}
			if(queryuser.getSex()==null||queryuser.getSex().equals("")||queryuser.getSex().equals("null")){
				setting_grxx_sex_text.setText("未填写");
			}else{
				setting_grxx_sex_text.setText(queryuser.getSex());
			}
			if(queryuser.getWorktime()!=null&&!"null".equals(queryuser.getWorktime())){
				long age=DateUtil.getDiffYears(new Date(),DateUtil.StrToDate(queryuser.getWorktime(),"yyyy"));
				setting_age_text.setText(age+"年");
			}
			if(queryuser.getAddress()!=null&&!"null".equals(queryuser.getAddress())){
				setting_address_text.setText(queryuser.getAddress());
			}
			if(queryuser.getPerson_desc()!=null&&!"null".equals(queryuser.getPerson_desc())){
				setting_desc_text.setText(queryuser.getPerson_desc());
			}
			if(queryuser.getLocal_imgurl()!=null)
				userimg.setImageBitmap(BitmapUtils.getImageBitmap(queryuser.getLocal_imgurl()));
  		}
	}
   
	//遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize, heightSize);  
  	 }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
 		for (int i = 0; i < this.getChildCount(); i++) {
			View view = getChildAt(i);
			int measureHeight = view.getMeasuredHeight();  
  			view.layout(0, 0, this.getWidth(),measureHeight);
 		}
	}
	 

}
