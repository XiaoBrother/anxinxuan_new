package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

 import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.net.LoadNetImgThread.LoadImgFinishListener;
import com.axinxuandroid.data.LoadImageInterface;
import com.axinxuandroid.data.RecordResource;
import com.ncpzs.util.LogUtil;

import android.os.Handler;
import android.view.View;

/**
 * 加载图片的队列
 * 
 * @author hubobo
 * 
 */
public class LoadImageQueue {
	private List<ImageItem> items;
	private volatile boolean isloading = false;
	private NetFinishListener liserner;
    private Set<ImageItem> finishs;
    private volatile boolean stop=false;
	public LoadImageQueue() {
		items = new ArrayList<ImageItem>();
		finishs=new HashSet<ImageItem>();
		this.startLoad();
		//LogUtil.logInfo(LoadImageQueue.class, "start....");
	}

//	public void addItem(RecordResource resource,View view) {
//		 addItem(new ImageItem(resource, view));
// 	}
    public void reloadItem(LoadImageInterface resource,View view,int index){
    	ImageItem newitem=new ImageItem(resource, view,index);
    	//先查看当前加载队列中是否有加载该图片的任务，有就退出
    	if (hasItem(newitem.res,newitem.view))
    		return ;
		//查看改任务是否已经执行过，并且有执行结果，有则删除，重新加载	 
     	ImageItem item=getFinishItem(newitem.res,newitem.view);
    	if(item!=null){
    		finishs.remove(item);
 		}
    	addItem(newitem);
    	//LogUtil.logInfo(getClass(), "reload img");
	}
	public void addItem(LoadImageInterface resource,View view,int index) {
		 addItem(new ImageItem(resource, view,index));
	}
	public void addItem(ImageItem imgitem) {
		ImageItem item=getFinishItem(imgitem.res,imgitem.view);
		if(item!=null){
			if (liserner != null)
				liserner.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, "加载成功", item));
		}else{
			//LogUtil.logInfo(getClass(), "add item:"+imgitem.res.getLoadUrl());
			if (!hasItem(imgitem.res,imgitem.view))
				items.add(imgitem);
		}
 	}
	
	

	private boolean hasItem(LoadImageInterface resource,View view) {
		if (resource == null || resource.getLoadUrl() == null)
			return true;
		if (items != null) {
			for (ImageItem item : items)
				if (item.res.getLoadUrl().equals(resource.getLoadUrl())){
					item.view=view;
					return true;
				}
		}
		return false;
	}
	public ImageItem getFinishItem(LoadImageInterface resource,View view) {
		if (resource == null || resource.getLoadUrl() == null)
			return null;
		if (finishs != null) {
			for (ImageItem item : finishs)
				if (item.res.getLoadUrl().equals(resource.getLoadUrl())){
					item.view=view;
					return item;
				}
		}
		return null;
	}
	private void startLoad() {
		new Thread() {
			@Override
			public void run() {
				while (!stop) {
					try {
						if (items.size() > 0 && !isloading) {
							isloading = true;
 							loadImg(items.get(0));
						} else {
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	
	 
	
	private void loadImg(final ImageItem item) {
		LoadNetImgThread thread = new LoadNetImgThread(item.res.getLoadUrl(),null,null);//使用默认路径保存
		thread.setLoadImgFinishListener(new LoadImgFinishListener() {
 			@Override
			public void onfinish(int result, String path) {
				if (result == NetResult.RESULT_OF_SUCCESS) {// 加载图片成功
					item.res.setResult(path);
				} else item.res.setResult(null);
				loadFinishImg(item);
			}
		});
		thread.start();
	}

	private void loadFinishImg(ImageItem item) {
		//LogUtil.logInfo(LoadImageQueue.class, "load finish:"+ item.res.getLoadUrl());
		finishs.add(item);
		if (item != null)
			items.remove(item);
		//LogUtil.logInfo(getClass(), "items:"+items.size());
		if (liserner != null)
			liserner.onfinish(new NetResult(NetResult.RESULT_OF_SUCCESS, "加载成功", item));
 		isloading = false;
	}

	public void setLiserner(NetFinishListener liserner) {
		this.liserner = liserner;
	}

	public void stopLoad(){
		this.stop=true;
	}
	public class ImageItem{
		public LoadImageInterface res;
		public View view;
		public int index;
		public ImageItem(LoadImageInterface res,View view){
			this(res,view,0);
		}
		public ImageItem(LoadImageInterface res,View view,int index){
			this.res=res;
			this.view=view;
			this.index=index;
		}
		@Override
		public boolean equals(Object o) {
		     if(o==null||!(o instanceof ImageItem)) return false;
		     ImageItem tmp=(ImageItem) o;
 		     if(tmp.res.getImgUniqId()==this.res.getImgUniqId())
		    	 return true;
			return false;
		}
		@Override
		public int hashCode() {
 			return 1;
		}
		
		
	}
}
