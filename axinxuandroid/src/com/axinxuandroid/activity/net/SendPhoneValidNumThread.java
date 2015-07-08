package com.axinxuandroid.activity.net;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SendPhoneValidNumThread extends NetThread {

	private String phone;

	public SendPhoneValidNumThread(String phone) {
		this.phone = phone;
	}

 
	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.SEND_PHONE_VALID_NUM ;
		RequestParams params=new RequestParams();
		params.put("phone", phone);
		HttpUtil.get(url,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null
					&& jsonObject.getInt("ajax_optinfo") == 1) {
				// 发送成功
				 return new NetResult(NetResult.RESULT_OF_SUCCESS, "发送成功！",jsonObject.getString("validnum"));
			} else {
				// 发送失败
				return new NetResult(NetResult.RESULT_OF_ERROR, "发送失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
	}
	
}
