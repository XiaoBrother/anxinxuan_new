package com.axinxuandroid.data;

public class Comment {
	private long id	;//	id������
	private int comment_id;
 	private String user_name;//�û���
  	private String context	;//	��������
  	private String comment_date		;//	 	����ʱ��
  	private String user_img		;//	 	�û�ͷ��
  	private int parent_id;//������id
  	private String replyUserId;//�������û�id
  	private String replyUserName;//�������û�����
  	private String varietyName;//���۵�Ʒ����
  	private String batchCode;//���۵����κ�
  	private int user_id;
  	private int recordid;//��¼id
  	private int isdel;//�Ƿ���ɾ��
	private String lastoptime;//������ʱ��
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	 
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	 
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String commentDate) {
		comment_date = commentDate;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int commentId) {
		comment_id = commentId;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public int getRecordid() {
		return recordid;
	}
	public void setRecordid(int recordid) {
		this.recordid = recordid;
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
	public String getReplyUserName() {
		return replyUserName;
	}
	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}
	
	public String getReplyUserId() {
		return replyUserId;
	}
	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}
	
	
	public String getVarietyName() {
		return varietyName;
	}
	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Comment)) return false;
		Comment cpr=(Comment) o;
		if(cpr.getComment_id()==this.getComment_id())
			return true;
		return false;
	}
	@Override
	public int hashCode() {
 		return 1;
	}
   	
}
