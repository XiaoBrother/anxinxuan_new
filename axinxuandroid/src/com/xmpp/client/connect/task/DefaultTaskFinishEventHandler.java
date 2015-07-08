package com.xmpp.client.connect.task;

public class DefaultTaskFinishEventHandler implements TaskFinishEventHandler {

	@Override
	public void onfinish(TaskResult result) {
		// TODO Auto-generated method stub
      System.out.println("任务："+result.getTask().getTaskName());
      System.out.println("在："+result.getStartDate()+"开始执行，");
      System.out.println(result.getEndDate()+"执行完成，");
      System.out.println("执行结果:"+result.getResult());
	}

}
