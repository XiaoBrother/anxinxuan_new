package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.Villeage;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class RemoveUserFavoriteThread extends NetThread {

	private int id;

	public RemoveUserFavoriteThread(int id) {
		this.id = id;
	}
 

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.REMOVE_USER_FAVORITE;
		RequestParams params=new RequestParams();
		params.put("id", id+"");
		HttpUtil.get(url,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				// 取消收藏成功
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "取消成功！");
			} else {
				//取消 收藏失败
 				return new NetResult(NetResult.RESULT_OF_ERROR, jsonObject.getString("ajax_optmsg"));
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}
	
	
}
