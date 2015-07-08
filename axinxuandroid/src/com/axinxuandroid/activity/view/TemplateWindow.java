package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.axinxuandroid.activity.R;
 import com.axinxuandroid.data.Template;

import com.ncpzs.util.LogUtil;
import com.ncpzs.util.SendShareInfo;
    
 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TemplateWindow extends PopupWindow{
  	private Context context;
 	public static final int WINDOW_HEIGHT=100;
 	private TableLayout jsontable;
    public TemplateWindow(Context context) {
		super(context);
		this.context=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.templatewindow, null);
		view.setFocusableInTouchMode(true);
		this.setContentView(view);
		jsontable=(TableLayout) view.findViewById(R.id.templatewindow_tablelayout);
   		// 必须设置宽高
 		this.setWidth(WindowManager.LayoutParams.FILL_PARENT);  
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);  
        //ColorDrawable dw = new ColorDrawable(Color.rgb(246, 243, 228));
        this.setBackgroundDrawable(new ColorDrawable());
   		//this.setFocusable(true); 
		this.setTouchable(true); //设置PopupWindow可触摸  
	    this.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		this.setAnimationStyle(R.style.SharePopupAnimation);
  	} 
    public void setTemplate(Template tlp){
  		try {
 			JSONObject jsonobj = new JSONObject(tlp.getContext());
 			LogUtil.logInfo(getClass(), tlp.getContext());
 			if(jsonobj.has("titles")){
 				JSONArray answers_array = jsonobj.getJSONArray("titles");
  				jsontable.removeAllViews();
 				LogUtil.logInfo(getClass(), answers_array.length()+"");
 				for (int j = 0; j < answers_array.length(); j++) {
 					JSONObject joa = answers_array.getJSONObject(j);
 					String joa_name = joa.getString("name");
   						TableRow tableRow = new TableRow(context);
 						TableRow.LayoutParams Tr = new TableRow.LayoutParams(
 								ViewGroup.LayoutParams.WRAP_CONTENT,
 								ViewGroup.LayoutParams.WRAP_CONTENT);
 						Tr.setMargins(0, 0, 0, 1);
 						TextView textView_name = new TextView(context);
  						textView_name.setText(joa_name);
 						textView_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
 						textView_name.setGravity(Gravity.LEFT);
 						textView_name.setPadding(10, 15, 10, 15);
 						textView_name.setTextColor(Color.rgb(168, 143, 121));
 						textView_name.setBackgroundColor(Color.WHITE);
  						tableRow.addView(textView_name, Tr);
  						tableRow.setBackgroundColor(Color.rgb(222, 220, 210));
 						jsontable.addView(tableRow);
   				}
 			}
 		} catch (Exception ex) {
			ex.printStackTrace();
		}
      }
}
