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

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.oauth.OAuthConstant;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
 
public class WeiBoFollowAxxThread extends NetThread {

	private String acccesstoken;
 
	public WeiBoFollowAxxThread(String acccesstoken) {
		this.acccesstoken = acccesstoken;
 	}

	
	@Override
	public void requestHttp() {
 		RequestParams params=new RequestParams();
		params.put("screen_name", "安心选" );
		params.put("access_token",acccesstoken);
        HttpUtil.post(OAuthConstant.Sina.FOLLOW_ANXINXUAN, params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		if (jsonObject != null&& !jsonObject.has("error_code")) {
			// 关注成功
			return new NetResult(NetResult.RESULT_OF_SUCCESS, "关注成功！");
		} else {
			// 关注失败
 			return new NetResult(NetResult.RESULT_OF_ERROR, "关注失败！");
		}
	}
	
}
