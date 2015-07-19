package com.axinxuandroid.data;

public class AntiFakePublishBatch  {
 	public int id	;//	id，自增
 	public Integer user_id	;//	用户id
 	public Integer batchid	;//	农场批次id
 	public Integer variety_id;//品种
 	public String batchtime	;//	生产日期
  	public String timebatch	;//时间批次
  	public String snum	;//时间批次
  	public String endnum	;//时间批次
  	public int status=1;//显示状态
 	public Integer tcount;//数量
 	public String bdesc;//描述
 	public int isdel;//描述
  	public String createtime;//创建时间
 	public String lastoptime;//最后操作时间
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
