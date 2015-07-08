package com.axinxuandroid.data;

/**
 * 系统定义标签
 * @author Administrator
 *
 */
public class SystemLabel {
	private int  id;
	private int  label_id;//id
	private String label_name;// 标签名称
	private String label_imageurl;// 标签图片地址
	private int type=1;//类型,默认1
	private int module_id;//所属模块id
	private int index_order;//排序号
	
	
	public static class SystemModule{
	  	public static final int MODULE_OF_NCZZ=1;//"农场资质"
	  	public static final int MODULE_OF_NCXC=2;//"农场相册"
	  	public static final int MODULE_OF_SCJL=3;//"生产记录"
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
