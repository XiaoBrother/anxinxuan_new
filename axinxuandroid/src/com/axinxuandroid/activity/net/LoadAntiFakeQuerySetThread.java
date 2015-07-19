package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.AntiFakeQuerySet;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Variety;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadAntiFakeQuerySetThread extends NetThread {

	private int user_id;
	private String lastoptime;

	public LoadAntiFakeQuerySetThread(int user_id) {
		this.user_id = user_id;
	//	this.lastoptime = lastoptime;
	}

	 

	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
				+ HttpUrlConstant.LOAD_ANTIFAKE_QUERYSET ;
		RequestParams params=new RequestParams();
		params.put("user_id", user_id+"");
		//params.put("lastoptime", DateUtil.delSpaceDate(lastoptime));
		HttpUtil.get(netdataurl, params,this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
 		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") ==NET_RETURN_SUCCESS&&jsonObject.getInt("hasdata") == HAS_DATA) {
				AntiFakeQuerySet qset = new AntiFakeQuerySet();
				qset.setId(jsonObject.getInt("id"));
				qset.setUser_id(jsonObject.getInt("user_id"));
				qset.setTel(jsonObject.getString("tel"));
				qset.setStitle(jsonObject.getString("stitle"));
				qset.setBottominf(jsonObject.getString("bottominf"));
				qset.setPubaccount(jsonObject.getString("pubaccount"));
				qset.setSinaweibo(jsonObject.getString("sinaweibo"));
				qset.setWeixin(jsonObject.getString("weixin"));
				qset.setWeidian(jsonObject.getString("weidian"));
  				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
  						qset);
			}else{
 				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}

 	}

}
