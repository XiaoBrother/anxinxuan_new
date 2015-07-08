package com.xmpp.client.connect.task;

 import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskManager {
	private ExecutorService executorService ;
	private List<TaskExcute> tasks;
	private static TaskManager manager;
	private Thread excuteTaskThread;
	private boolean isrunningtask=false;
	private TaskManager(){
		executorService=Executors.newSingleThreadExecutor();
		tasks=new ArrayList<TaskExcute>();
		excuteTaskThread=new ExcuteTaskThreadThread();
		excuteTaskThread.start();
  	}
	public static TaskManager getInstance(){
		if(manager==null)
			manager=new TaskManager();
		return manager;
    }
	/**
	 * ���һ��ִ������
	 * @param task
	 */
	public void addTask(Task task){
		synchronized (tasks) {
			tasks.add(new TaskExcute(task));
			tryRestartTaskThread();//��ִ֤��������߳�����
			if(!isrunningtask){
				//����߳���˯���У����ֶ�ִ�е�һ������
				runTaskFirst();
			}
  		}
		
	}
	/**
	 * �õ���ǰ��������
	 * @return
	 */
	public int getTaskCount(){
		return this.tasks.size();
	}
	private TaskExcute removeAndGetFirstTaskExcute(){
		TaskExcute task=null;
		synchronized (tasks) {
		  	if(tasks.size()>0){
		  		task=tasks.get(0);
		  		tasks.remove(0);
		  	}
		}
		return task;
	}
	@SuppressWarnings("unchecked")
	private  Future submit(Runnable task) {
	        Future result = null;
	        if (!executorService.isTerminated()
	                && !executorService.isShutdown()
	                && task != null) {
	            result = executorService.submit(task);
	        }
	        return result;
    }
	/**
	 * ���е�һ������
	 */
	private synchronized void runTaskFirst(){
		TaskExcute exc=removeAndGetFirstTaskExcute();
		if(exc!=null){
 			submit(exc);
		}
	}
	/**
	 * ���ִ�������߳�������������
	 */
	private void tryRestartTaskThread(){
		if(!excuteTaskThread.isAlive()){
			excuteTaskThread=new ExcuteTaskThreadThread();
			excuteTaskThread.start();
		}
	}
	private class ExcuteTaskThreadThread extends Thread{
 		@Override
		public   void run() {
			 while(true){
  					if(tasks.size()>0){
  						isrunningtask=true;
  						runTaskFirst();
 					}else{
						try {
							isrunningtask=false;
							Thread.sleep(5*60*60);//��������
						} catch (InterruptedException e) {
 					  }
					}
 			 }
		}
 	}
	 
	
}
