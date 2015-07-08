package com.axinxuandroid.data;

public class Intelligence implements LoadImageInterface{
	private long id; //id自增
	private int intelligenceid;
	private String label_name;	//	标签名称
	private String intelligence_desc;	//	资质描述
	private String intelligence_imgurl;	//	资质图片url
	private String localurl;//本地图片地址
	private int villeage_id	;//所属农场id
	private String create_time;	//	创建时间
	private String thumbnail_url;	  //	缩略图地址
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getIntelligenceid() {
		return intelligenceid;
	}
	public void setIntelligenceid(int intelligenceid) {
		this.intelligenceid = intelligenceid;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String labelName) {
		label_name = labelName;
	}
	public String getIntelligence_desc() {
		return intelligence_desc;
	}
	public void setIntelligence_desc(String intelligenceDesc) {
		intelligence_desc = intelligenceDesc;
	}
	public String getIntelligence_imgurl() {
		return intelligence_imgurl;
	}
	public void setIntelligence_imgurl(String intelligenceImgurl) {
		intelligence_imgurl = intelligenceImgurl;
	}
	 
	public int getVilleage_id() {
		return villeage_id;
	}
	public void setVilleage_id(int villeageId) {
		villeage_id = villeageId;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	public void setThumbnail_url(String thumbnailUrl) {
		thumbnail_url = thumbnailUrl;
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
	public String getLocalurl() {
		return localurl;
	}
	public void setLocalurl(String localurl) {
		this.localurl = localurl;
	}
	@Override
	public int getImgUniqId() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getLoadUrl() {
 		return this.intelligence_imgurl;
	}
	@Override
	public void setResult(String localurl) {
       this.localurl=	localurl;	
	}
	
	
}
