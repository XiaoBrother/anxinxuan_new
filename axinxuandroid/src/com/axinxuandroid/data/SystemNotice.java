package com.axinxuandroid.data;

public class SystemNotice {

	private int id;
	private int type;//通知类型
	private String notice;//通知内容
	private String jsondata;//json数据
	
	public static class SystemNoticeType{
		public static final int NOTICE_TYPE_TEMPLATE=1;//模板更新
		public static final int NOTICE_TYPE_VERSION=2;//版本更新
		public static final int NOTICE_TYPE_CATEGORY=3;//品种更新
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	
	
}
