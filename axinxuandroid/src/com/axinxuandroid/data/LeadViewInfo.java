package com.axinxuandroid.data;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

 
public class LeadViewInfo {
 	public int imgresid;
 	public List<View> clickview;
 	public LeadViewInfo(int imgresid){
 		this.imgresid=imgresid;
 		this.clickview=new ArrayList<View>();
 	}
 	
 	public void addClickView(View view){
 		this.clickview.add(view);
 	}
}
