package com.axinxuandroid.oauth;

public class OAuthResult {
	private int type;
	private int result;
	private String accesstoken;
	private long expirein;//过期时间
	private String oauthlogintime;
	private String message;
	private String userid;
	private String name;
	private String imgurl;
    public final static int  RESULT_OF_CANCEL=-1;
    public final static int  RESULT_OF_SUCCESS=1;
    public final static int  RESULT_OF_ERROR=0;
    public final static int  RESULT_OF_UNINSTALL=-2;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
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

	 
 

}
