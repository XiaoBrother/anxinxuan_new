package com.xmpp.client.connect.task;

import java.util.Date;

public class TaskResult {

	public static final int RESULT_OK=1;
	public static final int RESULT_ERROR=-1;
	public static final int RESULT_EXCEPTION=0;
	private Task task;
	private int  result;//ִ�н��
	private Date startDate;//��ʼִ��ʱ�䣬
	private Date endDate;//ִ�н���ʱ��
	public TaskResult(Task task){
		this.task=task;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Task getTask() {
		return task;
	}
	
	
}
