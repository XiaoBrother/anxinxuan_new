package com.axinxuandroid.activity.net;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.service.RecordService;
import com.google.zxing.qrcode.encoder.Encoder;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.zxing.decoding.Intents.Encode;

public class LoadTableJsonThread extends NetThread {

	private int variety_id;//品种id
	private String labelname;//标签名
 
	public LoadTableJsonThread(int variety_id, String labelname) {
		this.variety_id = variety_id;
		this.labelname = labelname;
	}

 

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.TALBEL_JSON ;
 		RequestParams params=new RequestParams();
		params.put("variety_id", variety_id+"" );
		params.put("labelname",   labelname);
 		HttpUtil.get(url,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		List<String> rddata = new ArrayList<String>();
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1&&jsonObject.has("context")) {
				String context = jsonObject.getString("context");
				  if(context!=null){
					JSONObject contextjson = new JSONObject(context);
					JSONArray titles=contextjson.getJSONArray("titles");
					for (int i = 0; i < titles.length(); i++) {
							rddata.add(titles.getJSONObject(i).getString("name"));
					}
				   }
 					return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
							rddata);
				} else {
 				   return new NetResult(NetResult.RESULT_OF_ERROR, "该环节没有表格数据！");
				}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
	 
	}
	

	 

}
