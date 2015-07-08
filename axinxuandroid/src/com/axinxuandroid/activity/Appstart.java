package com.axinxuandroid.activity;




import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.net.CheckVersionThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.UploadVideoThread;
import com.axinxuandroid.activity.view.MenuWindow;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.oauth.YukuOAuth.YukuOAuthInfo;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.BaiDuLocation;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.axinxuandroid.sys.gloable.BaiDuLocation.GetBaiduLocationPositionListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.GPSUtil;
import com.ncpzs.util.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

  
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
 
public class Appstart  extends NcpZsActivity  {
	 
    @Override
	public void onCreate(Bundle savedInstanceState) {
 	    super.onCreate(savedInstanceState);	
 	    //this.setContentView(R.layout.welcome);
 	    Intent inte=new Intent(Appstart.this,WelcomeActivity.class);
// 	 	if(SharedPreferenceService.getAppStartCount()>0){
// 	 		inte=new Intent(Appstart.this,WelcomeActivity.class);
// 	 	}else{
//	 		inte=new Intent(Appstart.this,GuideAcitivity.class);
//	 	}
 	   MobclickAgent.updateOnlineConfig( Appstart.this );
  	 	startActivity(inte);
     }
   }