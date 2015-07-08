package com.axinxuandroid.activity;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
 import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.service.BatchLabelService;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.CommentService;
import com.axinxuandroid.service.IntelligenceService;
import com.axinxuandroid.service.NetUpdateRecordService;
import com.axinxuandroid.service.ProustService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserVisitHistoryService;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.service.VilleageBannerService;
import com.axinxuandroid.service.VilleagePhotoService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.os.Bundle;

public class CleanActivity  extends  NcpZsActivity{
	private ProgressDialog processDialog;
  	private static  final int CLEAN_SUCCESS=1;
	private static  final int CLEAN_FAILED=0;
	private BatchService batchservice;
	private BatchLabelService batchlabelservice;
	private CommentService commentservice;
	private IntelligenceService intelligenceservice;
	private NetUpdateRecordService netupdaterecordservice;
	private ProustService proustservice;
	private RecordService recordservice;
	private RecordResourceService recordresourceservice;
	private SystemNoticeService noticeservice;
	private UserVisitHistoryService visithistoryservice;
	private VarietyService varietyService;
	private VilleageBannerService villeageBannerService;
	private VilleagePhotoService villeagePhotoService;
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
   		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 		hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				int sel=(Integer)((Map)result).get("result");
 				if(sel==1){
 					prepareClean();
 				}else{
 					CleanActivity.this.finish();
 				}
			}
		});
		hand.excuteMethod(new ConfirmDialogHandlerMethod("","清除记录将清除所有记录和下载的图片、视频、音频文件，确定清除吗？"));
  	}
	public void prepareClean(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
  					processDialog=(ProgressDialog) ((Map)result).get("process");
 				startClean();
 				
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","清理中..."));
	}
	
	public void startClean(){
		 new Thread(){
 			@Override
			public void run() {
 				try{
 					//清理数据库数据
 	 				cleanDbdata();
  	 				//清理图片
 	 				cleanFiles(AppConstant.PHOTO_DIR);
 	 				cleanFiles(AppConstant.CAMERA_DIR);
 	 				cleanFiles(AppConstant.BANNER_DIR);
 	 				//清理音频
 	 				cleanFiles(AppConstant.MEDIA_DIR);
 	 				//清理视频
 	 				cleanFiles(AppConstant.VIDEO_DIR);
 	 				
 	 				cleanFinish(CLEAN_SUCCESS);
 				}catch(Exception ex){
 					ex.printStackTrace();
 					cleanFinish(CLEAN_FAILED);
  				}
 				
			}
			 
		 }.start();
	}
	public void cleanFinish(int result){
 		if(processDialog!=null)
			processDialog.dismiss();
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
				@Override
			public void onHandlerFinish(Object result) {
					CleanActivity.this.finish();
		    }
		});
 		 if(result==CLEAN_SUCCESS ){
 				handler.excuteMethod(new MessageDialogHandlerMethod("","清理完成！"));
	     }else{
				handler.excuteMethod(new MessageDialogHandlerMethod("","清理失败！"));
		}
	}
	 
    /**
     * 清理无用文件
     */
	public void cleanFiles(String directory){
		//获取所有文件
		File direct=new File(directory);
		if(direct.exists()&&direct.isDirectory()){
			File[] fls=direct.listFiles();
			if(fls!=null&&fls.length>0)
				for(File fl:fls){
  					fl.delete();
					LogUtil.logInfo(getClass(), "clean file:"+fl.getPath());
 				}
		}
	}
 	 /**
     * 清理数据库数据
     */
	public void cleanDbdata(){
		batchservice=new BatchService();
		batchservice.clearData();
		batchlabelservice=new BatchLabelService();
		batchlabelservice.clearData();
		commentservice=new CommentService();
		commentservice.clearData();
		intelligenceservice=new IntelligenceService();
		intelligenceservice.clearData();
		netupdaterecordservice=new NetUpdateRecordService();
		netupdaterecordservice.clearData();
		proustservice=new ProustService();
		proustservice.clearData();
		recordservice=new RecordService();
		recordservice.clearData();
		recordresourceservice=new RecordResourceService();
		recordresourceservice.clearData();
		noticeservice=new SystemNoticeService();
		noticeservice.clearData();
		visithistoryservice=new UserVisitHistoryService();
		visithistoryservice.clearData();
		varietyService=new VarietyService();
		varietyService.clearData();
		villeageBannerService=new VilleageBannerService();
		villeageBannerService.clearData();
		villeagePhotoService=new VilleagePhotoService();
		villeagePhotoService.clearData();
 	}
// 	public boolean isUseing(String path){
//		if(path==null||this.datas==null||this.datas.size()<1) return false;
//		for(RecordResource rr:datas){
//			if(rr.getLocalurl()!=null&&(path.equals(rr.getLocalurl())))
//				return true;
//		}
//		return false;
//	}
}
