package com.axinxuandroid.sys.gloable;

import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.net.LoadCategoryThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.NetThread;
import com.axinxuandroid.activity.net.TemplateCheckThread;
import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;

import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.CategoryService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.TemplateService;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CategoryUpdateService extends Service {
     private CategoryService  cateservice;
  	@Override
	public IBinder onBind(Intent intent) {
 		return null;
	}

	@Override
	public void onCreate() {
 		super.onCreate();
 		startCheckTemplate();
	}

	@Override
	public void onDestroy() {
		LogUtil.logInfo(getClass(), "destory service:"+getClass().getName());
 		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
 		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
 		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
 		return super.onStartCommand(intent, flags, startId);
	}

	public void startCheckTemplate(){
		LogUtil.logInfo(getClass(), "start service:"+getClass().getName());
		cateservice=new CategoryService();
//		List<Category> cates=cateservice.selectbyParentId(1);
//		if(cates!=null){
//			for(Category cate:cates)
//				LogUtil.logError(getClass(), "lastoptime:"+cate.getLastoptime());
//		}
 		String lastoptime=cateservice.getLatoptime();
 		LoadCategoryThread th=new LoadCategoryThread(lastoptime);
 		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				if(data.result==NetResult.RESULT_OF_SUCCESS&&data.returnData!=null){
  					 List<Category> cates=(List<Category>) data.returnData;
 					if(cates!=null){
						 for(Category cate:cates){
							 cateservice.saveOrUpdate(cate);
						 }
  					} 
  				}
 			    stopSelf();
			}
 			
		});
 		th.start();
	}
	 
 
}
