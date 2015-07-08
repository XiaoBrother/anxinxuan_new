package com.axinxuandroid.data;

public class UserVisitHistory {
	private int id;
	private int userid;
	private int type;
	private int visitobjid;
	private String visittime;

	public static class  UserVisitType{
		public static final int VISIT_TYPE_BATCH=1;
		public static final int VISIT_TYPE_VILLEAGE=2;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getVisitobjid() {
		return visitobjid;
	}

	public void setVisitobjid(int visitobjid) {
		this.visitobjid = visitobjid;
	}

	public String getVisittime() {
		return visittime;
	}

	public void setVisittime(String visittime) {
		this.visittime = visittime;
	}

}
