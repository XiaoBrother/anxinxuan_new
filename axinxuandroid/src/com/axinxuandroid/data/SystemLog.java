package com.axinxuandroid.data;

import java.util.HashMap;
import java.util.Map;

public class SystemLog {
	private int id;
 	private String  message;//操作
 	private String actiontime;//操作时间
  	private String extrainfo;//附加信息
  	 
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getActiontime() {
		return actiontime;
	}
	public void setActiontime(String actiontime) {
		this.actiontime = actiontime;
	}
	 
	 
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	 
	
}
