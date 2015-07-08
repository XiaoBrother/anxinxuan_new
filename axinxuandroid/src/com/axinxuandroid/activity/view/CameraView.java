package com.axinxuandroid.activity.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import com.anxinxuandroid.constant.AppConstant;
import com.ncpzs.util.LogUtil;

import android.R.integer;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
 
/**
 * 
 * 这个例子就来定义一个自己的Camera
 * 首先，在Manifest中需要引入权限<uses-permission android:name="android:permission.CAMERA"/>
 * 我们需要用来存放取景器的容器，这个容器就是SurfaceView。
 * 使用SurfaceView的同时，我们还需要使用到SurfaceHolder，SurfaceHolder相当于一个监听器，可以监听
 * Surface上的变化,通过其内部类CallBack来实现。
 * 为了可以获取图片，我们需要使用Camera的takePicture方法同时我们需要实现Camera.PictureCallBack类，实现onPictureTaken方法
 * @author Administrator
 *
 */

public class CameraView  extends SurfaceView implements SurfaceHolder.Callback,Camera.PictureCallback{
	protected SurfaceHolder hodler;
    protected Camera camera; //这个是hardare的Camera对象
     protected boolean isruning=false;
    protected TakePictureListener listener;
    public static final int MAX_WIDTH=1200;
    public static final int MAX_HEIGHT=1200;
 	public CameraView(Context context) {
		super(context);
		initview(context);
 	}
	public CameraView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}
	private void initview(Context context){
		hodler=this.getHolder();//获得SurfaceView的控制器   
		hodler.addCallback(this);//设置Holder的回调接口   ,holder将会通过这个回调接口控制SurfaceView的显示或者刷新等行为   
        //SURFACE_TYPE_PUSH_BUFFERS表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，
		//在Camera图像预览中就使用该类型的Surface，
		//有Camera负责提供给预览Surface数据，这样图像预览会比较流畅
		hodler.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
//		this.setOnClickListener(new OnClickListener() {
// 			@Override
//			public void onClick(View v) {
//  				takeCamera();
// 			}
//		});
		LogUtil.logInfo(getClass(), "init ....");
	}
	/**
	 * 拍照
	 */
 	public void takeCamera(){
 		//这三个参数分别是三个回调函数   
         //shutter表示 按下快门时 要执行的操作   
         //raw 表示要把图片保存成raw格式时所作的操作   
         //jpeg表示要把图片保存成jpg格式   
 		if(camera!=null){
 			camera.takePicture(null, null, CameraView.this);
 			startCamera();
 		}
 		
 	}
 	/**
 	 * 开启照相
 	 */
 	public void startCamera(){
  		if( camera!=null)  {  
             //如果已经处于预览状态  需要先结束 再启动   
 			if(isruning)
              camera.stopPreview();  
            camera.startPreview();
            isruning=true;
            LogUtil.logInfo(getClass(), "start ....");
        }  
  	}
 	/**
	 * 初始化一些参数
	 */
	private void initCamera(){
		if(camera!=null){
			Camera.Parameters param = camera.getParameters();
			param.set("rotation", 90);//这个是让照片旋转90°，否则拍出来的照片是倾斜的
	 		int bestWidth = 0;
			int bestHeight = 0;
 			List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
			//如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
			if(sizeList.size() > 1){
				Iterator<Camera.Size> itor = sizeList.iterator();
				while(itor.hasNext()){
					Camera.Size cur = itor.next();
					if(cur.width > bestWidth && cur.height>bestHeight && cur.width <MAX_WIDTH && cur.height < MAX_HEIGHT){
						bestWidth = cur.width;
						bestHeight = cur.height;
					}
				}
				if(bestWidth != 0 && bestHeight != 0){
					param.setPreviewSize(bestWidth, bestHeight);
					//这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
					 
					//this.setLayoutParams(new LinearLayout.LayoutParams(bestWidth, bestHeight));
				}
			}
			LogUtil.logInfo(getClass(), "width:"+bestWidth+",height:"+bestHeight);
			camera.setParameters(param);
		}
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		LogUtil.logInfo(getClass(), "change ....");
	}
	 
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open(); //打开摄像头，获取Camera实例
		if(camera!=null){
			try{
 	 			if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
					//如果是竖屏
					//param.set("orientation", "portrait");
					//在2.2以上可以使用
					camera.setDisplayOrientation(90);
				}else{
					//param.set("orientation", "landscape");
					//在2.2以上可以使用
					camera.setDisplayOrientation(0);				
				}
 	  	 		camera.setPreviewDisplay(holder);
 	  	 		/**
				 * 在显示了预览后，我们有时候希望限制预览的Size
				 * 我们并不是自己指定一个SIze而是指定一个Size，然后
				 * 获取系统支持的SIZE，然后选择一个比指定SIZE小且最接近所指定SIZE的一个
				 * Camera.Size对象就是该SIZE。
				 * 
				 */
 	 			initCamera();
	  	 		startCamera();
			}catch(Exception ex){
				  camera.release();
	              camera = null;
	              ex.printStackTrace();
			}
 		}
		LogUtil.logInfo(getClass(), "create ....");
 	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(camera!=null)  {  
			//停止预览，释放资源
			  camera.stopPreview();
              camera.release();  
              camera=null;  
              isruning=false;
        }  
		LogUtil.logInfo(getClass(), "destroyed ....");
	}
	/**
	 * 获取拍摄的图片
	 */
	@Override
	public void onPictureTaken(byte[] data, Camera arg1) {
		File picture = new File(AppConstant.CAMERA_DIR,System.currentTimeMillis()+".jpg");
        try {
            String cameraPath = picture.getPath().trim();
            FileOutputStream fos = new FileOutputStream(cameraPath);// 获得文件输出流
            fos.write(data);// 写入文件
            fos.flush();
            fos.close();// 关闭文件流

            LogUtil.logInfo(getClass(), "save img:"+picture.getPath());
            if(this.listener!=null)
            	this.listener.onSaved(picture.getPath());
         } catch (Exception e) {
        	e.printStackTrace();
        }
	}
  
	public void setTakePictureListener(TakePictureListener listener){
		this.listener=listener;
	}
	public interface  TakePictureListener{
		public void onSaved(String path);
	}
}
