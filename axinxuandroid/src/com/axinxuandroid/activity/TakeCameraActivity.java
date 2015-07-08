package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 

import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.view.CameraView;
import com.axinxuandroid.activity.view.CameraView.TakePictureListener;
import com.axinxuandroid.sys.gloable.Gloable;
 import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
 
public class TakeCameraActivity extends NcpZsActivity{
	private CameraView cv = null;
    private Button takecamera;
    private Button savebtn;
    private List<String> imgs;
    private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.takecamera);
  		cv=(CameraView) this.findViewById(R.id.takecamera_cameraview);
 		takecamera=(Button) this.findViewById(R.id.takecamera_take);
 		savebtn=(Button) this.findViewById(R.id.takecamera_save);
 		imgs=new ArrayList<String>();
 		takecamera.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				cv.takeCamera();
			}
		});
 		cv.setTakePictureListener(new TakePictureListener() {
 			@Override
			public void onSaved(String path) {
 				imgs.add(path);
			}
		});
 		savebtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 			 prepareDeal();
 			 cv.setVisibility(View.GONE);
 			}
		});
 	}
	
	public void prepareDeal(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				 if(result!=null)
 					progress=(ProgressDialog) ((Map)result).get("process");
 				dealImages();
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","处理图片中...."));
		
	}

	/**
	 * 处理拍摄图片角度问题
	 */
	private void dealImages(){
	   new Thread(){
 		@Override
		public void run() {
			if(imgs!=null&&imgs.size()>0){
				for(String img:imgs){
	 				BitmapUtils.rotaingImageView(img);
				}
			}
			dealFinish();
		}
 	   }.start();
 	}
   
	private void dealFinish(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				Session.getInstance().setAttribute(SessionAttribute.SESSION_TAKE_CAMERA_IMAGES, imgs);
 			    Intent aintent = new Intent(TakeCameraActivity.this, AddRecordActivity.class);
 	  			setResult(RESULT_OK,aintent);
 	 			TakeCameraActivity.this.finish();
			}
		});
	}

}
