package com.axinxuandroid.data;

public class Comment {
	private long id	;//	id，自增
	private int comment_id;
 	private String user_name;//用户名
  	private String context	;//	评论内容
  	private String comment_date		;//	 	评论时间
  	private String user_img		;//	 	用户头像
  	private int parent_id;//被评论id
  	private String replyUserId;//被评论用户id
  	private String replyUserName;//被评论用户名称
  	private String varietyName;//评论的品种名
  	private String batchCode;//评论的批次号
  	private int user_id;
  	private int recordid;//记录id
  	private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
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
