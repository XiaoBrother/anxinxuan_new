package com.axinxuandroid.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.net.CheckVersionThread;
import com.axinxuandroid.activity.net.LoadUserVilleageThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.UpdateNoticeDialog;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.service.VilleageService;
import com.axinxuandroid.sys.gloable.BaiDuLocation;
import com.axinxuandroid.sys.gloable.CategoryUpdateService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.TemplateUpdateService;
import com.axinxuandroid.sys.gloable.BaiDuLocation.GetBaiduLocationPositionListener;
import com.axinxuandroid.sys.gloable.UploadSystemErrorService;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.NetUtil;

public class WelcomeActivity extends NcpZsActivity{
 	private UserService userservice;
	private SystemNoticeService noticeservice;
	private UserVilleageService uservilleageservice;
	private VilleageService villeageservice;
    	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.welcome);
		//��ȡ�û�����
        new BaiDuLocation(WelcomeActivity.this).start(new GetBaiduLocationPositionListener() {
 			@Override
			public void position(int result, double lat, double litu) {
				 if(result==BaiDuLocation.GET_POSITION_RESULT_SUCCESS){
 					 SharedPreferenceService.saveUserLocation(lat, litu);
				 }
				 LogUtil.logInfo(getClass(), "user location:lat"+lat+",litu:"+litu);
				 check();
			}
		});
   	 //check();
//		User user=userservice.getLastLoginUser();
//		RecordResource rs=new RecordResource();
//		rs.setLocalurl(AppConstant.VIDEO_DIR+"/axxtest.mp4");
//		List<RecordResource> ars=new ArrayList<RecordResource>();
//		ars.add(rs);
//  		UploadVideoThread uth=new UploadVideoThread("1","aas","shipin",ars,user);
//  		uth.start();
  		
    }
   	private void check(){
   		if(checkSdCard()){
			userservice = new UserService();
			uservilleageservice=new UserVilleageService();
			villeageservice=new VilleageService();
			noticeservice=new SystemNoticeService();
			createallDirectory();
			//������̨����
			startService(new Intent(this,UploadSystemErrorService.class));//�ϴ�������־
			//startService(new Intent("com.axinxuandroid.backservice.UserFavoriteUpdateService"));//�û��ղظ��·���
			startService(new Intent(this,TemplateUpdateService.class));//ģ����·���
			//startService(new Intent("com.axinxuandroid.backservice.VersionUpdateService"));//ϵͳ���·���
			startService(new Intent(this,CategoryUpdateService.class));//Ʒ�ָ��·���
	  		new Handler().postDelayed(new Runnable(){
	 			@Override
				public void run() {
	 				if(NetUtil.isInNetworkState()){
	 					checkversion();
	 				}
	 				else{
	 					checkuser();
	 				}
	  			}
				
			},2000);
		}else{
			NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 			hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					WelcomeActivity.this.finish();
 				}
			});
 			hand.excuteMethod(new MessageDialogHandlerMethod("","δ����SD�洢�������Ȱ�װSD������ʹ�ã�"));
		}
		
		
   	}
    	
    	
   	/**
   	 * ���sd��״��
   	 * @return
   	 */
   	public boolean checkSdCard(){
   		LogUtil.logInfo(getClass(), "state:"+Environment.getExternalStorageState());
   		if (Environment.getExternalStorageState().equals(    
                Environment.MEDIA_MOUNTED)) {    
            //sd card ����    
			return true;
 		}else {    
		   return false;
		}    
   	}
  	
	public void checkuser(){
 		User user=userservice.getLastLoginUser();
		if(user==null){
			//��ת����½ҳ
 			login();
 		}else{
			//�Ƚ��ϴε�½���������ڵ�����
			Date curdate=new Date();
			Date lastdate=DateUtil.StrToDate(user.getLogin_time());
			long diff=DateUtil.getDiffDays(lastdate, curdate);
			LogUtil.logInfo(Appstart.class, user.getLogin_time()+":"+diff);
			if(diff>7||diff<0){
				//����7�����µ�½
				login();
			}else{
				updateUserVilleage();//�����û�ũ����Ϣ
 			}
 		}
	}
	public void login(){
		//��ת����½ҳ
		Intent intent = new Intent (WelcomeActivity.this,LoginActivity.class);			
		startActivity(intent);			
		WelcomeActivity.this.finish();
	}
	public void welcome(){
		User user=userservice.getLastLoginUser();
		//��ת����ҳ
		user.setLogin_time(DateUtil.dateToStrWithPattern(new Date(), "yyyy-MM-dd HH:mm:ss"));
		SharedPreferenceService.saveLastLoginUser(user);
	    startActivity(new Intent(WelcomeActivity.this, ScanCodeActivity.class));
	    WelcomeActivity.this.finish();
	}
	public void createallDirectory(){
		FileUtil.createDirectory(AppConstant.MEDIA_DIR);
		FileUtil.createDirectory(AppConstant.VIDEO_DIR);
		FileUtil.createDirectory(AppConstant.CAMERA_DIR);
		FileUtil.createDirectory(AppConstant.PHOTO_DIR);
		//FileUtil.createDirectory(AppConstant.PHOTO_THUMB_DIR);
		FileUtil.createDirectory(AppConstant.ROOT_DIR);
		FileUtil.createDirectory(AppConstant.USERIMG_DIR);
		FileUtil.createDirectory(AppConstant.BANNER_DIR);
	}
	
	String  message="";
	/**
	 * ���ϵͳ�汾
	 */
	private void checkversion(){
 		CheckVersionThread th=new CheckVersionThread();
 		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					float oldversion=SharedPreferenceService.getVersion();
 					final Version version=(Version) data.returnData;
 					if(oldversion<version.getVersion()){
 						boolean isold=version.isOldVersion(oldversion);
 						
	 					String title="�����°汾������º�ʹ��";
 	 					//if(version.getImportant()==Version.IMPORTANT_TRUE||isold){
   	 						String  versiondesc=version.getVersiondesc();
  	 						if(versiondesc!=null&&!"".equals(versiondesc.trim())){
 	 							JSONArray descs=JSONObject.parseArray(versiondesc);
 	 							//LogUtil.logInfo(getClass(), "message:"+descs.size());
 	 							if(descs!=null){
  	 								for(Object obj:descs){
 	 									JSONObject jsonobj=(JSONObject) obj;
 	 									message+=jsonobj.getString("num")+":"+jsonobj.getString("desc")+"\n";
 	 								}
 	 							}
 	 						}
  	 					//} 
 	 					
 	 					SystemNotice notice=new SystemNotice();
					    notice.setNotice(version.getVersion()+"");
						notice.setType(SystemNoticeType.NOTICE_TYPE_VERSION);
						Map map = new HashMap();  
					    map.put( "newversion", version.getVersion() );  
					    map.put( "versiondesc", version.getVersiondesc() ); 
					    map.put( "downurl", version.getDownurl());  
					    notice.setJsondata(JSONObject.toJSON(map).toString());
						noticeservice.saveOrUpdateNotice(notice);	
// 	 					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
// 							@Override
//							public void onHandlerFinish(Object result) {
// 								Integer sel=(Integer) ((Map)result).get("result");
// 								if(sel==1){
// 									 Intent upint=new Intent("com.axinxuandroid.backservice.SystemUpdateService");
// 	 								 upint.putExtra("downurl", version.getDownurl());
// 	 								 upint.putExtra("version",(double) version.getVersion());
// 	 								 startService(upint);//����ϵͳ
// 								}
//  								checkuser();
//							} 
//						});
 	 					handler.post(new Runnable() {
							
							@Override
							public void run() {
								UpdateNoticeDialog.Builder noticebuilder=new UpdateNoticeDialog.Builder(WelcomeActivity.this);
		 	 					noticebuilder.setMessage(message);
		 	 					noticebuilder.setPositiveButton(null, new OnClickListener() {
		 							@Override
									public void onClick(DialogInterface dialog, int which) {
		 								Intent upint=new Intent("com.axinxuandroid.backservice.SystemUpdateService");
		 								 upint.putExtra("downurl", version.getDownurl());
		 								 upint.putExtra("version",(double) version.getVersion());
		 								 startService(upint);//����ϵͳ
		 								 checkuser();
									}
								});
		 	 					noticebuilder.setNegativeButton(null, new OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										checkuser();
									}
								});
		 	 					noticebuilder.create().show();
							}
						});
 	 					
	 					//handler.excuteMethod(new ConfirmDialogHandlerMethod(title,message,"����","ȡ��"));
	 					
 					}else{
 						noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_VERSION);
 						checkuser();
 					}
  				}else{
  					checkuser();
  				}
  				
 			}
		});
		th.start();
	}
	 
 
	/**
	 * �����û�ũ����Ϣ
	 */
	private void updateUserVilleage(){
		final User user=userservice.getLastLoginUser();
		if(user!=null){
			LoadUserVilleageThread uvith=new LoadUserVilleageThread(user.getUser_id());
			uvith.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
  						if (data.result == NetResult.RESULT_OF_SUCCESS) {
 							List<UserVilleage> tuservils = (List<UserVilleage>) data.returnData;
 							if (tuservils != null) {
 								for (UserVilleage vil : tuservils) {
 										uservilleageservice.saveOrUpdate(vil);
 									if(vil.getVilleage()!=null)
 									   villeageservice.saveOrUpdate(vil.getVilleage());
 								}
 							  }  
 						} 
   						welcome();
 				}
			});
			uvith.start();
		}else{
			login();
		}
		
	}
	@Override
	protected boolean isExitActivity() {
 		return true;
	}
	
}
