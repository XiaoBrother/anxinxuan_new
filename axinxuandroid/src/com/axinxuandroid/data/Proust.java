package com.axinxuandroid.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proust {
	private long id;
	private int user_id;
	private int proust_id;
	private String answer;
	private String question;
	private String create_date;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
 	
 	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int userId) {
		user_id = userId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getProust_id() {
		return proust_id;
	}

	public void setProust_id(int proustId) {
		proust_id = proustId;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
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
