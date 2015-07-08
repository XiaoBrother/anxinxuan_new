package com.axinxuandroid.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;



public class Record implements Comparable,TimeOrderInterface{
	private long id;
	private String phone;
	private int save_type;//记录总类型
	private String label_name;
	private String save_date;
	private String send_date;
	private String context;
 	private String user_id;
 	private String user_img;
	private String nick_name;
	private String auth_code;
	private int villeage_id;
	private int batch_id;//批次id
	private int record_id;//记录id
	private String trace_code;//批次码
	private int type;//每个记录内容类型，
	private int state;//同步的进度
	private String variety_name;//品种名
	private int variety_id;//品种id
	private int user_type;
	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
	private double lng;//经度
	private double lat;//纬度
	private int savestatus=BATCHRECORD_SAVESTATE_NET;//保存状态 
	private List<RecordResource> resources;//资源信息
  	public static final int BATCHRECORD_TYPE_OF_TEXT=1;//文字类型
	public static final int BATCHRECORD_TYPE_OF_IMAGE=2;//图片类型
	public static final int BATCHRECORD_TYPE_OF_VIDEO=4;//视频类型
	public static final int BATCHRECORD_TYPE_OF_AUDIO=8;//音频类型
	public static final int BATCHRECORD_TYPE_OF_TEMPLATE=16;//模板类型
	
	public static final int BATCHRECORD_USERTYPE_OF_CONSUMER=1;//消费者记录
	public static final int BATCHRECORD_USERTYPE_OF_CREATOR=2;//生产者记录
	
	public static final int BATCHRECORD_STATE_UNSAVED=0;//未保存
	public static final int BATCHRECORD_STATE_TEXTSAVED=1;//基本信息保存成功
	public static final int BATCHRECORD_STATE_IMAGESAVED=2;//图片保存成功
	public static final int BATCHRECORD_STATE_VIDEOSAVED=4;//视频保存成功
	public static final int BATCHRECORD_STATE_AUDIOSAVED=8;//音频保存成功
	public static final int BATCHRECORD_STATE_TEMPLATESAVED=16;//模板保存成功
	
	public static final int BATCHRECORD_SAVESTATE_PREPARESAVE=-1;//待同步记录
	public static final int BATCHRECORD_SAVESTATE_DRAFT=0;//草稿箱记录
 	public static final int BATCHRECORD_SAVESTATE_NET=1;//已同步记录

	public Record(){
		resources=new ArrayList<RecordResource>();
 	}
	 
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTrace_code() {
		return trace_code;
	}
	public void setTrace_code(String trace_code) {
		this.trace_code = trace_code;
	}
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	 
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}
	public String getSave_date() {
		return save_date;
	}
	public void setSave_date(String save_date) {
		this.save_date = save_date;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	 
  
	 
	public int getSave_type() {
		return save_type;
	}


	public void setSave_type(int saveType) {
		save_type = saveType;
	}


	public int getVilleage_id() {
		return villeage_id;
	}


	public void setVilleage_id(int villeageId) {
		villeage_id = villeageId;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getVariety_name() {
		return variety_name;
	}
	public void setVariety_name(String varietyName) {
		variety_name = varietyName;
	}
	public List<RecordResource> getResources() {
		return resources;
	}
	public void setResources(List<RecordResource> resources) {
		this.resources = resources;
	}
    
	public double getLng() {
		return lng;
	}


	public void setLng(double lng) {
		this.lng = lng;
	}


	public double getLat() {
		return lat;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public void addResource(RecordResource rec){
		if(this.resources==null)
			resources=new ArrayList<RecordResource>();
		resources.add(rec);
	}
	
	public List<RecordResource> getResourceByType(int type){
		if(this.resources!=null&&this.resources.size()>0){
			List<RecordResource> goal=new ArrayList<RecordResource>();
			for(RecordResource rec:resources){
				if(rec.getType()==type)
					goal.add(rec);
			}
			return goal;
		}
		return null;
	}


	public int getVariety_id() {
		return variety_id;
	}


	public void setVariety_id(int varietyId) {
		variety_id = varietyId;
	}


	public int getUser_type() {
		return user_type;
	}


	public void setUser_type(int userType) {
		user_type = userType;
	}


	public String getUser_img() {
		return user_img;
	}


	public void setUser_img(String userImg) {
		user_img = userImg;
	}


	@Override
	public int compareTo(Object another) {
 		if( another==null||!(another instanceof Record) ) return -1;
		Record com=(Record) another;
		if(com.getRecord_id()!=0&&this.getRecord_id()!=0){
			//比较id
			if(com.getRecord_id()>this.getRecord_id())
				return 1;
			else if(com.getRecord_id()==this.getRecord_id())
				return 0;
			else return -1;
		}else{
			//比较日期
			Date cdate=DateUtil.StrToDate(com.getSave_date());
			Date tdate=DateUtil.StrToDate(this.getSave_date());
			return DateUtil.compare(cdate, tdate);
		}
		
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


	public boolean isAllSaved(){
 		if(this.getState()==this.getSave_type())
			return true;
		return false;
	}


	public int getSavestatus() {
		return savestatus;
	}


	public void setSavestatus(int savestatus) {
		this.savestatus = savestatus;
	}


	@Override
	public String getTimeStr() {
		// TODO Auto-generated method stub
		return lastoptime;
	}
}
