package com.ncpzs.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.sys.gloable.Gloable;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.view.View;

public class BitmapUtils
{
  
	/**
	 * ���android:tileMode="repeat" ʧЧ����
	 * @param view
	 */
	public static void fixBackgroundRepeat(View view) { 
	      Drawable bg = view.getBackground(); 
	      if(bg != null) { 
	           if(bg instanceof BitmapDrawable) { 
	                BitmapDrawable bmp = (BitmapDrawable) bg; 
	                bmp.mutate(); // make sure that we aren't sharing state anymore 
	                bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT); 
	           } 
	      } 
	} 
 
 /**
  * ���ر���ͼƬ
  * @param resourceid
  * @return
  */
 public static Bitmap loadResourceImg(int resourceid){
	 return  BitmapFactory.decodeResource(Gloable.getInstance().getCurContext().getResources(), resourceid);
   }
 
 /**
	 * �����ļ�
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveToFile(Bitmap bm, String path)  {
		if(bm==null) return ;
		try{
			File myCaptureFile = new File(path);
			if(myCaptureFile.exists())
				myCaptureFile.delete();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		}	catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	 

	
	// ����·����ȡͼƬ
	public static Bitmap getImageBitmap(String path){
		Bitmap bmp = null;
		try {
 			BitmapFactory.Options opts = new BitmapFactory.Options();
 			opts.inJustDecodeBounds = true;
 			opts.inDither = false;  /*������ͼƬ��������*/
 			//���ü���ͼƬ����ɫ��Ϊ16bit��Ĭ����RGB_8888����ʾ24bit��ɫ��͸��ͨ������һ���ò���  
 			//��ÿ����2�ֽڶ�ȡ ,Ĭ��argb_8888��4�ֽ�
  			opts.inPreferredConfig = Bitmap.Config.RGB_565;     
  		    //��ȡͼƬʱ�ڴ治���Զ����ձ�bitmap ,���߽��ʹ�ã�  inPurgeable����ΪfalseʱinInputShareable������
  		  	opts.inPurgeable = true;   
   		    opts.inInputShareable = true;  
   		    
			bmp=BitmapFactory.decodeFile(path, opts);
			int curwidth=opts.outWidth;
			int screenwidth=Gloable.getInstance().getScreenWeight();
			opts.inSampleSize=curwidth/screenwidth;
			opts.inJustDecodeBounds = false;
			BufferedInputStream bs = new BufferedInputStream(new FileInputStream(path));
			bmp = BitmapFactory.decodeStream(bs,null,opts);
 			//bmp = BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			// e.printStackTrace();
			 System.gc();
		}
		return bmp;
	}
   /**
    * ��ȡѹ����ͼƬ
    * @param path ·��
     * @return
    */
	public static Bitmap getCompressImage(String path) {
		Bitmap image=getImageBitmap(path);
		int degree=20;//Ĭ��ѹ���̶�
		if(image!=null){
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        image.compress(Bitmap.CompressFormat.JPEG, degree, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		    	BitmapFactory.Options opts = new BitmapFactory.Options();
	 			opts.inJustDecodeBounds = false;
	 			opts.inDither = false;  /*������ͼƬ��������*/
	 			//���ü���ͼƬ����ɫ��Ϊ16bit��Ĭ����RGB_8888����ʾ24bit��ɫ��͸��ͨ������һ���ò���  
	 			//��ÿ����2�ֽڶ�ȡ ,Ĭ��argb_8888��4�ֽ�
	  			opts.inPreferredConfig = Bitmap.Config.RGB_565;     
	  		    //��ȡͼƬʱ�ڴ治���Զ����ձ�bitmap ,���߽��ʹ�ã�  inPurgeable����ΪfalseʱinInputShareable������
	  		  	opts.inPurgeable = true;   
	   		    opts.inInputShareable = true;  
		        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��
		        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opts);//��ByteArrayInputStream��������ͼƬ
		       // LogUtil.logInfo(BitmapUtils.class,  "size:"+baos.toByteArray().length);
		        image.recycle();
		        return bitmap;

		}
              return null;
    }
	// ����·����ȡͼƬ
	public static Bitmap getImageBitmap(String path,int width,int height){
		Bitmap bmp = null;
		try {
			//LogUtil.logInfo(DownloadUtil.class, width+":"+height);
 			BitmapFactory.Options opts = new BitmapFactory.Options();
 			opts.inJustDecodeBounds = true;
			bmp=BitmapFactory.decodeFile(path, opts);
			int curwidth=opts.outWidth;
			int curheight=opts.outHeight;
			//LogUtil.logInfo(DownloadUtil.class, curwidth+":"+curheight);
			//ȷ����С���ű���
			int min=curwidth/width;
			if((curheight/height)<min)
				min=curheight/height;
			if(min<1) min=1;
			opts.inSampleSize=min;
			//LogUtil.logInfo(DownloadUtil.class, min+"");
			//opts.inSampleSize = ImageTools.computeSampleSize(opts, -1, size);// �õ�����ͼ
			opts.inJustDecodeBounds = false;
			BufferedInputStream bs = new BufferedInputStream(new FileInputStream(path));
			Bitmap tmpbmp = BitmapFactory.decodeStream(bs,null,opts);
 			//bmp = BitmapFactory.decodeFile(path, opts);
 			bmp=ThumbnailUtils.extractThumbnail(tmpbmp, width, height);
 			tmpbmp.recycle();
 				//Bitmap.createBitmap(bmp, 0, 0, width, height);
 			//LogUtil.logInfo(DownloadUtil.class, bmp.getWidth()+":"+bmp.getHeight());
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return bmp;
	}
	 
	// ����·����ȡͼƬ,�̶����
	public static Bitmap getImageBitmapWithWidth(String path,int width){
		Bitmap bmp = null;
		try {
			
 			BitmapFactory.Options opts = new BitmapFactory.Options();
 			opts.inJustDecodeBounds = true;
			bmp=BitmapFactory.decodeFile(path, opts);
			int curwidth=opts.outWidth;
			int curheight=opts.outHeight;
			if(curwidth<width){
				return getImageBitmap(path);
			}
 			//ȷ����С���ű���
			int min=curwidth/width;
 			if(min<1) min=1;
 			int height=curheight/min;
			opts.inSampleSize=min;
			//LogUtil.logInfo(BitmapUtils.class, curheight+":"+height);
 			opts.inJustDecodeBounds = false;
 			BufferedInputStream bs = new BufferedInputStream(new FileInputStream(path));
			bmp = BitmapFactory.decodeStream(bs,null,opts);
 			//bmp = BitmapFactory.decodeFile(path, opts);
  			bmp=ThumbnailUtils.extractThumbnail(bmp, width, height);
  			return bmp;
 		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	// ����ͼƬ������ͼ
//	public static Bitmap createImageThumb(String path,int width,String fileName, String savepath){
// 		try {
//  			BitmapFactory.Options opts = new BitmapFactory.Options();
// 			opts.inJustDecodeBounds = true;
// 			Bitmap bmp=BitmapFactory.decodeFile(path, opts);
//			int curwidth=opts.outWidth;
//			int curheight=opts.outHeight;
// 			//ȷ����С���ű���
//			int min=curwidth/width;
// 			if(min<1) min=1;
// 			int height=curheight/min;
//			opts.inSampleSize=min;
//  			opts.inJustDecodeBounds = false;
// 			bmp = BitmapFactory.decodeFile(path, opts);
//  			Bitmap rebmp=ThumbnailUtils.extractThumbnail(bmp, width, height);
//  			saveFile(rebmp,fileName,savepath);
// 			bmp.recycle();
//  			return rebmp;
//  		} catch (Exception e) {
//			 e.printStackTrace();
//		}
//  		return null;
// 	} 
	
	// ����ͼƬ������ͼ
	public static Bitmap createImageThumb(Bitmap bmp,int width,String fileName, String savepath){
 		try {
 			int curwidth=bmp.getWidth();
			int curheight=bmp.getHeight();
 			//ȷ����С���ű���
			int min=curwidth/width;
 			if(min<1) min=1;
 			int height=curheight/min;
    		Bitmap rebmp=ThumbnailUtils.extractThumbnail(bmp, width, height);
  			saveFile(rebmp,fileName,savepath);
 			bmp.recycle();
  			return rebmp;
  		} catch (Exception e) {
			 e.printStackTrace();
		}
  		return null;
 	} 
	/**
	 * �ж�ͼƬ�費��Ҫ��С
	 * @return
	 */
//	public static boolean needThumbImg(String path,int width){
// 		try {
//  			BitmapFactory.Options opts = new BitmapFactory.Options();
// 			opts.inJustDecodeBounds = true;
// 			Bitmap bmp=BitmapFactory.decodeFile(path, opts);
//			int curwidth=opts.outWidth;
//  			//ȷ����С���ű���
//			int min=curwidth/width;
// 			if(min<=1) return false;
// 			return true;
//  		} catch (Exception e) {
//			// e.printStackTrace();
//		}
//		return false;
//	}
	// ����·����ȡͼƬ,�̶��߶�
//	public static Bitmap getImageBitmapWithHeight(String path,int height){
//		Bitmap bmp = null;
//		try {
//			//LogUtil.logInfo(DownloadUtil.class, width+":"+height);
// 			BitmapFactory.Options opts = new BitmapFactory.Options();
// 			opts.inJustDecodeBounds = true;
//			bmp=BitmapFactory.decodeFile(path, opts);
//			int curwidth=opts.outWidth;
//			int curheight=opts.outHeight;
//			//LogUtil.logInfo(DownloadUtil.class, curwidth+":"+curheight);
//			//ȷ����С���ű���
//			int min=curheight/height;
// 			if(min<1) min=1;
// 			int width=curwidth/min;
//			opts.inSampleSize=min;
//			//LogUtil.logInfo(BitmapUtils.class, curheight+":"+height);
//			//opts.inSampleSize = ImageTools.computeSampleSize(opts, -1, size);// �õ�����ͼ
//			opts.inJustDecodeBounds = false;
// 			bmp = BitmapFactory.decodeFile(path, opts);
// 			bmp=Bitmap.createBitmap(bmp, 0, 0, width, height);
// 		} catch (Exception e) {
//			// e.printStackTrace();
//		}
//		return bmp;
//	}
	 
	// ����·����ȡͼƬ,�̶��߶�
	public static int[] getImageWidthHeight(int resid){
		Bitmap bmp = null;
		try {
  			BitmapFactory.Options opts = new BitmapFactory.Options();
 			opts.inJustDecodeBounds = true;
			bmp=BitmapFactory.decodeResource(Gloable.getInstance().getCurContext().getResources(), resid,opts);
			int[] res=new int[2];
			res[0]=opts.outWidth;
			res[1]=opts.outHeight;
			return res;
   		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}
	/**
	 * �����ļ�
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bm, String fileName, String path)
			throws IOException {
		if(path==null||fileName==null) return ;
		path=path.trim();
		fileName=fileName.trim();
 		File dirFile = new File(path);
		if (!dirFile.exists()) {
  			FileUtil.createDirectory(path);
		}
		File myCaptureFile = new File(path + fileName);
 		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();
	}
	
	
	 /**
     * ��תͼƬ 
     * @param angle 
     * @param bitmap 
     * @return Bitmap 
     */  
    public static  void rotaingImageView(String path) {  
    	Bitmap bmp=BitmapFactory.decodeFile(path);
    	if(bmp==null) return ;
    	int angle=readPictureDegree(path);
        //��תͼƬ ����   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
         // �����µ�ͼƬ   
        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,  
        		bmp.getWidth(), bmp.getHeight(), matrix, true);  
        saveToFile(resizedBitmap,path);
     } 
	/** 
	 * ��ȡͼƬ���ԣ���ת�ĽǶ� 
	 * @param path ͼƬ����·�� 
	 * @return degree��ת�ĽǶ� 
	 */  
	   public  static int readPictureDegree(String path) {  
	       int degree  = 0;  
	       try {  
	               ExifInterface exifInterface = new ExifInterface(path);  
	               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
	               switch (orientation) {  
	               case ExifInterface.ORIENTATION_ROTATE_90:  
	                       degree = 90;  
	                       break;  
	               case ExifInterface.ORIENTATION_ROTATE_180:  
	                       degree = 180;  
	                       break;  
	               case ExifInterface.ORIENTATION_ROTATE_270:  
	                       degree = 270;  
	                       break;  
	               }  
	       } catch (IOException e) {  
	               e.printStackTrace();  
	       }  
	       return degree;  
	   }  
	   
	/**
	 * ��ȡassetsĿ¼�µ�ͼƬ
	 * @param imgurl
	 * @return
	 */
	public static  Bitmap getAssetImg(String imgurl) {
		Bitmap bitmap = null;
		try {
			AssetManager assetManager = Gloable.getInstance().getCurContext()
					.getAssets();
			InputStream is = assetManager.open(imgurl);
		    bitmap = BitmapFactory.decodeStream(is);
 		} catch (Exception e) {
 			  e.printStackTrace();  
		}
 		return bitmap;
	}
	
	/**
	 * ѹ��ͼƬ����
	 * @param image
	 * @return
	 */
	public static void compressImage(String  path) {  
  	    File fl=new File(path);
  	    if(fl.exists()){
  	      Bitmap btm=getImageBitmap(path);
  	       ByteArrayOutputStream baos = new ByteArrayOutputStream();  
  	       btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��  
  	        int options = 100;  
  	        while ( baos.toByteArray().length / 1024>50) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
  	            baos.reset();//����baos�����baos  
  	            btm.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��  
  	            options -= 10;//ÿ�ζ�����10  
  	        }  
  	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
  	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ  
  	        saveToFile(bitmap, path);
  	        if(btm!=null){
  	        	btm.recycle();
  	        	btm=null;
  	        }
  	      if(bitmap!=null){
  	    	bitmap.recycle();
  	    	bitmap=null;
	        }
   	    }
       
    }  
}