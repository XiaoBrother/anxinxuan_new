package com.axinxuandroid.data;

/**
 * ϵͳ�����ǩ
 * @author Administrator
 *
 */
public class SystemLabel {
	private int  id;
	private int  label_id;//id
	private String label_name;// ��ǩ����
	private String label_imageurl;// ��ǩͼƬ��ַ
	private int type=1;//����,Ĭ��1
	private int module_id;//����ģ��id
	private int index_order;//�����
	
	
	public static class SystemModule{
	  	public static final int MODULE_OF_NCZZ=1;//"ũ������"
	  	public static final int MODULE_OF_NCXC=2;//"ũ�����"
	  	public static final int MODULE_OF_SCJL=3;//"������¼"
   }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLabel_id() {
		return label_id;
	}
	public void setLabel_id(int labelId) {
		label_id = labelId;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String labelName) {
		label_name = labelName;
	}
	public String getLabel_imageurl() {
		return label_imageurl;
	}
	public void setLabel_imageurl(String labelImageurl) {
		label_imageurl = labelImageurl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getModule_id() {
		return module_id;
	}
	public void setModule_id(int moduleId) {
		module_id = moduleId;
	}
	public int getIndex_order() {
		return index_order;
	}
	public void setIndex_order(int indexOrder) {
		index_order = indexOrder;
	}
	
	
	
}
