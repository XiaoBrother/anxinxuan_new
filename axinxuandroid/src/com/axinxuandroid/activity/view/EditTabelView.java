package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

 import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditTabelView extends ViewGroup{
    private List<TableItem> items;
    private TableLayout tlay;
    private Context context;
    private boolean isonlyRead=false;
    private Integer textColor;
    private boolean showempty=true;
    private static final int FIRST_LABEL_WIDTH=100;
 	public EditTabelView(Context context) {
		super(context);
		initview(context);
 	}
	
	public EditTabelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview(context);
 	}
	/**
	 * 设置为只读
	 */
	public void setReadOnly(){
		isonlyRead=true;
	}
	
	public void setTextColor(int textColor){
		this.textColor=textColor;
	}
	/**
	 * 设置当答案为空时是否显示
	 */
	public void setShowEmpty(boolean show){
		this.showempty=show;
	}
	private void initview(Context context){
		this.context=context;
		items=new ArrayList<TableItem>();
	    tlay=new TableLayout(context);
 	    tlay.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    //tlay.setBackgroundResource(R.drawable.shape_corners);
 	   // tlay.setBackgroundColor(Color.RED);
 	    tlay.setColumnStretchable(1, true);
	    tlay.setColumnShrinkable(1, true);
 		this.addView(tlay);
		
	}
	
	public void addItem(int id,String question){
		addItem(id,question,null);
	}
	public void addItem(int id,String question,String value){
		if(findById(id)==null){
			if(showempty||(value!=null&&!"".equals(value.trim()))){
				TableItem item=new TableItem(id,question);
				item.answer=value;
				items.add(item);
	 		    showEditItem(item);
			}
 		}
	}
	private void showEditItem(TableItem item){
		if(tlay!=null){
			TableRow tableRow = new TableRow(context);
 			TableRow.LayoutParams namely=new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
 			namely.setMargins(1, 1, 1, 1);	
 			namely.width=DensityUtil.dip2px(FIRST_LABEL_WIDTH);
 			TableRow.LayoutParams ansly=new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
 			ansly.setMargins(1, 1, 1, 1);	
     		TextView textView_name = new TextView(context);
			textView_name.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
 			textView_name.setPadding(5, 0, 5, 0);
			textView_name.setBackgroundColor(Color.WHITE);
	 		textView_name.setText(item.question);
	 		if(this.textColor!=null)  textView_name.setTextColor(this.textColor);
 	 		else textView_name.setTextColor(Color.BLACK);
	 		
      	 	tableRow.addView(textView_name,namely);
   			if(isonlyRead){
	 			TextView text=createTextView(item);
	 			text.setHeight(DensityUtil.dip2px(40));
	 			text.setPadding(10, 0, 0, 0);
	 			item.text=text;
	 			text.setGravity(Gravity.CENTER_VERTICAL);
	 			tableRow.addView(text,ansly);
 	 		}else{
	 			EditText input=createEditText( item);
	 			item.input=input;
	 			tableRow.addView(input,ansly);
	 		}
  			tableRow.setBackgroundColor(Color.rgb(232, 232, 233)); 
 			tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,150));
 			tlay.addView(tableRow); 
   			
 		 
		}
 	}
	private EditText createEditText(TableItem item){
	    EditText input=new EditText(context);
	 	input.setBackgroundColor(Color.WHITE);
	 	if(this.textColor!=null) input.setTextColor(this.textColor);
 		//hsv.setBackgroundColor(Color.WHITE);
// 		hsv.setOnClickListener(new OnClickListener() {
//				@Override
//			public void onClick(View v) {
//					input.requestFocus();
//			}
//		});
 		if(item.answer==null)
 		  input.setHint("");
 		else input.setText(item.answer);
 	    input.setHintTextColor(Color.rgb(160, 160, 160));
 	    return input;
	}
	private TextView createTextView(TableItem item){
		TextView text=new TextView(context);
		text.setBackgroundColor(Color.WHITE);
	 	if(this.textColor!=null) text.setTextColor(this.textColor);
	 	else text.setTextColor(Color.BLACK);
 	 	text.setText(item.answer);
  	    return text;
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
	 
	/**
	 * 用户是否输入过内容
	 * @return
	 */
	public String getAnswerString(){
		if(this.items==null||this.items.size()<1)
			return "";
		 String text="";
		 for(TableItem item:items){
			  text+=item.input.getText().toString();
 		 }
		 return text;
	}
	public void clearAnswer(){
		if(this.items==null||this.items.size()<1) return ;
 		 for(TableItem item:items){
 			item.input.setText("");
 		 }
 	}
	
	public void clear(){
	   tlay.removeAllViews();
	   if(this.items!=null)	this.items.clear();
 	}
	//遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	   // int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize, tlay.getMeasuredHeight());  
  	 }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
 		for (int i = 0; i < this.getChildCount(); i++) {
			View view = getChildAt(i);
			int measureHeight = view.getMeasuredHeight();  
  			view.layout(0, 0, this.getWidth(),measureHeight);
 		}
	}
	/**
	 * 获取json 
	 */
	public String getItemJson(){
		String str="{answers:[";
		int  size=this.items==null?0:this.items.size();
		 if(size>0)
			 for(int i=0;i<size;i++){
				if(i==size-1){
						str+="{'name':'"+items.get(i).question+"','value':'"+items.get(i).input.getText().toString()+"'}";
				}else str+="{'name':'"+items.get(i).question+"','value':'"+items.get(i).input.getText().toString()+"'},";
			 }
		str+="]}";
		return str;
 	}
	public class TableItem{
		public int id;
		public String question;
		public String answer;
 		public EditText input;
 		public TextView text;
		public TableItem(int id,String question){
			this.id=id;
			this.question=question;
		}
	}

}
