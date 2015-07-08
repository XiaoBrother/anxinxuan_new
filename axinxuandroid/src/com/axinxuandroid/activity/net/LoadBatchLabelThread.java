package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.BatchLabel;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadBatchLabelThread extends NetThread {
	private int variery_id;
    private String lastoptime;
	public LoadBatchLabelThread(int variery_id,String lastoptime) {
		this.variery_id = variery_id;
		this.lastoptime=lastoptime;
	}
 	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.URL_GETTPYEDATAS;
		RequestParams params=new RequestParams();
		params.put("variety_id", variery_id+"");
		params.put("lastoptime", DateUtil.delSpaceDate(lastoptime));
		HttpUtil.get(netdataurl,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
 		try {
			if(jsonObject != null&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS&&jsonObject.getInt("hasdata") == HAS_DATA){
				List<BatchLabel> Labeldata = null;
					if(jsonObject.has("labels")){
						Labeldata = new ArrayList<BatchLabel>();
						JSONArray array = jsonObject.getJSONArray("labels");
						BatchLabel label;
						for (int i = 0; i < array.length(); i++) {
								JSONObject jo = array.getJSONObject(i);
								label = new BatchLabel();
								label.setVariety_id(variery_id);
								label.setRecordid(jo.getInt("recordid"));
								label.setLabel_name(jo.getString("label_name"));
								label.setIsdel(jo.getInt("isdel"));
								label.setLastoptime(jo.getString("lastoptime"));
								Labeldata.add(label);
						}
					}
  					return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
							Labeldata);
			}else {
  		 			return new NetResult(NetResult.RESULT_OF_ERROR, "获取数据失败！");
 			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
 	}

}
