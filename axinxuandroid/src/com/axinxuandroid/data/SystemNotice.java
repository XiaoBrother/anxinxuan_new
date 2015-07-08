package com.axinxuandroid.data;

public class SystemNotice {

	private int id;
	private int type;//֪ͨ����
	private String notice;//֪ͨ����
	private String jsondata;//json����
	
	public static class SystemNoticeType{
		public static final int NOTICE_TYPE_TEMPLATE=1;//ģ�����
		public static final int NOTICE_TYPE_VERSION=2;//�汾����
		public static final int NOTICE_TYPE_CATEGORY=3;//Ʒ�ָ���
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
