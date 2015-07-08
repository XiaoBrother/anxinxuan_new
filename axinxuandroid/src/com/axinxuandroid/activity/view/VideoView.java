package com.axinxuandroid.activity.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;
 
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

@SuppressLint("NewApi")
public class VideoView extends   SurfaceView implements SurfaceHolder.Callback {
	private MediaRecorder mediaRecorder;
 	private final int videoFramesPerSecond = 20;// ÿ��֡��
 	private boolean isrecording=false;
	private boolean isplaying=false;
	private MediaPlayer mediaPlayer;
 	public static final int PLAY_SUCESS=1;
	public static final int PLAY_FAILED_NO_FILE=-1;
	public static final int PLAY_FAILED_ERROR=-2;
	protected SurfaceHolder hodler;
	private static final int VIDEO_WIDTH=320;
	private static final int VIDEO_HEIGHT=240;
 	private VideoPlayFinishListener listener;
	private String thumbimgurl;
	private String videofilename;
	private Camera mCamera;
	public static final float  PREVIEWRATE=1.5f;
  	public VideoView(Context context) {
		super(context);
		initview();
	}

	public VideoView(Context context, AttributeSet sets) {
		super(context, sets);
		initview();
	}
 
	private void initview(){
		hodler=this.getHolder();//���SurfaceView�Ŀ�����   
		hodler.addCallback(this);//����Holder�Ļص��ӿ�   ,holder����ͨ������ص��ӿڿ���SurfaceView����ʾ����ˢ�µ���Ϊ   
 		hodler.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
 	}
	 
	/**
	 * ��ȡ����ͷʵ������
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void initCamera() throws IOException {
		int cameraCount = 0;
		cameraCount = Camera.getNumberOfCameras();//�õ�����ͷ�ĸ���
		//�л�ǰ������ͷ
//        CameraInfo cameraInfo = new CameraInfo();
         
//        int cameraPosition=1;
//        for(int i = 0; i < cameraCount; i++) {
//            Camera.getCameraInfo(i, cameraInfo);//�õ�ÿһ������ͷ����Ϣ
//            if(cameraPosition == 1) {
//                //�����Ǻ��ã����Ϊǰ��
//            	//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����
//                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {  
//                	if (mCamera!=null) {
//                		mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
//                		mCamera.release();//�ͷ���Դ
//                		mCamera = null;//ȡ��ԭ������ͷ 
//					}
//                    mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ 1����ǰ������ͷ
//                    try {
//                        mCamera.setPreviewDisplay(hodler);//ͨ��surfaceview��ʾȡ������
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    mCamera.startPreview();//��ʼԤ��
//                    cameraPosition = 0;
//                    break;
//                }
//            } else {
//                //������ǰ�ã� ���Ϊ����
//                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����  
//                    mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
//                    mCamera.release();//�ͷ���Դ
//                    mCamera = null;//ȡ��ԭ������ͷ
//                    mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ
//                    try {
//                        mCamera.setPreviewDisplay(hodler);//ͨ��surfaceview��ʾȡ������
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    mCamera.startPreview();//��ʼԤ��
//                    cameraPosition = 1;
//                    break;
//                }
//            }
//
//        }
         if(cameraCount>0){
        	//�򿪺�������ͷ
        	try {
     			mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
    		} catch (Exception e) {
    			// ������ͷ����
    			LogUtil.logInfo(getClass(), "������ͷ����");
    		}
        	if(mCamera==null) return ;
        	mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
     		mCamera.setPreviewDisplay(hodler);//ͨ��surfaceview��ʾȡ������
      		 // �������
            if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
      			//��2.2���Ͽ���ʹ��
            	mCamera.setDisplayOrientation(90);
    		}else{
     			//��2.2���Ͽ���ʹ��
    			mCamera.setDisplayOrientation(0);				
    		}
            Camera.Parameters params=mCamera.getParameters();
            params .set("orientation", "portrait"); 
            //params.set("rotation", 180);//���������Ƭ��ת90�㣬�����ĳ�������Ƭ����б��
    		//params.setPictureSize(640,480);// 640x480,320x240,176x144,160x120
    		Camera.Size bestsize=getClosestSize();
 			if( bestsize!=null){
 				   
					params.setPreviewSize(bestsize.width, bestsize.height);
 					//����ı���SIze�����ǻ�Ҫ����SurfaceView������Surface������ı��С������Camera��ͼ�������ܲ�
					LayoutParams lp=this.getLayoutParams();
 					lp.height=bestsize.height*2;
 					this.setLayoutParams(lp);
 					LogUtil.logInfo(getClass(),  Gloable.getInstance().getScreenWeight()+",width:"+bestsize.width+" ,height:"+bestsize.height);
			}
 			
     		mCamera.setParameters(params);
    		mCamera.startPreview();//��ʼԤ��
    		mCamera.autoFocus(null);// 
     		mCamera.unlock(); // ����camera
        }
      
 	}
	/**
	 * Ѱ����ӽ���Ļ��Ⱥ͸߶ȵ�size
	 * @return
	 */
	private Size getClosestSize(){
		 Camera.Size bestsize=null;
 		 Camera.Parameters params=mCamera.getParameters();
		 List<Camera.Size> sizeList = params.getSupportedPreviewSizes();
		 int srceenW=Gloable.getInstance().getScreenWeight();
		 if(sizeList.size() > 0){
				Iterator<Camera.Size> itor = sizeList.iterator();
				float rate=0;
				float bestrate=Integer.MAX_VALUE;
				while(itor.hasNext()){
					Camera.Size cur = itor.next();
					if(cur.width<srceenW){
						rate=(float) (cur.width*1.0/cur.height);
	 					if(bestsize!=null){
	 						bestrate=(float) (bestsize.width*1.0/bestsize.height);
						}
					    if(Math.abs(rate-PREVIEWRATE)<Math.abs(bestrate-PREVIEWRATE))
					    	bestsize=cur;
					}
 				}
				 
		}
	   LogUtil.logInfo(getClass(), "width:"+bestsize.width+" ,height:"+bestsize.height);

	   return bestsize;
	}
	private void initMediaRecorder() {
		if(mCamera!=null){
			mediaRecorder = new MediaRecorder();
 			// ��1��:������������ͷָ��MediaRecorder   
//	        if(camera!=null){
//	         	camera.unlock();//�ͷ�camera ������������ʹ��
//	         	mediaRecorder.setCamera(camera);
//	        }
	        // ��2��:ָ��Դ 
			mediaRecorder.setCamera(mCamera);
			
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			// ����¼����ɺ���Ƶ�ķ�װ��ʽTHREE_GPPΪ3gp.MPEG_4Ϊmp4
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediaRecorder.setVideoSize(VIDEO_WIDTH,VIDEO_HEIGHT);
			//mediaRecorder.setMaxDuration(maxDurationInMs);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			// ����¼�Ƶ���Ƶ����h263 h264
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
			FileUtil.createDirectory(AppConstant.VIDEO_DIR);
			videofilename=(new Date()).getTime()+".mp4";
			File tempFile = new File(AppConstant.VIDEO_DIR, videofilename);
			mediaRecorder.setOutputFile(tempFile.getPath());
			// ������Ƶ¼�Ƶķֱ��ʡ�����������ñ���͸�ʽ�ĺ��棬���򱨴�
			//mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
			
			// ����¼�Ƶ���Ƶ֡��
			//mediaRecorder.setVideoFrameRate(videoFramesPerSecond);

 			//mediaRecorder.setMaxFileSize(maxFileSizeInBytes);
			//mediaRecorder.setOrientationHint(90);
  	          
	 		mediaRecorder.setPreviewDisplay(hodler.getSurface());
	 		
		}
		
	}

	 

	public boolean startRecording() {
		try {
			if(!isrecording){
				isrecording=true;
				 initCamera();
				 initMediaRecorder();
 			    if(mediaRecorder!=null){
			    	// ׼��¼��
				    mediaRecorder.prepare();
					// ��ʼ¼��
				    mediaRecorder.start();
			    }
 				 
 			} 
 			return true;

		} catch (Exception e) {
			stopRecording();
			mediaRecorder = null;
			e.printStackTrace();
		}
		return false;
	}

	// ֹͣ����
	public void stopRecording() {
		
		if (mediaRecorder != null) {
			mediaRecorder.stop();
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			isrecording=false;
		}
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.lock();
			mCamera.release();
 			mCamera = null;
		}
	}
   /**
    * ����,����״̬��
    */
	public int startPlay(String path){
		mediaPlayer = new MediaPlayer(); 
 		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
		mediaPlayer.setDisplay(this.hodler); // ����һ��SurfaceView������ 
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() { 
			@Override 
			public void onCompletion(MediaPlayer arg0) { 
				stopPlay(); 
	 		} 
		}); 
	    try { 
	    	File file = new File(path);
	        if(file.exists()){
	        	mediaPlayer.setDataSource(file.getPath()); 
				mediaPlayer.prepare(); 
				mediaPlayer.start(); 
				isplaying=true;
				return PLAY_SUCESS;
	        }else{
	        	return PLAY_FAILED_NO_FILE;
	        }
			
		}  catch (Exception e) { 
 		    e.printStackTrace(); 
 		    return PLAY_FAILED_ERROR;
		} 
  	}
	
	 /**
	    * ����,����״̬��
	    */
		public int startNetPlay(String url){
			mediaPlayer = new MediaPlayer(); 
	 		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
			mediaPlayer.setDisplay(this.hodler); // ����һ��SurfaceView������ 
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() { 
				@Override 
				public void onCompletion(MediaPlayer arg0) { 
					stopPlay(); 
		 		} 
			}); 
		    try { 
  		        if(url!=null){
  		        	mediaPlayer.reset();  
  		            mediaPlayer.setDataSource(url);  
  		            mediaPlayer.prepare();
 					mediaPlayer.start(); 
					isplaying=true;
					return PLAY_SUCESS;
		        }else{
		        	return PLAY_FAILED_NO_FILE;
		        }
				
			}  catch (Exception e) { 
	 		    e.printStackTrace(); 
	 		    return PLAY_FAILED_ERROR;
			} 
	  	}
	public void pauseOrContinue(){
		if(mediaPlayer!=null){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
			}else mediaPlayer.start();
		}
 	}
	/**
	 * ֹͣ����
	 */
	public void stopPlay() { 
		if (mediaPlayer != null) { 
  			mediaPlayer.release(); 
			mediaPlayer = null; 
			isplaying=false;
		}
		if(this.listener!=null)
			this.listener.onPlayFinish();
	} 

 	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
 		stopRecording();
 		hodler = null; 
  	}

	public boolean isRecording() {
		return isrecording;
	}
	public boolean isPlaying() {
		return this.isplaying;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
     this.hodler=holder;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
 
		this.hodler=holder;
 	}

	/**
	 * ��ȡ��Ƶ����ͼ
	 * @return
	 */
	public Bitmap getVideoThumbnail() {
		if(videofilename==null) return null;
 		File fl=new File(AppConstant.VIDEO_DIR, videofilename);
 		if(fl.exists()&&fl.isFile()){
 			Bitmap bitmap = null;
 			bitmap=ThumbnailUtils.createVideoThumbnail(AppConstant.VIDEO_DIR+videofilename, Thumbnails.MICRO_KIND);
 			if(bitmap!=null){
 				bitmap = ThumbnailUtils.extractThumbnail(bitmap,VIDEO_WIDTH,  VIDEO_HEIGHT, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
 	 			this.thumbimgurl=AppConstant.VIDEO_DIR+(new Date()).getTime()+".jpg";
 				BitmapUtils.saveToFile(bitmap, thumbimgurl);
 				 
 			}
 	 	    return bitmap;
 		}
		return null;
    }
	
	public String getThumbnailImgUrl(){
		return this.thumbimgurl;
	}
	public String getVideoUrl(){
		if(videofilename==null) return null;
		return AppConstant.VIDEO_DIR+videofilename;
	}
	public void setVideoPlayFinishListener(VideoPlayFinishListener listener){
		this.listener=listener;
	}
	 public interface VideoPlayFinishListener{
		 public void onPlayFinish();
	 }

}
