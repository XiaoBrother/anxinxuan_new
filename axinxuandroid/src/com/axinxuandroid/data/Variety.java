package com.axinxuandroid.data;

public class Variety {
	private long id;
	private int variety_id;
	private String variety_desc;
	private String variety_name;
	private int category_id;
	private int villeage_id;
	private int user_id;
 	private String create_time;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	private String categorynames;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVariety_id() {
		return variety_id;
	}

	public void setVariety_id(int varietyId) {
		variety_id = varietyId;
	}

	public String getVariety_name() {
		return variety_name;
	}

	public void setVariety_name(String varietyName) {
		variety_name = varietyName;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int categoryId) {
		category_id = categoryId;
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}

	public String getVariety_desc() {
		return variety_desc;
	}

	public void setVariety_desc(String varietyDesc) {
		variety_desc = varietyDesc;
	}

	public String getCategorynames() {
		return categorynames;
	}

	public void setCategorynames(String categorynames) {
		this.categorynames = categorynames;
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
