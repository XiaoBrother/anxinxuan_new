package com.axinxuandroid.activity.net;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;

public class UploadSystemErrorThread extends NetThread{
   
	private List<SystemLog>  logs;
	private String userid;
	public UploadSystemErrorThread(List<SystemLog> logs,String userid){
		this.logs=logs;
		this.userid=userid;
	}
 	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.UPLOAD_SYSTEM_ERROR;
		SystemLog log=logs.get(0);
		RequestParams params=new RequestParams();
		params.put("userid", userid);
		params.put("message", log.getMessage());
		HttpUtil.get(netdataurl, params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		List<SystemLog> hasupload=new ArrayList<SystemLog>();
		SystemLog log=logs.get(0);
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				hasupload.add(log);
			}
			return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功", hasupload);
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "保存失败", null);
		} 
	}
	
	  

}
