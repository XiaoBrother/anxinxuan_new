package com.axinxuandroid.data;

import java.util.HashMap;
import java.util.Map;

public class SystemLog {
	private int id;
 	private String  message;//����
 	private String actiontime;//����ʱ��
  	private String extrainfo;//������Ϣ
  	 
	
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
