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
 	private final int videoFramesPerSecond = 20;// 每秒帧数
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
		hodler=this.getHolder();//获得SurfaceView的控制器   
		hodler.addCallback(this);//设置Holder的回调接口   ,holder将会通过这个回调接口控制SurfaceView的显示或者刷新等行为   
 		hodler.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
 	}
	 
	/**
	 * 获取摄像头实例对象
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void initCamera() throws IOException {
		int cameraCount = 0;
		cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
		//切换前后摄像头
//        CameraInfo cameraInfo = new CameraInfo();
         
//        int cameraPosition=1;
//        for(int i = 0; i < cameraCount; i++) {
//            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
//            if(cameraPosition == 1) {
//                //现在是后置，变更为前置
//            	//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {  
//                	if (mCamera!=null) {
//                		mCamera.stopPreview();//停掉原来摄像头的预览
//                		mCamera.release();//释放资源
//                		mCamera = null;//取消原来摄像头 
//					}
//                    mCamera = Camera.open(i);//打开当前选中的摄像头 1代表前置摄像头
//                    try {
//                        mCamera.setPreviewDisplay(hodler);//通过surfaceview显示取景画面
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    mCamera.startPreview();//开始预览
//                    cameraPosition = 0;
//                    break;
//                }
//            } else {
//                //现在是前置， 变更为后置
//                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
//                    mCamera.stopPreview();//停掉原来摄像头的预览
//                    mCamera.release();//释放资源
//                    mCamera = null;//取消原来摄像头
//                    mCamera = Camera.open(i);//打开当前选中的摄像头
//                    try {
//                        mCamera.setPreviewDisplay(hodler);//通过surfaceview显示取景画面
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    mCamera.startPreview();//开始预览
//                    cameraPosition = 1;
//                    break;
//                }
//            }
//
//        }
         if(cameraCount>0){
        	//打开后置摄像头
        	try {
     			mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
    		} catch (Exception e) {
    			// 打开摄像头错误
    			LogUtil.logInfo(getClass(), "打开摄像头错误");
    		}
        	if(mCamera==null) return ;
        	mCamera.stopPreview();//停掉原来摄像头的预览
     		mCamera.setPreviewDisplay(hodler);//通过surfaceview显示取景画面
      		 // 解决竖屏
            if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
      			//在2.2以上可以使用
            	mCamera.setDisplayOrientation(90);
    		}else{
     			//在2.2以上可以使用
    			mCamera.setDisplayOrientation(0);				
    		}
            Camera.Parameters params=mCamera.getParameters();
            params .set("orientation", "portrait"); 
            //params.set("rotation", 180);//这个是让照片旋转90°，否则拍出来的照片是倾斜的
    		//params.setPictureSize(640,480);// 640x480,320x240,176x144,160x120
    		Camera.Size bestsize=getClosestSize();
 			if( bestsize!=null){
 				   
					params.setPreviewSize(bestsize.width, bestsize.height);
 					//这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
					LayoutParams lp=this.getLayoutParams();
 					lp.height=bestsize.height*2;
 					this.setLayoutParams(lp);
 					LogUtil.logInfo(getClass(),  Gloable.getInstance().getScreenWeight()+",width:"+bestsize.width+" ,height:"+bestsize.height);
			}
 			
     		mCamera.setParameters(params);
    		mCamera.startPreview();//开始预览
    		mCamera.autoFocus(null);// 
     		mCamera.unlock(); // 解锁camera
        }
      
 	}
	/**
	 * 寻找最接近屏幕宽度和高度的size
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
 			// 第1步:解锁并将摄像头指向MediaRecorder   
//	        if(camera!=null){
//	         	camera.unlock();//释放camera 允许其他进程使用
//	         	mediaRecorder.setCamera(camera);
//	        }
	        // 第2步:指定源 
			mediaRecorder.setCamera(mCamera);
			
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediaRecorder.setVideoSize(VIDEO_WIDTH,VIDEO_HEIGHT);
			//mediaRecorder.setMaxDuration(maxDurationInMs);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			// 设置录制的视频编码h263 h264
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
			FileUtil.createDirectory(AppConstant.VIDEO_DIR);
			videofilename=(new Date()).getTime()+".mp4";
			File tempFile = new File(AppConstant.VIDEO_DIR, videofilename);
			mediaRecorder.setOutputFile(tempFile.getPath());
			// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
			//mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
			
			// 设置录制的视频帧率
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
			    	// 准备录制
				    mediaRecorder.prepare();
					// 开始录制
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

	// 停止拍摄
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
    * 播放,返回状态码
    */
	public int startPlay(String path){
		mediaPlayer = new MediaPlayer(); 
 		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
		mediaPlayer.setDisplay(this.hodler); // 定义一个SurfaceView播放它 
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
	    * 播放,返回状态码
	    */
		public int startNetPlay(String url){
			mediaPlayer = new MediaPlayer(); 
	 		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
			mediaPlayer.setDisplay(this.hodler); // 定义一个SurfaceView播放它 
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
	 * 停止播放
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
	 * 获取视频缩略图
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
