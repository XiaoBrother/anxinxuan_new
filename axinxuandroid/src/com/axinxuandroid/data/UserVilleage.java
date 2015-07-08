package com.axinxuandroid.data;

import java.util.List;

public class UserVilleage {
	private long id;
 	private int villeage_id;
	private int user_id;
	private int role;
	private Villeage villeage;
 
	public static class UserVilleageRole{  
		public static final int VILLEAGE_ROLE_MASTER=1;//ũ����
		public static final int VILLEAGE_ROLE_STAFF=2;//ũ����Ա
		public static final int VILLEAGE_ROLE_EXPERT=3;//ũҵר��
		public static final int VILLEAGE_ROLE_TECHNOLOGIST=4;//ũҵ����Ա
		public static final int VILLEAGE_ROLE_MANAGER=5;//Ͷ��Ʒ����Ա
		public static final int VILLEAGE_ROLE_PARTNER=6;//�ϻ���
		public static final int VILLEAGE_ROLE_FAMILY=7;//��ͥ��Ա
		public static final int VILLEAGE_ROLE_QUALITY=8;//�����ලרԱ
		public static final int VILLEAGE_ROLE_QCONTROLL=9;//Ʒ��רԱ
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
