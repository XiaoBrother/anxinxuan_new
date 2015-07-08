package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Template;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

/**
 * 模板检查更新线程
 * 
 * @author Administrator
 * 
 */
public class TemplateCheckThread extends NetThread {
 	private String lastoptime;
	public TemplateCheckThread(String lastoptime){
		this.lastoptime=lastoptime;
	}
	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.TEMPLATE_CHECKVERSION;
		RequestParams params=new RequestParams();
		params.put("lastoptime", lastoptime);
		HttpUtil.get(netdataurl, params,this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS) {
				int totalcount=0;
				if(jsonObject.getInt("hasdata")== HAS_DATA){
					totalcount=jsonObject.getInt("totalcount");
				} 
			   return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取更新成功！",totalcount);

			}else{
				return new NetResult(NetResult.RESULT_OF_ERROR, "获取更新失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "发送失败！");
		}
	}
  
 
	 
}
