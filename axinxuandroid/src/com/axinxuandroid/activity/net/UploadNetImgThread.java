package com.axinxuandroid.activity.net;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.ImgUploadUtil;
import com.ncpzs.util.LogUtil;

public class UploadNetImgThread extends Thread {
	private List<RecordResource> localurls;
 	private String batchid;
	private String recordid;
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_ERROR = 2;
	private UploadImgFinishListener listner;
  	public UploadNetImgThread(List<RecordResource> localurls,
			String batchid, String recordid) {
		this.localurls = localurls;
 		this.batchid = batchid;
		this.recordid = recordid;
	}

	@Override
	public void run() {
 		try {
 			String neturl=HttpUrlConstant.URL_HEAD+HttpUrlConstant.URL_IMG;
			String  result =null;
			List<RecordResource> success = new ArrayList<RecordResource>();
			if (localurls != null) {
				for (RecordResource rec : localurls) {
					Date date = new Date();
					String imgpath=rec.getLocalurl();
					Map<String,String> params=new HashMap<String, String>();
					params.put("batchid", batchid+"");
					params.put("recordid", recordid+"");
					params.put("time", date.getTime()+"");
					String filesubfix=imgpath.substring(imgpath.lastIndexOf("."));
 					//Log.e("dates", dates);
					result = ImgUploadUtil.uploadFile(imgpath, neturl, (new Date().getTime())+filesubfix,params);
  				    LogUtil.logInfo(getClass(),  "upfile:"+(result==null?"":result));
					if ( result != null) {
						JSONObject jsonObject = new JSONObject(result);
						if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
 							rec.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
							rec.setNeturl(jsonObject.getString("neturl"));
							success.add(rec);
						}
					}
				}
				if(this.listner!=null)
					this.listner.onfinish(RESULT_SUCCESS, success);
  			}

		} catch (Exception e) {
			e.printStackTrace();
			if(this.listner!=null)
				this.listner.onfinish(RESULT_ERROR, null);
		}
	}

	public void setUploadImgFinishListener(UploadImgFinishListener lis){
		this.listner=lis;
	}
	public interface UploadImgFinishListener {
		public void onfinish(int resultcode,Object resultdata);
	}
}
