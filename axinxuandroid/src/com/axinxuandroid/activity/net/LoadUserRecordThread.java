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

public class LoadUserRecordThread extends NetThread {

  	private int userid;
	private String starttime;
	private String endtime;
 	public LoadUserRecordThread(int userid,String starttime, String endtime) {
		this.userid = userid;
		this.starttime = starttime==null?"":starttime;
		this.endtime = endtime==null?"":endtime;
  	}

	 

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.USER_RECORDS  ;
		RequestParams params=new RequestParams();
		params.put("starttime", DateUtil.delSpaceDate(starttime) );
		params.put("endtime", DateUtil.delSpaceDate(endtime) );
		params.put("userid",  userid+"");
 		HttpUtil.get(url, params,this.jsonhand);
 	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS&&jsonObject.getInt("hasdata") == HAS_DATA){
				List<Record> recorddata = null;
 					int count=jsonObject.getInt("totalcount");
					if (jsonObject.has("records")) {
						JSONArray array = jsonObject.getJSONArray("records");
						Record rec = null;
						recorddata=new ArrayList<Record>();
						for (int i = 0; i < array.length(); i++) {
							rec = new Record();
							JSONObject jo = array.getJSONObject(i);
							rec.setType(jo.getInt("type"));
							rec.setUser_id(jo.getString("user_id"));
							rec.setVilleage_id(jo.getInt("farm_id"));
							rec.setSave_type(jo.getInt("save_type"));
							rec.setRecord_id(jo.getInt("record_id"));
							rec.setLabel_name(jo.getString("label_name"));
							rec.setNick_name(jo.getString("nick_name"));
							rec.setBatch_id(jo.getInt("batch_id"));
							rec.setSave_date(jo.getString("create_time"));
							rec.setSend_date(jo.getString("save_date"));
							rec.setIsdel(jo.getInt("isdel"));
							rec.setLastoptime(jo.getString("lastoptime"));
							rec.setLng(jo.getDouble("lng"));
							rec.setLat(jo.getDouble("lat"));
							rec.setContext("");
							rec.setTrace_code(jo.getString("batch_code"));
							rec.setVariety_id(jo.getInt("variety_id"));
							rec.setVariety_name(jo.getString("variety_name"));
							rec.setUser_type(jo.getInt("user_type"));
							if ((jo.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_TEXT) == Record.BATCHRECORD_TYPE_OF_TEXT) {
								if(jo.has("context"))
								  rec.setContext(jo.getString("context"));
								}
							if ((jo.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_IMAGE) == Record.BATCHRECORD_TYPE_OF_IMAGE) {
								if (jo.has("images")) {
									JSONArray imgs = jo.optJSONArray("images");
									String img = null;
									RecordResource resource = null;
									for (int m = 0; m < imgs.length(); m++) {
										resource = new RecordResource();
										img = (String) imgs.get(m);
										//img = img.replaceAll("\\.", "_thumb.");
									    	int lastp=img.lastIndexOf(".");
									    	img=img.substring(0,lastp)+"_cutmid."+img.substring(lastp+1);
										resource.setNeturl(HttpUrlConstant.URL_HEAD
														+ img);
										resource.setType(Record.BATCHRECORD_TYPE_OF_IMAGE);
										resource.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
										rec.addResource(resource);
									}
								}
							}
							if ((jo.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_VIDEO) == Record.BATCHRECORD_TYPE_OF_VIDEO) {
								if (jo.has("videos")) {
									JSONArray imgs = jo.optJSONArray("videos");
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
							if ((jo.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_AUDIO) == Record.BATCHRECORD_TYPE_OF_AUDIO) {
								if (jo.has("audios")) {
									JSONArray imgs = jo.optJSONArray("audios");
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
							if ((jo.getInt("save_type") & Record.BATCHRECORD_TYPE_OF_TEMPLATE) == Record.BATCHRECORD_TYPE_OF_TEMPLATE) {
								if (jo.has("template")) {
									JSONArray templates = jo
											.optJSONArray("template");
									if (templates != null
											&& templates.length() > 0)
										rec.setContext(templates.get(0)
												.toString());
								}

								LogUtil.logInfo(LoadUserRecordThread.class,
										"template:" + jo.getString("template"));
							}
							// 用户头像
							File fl = new File(AppConstant.USERIMG_DIR
									+ rec.getUser_id());
							if (fl.exists()) {
								rec.setUser_img(AppConstant.USERIMG_DIR
										+ rec.getUser_id());
							} else {
								String userimgurl = jo
										.getString("person_imageurl");
								if (userimgurl != null) {
									Bitmap mBitmap = null;
									if (userimgurl.indexOf("http") >= 0)
										mBitmap = BitmapFactory
												.decodeStream(HttpUtil
														.getImageStream(userimgurl));
									else
										mBitmap = BitmapFactory
												.decodeStream(HttpUtil
														.getImageStream(HttpUrlConstant.URL_HEAD
																+ userimgurl));
									if (mBitmap != null) {
										userimgurl = rec.getUser_id();
										BitmapUtils.saveFile(mBitmap,
												userimgurl,
												AppConstant.USERIMG_DIR);
										rec.setUser_img(AppConstant.USERIMG_DIR
												+ userimgurl);
									}
								}
								}
							rec.setState(rec.getSave_type());
							
							recorddata.add(rec);
						}
					}
 					Map<String,Object> rdata=new HashMap<String, Object>();
					rdata.put("datacount", count);
					rdata.put("datas", recorddata);
					return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
	  						rdata);
 				}else{
					return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
				}
		}  catch (Exception e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
 	}

}
