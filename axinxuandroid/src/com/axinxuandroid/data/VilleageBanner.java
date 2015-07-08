package com.axinxuandroid.data;

public class VilleageBanner {
	private long id;
	private int bannerid;
 	private int villeage_id;
	private String neturl;
	private String localurl;
	private String thumb_url;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	public long getId() {
		return id;
	}

	public int getVilleage_id() {
		return villeage_id;
	}

	public void setVilleage_id(int villeageId) {
		villeage_id = villeageId;
	}

	public String getNeturl() {
		return neturl;
	}

	public void setNeturl(String neturl) {
		this.neturl = neturl;
	}

	public String getLocalurl() {
		return localurl;
	}

	public void setLocalurl(String localurl) {
		this.localurl = localurl;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumbUrl) {
		thumb_url = thumbUrl;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBannerid() {
		return bannerid;
	}

	public void setBannerid(int bannerid) {
		this.bannerid = bannerid;
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
