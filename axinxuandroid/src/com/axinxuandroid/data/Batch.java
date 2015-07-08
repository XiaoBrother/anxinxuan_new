package com.axinxuandroid.data;

import java.util.List;

public class Batch  implements TimeOrderInterface{

	private int id;
	private long batch_id;
	private long variety_id;
	private int villeage_id;
	private int categoryid;
	private String villeage_name;
	private String variety;//Ʒ��
	private int producecount;//�����߼�¼��
	private int consumercount;//�����߼�¼��
	private String code;//������
	private int status;//������ʾ
	private int stage;//����
 	private String buylink;//��������
	private String create_time;//��������
	private int order_index=0;//�����
	private long order_date=0;//����ʱ��
	private int isdel;//�Ƿ���ɾ��
	private String lastoptime;//������ʱ��
	private int userid;
 	private List<Record> records;
	 
 	public static class Stage{
 		public static final int BATCH_STAGE_PRODUCE=1;//����
 		public static final int BATCH_STAGE_SALE=2;//����
 		public static final int BATCH_STAGE_SALEOUT=3;//����
 	}
 	public static class Status{
 		public static final int BATCH_STATUS_OPEN=1;//����
 	 	public static final int BATCH_STATUS_HIDDEN=0;//����
 	 	public static final int BATCH_STATUS_DELETE=0;//ɾ��
  	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVariety() {
		return variety;
	}
	public void setVariety(String variety) {
		this.variety = variety;
	}
	 
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBuylink() {
		return buylink;
	}
	public void setBuylink(String buylink) {
		this.buylink = buylink;
	}
	public List<Record> getRecords() {
		return records;
	}
	public void setRecords(List<Record> records) {
		this.records = records;
	}
	public long getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(long batchId) {
		batch_id = batchId;
	}
	public long getVariety_id() {
		return variety_id;
	}
	public void setVariety_id(long varietyId) {
		variety_id = varietyId;
	}
	public int getVilleage_id() {
		return villeage_id;
	}
	public void setVilleage_id(int villeageId) {
		villeage_id = villeageId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getProducecount() {
		return producecount;
	}
	public void setProducecount(int producecount) {
		this.producecount = producecount;
	}
	public int getConsumercount() {
		return consumercount;
	}
	public void setConsumercount(int consumercount) {
		this.consumercount = consumercount;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public int getOrder_index() {
		return order_index;
	}
	public void setOrder_index(int orderIndex) {
		order_index = orderIndex;
	}
	public long getOrder_date() {
		return order_date;
	}
	public void setOrder_date(long orderDate) {
		order_date = orderDate;
	}
	public String getVilleage_name() {
		return villeage_name;
	}
	public void setVilleage_name(String villeageName) {
		villeage_name = villeageName;
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
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	@Override
	public String getTimeStr() {
 		return lastoptime;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
}
