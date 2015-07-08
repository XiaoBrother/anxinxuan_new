package com.axinxuandroid.data;

import java.util.List;

public class Villeage implements MoreWindowDataInterface{
	private long id;
 	private int villeage_id;
	private String villeage_name;
	private String collect_code;
	private String manager_name;
	private String tele;
	private String address;
 	private String bulid_time;//建立时间
	private String scale;//规模
	private int type;//农场类型
	private String manage_scope;//经营范围
	private String villeage_desc;
	private double lat;
	private double lng;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
 	private List<Variety> varieties;
	private List<User> users;
	private List<Intelligence> intelligences;
	private List<VilleageBanner> banners;
	public long getId() {
		return id;
	}

	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}

	public String getTele() {
		return tele;
	}

	public void setTele(String tele) {
		this.tele = tele;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getVilleage_name() {
		return villeage_name;
	}

	public void setVilleage_name(String villeageName) {
		villeage_name = villeageName;
	}

	 

	public String getCollect_code() {
		return collect_code;
	}

	public void setCollect_code(String collectCode) {
		collect_code = collectCode;
	}

	 

	public List<Variety> getVarieties() {
		return varieties;
	}

	public void setVarieties(List<Variety> varieties) {
		this.varieties = varieties;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Intelligence> getIntelligences() {
		return intelligences;
	}

	public void setIntelligences(List<Intelligence> intelligences) {
		this.intelligences = intelligences;
	}

	public List<VilleageBanner> getBanners() {
		return banners;
	}

	public void setBanners(List<VilleageBanner> banners) {
		this.banners = banners;
	}

	public String getBulid_time() {
		return bulid_time;
	}

	public void setBulid_time(String bulidTime) {
		bulid_time = bulidTime;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getManage_scope() {
		return manage_scope;
	}

	public void setManage_scope(String manageScope) {
		manage_scope = manageScope;
	}

	public String getVilleage_desc() {
		return villeage_desc;
	}

	public void setVilleage_desc(String villeageDesc) {
		villeage_desc = villeageDesc;
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

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String getShowText() {
 		return villeage_name;
	}

}
