package com.axinxuandroid.data;

import java.util.List;

public class User {
	
	public static final String SEX_MAN="1";
	public static final String SEX_WOMAN="0";
	
	private long id;
	private int user_id;
 	private String phone;
	private String pwd;
	private String sex;
	private String user_name;
	private String worktime;
	private String address;
	private String email;
	private String create_time;
	private String login_time;
	private String person_desc;
	private String person_imageurl;
	private String local_imgurl;
    
	private int logintype=0;//登录方式,默认用户名密码方式
	private String accesstoken;//oauth访问令牌
	private String oauthlogintime;//
	private long expirein;//过期时间
	
  	private List<UserVilleage> uservilleages;
 	
 	private List<Villeage> villeages;
 	
 	private List<OAuthAccount> oauths;
	public int getUser_id() {
		return user_id;
	}
	public String getPhone() {
		return phone;
	}
	public String getPwd() {
		return pwd;
	}
	 
	public String getUser_name() {
		return user_name;
	}
	public String getEmail() {
		return email;
	}
	
	 
	 
	public String getCreate_time() {
		return create_time;
	}
	public String getLogin_time() {
		return login_time;
	}
	public String getPerson_desc() {
		return person_desc;
	}
	public String getPerson_imageurl() {
		return person_imageurl;
	}
	 
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	 
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	public void setPerson_desc(String person_desc) {
		this.person_desc = person_desc;
	}
	public void setPerson_imageurl(String person_imageurl) {
		this.person_imageurl = person_imageurl;
	}
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocal_imgurl() {
		return local_imgurl;
	}
	public void setLocal_imgurl(String localImgurl) {
		local_imgurl = localImgurl;
	}
    public List<UserVilleage> getUserVilleages(){
    	return this.uservilleages;
    }
	public void setUservilleages(List<UserVilleage> uservilleages) {
		this.uservilleages = uservilleages;
	}
	public List<Villeage> getVilleages() {
		return villeages;
	}
	public void setVilleages(List<Villeage> villeages) {
		this.villeages = villeages;
	}
	public int getLogintype() {
		return logintype;
	}
	public void setLogintype(int logintype) {
		this.logintype = logintype;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public long getExpirein() {
		return expirein;
	}
	public void setExpirein(long expirein) {
		this.expirein = expirein;
	}
	public String getOauthlogintime() {
		return oauthlogintime;
	}
	public void setOauthlogintime(String oauthlogintime) {
		this.oauthlogintime = oauthlogintime;
	}
	public List<OAuthAccount> getOauths() {
		return oauths;
	}
	public void setOauths(List<OAuthAccount> oauths) {
		this.oauths = oauths;
	}
	public String getWorktime() {
		return worktime;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	 
	 
	 
	
}
