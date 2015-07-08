package com.axinxuandroid.data;

public class CategoryProcedure {
	private int id;
	private int categoryprocedureid;
	private int category_id;
	private String name;
	private int index_order;
	private String create_time;
	private int isdel;
	private String lastoptime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryprocedureid() {
		return categoryprocedureid;
	}

	public void setCategoryprocedureid(int categoryprocedureid) {
		this.categoryprocedureid = categoryprocedureid;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int categoryId) {
		category_id = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex_order() {
		return index_order;
	}

	public void setIndex_order(int indexOrder) {
		index_order = indexOrder;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
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
