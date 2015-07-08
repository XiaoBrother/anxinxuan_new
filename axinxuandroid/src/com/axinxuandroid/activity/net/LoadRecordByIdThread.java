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

/**
 * 记载记录以及它的批次信息
 * 
 * @author Administrator
 * 
 */
public class LoadRecordByIdThread extends NetThread {

	private int recordid;

	public LoadRecordByIdThread(int recordid) {
		this.recordid = recordid;
	}

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD + HttpUrlConstant.RECORD_INFO;
		RequestParams params = new RequestParams();
		params.put("recordid", recordid + "");
		HttpUtil.get(url, params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		List<Record> recorddata = null;
		try {
			Record rec = null;
			if (jsonObject != null
					&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS
					&& jsonObject.getInt("hasdata") == HAS_DATA) {
 					rec = new Record();
					rec.setType(jsonObject.getInt("type"));
					rec.setUser_id(jsonObject.getString("user_id"));
					rec.setVilleage_id(jsonObject.getInt("farm_id"));
					rec.setSave_type(jsonObject.getInt("save_type"));
					rec.setRecord_id(jsonObject.getInt("record_id"));
					rec.setLabel_name(jsonObject.getString("label_name"));
					rec.setNick_name(jsonObject.getString("nick_name"));
					rec.setBatch_id(jsonObject.getInt("batch_id"));
					rec.setSave_date(jsonObject.getString("create_time"));
					rec.setSend_date(jsonObject.getString("save_date"));
					rec.setIsdel(jsonObject.getInt("isdel"));
					rec.setLastoptime(jsonObject.getString("lastoptime"));
 					if(jsonObject.has("lat")&&!jsonObject.isNull("lat"))
						rec.setLat(jsonObject.getDouble("lat"));
					if(jsonObject.has("lng")&&!jsonObject.isNull("lng"))
						rec.setLng(jsonObject.getDouble("lng"));
					rec.setContext("");
					rec.setTrace_code(jsonObject.getString("batch_code"));
					rec.setVariety_id(jsonObject.getInt("variety_id"));
					rec.setUser_type(jsonObject.getInt("user_type"));
					if ((jsonObject.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_TEXT) == Record.BATCHRECORD_TYPE_OF_TEXT) {
						if (jsonObject.has("context"))
							rec.setContext(jsonObject.getString("context"));
					}
					if ((jsonObject.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_IMAGE) == Record.BATCHRECORD_TYPE_OF_IMAGE) {
						if (jsonObject.has("images")) {
							JSONArray imgs = jsonObject.optJSONArray("images");
							String img = null;
							RecordResource resource = null;
							for (int m = 0; m < imgs.length(); m++) {
								resource = new RecordResource();
								img = (String) imgs.get(m);
								// img = img.replaceAll("\\.", "_thumb.");
								int lastp = img.lastIndexOf(".");
								img = img.substring(0, lastp) + "_cutmid."
										+ img.substring(lastp + 1);
								resource.setNeturl(HttpUrlConstant.URL_HEAD
										+ img);
								resource
										.setType(Record.BATCHRECORD_TYPE_OF_IMAGE);
								resource
										.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
								rec.addResource(resource);
							}
						}
					}
					if ((jsonObject.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_VIDEO) == Record.BATCHRECORD_TYPE_OF_VIDEO) {
						if (jsonObject.has("videos")) {
							JSONArray imgs = jsonObject.optJSONArray("videos");
							String video = null;
							RecordResource resource = null;
							for (int m = 0; m < imgs.length(); m++) {
								resource = new RecordResource();
								video = (String) imgs.get(m);
								resource.setNeturl(video);
								resource
										.setType(Record.BATCHRECORD_TYPE_OF_VIDEO);
								resource
										.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
								rec.addResource(resource);
							}
						}
					}
					if ((jsonObject.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_AUDIO) == Record.BATCHRECORD_TYPE_OF_AUDIO) {
						if (jsonObject.has("audios")) {
							JSONArray imgs = jsonObject.optJSONArray("audios");
							String audio = null;
							RecordResource resource = null;
							for (int m = 0; m < imgs.length(); m++) {
								resource = new RecordResource();
								audio = (String) imgs.get(m);
								resource.setNeturl(audio);
								resource
										.setType(Record.BATCHRECORD_TYPE_OF_AUDIO);
								rec.addResource(resource);
							}
						}
					}
					if ((jsonObject.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_TEMPLATE) == Record.BATCHRECORD_TYPE_OF_TEMPLATE) {
						if (jsonObject.has("template")) {
							JSONArray templates = jsonObject.optJSONArray("template");
							if (templates != null && templates.length() > 0)
								rec.setContext(templates.get(0).toString());
						}

						LogUtil.logInfo(LoadRecordByIdThread.class,
								"template:" + jsonObject.getString("template"));
					}
					// 用户头像
					File fl = new File(AppConstant.USERIMG_DIR
							+ rec.getUser_id());
					if (fl.exists()) {
						rec.setUser_img(AppConstant.USERIMG_DIR
								+ rec.getUser_id());
					} else {
						String userimgurl = jsonObject.getString("person_imageurl");
						if (userimgurl != null) {
							Bitmap mBitmap = null;
							if (userimgurl.indexOf("http") >= 0)
								mBitmap = BitmapFactory.decodeStream(HttpUtil
										.getImageStream(userimgurl));
							else
								mBitmap = BitmapFactory
										.decodeStream(HttpUtil
												.getImageStream(HttpUrlConstant.URL_HEAD
														+ userimgurl));
							if (mBitmap != null) {
								userimgurl = rec.getUser_id();
								BitmapUtils.saveFile(mBitmap, userimgurl,
										AppConstant.USERIMG_DIR);
								rec.setUser_img(AppConstant.USERIMG_DIR
										+ userimgurl);
							}
						}
						LogUtil.logInfo(getClass(), "load img:"
								+ rec.getUser_id());
					}
					rec.setState(rec.getSave_type());
					rec.setVariety_name(jsonObject.getString("variety_name"));
 				Map<String, Object> rdata = new HashMap<String, Object>();
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						rec);
			} else {
				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
	}

}
