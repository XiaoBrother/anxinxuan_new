package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Variety;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadVarietyThread extends NetThread {

	private int villeageid;
	private String lastoptime;

	public LoadVarietyThread(int villeageid, String lastoptime) {
		this.villeageid = villeageid;
		this.lastoptime = lastoptime;
	}

	 

	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
				+ HttpUrlConstant.LOAD_VARIETY ;
		RequestParams params=new RequestParams();
		params.put("farmid", villeageid+"");
		params.put("lastoptime", DateUtil.delSpaceDate(lastoptime));
		HttpUtil.get(netdataurl, params,this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
 		try {
			if (jsonObject.getInt("hasdata") == HAS_DATA&& jsonObject.has("varieties")) {
				List<Variety> varieties = new ArrayList<Variety>();
				JSONArray array = jsonObject.getJSONArray("varieties");
				Variety variety;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					variety = new Variety();
					variety.setVariety_id(jo.getInt("id"));
					variety.setVariety_name(jo.getString("variety_name"));
					variety.setVilleage_id(jo.getInt("farm_id"));
					variety.setUser_id(jo.getInt("user_id"));
					variety.setCategory_id(jo.getInt("category_id"));
					variety.setCreate_time(jo.getString("create_time"));
					variety.setVariety_desc(jo.getString("variety_desc"));
					variety.setLastoptime(jo.getString("lastoptime"));
					varieties.add(variety);
				}
  				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						varieties);
			}else{
 				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}

 	}

}
