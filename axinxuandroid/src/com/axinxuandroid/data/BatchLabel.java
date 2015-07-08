package com.axinxuandroid.data;

public class BatchLabel {
	private long id;
	private int variety_id;
	private int recordid;//标签都是从记录里面查询出来的，所以带个记录id
	private String label_name;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	public int getVariety_id() {
		return variety_id;
	}
	public void setVariety_id(int varietyId) {
		variety_id = varietyId;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String labelName) {
		label_name = labelName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public int getRecordid() {
		return recordid;
	}
	public void setRecordid(int recordid) {
		this.recordid = recordid;
	}
	 
	
}
