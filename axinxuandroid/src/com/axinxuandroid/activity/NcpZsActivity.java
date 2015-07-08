package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.zip.Inflater;
  
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.data.LeadViewInfo;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;
import com.umeng.analytics.MobclickAgent;
 
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.Display;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
public class NcpZsActivity extends Activity {
	protected static final int RESULT_BACK=-999;
 	private NcpzsHandler handler;
 	private long pressbackftime=-1;
 	private TimerTask exittask = null;
 	private View leadrootview;
 	private LeadViewInfo leadinfo;
 	private Map<String,Bitmap> recbtms;
 	//ͨ���㲥�ķ�ʽ��������Ӧ�õ�����Activity
    private BroadcastReceiver finishAppBroadCastReceiver = new BroadcastReceiver(){
  		@Override
		public void onReceive(Context arg0, Intent arg1) {
			finish();
 		}
 	};
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		recbtms=new HashMap<String,Bitmap>();
		handler=new NcpzsHandler();
		 //ȡ������
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        //����
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
        	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        	 }
        //����ȫ��
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        initGloabelParam();
 	}
    
 	
 	
	@Override
	protected void onStart() {
		super.onStart();
		leadrootview=this.findViewById(R.id.lead_root_layout);
  		if(leadrootview!=null&&this.leadinfo!=null){
  			ViewParent parent=leadrootview.getParent();
  			if(parent instanceof FrameLayout){
   				int count=SharedPreferenceService.getLeadViewCount(this.getClass().getName());
  				if(count<1){
  					LayoutInflater inflater=LayoutInflater.from(this);
  	 				View leadview=inflater.inflate(R.layout.leadview, null);
   					((ImageView)leadview.findViewById(R.id.leadview_leadimg)).setBackgroundResource(leadinfo.imgresid);
  					((ImageView)leadview.findViewById(R.id.leadview_leadimg)).getBackground().setAlpha(100);
    				leadview.setOnTouchListener(new OnTouchListener() {
  	 					@Override
  						public boolean onTouch(View v, MotionEvent event) {
  	 						LogUtil.logInfo(getClass(), "touch");
  	 						if(event.getAction()==MotionEvent.ACTION_UP){
  	 							if(leadinfo.clickview.size()>0){
  	 								int[] xy=new int[2];
  	 								for(View view:leadinfo.clickview){
  	 									view.getLocationInWindow(xy);
  	 		  							float t_x=event.getX();
  	 									float t_y=	event.getY()+Gloable.getInstance().getStatusbarheight();
  	 									if(t_x>xy[0]&&t_x<(xy[0]+view.getWidth())&&t_y>xy[1]&&t_y<(xy[1]+view.getHeight())){
   	 										view.performClick();
  	 									}
  	 						        }
  	  	 						}
   	 						}
    	 					return true;
  						}
  					});
  					((FrameLayout)parent).addView(leadview);
  				}
  				
  			}
  		}	
 	}



	@Override
	protected void onResume() {
		pressbackftime=-1;
 		initGloabelParam();
		IntentFilter filter = new IntentFilter();  
	    filter.addAction(Gloable.EXIT_APP_SIGN);  
	    this.registerReceiver(finishAppBroadCastReceiver, filter);
		super.onResume();
		MobclickAgent.onResume(this);
	}

 
	@Override
	protected void onPause() {
 		super.onPause();
 		MobclickAgent.onPause(this);
	}



	private void initGloabelParam(){
		//LogUtil.logInfo(NcpZsActivity.class, "init");
		if(this!=Gloable.getInstance().getCurContext()){
			Gloable.getInstance().setCurContext(this);
			Gloable.getInstance().setCurHandler(handler);
		}
		 //��ʼ��һЩϵͳ����
		if(!Gloable.getInstance().hasInit()){
			 Display currDisplay = this.getWindowManager().getDefaultDisplay();//��ȡ��Ļ��ǰ�ֱ���
			 Gloable.getInstance().setScreenWeight(currDisplay.getWidth());
		 	 Gloable.getInstance().setScreenHeight(currDisplay.getHeight());
		 	 Gloable.getInstance().initAll();
		}
 		   
	}
 	 
   
	@Override
	protected void onDestroy() {
		this.unregisterReceiver(finishAppBroadCastReceiver);
		//�ͷ�ͼƬ��Դ
		if (this.recbtms != null && this.recbtms.size() > 0) {
			Iterator its=recbtms.entrySet().iterator();
			while(its.hasNext()){
				Entry ent=(Entry) its.next();
				Bitmap bm =(Bitmap) ent.getValue();
				if (bm != null && !bm.isRecycled()) {
 					bm.recycle();
				}
			}
			this.recbtms.clear();
   		}
 		super.onDestroy();
	}
   
	
  /**
   * ��ſɻ���ͼƬ�������˳�ʱ���Զ��ͷ���ԴͼƬ
   * @param key
   * @param btm
   */
   protected void addRecycleImg(String key,Bitmap btm){
	   this.recbtms.put(key, btm);
   }
   /**
    * ��ȡ�ɻ���ͼƬ
    * @param key
    * @param btm
    * @return
    */
   protected Bitmap getRecycleImg(String key){
	   return this.recbtms.get(key);
   }
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode=event.getKeyCode();
         switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
            	 if(isExitActivity()){
            		 Gloable.getInstance().exitApplication();
            	 }else{
            		 if(event.isLongPress()) {  //�ж��¼��Ƿ��ǳ����¼�
                      	handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
       	 				@Override
       					public void onHandlerFinish(Object result) {
       	 					Map rdata=(Map) result;
       	 					if(rdata!=null){
       	 						int select=(Integer)rdata.get("result");
       	 						if(select==1)
       	 							Gloable.getInstance().exitApplication();
       	 					}
       					}
       				});
       				handler.excuteMethod(new ConfirmDialogHandlerMethod("�˳�", "ȷ��Ҫ�˳�����ѡ��"));
                      return true;
                   }else if( event.getAction() == KeyEvent.ACTION_DOWN){//ֻ�е��ɿ���ʱ��������ᱻ��������
                 	       if(pressbackftime>0){
                         		 long secondTime = System.currentTimeMillis(); 
                         		 LogUtil.logInfo(getClass(), "time:"+pressbackftime+":"+secondTime); 
                         		 if(secondTime-pressbackftime<300){
                         			 //�ж�Ϊ������������
                         			 Gloable.getInstance().exitApplication();
                         		 }else{
                         			 pressbackftime=-1;
                         		 }
                         	 }else{
                         		 //��һ�ΰ�
                         		 pressbackftime=System.currentTimeMillis(); 
                         		 //���û�1.5����û�����һ�Σ���finish��ǰactivity
                         		 if(exittask==null){
                         			exittask=new TimerTask() {
 										@Override
										public void run() {
 											finish();
										}
									};
									Timer timer = new Timer();
									timer.schedule(exittask, 500);
									Toast.makeText(getApplicationContext(), "���������˳���",
										     Toast.LENGTH_SHORT).show(); 
                         		 };
                          		 return true;
                         	 }
                	       
                             return super.dispatchKeyEvent(event);  //������ǳ����������ԭ�з�����ִ�а���back��Ӧ�еĴ���
                        
                    }
                 
            	 }
            	
           default:
             break;
        }
        return super.dispatchKeyEvent(event);
 	}
	protected boolean isExitActivity(){
		return false;
	}
	
	protected void setLeadViewInfo(LeadViewInfo info){
		this.leadinfo=info;
	}
 
}
