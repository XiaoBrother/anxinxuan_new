package com.axinxuandroid.data;

public class AntiFakePublishBatch  {
 	public int id	;//	id������
 	public Integer user_id	;//	�û�id
 	public Integer batchid	;//	ũ������id
 	public Integer variety_id;//Ʒ��
 	public String batchtime	;//	��������
  	public String timebatch	;//ʱ������
  	public String snum	;//ʱ������
  	public String endnum	;//ʱ������
  	public int status=1;//��ʾ״̬
 	public Integer tcount;//����
 	public String bdesc;//����
 	public int isdel;//����
  	public String createtime;//����ʱ��
 	public String lastoptime;//������ʱ��
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getBatchid() {
		return batchid;
	}
	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}
	public Integer getVariety_id() {
		return variety_id;
	}
	public void setVariety_id(Integer variety_id) {
		this.variety_id = variety_id;
	}
	public String getBatchtime() {
		return batchtime;
	}
	public void setBatchtime(String batchtime) {
		this.batchtime = batchtime;
	}
	public String getTimebatch() {
		return timebatch;
	}
	public void setTimebatch(String timebatch) {
		this.timebatch = timebatch;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getTcount() {
		return tcount;
	}
	public void setTcount(Integer tcount) {
		this.tcount = tcount;
	}
	public String getBdesc() {
		return bdesc;
	}
	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getLastoptime() {
		return lastoptime;
	}
	public void setLastoptime(String lastoptime) {
		this.lastoptime = lastoptime;
	}
	public String getSnum() {
		return snum;
	}
	public void setSnum(String snum) {
		this.snum = snum;
	}
	public String getEndnum() {
		return endnum;
	}
	public void setEndnum(String endnum) {
		this.endnum = endnum;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
 	
	
}
