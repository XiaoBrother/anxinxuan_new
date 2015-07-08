package com.axinxuandroid.activity.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class TriangleView  extends View{

	private Point toppoint;
	private Point leftpoint;
	private Point rightpoint;
	public TriangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
 	}

	@Override
	protected void onDraw(Canvas canvas) {
 		super.onDraw(canvas);
 		 Paint paint=new Paint();  
         /*去锯齿*/  
         paint.setAntiAlias(true);  
         /*设置paint的颜色*/  
         paint.setColor(Color.rgb(38, 34, 35));  
         /*设置paint的　style　为FILL：实心*/  
         paint.setStyle(Paint.Style.FILL);  
         /*设置paint的外框宽度*/  
         paint.setStrokeWidth(1);  
 		if(toppoint!=null&&toppoint!=null&&rightpoint!=null){
 			Path path=new Path();  
 			path.moveTo(toppoint.x,toppoint.y);  
 			path.lineTo(leftpoint.x,leftpoint.y);  
 			path.lineTo(rightpoint.x,rightpoint.y);  
 			path.close();  
 	        canvas.drawPath(path, paint);  
 	          
 	        Shader mShader=new LinearGradient(0,0,100,100,  
 	        new int[]{Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW},  
 	        null,Shader.TileMode.REPEAT);  
 	        paint.setShader(mShader);  
 		}
	}

	public void setPoints(Point top,Point left,Point right){
		this.toppoint=top;
		this.leftpoint=left;
		this.rightpoint=right;
		invalidate();
	}
	 
}
