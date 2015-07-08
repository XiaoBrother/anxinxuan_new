package com.ncpzs.util;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("NewApi")
public class RecordThread extends Thread {
	private MediaRecorder mediarecorder;// 录制视频的类
	private SurfaceHolder surfaceHolder;
	private long recordTime;
	private SurfaceView surfaceview;// 显示视频的控件
	public Camera mCamera;

	public RecordThread(long recordTime, SurfaceView surfaceview,
			SurfaceHolder surfaceHolder) {
		this.recordTime = recordTime;
		this.surfaceview = surfaceview;
		this.surfaceHolder = surfaceHolder;
	}

	@Override
	public void run() {

		/**
		 * 开始录像
		 */
		startRecord();

		/**
		 * 启动定时器，到规定时间recordTime后执行停止录像任务
		 */
		Timer timer = new Timer();

		timer.schedule(new TimerThread(), recordTime);
	}
	 
	/**
	 * 获取摄像头实例对象
	 * 
	 * @return
	 */
	public Camera getCameraInstance() {
		Camera c = null;
		try {
 			c = Camera.open();
		} catch (Exception e) {
 			// 打开摄像头错误
			Log.i("info", "打开摄像头错误");
		}
		return c;
	}

	/**
	 * 开始录像
	 */
	public void startRecord() {
		
		mCamera = getCameraInstance();
		//切换前后摄像头
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        int cameraPosition=1;
        for(int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if(cameraPosition == 1) {
                //现在是后置，变更为前置
            	//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {  
                	if (mCamera!=null) {
                		mCamera.stopPreview();//停掉原来摄像头的预览
                		mCamera.release();//释放资源
                		mCamera = null;//取消原来摄像头 
					}
                    mCamera = Camera.open(i);//打开当前选中的摄像头 1代表前置摄像头
                    try {
                        mCamera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }

        }
        
		mCamera.setDisplayOrientation(0);// 解决竖屏的时候，摄像头旋转90度的问题
		Camera.Parameters params=mCamera.getParameters();
		params.setPictureSize(640,480);// 640x480,320x240,176x144,160x120
		mCamera.setParameters(params);
		
		mCamera.unlock(); // 解锁camera
	    //1st. Initial state  // 第1步:解锁并将摄像头指向MediaRecorder 
	    mediarecorder = new MediaRecorder();  
	    mediarecorder.setCamera(mCamera);  
	      
	    //2st. Initialized state    // 第2步:指定源  
	    mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);  
	    mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
	      
	    //3st. config  
	    // 第3步:指定CamcorderProfile(需要API Level 8以上版本)  
	    // mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
	    //如果使用CamcorderProfile做配置的话，就应该注释设置输出格式，音频编码，视频编码3条语句  
        // 第3步:设置输出格式和编码格式(针对低于API Level 8版本) 
	    mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);    
        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
	    mediarecorder.setOutputFile("/sdcard/FBVideo.3gp");  
	    mediarecorder.setVideoSize(320,240);//设置视频分辨率，这里很重要，设置错start()报未知错误
//	    mediarecorder.setVideoFrameRate(24);//设置视频帧率  这个我把它去掉了，感觉没什么用
	    mediarecorder.setVideoEncodingBitRate(10*1024*1024);//在这里我提高了帧频率，然后就清晰了 ,解决了花屏、绿屏的问题
	  
	    mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());  
	    try {
			// 准备录制
			mediarecorder.prepare();
			// 开始录制
			mediarecorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 停止录制
	 */
	public void stopRecord() {
		System.out.print("stopRecord()");
		surfaceview = null;
		surfaceHolder = null;
		if (mediarecorder != null) {
			// 停止录制
			mediarecorder.stop();
			mediarecorder.reset();
			// 释放资源
			mediarecorder.release();
			mediarecorder = null;

			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
		}
	}

	/**
	 * 定时器
	 * 
	 * @author bcaiw
	 * 
	 */
	class TimerThread extends TimerTask {

		/**
		 * 停止录像
		 */
		@Override
		public void run() {
			stopRecord();
			this.cancel();
		}
	}

}
