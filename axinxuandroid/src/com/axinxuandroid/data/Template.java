package com.axinxuandroid.data;

public class Template {
	private long id;
	private int template_id;
	private int category_id;
	private String label_name;
	private String head;
	private String foot;
	private String sign;
	private int version;
	private String context;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(int templateId) {
		template_id = templateId;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int categoryId) {
		category_id = categoryId;
	}

	public String getLabel_name() {
		return label_name;
	}

	public void setLabel_name(String labelName) {
		label_name = labelName;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getFoot() {
		return foot;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
