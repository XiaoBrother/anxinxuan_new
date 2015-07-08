package com.axinxuandroid.data;

import java.util.List;

public class UserVilleage {
	private long id;
 	private int villeage_id;
	private int user_id;
	private int role;
	private Villeage villeage;
 
	public static class UserVilleageRole{  
		public static final int VILLEAGE_ROLE_MASTER=1;//农场主
		public static final int VILLEAGE_ROLE_STAFF=2;//农场雇员
		public static final int VILLEAGE_ROLE_EXPERT=3;//农业专家
		public static final int VILLEAGE_ROLE_TECHNOLOGIST=4;//农业技术员
		public static final int VILLEAGE_ROLE_MANAGER=5;//投入品管理员
		public static final int VILLEAGE_ROLE_PARTNER=6;//合伙人
		public static final int VILLEAGE_ROLE_FAMILY=7;//家庭成员
		public static final int VILLEAGE_ROLE_QUALITY=8;//质量监督专员
		public static final int VILLEAGE_ROLE_QCONTROLL=9;//品控专员
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVilleage_id() {
		return villeage_id;
	}
	public void setVilleage_id(int villeageId) {
		villeage_id = villeageId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public Villeage getVilleage() {
		return villeage;
	}
	public void setVilleage(Villeage villeage) {
		this.villeage = villeage;
	}
 
 

}
