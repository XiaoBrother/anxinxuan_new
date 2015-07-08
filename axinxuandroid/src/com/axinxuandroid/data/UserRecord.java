package com.axinxuandroid.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;



public class UserRecord  {
	private int id;
	private int user_id;
	private int record_id;// 
	private int isdel;// 
	private String lastoptime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int recordId) {
		record_id = recordId;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public String getLastoptime() {
		return lastoptime;
	}
	public void setLastoptime(String lastoptime) {
		this.lastoptime = lastoptime;
	}
 
	  
	 
}
