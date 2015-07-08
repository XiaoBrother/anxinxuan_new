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
 * ������Ӿ�������һ���Լ���Camera
 * ���ȣ���Manifest����Ҫ����Ȩ��<uses-permission android:name="android:permission.CAMERA"/>
 * ������Ҫ�������ȡ�����������������������SurfaceView��
 * ʹ��SurfaceView��ͬʱ�����ǻ���Ҫʹ�õ�SurfaceHolder��SurfaceHolder�൱��һ�������������Լ���
 * Surface�ϵı仯,ͨ�����ڲ���CallBack��ʵ�֡�
 * Ϊ�˿��Ի�ȡͼƬ��������Ҫʹ��Camera��takePicture����ͬʱ������Ҫʵ��Camera.PictureCallBack�࣬ʵ��onPictureTaken����
 * @author Administrator
 *
 */

public class CameraView  extends SurfaceView implements SurfaceHolder.Callback,Camera.PictureCallback{
	protected SurfaceHolder hodler;
    protected Camera camera; //�����hardare��Camera����
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
		hodler=this.getHolder();//���SurfaceView�Ŀ�����   
		hodler.addCallback(this);//����Holder�Ļص��ӿ�   ,holder����ͨ������ص��ӿڿ���SurfaceView����ʾ����ˢ�µ���Ϊ   
        //SURFACE_TYPE_PUSH_BUFFERS������Surface������ԭ�����ݣ�Surface�õ������������������ṩ��
		//��Cameraͼ��Ԥ���о�ʹ�ø����͵�Surface��
		//��Camera�����ṩ��Ԥ��Surface���ݣ�����ͼ��Ԥ����Ƚ�����
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
	 * ����
	 */
 	public void takeCamera(){
 		//�����������ֱ��������ص�����   
         //shutter��ʾ ���¿���ʱ Ҫִ�еĲ���   
         //raw ��ʾҪ��ͼƬ�����raw��ʽʱ�����Ĳ���   
         //jpeg��ʾҪ��ͼƬ�����jpg��ʽ   
 		if(camera!=null){
 			camera.takePicture(null, null, CameraView.this);
 			startCamera();
 		}
 		
 	}
 	/**
 	 * ��������
 	 */
 	public void startCamera(){
  		if( camera!=null)  {  
             //����Ѿ�����Ԥ��״̬  ��Ҫ�Ƚ��� ������   
 			if(isruning)
              camera.stopPreview();  
            camera.startPreview();
            isruning=true;
            LogUtil.logInfo(getClass(), "start ....");
        }  
  	}
 	/**
	 * ��ʼ��һЩ����
	 */
	private void initCamera(){
		if(camera!=null){
			Camera.Parameters param = camera.getParameters();
			param.set("rotation", 90);//���������Ƭ��ת90�㣬�����ĳ�������Ƭ����б��
	 		int bestWidth = 0;
			int bestHeight = 0;
 			List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
			//���sizeListֻ��һ������Ҳû�б�Ҫ��ʲô�ˣ���Ϊ����һ������ѡ��
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
					//����ı���SIze�����ǻ�Ҫ����SurfaceView������Surface������ı��С������Camera��ͼ�������ܲ�
					 
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
		camera = Camera.open(); //������ͷ����ȡCameraʵ��
		if(camera!=null){
			try{
 	 			if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
					//���������
					//param.set("orientation", "portrait");
					//��2.2���Ͽ���ʹ��
					camera.setDisplayOrientation(90);
				}else{
					//param.set("orientation", "landscape");
					//��2.2���Ͽ���ʹ��
					camera.setDisplayOrientation(0);				
				}
 	  	 		camera.setPreviewDisplay(holder);
 	  	 		/**
				 * ����ʾ��Ԥ����������ʱ��ϣ������Ԥ����Size
				 * ���ǲ������Լ�ָ��һ��SIze����ָ��һ��Size��Ȼ��
				 * ��ȡϵͳ֧�ֵ�SIZE��Ȼ��ѡ��һ����ָ��SIZEС����ӽ���ָ��SIZE��һ��
				 * Camera.Size������Ǹ�SIZE��
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
			//ֹͣԤ�����ͷ���Դ
			  camera.stopPreview();
              camera.release();  
              camera=null;  
              isruning=false;
        }  
		LogUtil.logInfo(getClass(), "destroyed ....");
	}
	/**
	 * ��ȡ�����ͼƬ
	 */
	@Override
	public void onPictureTaken(byte[] data, Camera arg1) {
		File picture = new File(AppConstant.CAMERA_DIR,System.currentTimeMillis()+".jpg");
        try {
            String cameraPath = picture.getPath().trim();
            FileOutputStream fos = new FileOutputStream(cameraPath);// ����ļ������
            fos.write(data);// д���ļ�
            fos.flush();
            fos.close();// �ر��ļ���

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
