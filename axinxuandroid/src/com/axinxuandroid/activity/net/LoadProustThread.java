package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadProustThread extends NetThread {
	private int user_id;
    private String lastoptime;
	public LoadProustThread(int user_id,String lastoptime) {
		this.user_id = user_id;
		this.lastoptime=lastoptime;
	}
  
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.LOAD_PROUST ;
		RequestParams params=new RequestParams();
		params.put("user_id",user_id +"");
		params.put("lastoptime",DateUtil.delSpaceDate(lastoptime));
		HttpUtil.get(netdataurl,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		List<Proust> prousts = null;
		try {
			if (jsonObject.getInt("hasdata") == HAS_DATA&&jsonObject.has("prousts")) {
				prousts=new ArrayList<Proust>();
				JSONArray array = jsonObject.getJSONArray("prousts");
				Proust proust;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					proust = new Proust();
					proust.setUser_id(user_id);
					proust.setProust_id(jo.getInt("proust_id"));
					proust.setQuestion(jo.getString("question"));
					proust.setAnswer(jo.getString("answer"));
					proust.setCreate_date(jo.getString("create_time"));
					proust.setIsdel(jo.getInt("isdel"));
					proust.setLastoptime(jo.getString("lastoptime"));
					prousts.add(proust);
				}
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						prousts);
			}else{
  	 			return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");

			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}

}
