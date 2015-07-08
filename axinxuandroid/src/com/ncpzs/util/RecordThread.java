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
	private MediaRecorder mediarecorder;// ¼����Ƶ����
	private SurfaceHolder surfaceHolder;
	private long recordTime;
	private SurfaceView surfaceview;// ��ʾ��Ƶ�Ŀؼ�
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
		 * ��ʼ¼��
		 */
		startRecord();

		/**
		 * ������ʱ�������涨ʱ��recordTime��ִ��ֹͣ¼������
		 */
		Timer timer = new Timer();

		timer.schedule(new TimerThread(), recordTime);
	}
	 
	/**
	 * ��ȡ����ͷʵ������
	 * 
	 * @return
	 */
	public Camera getCameraInstance() {
		Camera c = null;
		try {
 			c = Camera.open();
		} catch (Exception e) {
 			// ������ͷ����
			Log.i("info", "������ͷ����");
		}
		return c;
	}

	/**
	 * ��ʼ¼��
	 */
	public void startRecord() {
		
		mCamera = getCameraInstance();
		//�л�ǰ������ͷ
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//�õ�����ͷ�ĸ���
        int cameraPosition=1;
        for(int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//�õ�ÿһ������ͷ����Ϣ
            if(cameraPosition == 1) {
                //�����Ǻ��ã����Ϊǰ��
            	//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {  
                	if (mCamera!=null) {
                		mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
                		mCamera.release();//�ͷ���Դ
                		mCamera = null;//ȡ��ԭ������ͷ 
					}
                    mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ 1����ǰ������ͷ
                    try {
                        mCamera.setPreviewDisplay(surfaceHolder);//ͨ��surfaceview��ʾȡ������
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//��ʼԤ��
                    cameraPosition = 0;
                    break;
                }
            } else {
                //������ǰ�ã� ���Ϊ����
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����  
                    mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
                    mCamera.release();//�ͷ���Դ
                    mCamera = null;//ȡ��ԭ������ͷ
                    mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ
                    try {
                        mCamera.setPreviewDisplay(surfaceHolder);//ͨ��surfaceview��ʾȡ������
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//��ʼԤ��
                    cameraPosition = 1;
                    break;
                }
            }

        }
        
		mCamera.setDisplayOrientation(0);// ���������ʱ������ͷ��ת90�ȵ�����
		Camera.Parameters params=mCamera.getParameters();
		params.setPictureSize(640,480);// 640x480,320x240,176x144,160x120
		mCamera.setParameters(params);
		
		mCamera.unlock(); // ����camera
	    //1st. Initial state  // ��1��:������������ͷָ��MediaRecorder 
	    mediarecorder = new MediaRecorder();  
	    mediarecorder.setCamera(mCamera);  
	      
	    //2st. Initialized state    // ��2��:ָ��Դ  
	    mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);  
	    mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
	      
	    //3st. config  
	    // ��3��:ָ��CamcorderProfile(��ҪAPI Level 8���ϰ汾)  
	    // mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
	    //���ʹ��CamcorderProfile�����õĻ�����Ӧ��ע�����������ʽ����Ƶ���룬��Ƶ����3�����  
        // ��3��:���������ʽ�ͱ����ʽ(��Ե���API Level 8�汾) 
	    mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);    
        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
	    mediarecorder.setOutputFile("/sdcard/FBVideo.3gp");  
	    mediarecorder.setVideoSize(320,240);//������Ƶ�ֱ��ʣ��������Ҫ�����ô�start()��δ֪����
//	    mediarecorder.setVideoFrameRate(24);//������Ƶ֡��  ����Ұ���ȥ���ˣ��о�ûʲô��
	    mediarecorder.setVideoEncodingBitRate(10*1024*1024);//�������������֡Ƶ�ʣ�Ȼ��������� ,����˻���������������
	  
	    mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());  
	    try {
			// ׼��¼��
			mediarecorder.prepare();
			// ��ʼ¼��
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
	 * ֹͣ¼��
	 */
	public void stopRecord() {
		System.out.print("stopRecord()");
		surfaceview = null;
		surfaceHolder = null;
		if (mediarecorder != null) {
			// ֹͣ¼��
			mediarecorder.stop();
			mediarecorder.reset();
			// �ͷ���Դ
			mediarecorder.release();
			mediarecorder = null;

			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
		}
	}

	/**
	 * ��ʱ��
	 * 
	 * @author bcaiw
	 * 
	 */
	class TimerThread extends TimerTask {

		/**
		 * ֹͣ¼��
		 */
		@Override
		public void run() {
			stopRecord();
			this.cancel();
		}
	}

}
