package com.axinxuandroid.data;

import java.util.List;

public class OAuthAccount {
	private int id;
 	private int userid;//�û�id
	private String account_id;//�˻�id
	private int type	;//	���ͣ�qq ������ �� ֧������
	private String authorizeimg	;//	�˺�ͼƬ
	private String nick_name;
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
	 
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String accountId) {
		account_id = accountId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAuthorizeimg() {
		return authorizeimg;
	}
	public void setAuthorizeimg(String authorizeimg) {
		this.authorizeimg = authorizeimg;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nickName) {
		nick_name = nickName;
	}
    
	 
	 
	
}
