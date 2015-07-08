package com.axinxuandroid.data;

public class RecordResource implements LoadImageInterface {

	private long id;
	private int type;
	private long recordid;
	private String neturl;
	private String localurl;
 	private int state;//同步状态，
	private String info;
	private int publishstate=0;//发布状态,主要针对优酷视频
	public static  final int RESOURCE_STATE_UNSYNCHRONISE=0;//未同步
	public static  final int RESOURCE_STATE_SYNCHRONISE=1;//已同步
	
	public class VideoState{
		public static  final int RESOURCE_VIDEO_STATE_FAIL=-1;//转码失败
		public static  final int RESOURCE_VIDEO_STATE_UNCHECKED=0;//未查询
 		public static  final int RESOURCE_VIDEO_STATE_ENCODING=1;//转码中
 		public static  final int RESOURCE_VIDEO_STATE_INREVIEW=2;//审核中
		public static  final int RESOURCE_VIDEO_STATE_NORMAL=3;//正常
		public static  final int RESOURCE_VIDEO_STATE_BLOCKED=4;//已屏蔽
		
	}
	public static String getVideoStatus(int status){ 
		String sta="";
		switch (status) {
		case VideoState.RESOURCE_VIDEO_STATE_FAIL:
			sta="转码失败";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_UNCHECKED:
			sta="未查询";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_ENCODING:
			sta="转码中";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_INREVIEW:
			sta="审核中";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_NORMAL:
			sta="正常";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_BLOCKED:
			sta="已屏蔽";
 			break;
 		}
		return sta;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	 
	 
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getNeturl() {
		return neturl;
	}
	public void setNeturl(String neturl) {
		this.neturl = neturl;
	}
	public String getLocalurl() {
		return localurl;
	}
	public void setLocalurl(String localurl) {
		this.localurl = localurl;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRecordid() {
		return recordid;
	}
	public void setRecordid(long recordid) {
		this.recordid = recordid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public int getImgUniqId() {
 		return (int)recordid;
	}
	@Override
	public String getLoadUrl() {
 		return this.neturl;
	}
	@Override
	public void setResult(String localurl) {
        this.localurl=localurl;
	}
	public int getPublishstate() {
		return publishstate;
	}
	public void setPublishstate(int publishstate) {
		this.publishstate = publishstate;
	}
	 
	 
	
}
