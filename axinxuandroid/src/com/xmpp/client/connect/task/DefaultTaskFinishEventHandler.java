package com.xmpp.client.connect.task;

public class DefaultTaskFinishEventHandler implements TaskFinishEventHandler {

	@Override
	public void onfinish(TaskResult result) {
		// TODO Auto-generated method stub
      System.out.println("����"+result.getTask().getTaskName());
      System.out.println("�ڣ�"+result.getStartDate()+"��ʼִ�У�");
      System.out.println(result.getEndDate()+"ִ����ɣ�");
      System.out.println("ִ�н��:"+result.getResult());
	}

}
