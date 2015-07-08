package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.UserPageActivity;
import com.axinxuandroid.activity.VilleageInfoActivity;
 
 import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserVilleageView extends ViewGroup{
    private List<TableItem> items;
    private LinearLayout llay;
    private Context context;
    private View view;
	public UserVilleageView(Context context) {
		super(context);
		initview(context);
 	}
	
	public UserVilleageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview(context);
 	}
	
	private void initview(Context context){
		this.context=context;
		items=new ArrayList<TableItem>();
	    llay=new LinearLayout(context);
	    llay.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    //LayoutInflater inflater = LayoutInflater.from(context);
 	   // llay.setBackgroundColor(Color.RED);
	    llay.setBackgroundResource(R.drawable.shape_corners);
 		this.addView(llay);
	}
	
	public void addItem(int id,String villeage_name,String collect_code,String manager_name,String tele,String address){
 		if(findById(id)==null){
 			TableItem item=new TableItem(id,villeage_name,collect_code, manager_name, tele, address);
			items.add(item);
			showItem(item);
		}
	}
	private void showItem(TableItem item){
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.main_tab_user_item, null);
		TextView textView_name =(TextView) view.findViewById(R.id.setting_grxx_name);
		RelativeLayout setting_grxx =(RelativeLayout) view.findViewById(R.id.setting_grxx);
 		textView_name.setText(item.villeage_name);
		textView_name.setGravity(Gravity.RIGHT);
		setting_grxx.setTag(item);
		llay.addView(view);
		setting_grxx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(context,VilleageInfoActivity.class);
				TableItem useritem=(TableItem) v.getTag();
				openCameraIntent.putExtra("villeage_id",useritem.id);
  	 			context.startActivity(openCameraIntent);
			}
		});
 	}

	private TableItem findById(int id){
		if(this.items==null||this.items.size()<1) return null;
		for(TableItem item:items)
			if(item.id==id)
				return item;
		return null;
	}
	public boolean hasData(){
		if(this.items!=null&&this.items.size()>0)
			return true;
		return false;
	}
	//遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	   // int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize, llay.getMeasuredHeight());  
  	 }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
 		for (int i = 0; i < this.getChildCount(); i++) {
			View view = getChildAt(i);
			int measureHeight = view.getMeasuredHeight();  
  			view.layout(0, 0, this.getWidth(),measureHeight);
 		}
	}
	public class TableItem{
		public int id;
		public String villeage_name;
		public String collect_code;
		public String manager_name;
		public String tele;
		public String address;
 		public TableItem(int id,String villeage_name,String collect_code,String manager_name,String tele,String address){
			this.id=id;
			this.villeage_name=villeage_name;
			this.collect_code=collect_code;
			this.manager_name=manager_name;
			this.tele=tele;
			this.address=address;
 		}
	}

}
