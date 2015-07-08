package com.axinxuandroid.activity;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.SlipButton;
import com.axinxuandroid.activity.view.SlipButton.OnChangedListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.SystemSet;
import com.axinxuandroid.data.SystemSet.SystemSetType;
import com.axinxuandroid.service.SystemSetService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author Administrator
 *
 */
public class SytemSetActivity  extends NcpZsActivity{

	private SlipButton imgwifi,audiowifi,videowifi;
 	private SystemSetService setservice;
	private SystemSet set;
	private CommonTopView topview;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.systemset);
 		imgwifi=(SlipButton) this.findViewById(R.id.systemset_imagewifi);
 		audiowifi=(SlipButton) this.findViewById(R.id.systemset_audiowifi);
 		videowifi=(SlipButton) this.findViewById(R.id.systemset_videowifi);
 		topview=(CommonTopView) this.findViewById(R.id.systemset_topview);
 		videowifi.setCheck(true);
 		setservice=new SystemSetService();
 		set=setservice.getByType(SystemSetType.SYSTEMSETTYPE_COMMONTYPE);
 		if(set!=null){
 			String imgset=set.getSetValue(SystemSetType.CommonType.IMAGE_WIFI);
 			if(imgset!=null&&SystemSetType.TRUE_VALUE.equals(imgset))
 				imgwifi.setCheck(true);
 			String audioset=set.getSetValue(SystemSetType.CommonType.AUDIO_WIFI);
 			if(audioset!=null&&SystemSetType.TRUE_VALUE.equals(audioset))
 				audiowifi.setCheck(true);
 			String videoset=set.getSetValue(SystemSetType.CommonType.VIDEO_WIFI);
 			if(videoset!=null&&SystemSetType.FALSE_VALUE.equals(videoset))
 				videowifi.setCheck(false);
 			String showshareset=set.getSetValue(SystemSetType.CommonType.SHOW_SHARE);
 			
 			//LogUtil.logInfo(getClass(),imgset+ ":"+audioset+":"+videoset);
 		}
  
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				SytemSetActivity.this.finish();
			}
		});
 		imgwifi.SetOnChangedListener(new OnChangedListener() {
 			@Override
			public void OnChanged(boolean CheckState) {
 				saveSet();
			}
 			 
		});
 		audiowifi.SetOnChangedListener(new OnChangedListener() {
 			@Override
			public void OnChanged(boolean CheckState) {
 				saveSet();
			}
 			 
		});
 		videowifi.SetOnChangedListener(new OnChangedListener() {
 			@Override
			public void OnChanged(boolean CheckState) {
 				saveSet();
			}
 			 
		});
 		 
	}
   
 	private void saveSet(){
 	   if(set==null){
 		  set=new SystemSet();
  	   }
 	   set.setType(SystemSetType.SYSTEMSETTYPE_COMMONTYPE);
   	   if(imgwifi.getPosition()==SlipButton.RIGHT_POSITION){
  		   set.addSet(SystemSetType.CommonType.IMAGE_WIFI, SystemSetType.TRUE_VALUE);
 	   }else set.addSet(SystemSetType.CommonType.IMAGE_WIFI, SystemSetType.FALSE_VALUE);
  	   if(audiowifi.getPosition()==SlipButton.RIGHT_POSITION){
		   set.addSet(SystemSetType.CommonType.AUDIO_WIFI, SystemSetType.TRUE_VALUE);
	   }else set.addSet(SystemSetType.CommonType.AUDIO_WIFI, SystemSetType.FALSE_VALUE);
  	   if(videowifi.getPosition()==SlipButton.RIGHT_POSITION){
		   set.addSet(SystemSetType.CommonType.VIDEO_WIFI, SystemSetType.TRUE_VALUE);
	   }else set.addSet(SystemSetType.CommonType.VIDEO_WIFI, SystemSetType.FALSE_VALUE);
   	   setservice.saveOrUpdate(set);
   	}
}
