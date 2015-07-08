package com.axinxuandroid.data;

public class RecordResource implements LoadImageInterface {

	private long id;
	private int type;
	private long recordid;
	private String neturl;
	private String localurl;
 	private int state;//ͬ��״̬��
	private String info;
	private int publishstate=0;//����״̬,��Ҫ����ſ���Ƶ
	public static  final int RESOURCE_STATE_UNSYNCHRONISE=0;//δͬ��
	public static  final int RESOURCE_STATE_SYNCHRONISE=1;//��ͬ��
	
	public class VideoState{
		public static  final int RESOURCE_VIDEO_STATE_FAIL=-1;//ת��ʧ��
		public static  final int RESOURCE_VIDEO_STATE_UNCHECKED=0;//δ��ѯ
 		public static  final int RESOURCE_VIDEO_STATE_ENCODING=1;//ת����
 		public static  final int RESOURCE_VIDEO_STATE_INREVIEW=2;//�����
		public static  final int RESOURCE_VIDEO_STATE_NORMAL=3;//����
		public static  final int RESOURCE_VIDEO_STATE_BLOCKED=4;//������
		
	}
	public static String getVideoStatus(int status){ 
		String sta="";
		switch (status) {
		case VideoState.RESOURCE_VIDEO_STATE_FAIL:
			sta="ת��ʧ��";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_UNCHECKED:
			sta="δ��ѯ";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_ENCODING:
			sta="ת����";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_INREVIEW:
			sta="�����";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_NORMAL:
			sta="����";
 			break;
		case VideoState.RESOURCE_VIDEO_STATE_BLOCKED:
			sta="������";
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
