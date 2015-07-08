package com.xmpp.client.connect.task;

import java.util.Date;

import com.ncpzs.util.LogUtil;
public class TaskExcute implements Runnable {
 	private Task task;
 	private TaskResult result;
 	public TaskExcute(Task task){
		 this.task=task;
		 result=new TaskResult(task);
 	}
 	@Override
	public void run() {
		LogUtil.logInfo(TaskExcute.class, "task start ...");
 		result.setStartDate(new Date());
 		try{
		  if(task!=null){
			  task.task();
			  result.setResult(TaskResult.RESULT_OK);
		  }
 		}catch(Exception ex){
 			ex.printStackTrace();
 			result.setResult(TaskResult.RESULT_EXCEPTION);
 		}
  		result.setEndDate(new Date());
  		LogUtil.logInfo(TaskExcute.class, "task end ...");
 		if(task.getTaskFinishEventHandler()!=null){
 			task.getTaskFinishEventHandler().onfinish(result);
 		}
 		
	}
	public Task getTask() {
		return task;
	}
	 
 	
 	
}
