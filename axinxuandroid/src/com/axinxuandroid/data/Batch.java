package com.axinxuandroid.data;

import java.util.List;

public class Batch  implements TimeOrderInterface{

	private int id;
	private long batch_id;
	private long variety_id;
	private int villeage_id;
	private int categoryid;
	private String villeage_name;
	private String variety;//品种
	private int producecount;//生成者记录数
	private int consumercount;//消费者记录数
	private String code;//批次码
	private int status;//隐藏显示
	private int stage;//过程
 	private String buylink;//购买链接
	private String create_time;//创建日期
	private int order_index=0;//排序号
	private long order_date=0;//排序时间
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	private int userid;
 	private List<Record> records;
	 
 	public static class Stage{
 		public static final int BATCH_STAGE_PRODUCE=1;//生产
 		public static final int BATCH_STAGE_SALE=2;//销售
 		public static final int BATCH_STAGE_SALEOUT=3;//售罄
 	}
 	public static class Status{
 		public static final int BATCH_STATUS_OPEN=1;//公开
 	 	public static final int BATCH_STATUS_HIDDEN=0;//隐藏
 	 	public static final int BATCH_STATUS_DELETE=0;//删除
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
