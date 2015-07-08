package com.axinxuandroid.activity.net;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.anxinxuandroid.constant.HttpUrlConstant;
 import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.VilleagePhoto;
import com.axinxuandroid.service.SharedPreferenceService;
import com.ncpzs.util.ImgUploadUtil;
import com.ncpzs.util.LogUtil;

public class UploadVilleagePhotoThread extends Thread {
	private String imgpath;
	private String label;
	private int villeageid;
	private int userid;
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_ERROR = 2;
	private UploadVilleagePhotoFinishListener listener;
	public UploadVilleagePhotoThread(String imgpath, String label, int villeageid,int userid) {
		this.imgpath = imgpath;
		this.villeageid = villeageid;
		this.userid=userid;
		this.label=label;
	}

	@Override
	public void run() {
		try {
			String neturl = HttpUrlConstant.URL_HEAD
					+ HttpUrlConstant.UPLOAD_VILELAGE_PHOTO;
			String result = null;
			if (imgpath != null) {
  				Map<String,String> params=new HashMap<String, String>();
 				params.put("villeageid", villeageid+"");
 				params.put("userid", userid+"");
 				params.put("label", URLEncoder.encode(label));
 				String filesubfix=imgpath.substring(imgpath.lastIndexOf("."));
				result = ImgUploadUtil.uploadFile(imgpath, neturl, (new Date().getTime())+filesubfix,params);
				LogUtil.logInfo(getClass(),  (result == null ? "" : result));
				//SharedPreferenceService.saveErrorMsg(new String(result.getBytes("iso-8859-1"),"utf-8"));
 				if (result != null) {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
//						VilleagePhoto photo = new VilleagePhoto();
//						photo.setPhotoid(jsonObject.getInt("id"));
//						photo.setImage_desc(jsonObject.getString("image_desc"));
//						photo.setImage_name(jsonObject.getString("image_name"));
//						photo.setImage_url(HttpUrlConstant.URL_HEAD+jsonObject.getString("image_url"));
//						photo.setIndex_order(jsonObject.getInt("index_order"));
//						photo.setLabel_name(jsonObject.getString("label_name"));
//						photo.setThumbnail_url(HttpUrlConstant.URL_HEAD+jsonObject.getString("thumbnail_url"));
//						photo.setVilleage_id(jsonObject.getInt("farm_id"));
//						photo.setIsdel(jsonObject.getInt("isdel"));
//						photo.setLastoptime(jsonObject.getString("lastoptime"));
//						if(photo.getImage_url()!=null){
// 						    int lastp=photo.getImage_url().lastIndexOf(".");
//						    String img=photo.getImage_url().substring(0,lastp)+"_cutmid."+photo.getImage_url().substring(lastp+1);
//						    photo.setThumbnail_url(img);
//						}
//						photo.setLocalurl(imgpath);
						//this.returnmap.put(RESULT, NetResult.RESULT_OF_SUCCESS);
						//this.returnmap.put(RETURNDATA, photo);
						if(this.listener!=null)
							this.listener.onfinish(RESULT_SUCCESS,null);
					}else {
						if(this.listener!=null)
							this.listener.onfinish(RESULT_ERROR,null);
 					}
				} else {
					if(this.listener!=null)
						this.listener.onfinish(RESULT_ERROR,null);
				}
			}else {
				if(this.listener!=null)
					this.listener.onfinish(RESULT_ERROR,null);
			}
			//Thread.sleep(8000);
			//this.returnmap.put(RESULT, NetResult.RESULT_OF_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
			if(this.listener!=null)
				this.listener.onfinish(RESULT_ERROR,null);
		}
	}
	public void setUploadVilleagePhotoFinishListener(UploadVilleagePhotoFinishListener lis){
		this.listener=lis;
	}
	public interface UploadVilleagePhotoFinishListener {
		public void onfinish(int resultcode,Object resultdata);
	}

}
