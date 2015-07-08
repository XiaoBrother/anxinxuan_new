package com.axinxuandroid.data;

public class VilleagePhoto implements LoadImageInterface {
	private int id;
	private int photoid	;//	  
	private String label_name	;//	��ǩ����
	private String image_url	;//	ͼƬ��ַ
	private String image_desc	;//	ͼƬ����
	private String image_name	;//	ͼƬ����
 	private int villeage_id	;//	����ũ��id
	private int index_order	;//	�����
	private String thumbnail_url	;//	����ͼ��ַ
	private String localurl	;//	���ص�ַ
	private int isdel;//�Ƿ���ɾ��
	private String lastoptime;//������ʱ��
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
