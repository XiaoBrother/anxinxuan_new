package com.ncpzs.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Administrator
 * 
 */
public class LoadThumbnailThreadPool {
	private final Object lock = new Object();
	private List<ExecuteTask> tasks;
	private ExecuteThread thread;
	private boolean stop = false;
  	private LoadThumbnailThreadPool() {
		tasks = new LinkedList<ExecuteTask>();
		thread = new ExecuteThread();
		thread.start();
 	}

	public static LoadThumbnailThreadPool createPool() {
		return new LoadThumbnailThreadPool();
	}

	public void addNewRunnableTask(String id, Runnable ftask) {
		synchronized (lock) {
			ExecuteTask task = new ExecuteTask(id, ftask);
 			if (!tasks.contains(task)) {
 				// 加入到第一个位置
				tasks.add(0, task);
			}
			
			lock.notify();
		}
	}

	private class ExecuteThread extends Thread {
		@Override
		public void run() {
			while (!stop) {
				try {
 					if (tasks.size() > 0) {// 如果有任务要执行
 						ExecuteTask excuteingtask = null;
  						synchronized (lock) {
 							excuteingtask = tasks.get(0);// 获取第一个任务
  							//LogUtil.logInfo(getClass(), "execute:"+excuteingtask.uniqid);
  							lock.notify();
 						}
						if (excuteingtask != null) {
							excuteingtask.runtask.run();// 执行任务
 							synchronized (lock) {
								tasks.remove(excuteingtask);
 	 							lock.notify();
	 						}
  						}
 						Thread.sleep(50);
 					} else {
 						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void stopLoad() {
		this.stop = true;
	}

	public class ExecuteTask {
		public String uniqid;// 唯一标示
		public Runnable runtask;
 
		public ExecuteTask(String id, Runnable task) {
			this.uniqid = id;
			this.runtask = task;
 		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;
			ExecuteTask tmp = (ExecuteTask) o;
			if (tmp.uniqid != null && tmp.uniqid.equals(uniqid))
				return true;
			return false;
		}

		@Override
		public int hashCode() {
			return 1;
		}

	}

}
