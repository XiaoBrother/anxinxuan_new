package com.ncpzs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.anxinxuandroid.constant.AppConstant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;

public final class ImageTools {
	 
	
	 
	
	
  /**
   * ��ˮӡ,�������
   * @param src
   * @param watermark
   * @return
   */
  public static Bitmap imageWater( Bitmap src, Bitmap watermark ){
  	if( src == null ||watermark ==null){
 	   return null;
 	}
 	int w = src.getWidth();
 	int h = src.getHeight();
 	int ww = watermark.getWidth();
 	int wh = watermark.getHeight();
 	//create the new blank bitmap
 	Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//����һ���µĺ�SRC���ȿ��һ����λͼ
 	Canvas cv = new Canvas( newb );
 	//draw src into
 	cv.drawBitmap( src, 0, 0, null );//�� 0��0���꿪ʼ����src
 	//draw watermark into
  	cv.drawBitmap( watermark, (w-ww)/2, (h - wh)/ 2, null );//��src�ľ��л���ˮӡ
 	//save all clip
 	cv.save( Canvas.ALL_SAVE_FLAG );//����
 	//store
 	cv.restore();//�洢
 	return newb;

	}

  @SuppressLint("NewApi")
public static String getLocalVideoImage(String videopath){
	  MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
	  retriever.setDataSource(videopath);
//	  File fl=new File(videopath);
//		if(fl.exists()&&fl.isFile()){
//			Bitmap bitmap = null;
//			bitmap=ThumbnailUtils.createVideoThumbnail(videopath, Thumbnails.MICRO_KIND);
//			if(bitmap!=null){
//				bitmap = ThumbnailUtils.extractThumbnail(bitmap, AppConstant.VIDEO_WIDTH, AppConstant.VIDEO_HEIGHT, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//	 			String thumbimgurl=AppConstant.VIDEO_DIR+(new Date()).getTime()+".jpg";
//				BitmapUtils.saveToFile(bitmap, thumbimgurl);
//				return thumbimgurl;
//			}
// 	 }
	 String thumbimgurl=AppConstant.VIDEO_DIR+(new Date()).getTime()+".jpg";
	 Bitmap bitmap = retriever.getFrameAtTime(1000);
	 if(bitmap!=null){
		 BitmapUtils.saveToFile(bitmap, thumbimgurl);
		 bitmap.recycle();
		 return thumbimgurl;
	 }
	return null;
  }
}