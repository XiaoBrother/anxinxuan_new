package com.axinxuandroid.data;

public class BatchLabel {
	private long id;
	private int variety_id;
	private int recordid;//��ǩ���ǴӼ�¼�����ѯ�����ģ����Դ�����¼id
	private String label_name;
	private int isdel;//�Ƿ���ɾ��
	private String lastoptime;//������ʱ��
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
