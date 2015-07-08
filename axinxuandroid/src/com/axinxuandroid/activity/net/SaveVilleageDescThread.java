package com.axinxuandroid.activity.net;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;

import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SaveVilleageDescThread extends NetThread {

	private String desc;
	private int villeageid;

	public SaveVilleageDescThread(int villeageid, String desc) {
		this.desc = desc;
		this.villeageid = villeageid;
	}

 
	@Override
	public void requestHttp() {
		RequestParams params=new RequestParams();
		params.put("villeageid", villeageid+"");
		params.put("desc",  desc );
 		HttpUtil.get(HttpUrlConstant.URL_HEAD+ HttpUrlConstant.SAVE_VILLEAGE_DESC, params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
					// 保存成功
				 return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！");
			} else {
				// 保存失败
				return new NetResult(NetResult.RESULT_OF_ERROR, "保存失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
 	}

}
