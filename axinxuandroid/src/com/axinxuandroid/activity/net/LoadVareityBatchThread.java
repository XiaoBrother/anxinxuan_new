package com.axinxuandroid.activity.net;

import java.io.File;
import java.io.IOException;
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
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.service.RecordService;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadVareityBatchThread extends NetThread {

	private int varietyid;
	private String starttime;
	private String endtime;

	public LoadVareityBatchThread(int varietyid, String starttime,
			String endtime) {
		this.varietyid = varietyid;
		this.starttime = starttime == null ? "" : starttime;
		this.endtime = endtime == null ? "" : endtime;
	}

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD + HttpUrlConstant.VAREITY_BATCH;
		RequestParams params = new RequestParams();
		params.put("starttime", DateUtil.delSpaceDate(starttime));
		params.put("endtime", DateUtil.delSpaceDate(endtime));
		params.put("varid", varietyid + "");
		HttpUtil.get(url, params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null
					&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS
					&& jsonObject.getInt("hasdata") == HAS_DATA) {
				List<Batch> bts = new ArrayList<Batch>();
				int count = jsonObject.getInt("totalcount");
				if (jsonObject.has("batchs")) {
					JSONArray array = jsonObject.getJSONArray("batchs");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jo = array.getJSONObject(i);
						Batch btch = new Batch();
						btch.setBatch_id(jo.getLong("id"));
						btch.setCode(jo.getString("batch_code"));
						btch.setProducecount(jo.getInt("producecount"));
						btch.setVariety(jo.getString("variety_name"));
						btch.setVariety_id(jo.getLong("variety_id"));
						btch.setVilleage_id(jo.getInt("farm_id"));
						btch.setStatus(jo.getInt("status"));
						btch.setStage(jo.getInt("stage"));
						btch.setBuylink(jo.getString("buy_link"));
						btch.setConsumercount(jo.getInt("consumercount"));
						btch.setCreate_time(jo.getString("create_time"));
						btch.setVilleage_name(jo.getString("farm_name"));
						btch.setIsdel(jo.getInt("isdel"));
						btch.setLastoptime(jo.getString("lastoptime"));
						btch.setCategoryid(jo.getInt("categoryid"));
						btch.setUserid(jo.getInt("user_id"));
						btch.setOrder_index(0);
						btch.setOrder_date(btch.getBatch_id());
						// 用户头像
						File fl = new File(AppConstant.USERIMG_DIR+ btch.getUserid());
						if (!fl.exists()) {
							String userimgurl = jo.getString("user_img");
							if (userimgurl != null) {
								Bitmap mBitmap = null;
								if (userimgurl.indexOf("http") >= 0)
									mBitmap = BitmapFactory.decodeStream(HttpUtil.getImageStream(userimgurl));
								else
									mBitmap = BitmapFactory.decodeStream(HttpUtil.getImageStream(HttpUrlConstant.URL_HEAD
															+ userimgurl));
								if (mBitmap != null) {
 									BitmapUtils.saveFile(mBitmap, userimgurl,AppConstant.USERIMG_DIR);
 									mBitmap.recycle();
 								}
							}
						} 
						bts.add(btch);
					}
				}
				Map<String, Object> rdata = new HashMap<String, Object>();
				rdata.put("datacount", count);
				rdata.put("datas", bts);
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						rdata);
			} else {
				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
	}

}
