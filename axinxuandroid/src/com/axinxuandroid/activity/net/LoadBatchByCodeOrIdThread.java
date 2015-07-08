package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadBatchByCodeOrIdThread extends NetThread {
	private String code;
	private String id;

	public LoadBatchByCodeOrIdThread(String code, String id) {
		this.code = code;
		this.id = id;
	}

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD + HttpUrlConstant.URL_BATCHINFO;
		RequestParams params=new RequestParams();
		if (id != null){
			params.put("batchid", id);
		}
 		else params.put("batchcode", code);
 		HttpUtil.get(url,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null
					&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS
					&& jsonObject.getInt("hasdata") == HAS_DATA) {
				Batch btch = new Batch();
				btch.setBatch_id(jsonObject.getLong("id"));
				btch.setCode(jsonObject.getString("batch_code"));
				btch.setProducecount(jsonObject.getInt("producecount"));
				btch.setVariety(jsonObject.getString("variety_name"));
				btch.setVariety_id(jsonObject.getLong("variety_id"));
				btch.setVilleage_id(jsonObject.getInt("farm_id"));
				btch.setStatus(jsonObject.getInt("status"));
				btch.setStage(jsonObject.getInt("stage"));
				btch.setBuylink(jsonObject.getString("buy_link"));
				btch.setConsumercount(jsonObject.getInt("consumercount"));
				btch.setCreate_time(jsonObject.getString("create_time"));
				btch.setVilleage_name(jsonObject.getString("farm_name"));
				btch.setIsdel(jsonObject.getInt("isdel"));
				btch.setLastoptime(jsonObject.getString("lastoptime"));
				btch.setCategoryid(jsonObject.getInt("categoryid"));
				btch.setUserid(jsonObject.getInt("user_id"));
				btch.setOrder_index(0);
				btch.setOrder_date(btch.getBatch_id());
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						btch);

			} else {
				return new NetResult(NetResult.RESULT_OF_ERROR, "获取数据失败！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
	}

}
