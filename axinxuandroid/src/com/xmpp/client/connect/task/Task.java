package com.xmpp.client.connect.task;

public abstract class Task {
	protected String taskName="MyTask";
	private   TaskFinishEventHandler finishHandler;
	public Task(){
		finishHandler=new DefaultTaskFinishEventHandler();
	}
    public String getTaskName(){
    	return taskName;
    }
	public abstract  void  task() throws Exception; 
	
	public void setTaskFinishEventHandler(TaskFinishEventHandler handler){
		if(handler!=null)
		 this.finishHandler=handler;
	}
	public TaskFinishEventHandler getTaskFinishEventHandler(){
		if(finishHandler!=null)
		 return finishHandler;
		return null;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	
}
