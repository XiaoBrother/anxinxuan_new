package com.axinxuandroid.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;


public class PhotoAibum implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;   //相册名称
	private String count; //照片数量
	private int  bitmap;  //第一张图片
	private String dir_id;//目录id
	private String path;//目录路径
	private Set<String> selectPaths = new HashSet<String>();
	
	public PhotoAibum() {
	}
	
	
	public PhotoAibum(String name, String count, int bitmap,Bitmap fistbitmap) {
		super();
		this.name = name;
		this.count = count;
		this.bitmap = bitmap;
	}


	 


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public int getBitmap() {
		return bitmap;
	}
	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}
	
  


	public String getDir_id() {
		return dir_id;
	}


	public void setDir_id(String dirId) {
		dir_id = dirId;
	}


	 


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Set<String> getSelectPaths() {
		return selectPaths;
	}


	public void setSelectPaths(Set<String> selectPaths) {
		this.selectPaths = selectPaths;
	}


	@Override
	public String toString() {
		return "PhotoAibum [name=" + name + ", count=" + count + ", bitmap="
				+ bitmap + ", bitList=" + selectPaths + "]";
	}
}
