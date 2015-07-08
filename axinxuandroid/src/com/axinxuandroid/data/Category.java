package com.axinxuandroid.data;

public class Category {
	private long id;
	private String category_name;
	private long parentid;
	private int cate_order;
	private int clevel;
    private int isdel;
    private String lastoptime;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	 

	public long getParentid() {
		return parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public int getCate_order() {
		return cate_order;
	}

	public void setCate_order(int cateOrder) {
		cate_order = cateOrder;
	}

	public int getClevel() {
		return clevel;
	}

	public void setClevel(int clevel) {
		this.clevel = clevel;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String categoryName) {
		category_name = categoryName;
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
