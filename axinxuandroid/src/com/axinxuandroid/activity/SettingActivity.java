package com.axinxuandroid.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.view.CommonBottomView;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
   * @author hubobo
 *
 */
public class SettingActivity extends NcpZsActivity{
  
	private RelativeLayout setting_grxx;//个人信息
	private RelativeLayout setting_tbmb;//模板同步
	private RelativeLayout setting_draft;//草稿箱
	private RelativeLayout setting_clean;//清空记录
 	private RelativeLayout setting_update;//系统更新
 	private RelativeLayout setting_scfs;//上传方式
 	private RelativeLayout setting_userfavorite;//用户收藏
 	private RelativeLayout setting_visithistory;//过往查询
 	private RelativeLayout setting_resetpwd;//重设密码
 	private RelativeLayout setting_usercomment;//用户评论
 	private RelativeLayout setting_anti;//防伪码
	private ImageView userimg,recordnotice,versionotice;
	private TextView draftnotice;
	private TextView username;
	private TextView versiontext;
	private Button exit;//退出登录
	private UserService uservice;
	private RecordService recordservice;
	private SystemNoticeService noticeservice;
	private User user;
	private SystemNotice versionnotice;
	private CommonBottomView bottomview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.main_tab_settings);
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		recordservice=new RecordService();
 		noticeservice=new SystemNoticeService();
 		setting_grxx=(RelativeLayout) this.findViewById(R.id.setting_grxx);
        setting_tbmb=(RelativeLayout) this.findViewById(R.id.setting_tbmb);
        setting_draft=(RelativeLayout) this.findViewById(R.id.setting_draft);
        setting_clean=(RelativeLayout) this.findViewById(R.id.setting_clean);
        setting_update=(RelativeLayout) this.findViewById(R.id.setting_checkversion);
        setting_scfs=(RelativeLayout) this.findViewById(R.id.setting_scfs);
        setting_visithistory=(RelativeLayout) this.findViewById(R.id.setting_visithistory);
        setting_userfavorite=(RelativeLayout) this.findViewById(R.id.setting_userfavorite);
        setting_resetpwd=(RelativeLayout) this.findViewById(R.id.setting_resetpwd);
        setting_usercomment=(RelativeLayout) this.findViewById(R.id.setting_usercomment);
        setting_anti=(RelativeLayout) this.findViewById(R.id.setting_anti);
        
        //如果登录账号没有创建农场则隐藏防伪管理
         Villeage v= uservice.getUserCreateVilleage(user.getUser_id());
         if(v !=null){
        	 setting_anti.setVisibility(View.VISIBLE);
         }
        
        exit=(Button) this.findViewById(R.id.setting_exit);
        username=(TextView) this.findViewById(R.id.setting_username);
        userimg=(ImageView) this.findViewById(R.id.setting_userimg);
        recordnotice=(ImageView) this.findViewById(R.id.setting_tbmb_notice);
        versionotice=(ImageView) this.findViewById(R.id.setting_checkversion_notice);
        draftnotice=(TextView) this.findViewById(R.id.setting_draft_notice);
        versiontext=(TextView) this.findViewById(R.id.setting_versiontext);
        bottomview=(CommonBottomView) this.findViewById(R.id.setting_bottom);
        bottomview.setCurrentItem(CommonBottomView.BOTTOM_ITEM_INDEX_OF_MORE);
      
        setting_grxx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(user!=null){
            		Intent openCameraIntent = new Intent(SettingActivity.this,UserPageActivity.class);
            	    openCameraIntent.putExtra("userid", user.getUser_id());
         			startActivity(openCameraIntent);
              	}
             }
        });
        setting_tbmb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	toIntent(TemplateSynchroniseActivity.class,null);
             }
        });
        
        setting_draft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	toIntent(DraftActivity.class,null);
             }
        });
        setting_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	toIntent(CleanActivity.class,null);
             }
        });
        setting_update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	toIntent(AboutAppActivity.class,null);
            	//systemUpdate();
             }
        });
        exit.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				exit();
			}
		});
        setting_userfavorite.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				toIntent(UserFovoriteActivity.class,null);
			}
		});
        setting_visithistory.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				toIntent(VisitHistoryActivity.class,null);
			}
		});
        setting_scfs.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				toIntent(SytemSetActivity.class,null);
			}
		});
        setting_resetpwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toIntent(ResetPwdActivity.class,null);
			}
		});
        setting_usercomment.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				toIntent(UserCommentActivity.class,null);				
			}
		});
        setting_anti.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		toIntent(AntiFakeActivity.class,null);				
        	}
        });
         if(user!=null){
        	username.setText(user.getUser_name());
        	if(user.getLocal_imgurl()!=null)
        	userimg.setImageBitmap(BitmapUtils.getImageBitmap(user.getLocal_imgurl()));
         }
         initState();
         
	}
	private void initState(){
		 if(noticeservice.getByType(SystemNoticeType.NOTICE_TYPE_TEMPLATE)!=null) 
         	recordnotice.setVisibility(View.VISIBLE);
		 else recordnotice.setVisibility(View.GONE);
		 versionnotice=noticeservice.getByType(SystemNoticeType.NOTICE_TYPE_VERSION);
        if(versionnotice!=null){
       	 float curversion=SharedPreferenceService.getVersion();
       	 try{
       		 JSONObject jsonObject = new JSONObject(versionnotice.getJsondata());
    			 double version=jsonObject.getDouble("newversion");
    			 if(curversion<version)
    				 versionotice.setVisibility(View.VISIBLE);
    			 else{
    				 //已经是最新的
    				noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_VERSION);
    			 }
       	 }catch(Exception ex){
       		 ex.printStackTrace();
       	 }
         }
          else versionotice.setVisibility(View.GONE);
        List<Record>  reds=recordservice.getDraftRecords(user.getUser_id());
        if(reds!=null){
       	 draftnotice.setVisibility(View.VISIBLE);
       	 draftnotice.setText(reds.size()+"");
        }else draftnotice.setVisibility(View.GONE);
        //获取系统版本
       //float version= SharedPreferenceService.getVersion();
       //versiontext.setText("当前版本"+version+"");
	}
	
	public void systemUpdate(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		if(versionnotice==null){
			handler.excuteMethod(new MessageDialogHandlerMethod("", "现在已经是最新版本！"));
		}else{
			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
				@Override
				public void onHandlerFinish(Object result) {
					int usersel=(Integer)((Map)result).get("result");
					if(usersel==1){
						try{
							 JSONObject jsonObject = new JSONObject(versionnotice.getJsondata());
							 String url=jsonObject.getString("downurl");
							 double version=jsonObject.getDouble("newversion");
	 						 Intent upint=new Intent("com.axinxuandroid.backservice.SystemUpdateService");
							 upint.putExtra("downurl", url);
							 upint.putExtra("version", version);
							 startService(upint);//下载系统
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					
				}
			});
			handler.excuteMethod(new ConfirmDialogHandlerMethod("", "是否下载最新版本？"));
		}
	}
	private void toIntent(Class clz,Map<String,String> params){
 		Intent inte=new Intent(SettingActivity.this,clz);
 		if(params!=null&&params.size()>0){
 			Iterator its=params.entrySet().iterator();
 			while(its.hasNext()){
 				Entry ent=(Entry) its.next();
 				inte.putExtra((String)ent.getKey(), (String)ent.getValue());
 			}
 		}
 		startActivity(inte);
  	}
 	
    /**
     * 用户退出登录
     */
 	private void exit(){
  			//LogUtil.logInfo(getClass(), user.getUservilleages().size()+"");
 		Date date=new Date();
 		Calendar cl=Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.DAY_OF_MONTH, -10);
 		user.setLogin_time(DateUtil.dateToStrWithPattern(cl.getTime(), "yyyy-MM-dd HH:mm:ss"));
		SharedPreferenceService.saveLastLoginUser(user);
		toIntent(Appstart.class,null);
 	}
	@Override
	protected void onResume() {
		initState();
		super.onResume();
	}
	
}
