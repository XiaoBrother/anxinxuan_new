package com.axinxuandroid.activity;

 
  
import java.io.File;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.sys.gloable.Gloable;
import com.example.recorddemo.MyProgressBar;
import com.example.recorddemo.RecordVideoView;
import com.example.recorddemo.RecordVideoView.OnRecordEndListener;
import com.ncpzs.util.BitmapUtils;

 
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera.CameraInfo;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TakeVideoActivity extends  NcpZsActivity {
 	private RecordVideoView videoview;
 	private Button startbtn, restart,okbtn;
 	private static final  int MAX_TIME=6000;//最大录制时间15s
   	private ProgressDialog dialog;
 	private MyProgressBar pbar;
 	private String videoSavePath;
 	private String savetime;
 	private String thumbimgurl;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.takevideo);
  		 
    	videoview=(RecordVideoView) this.findViewById(R.id.takevideo_videoview);
  		videoview.setMaxTime(MAX_TIME);
  		videoview.setOnRecordEndListener(new OnRecordEndListener() {
 			@Override
			public void onEnd() {
 				startbtn.setVisibility(View.GONE);
				restart.setVisibility(View.VISIBLE);
			}

			@Override
			public void recording(long totaltime) {
				pbar.setProgress((int)(totaltime*1.0/MAX_TIME*100));
			}
		});
 		startbtn=(Button) this.findViewById(R.id.takevideo_start);
 		restart=(Button) this.findViewById(R.id.takevideo_restart);
 		okbtn=(Button) this.findViewById(R.id.takevideo_ok);
 		pbar=(MyProgressBar) this.findViewById(R.id.takevideo_progressBar);
 		//videoview.startPreview();
  		startbtn.setOnTouchListener(new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if (event.getAction() == MotionEvent.ACTION_UP) {
 					stop(true);
  				}else if (event.getAction() == MotionEvent.ACTION_DOWN) {
  					if(!videoview.isRecording())
  					   videoview.startVideoRecoder();
   				}
 				return false;
			}
		});
  		restart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				prepare();
			}
		});
  		okbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				reback();
			}
		});
  		savetime=System.currentTimeMillis()+"";
  		videoSavePath=AppConstant.VIDEO_DIR+savetime+".mp4";
  		videoview.setVideoFilePath(videoSavePath);
	}
	public void prepare(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			
			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
 					dialog=(ProgressDialog) ((Map)result).get("process");
 				new AsyncTask<Object, Integer, Boolean>(){

 					@Override
 					protected Boolean doInBackground(Object... params) {
 		  				videoview.preapre();
 		 				return true;
 					}
 					
 					@Override
 					protected void onPostExecute(Boolean result) {
 		 				DisplayMetrics dm = new DisplayMetrics();   
 					    dm =  getApplicationContext().getResources().getDisplayMetrics();
 						LayoutParams params=videoview.getLayoutParams();
 						float scale= (float) (dm.widthPixels*1.0/videoview.getPreviewWidth());
 						params.height=(int) (videoview.getPreviewHeight()*scale);
 						videoview.setLayoutParams(params);
 						if(dialog!=null)
 							dialog.dismiss();
 						okbtn.setVisibility(View.GONE);
 						restart.setVisibility(View.GONE);
 						startbtn.setVisibility(View.VISIBLE);
 		 				
 		   			}
 					
 				}.execute();
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","请稍等......"));
 		
	}
	private void stop(boolean initstatus){
		    videoview.stopRecorder();
		    if(initstatus){
		    	startbtn.setVisibility(View.GONE);
				okbtn.setVisibility(View.VISIBLE);
				restart.setVisibility(View.VISIBLE);
		    }
			
	}

	@Override
	protected void onResume() {
		prepare();
 		super.onResume();
	}
	@Override
	protected void onPause() { 
	     stop(false);
		super.onPause();
	}
	 
	/**
	 * 获取视频缩略图
	 * @return
	 */
//	public String getVideoThumbnail() {
//  		File fl=new File(videoSavePath);
// 		if(fl.exists()&&fl.isFile()){
// 			Bitmap bitmap = null;
// 			bitmap=ThumbnailUtils.createVideoThumbnail(videoSavePath, Thumbnails.MICRO_KIND);
// 			if(bitmap!=null){
// 				bitmap = ThumbnailUtils.extractThumbnail(bitmap, videoview.getPreviewWidth(), videoview.getPreviewHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
// 				if(bitmap!=null){
// 					this.thumbimgurl=savetime+".jpg";
// 	 				BitmapUtils.saveToFile(bitmap, thumbimgurl);
// 	 				bitmap.recycle();
// 	 				return videoSavePath+thumbimgurl;
// 				}
//  			}
// 	 	    return null;
// 		}
//		return null;
//    }
	/**
	 * 返回到上一个activity
	 */
	public void reback(){
 		 Intent aintent = new Intent(TakeVideoActivity.this, AddRecordActivity.class);
 		 aintent.putExtra("videourl", videoSavePath);
  		 aintent.putExtra("thumburl", videoview.getVideoImgPath());
  		 aintent.putExtra("time", videoview.getTotalTime()/1000);
	     setResult(RESULT_OK,aintent);
	     TakeVideoActivity.this.finish();
	}
}
