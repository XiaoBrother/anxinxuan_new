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
	 * 解决android:tileMode="repeat" 失效问题
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
  * 加载本地图片
  * @param resourceid
  * @return
  */
 public static Bitmap loadResourceImg(int resourceid){
	 return  BitmapFactory.decodeResource(Gloable.getInstance().getCurContext().getResources(), resourceid);
   }
 
 /**
	 * 保存文件
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
	 

	
	// 根据路径获取图片
	public static Bitmap getImageBitmap(String path){
		Bitmap bmp = null;
		try {
 			BitmapFactory.Options opts = new BitmapFactory.Options();
 			opts.inJustDecodeBounds = true;
 			opts.inDither = false;  /*不进行图片抖动处理*/
 			//设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上  
 			//按每像素2字节读取 ,默认argb_8888是4字节
  			opts.inPreferredConfig = Bitmap.Config.RGB_565;     
  		    //读取图片时内存不足自动回收本bitmap ,两者结合使用，  inPurgeable设置为false时inInputShareable无意义
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
    * 获取压缩的图片
    * @param path 路径
     * @return
    */
	public static Bitmap getCompressImage(String path) {
		Bitmap image=getImageBitmap(path);
		int degree=20;//默认压缩程度
		if(image!=null){
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        image.compress(Bitmap.CompressFormat.JPEG, degree, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		    	BitmapFactory.Options opts = new BitmapFactory.Options();
	 			opts.inJustDecodeBounds = false;
	 			opts.inDither = false;  /*不进行图片抖动处理*/
	 			//设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上  
	 			//按每像素2字节读取 ,默认argb_8888是4字节
	  			opts.inPreferredConfig = Bitmap.Config.RGB_565;     
	  		    //读取图片时内存不足自动回收本bitmap ,两者结合使用，  inPurgeable设置为false时inInputShareable无意义
	  		  	opts.inPurgeable = true;   
	   		    opts.inInputShareable = true;  
		        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opts);//把ByteArrayInputStream数据生成图片
		       // LogUtil.logInfo(BitmapUtils.class,  "size:"+baos.toByteArray().length);
		        image.recycle();
		        return bitmap;

		}
              return null;
    }
	// 根据路径获取图片
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
			//确定最小缩放倍数
			int min=curwidth/width;
			if((curheight/height)<min)
				min=curheight/height;
			if(min<1) min=1;
			opts.inSampleSize=min;
			//LogUtil.logInfo(DownloadUtil.class, min+"");
			//opts.inSampleSize = ImageTools.computeSampleSize(opts, -1, size);// 得到缩略图
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
	 
	// 根据路径获取图片,固定宽度
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
 			//确定最小缩放倍数
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
	
	// 创建图片的缩略图
//	public static Bitmap createImageThumb(String path,int width,String fileName, String savepath){
// 		try {
//  			BitmapFactory.Options opts = new BitmapFactory.Options();
// 			opts.inJustDecodeBounds = true;
// 			Bitmap bmp=BitmapFactory.decodeFile(path, opts);
//			int curwidth=opts.outWidth;
//			int curheight=opts.outHeight;
// 			//确定最小缩放倍数
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
	
	// 创建图片的缩略图
	public static Bitmap createImageThumb(Bitmap bmp,int width,String fileName, String savepath){
 		try {
 			int curwidth=bmp.getWidth();
			int curheight=bmp.getHeight();
 			//确定最小缩放倍数
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
	 * 判断图片需不需要缩小
	 * @return
	 */
//	public static boolean needThumbImg(String path,int width){
// 		try {
//  			BitmapFactory.Options opts = new BitmapFactory.Options();
// 			opts.inJustDecodeBounds = true;
// 			Bitmap bmp=BitmapFactory.decodeFile(path, opts);
//			int curwidth=opts.outWidth;
//  			//确定最小缩放倍数
//			int min=curwidth/width;
// 			if(min<=1) return false;
// 			return true;
//  		} catch (Exception e) {
//			// e.printStackTrace();
//		}
//		return false;
//	}
	// 根据路径获取图片,固定高度
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
//			//确定最小缩放倍数
//			int min=curheight/height;
// 			if(min<1) min=1;
// 			int width=curwidth/min;
//			opts.inSampleSize=min;
//			//LogUtil.logInfo(BitmapUtils.class, curheight+":"+height);
//			//opts.inSampleSize = ImageTools.computeSampleSize(opts, -1, size);// 得到缩略图
//			opts.inJustDecodeBounds = false;
// 			bmp = BitmapFactory.decodeFile(path, opts);
// 			bmp=Bitmap.createBitmap(bmp, 0, 0, width, height);
// 		} catch (Exception e) {
//			// e.printStackTrace();
//		}
//		return bmp;
//	}
	 
	// 根据路径获取图片,固定高度
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
	 * 保存文件
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
     * 旋转图片 
     * @param angle 
     * @param bitmap 
     * @return Bitmap 
     */  
    public static  void rotaingImageView(String path) {  
    	Bitmap bmp=BitmapFactory.decodeFile(path);
    	if(bmp==null) return ;
    	int angle=readPictureDegree(path);
        //旋转图片 动作   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
         // 创建新的图片   
        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,  
        		bmp.getWidth(), bmp.getHeight(), matrix, true);  
        saveToFile(resizedBitmap,path);
     } 
	/** 
	 * 读取图片属性：旋转的角度 
	 * @param path 图片绝对路径 
	 * @return degree旋转的角度 
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
	 * 获取assets目录下的图片
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
	 * 压缩图片质量
	 * @param image
	 * @return
	 */
	public static void compressImage(String  path) {  
  	    File fl=new File(path);
  	    if(fl.exists()){
  	      Bitmap btm=getImageBitmap(path);
  	       ByteArrayOutputStream baos = new ByteArrayOutputStream();  
  	       btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
  	        int options = 100;  
  	        while ( baos.toByteArray().length / 1024>50) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
  	            baos.reset();//重置baos即清空baos  
  	            btm.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
  	            options -= 10;//每次都减少10  
  	        }  
  	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
  	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
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