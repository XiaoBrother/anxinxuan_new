package com.axinxuandroid.activity.net;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.RecordResource;
import com.ncpzs.util.ImgUploadUtil;
import com.ncpzs.util.LogUtil;

public class UploadNetMediaThread extends Thread {
	private List<RecordResource> localurls;
 	private String batchid;
	private String recordid;
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_ERROR = 2;
	private UploadMediaFinishListener listner;

	public UploadNetMediaThread(List<RecordResource> localurls,
			String batchid, String recordid) {
		this.localurls = localurls;
 		this.batchid = batchid;
		this.recordid = recordid;
	}

	@Override
	public void run() {
 		try {
 			String neturl=HttpUrlConstant.URL_HEAD+HttpUrlConstant.URL_MEDIA;
 			String  result =null;
			List<RecordResource> success = new ArrayList<RecordResource>();
			if (localurls != null) {
				for (RecordResource path : localurls) {
					String imgpath=path.getLocalurl();
  					Map<String,String> params=new HashMap<String, String>();
					params.put("batchid", batchid+"");
					params.put("recordid", recordid+"");
					params.put("info", path.getInfo());
 					String filesubfix=imgpath.substring(imgpath.lastIndexOf("."));
					result = ImgUploadUtil.uploadFile(imgpath, neturl, (new Date().getTime())+filesubfix,params);
  					if ( result != null) {
						JSONObject jsonObject = new JSONObject(result);
						if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
							path.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
							path.setNeturl(jsonObject.getString("neturl"));
							success.add(path);
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
	
	public void setUploadMediaFinishListener(UploadMediaFinishListener lis){
		this.listner=lis;
	}
	public interface UploadMediaFinishListener {
		public void onfinish(int resultcode,Object resultdata);
	}

}
