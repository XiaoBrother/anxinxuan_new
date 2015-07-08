package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Intelligence;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.VilleageBanner;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class ValidSecurityCodeResultThread extends NetThread {

 	private String vscode,safecode;
	public ValidSecurityCodeResultThread(String vscode,String safecode){
		this.vscode=vscode;
		this.safecode=safecode;
 	}
	 
	
	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.VALID_SECURITY_CODE ;
		RequestParams params=new RequestParams();
		params.put("vscode",vscode );
		params.put("safecode",  safecode);
   		HttpUtil.get(netdataurl,params, this.jsonhand);   
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
 			if (jsonObject.has("hasdata")&&jsonObject.getInt("hasdata")== HAS_DATA){
				Map result=new HashMap();
 				if(jsonObject.has("scode"))
			      	result.put("scode", jsonObject.getJSONObject("scode"));
				if(jsonObject.has("keyarr"))
			      	result.put("keyarr", jsonObject.getJSONArray("keyarr"));
				if(jsonObject.has("batch"))
			      	result.put("batch", jsonObject.getJSONObject("batch"));
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
 						result);
			}else{
 	 			return new NetResult(NetResult.RESULT_OF_ERROR,jsonObject.getString("ajax_optmsg"));
 			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "查询失败！");
 		}
  	}
	
	
}
