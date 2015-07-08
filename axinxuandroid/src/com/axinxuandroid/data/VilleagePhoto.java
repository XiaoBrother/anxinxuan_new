package com.axinxuandroid.data;

public class VilleagePhoto implements LoadImageInterface {
	private int id;
	private int photoid	;//	  
	private String label_name	;//	标签名称
	private String image_url	;//	图片地址
	private String image_desc	;//	图片描述
	private String image_name	;//	图片名称
 	private int villeage_id	;//	所属农场id
	private int index_order	;//	排序号
	private String thumbnail_url	;//	缩略图地址
	private String localurl	;//	本地地址
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	public int getPhotoid() {
		return photoid;
	}
	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String labelName) {
		label_name = labelName;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String imageUrl) {
		image_url = imageUrl;
	}
	public String getImage_desc() {
		return image_desc;
	}
	public void setImage_desc(String imageDesc) {
		image_desc = imageDesc;
	}
	public String getImage_name() {
		return image_name;
	}
	public void setImage_name(String imageName) {
		image_name = imageName;
	}
	public int getVilleage_id() {
		return villeage_id;
	}
	public void setVilleage_id(int villeageId) {
		villeage_id = villeageId;
	}
	public int getIndex_order() {
		return index_order;
	}
	public void setIndex_order(int indexOrder) {
		index_order = indexOrder;
	}
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	public void setThumbnail_url(String thumbnailUrl) {
		thumbnail_url = thumbnailUrl;
	}
	public String getLocalurl() {
		return localurl;
	}
	public void setLocalurl(String localurl) {
		this.localurl = localurl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	@Override
	public int getImgUniqId() {
 		return photoid;
	}
	@Override
	public String getLoadUrl() {
 		return this.thumbnail_url;
	}
	@Override
	public void setResult(String localurl) {
		this.localurl=	localurl;	
	}
	
	
}
