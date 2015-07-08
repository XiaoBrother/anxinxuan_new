package com.axinxuandroid.activity.net;

 import java.util.Date;
import java.util.List;
 
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
 import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
 
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SaveRecordThread  extends NetThread{

	private Record record;
  	public SaveRecordThread(Record record){
		this.record=record;
	}
 
 
	
	@Override
	public void requestHttp() {
 			String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.URL_SAVERECORD;
  			RequestParams params=new RequestParams();
	   		if (record != null) {
	   			params.put("user_id", record.getUser_id());
	   			params.put("labelname",record.getLabel_name());
	   			params.put("date", DateUtil.delSpaceDate(record.getSave_date()));
 				String senddate= DateUtil.dateToStrWithPattern(new Date(),
				"yyyy-MM-dd HH:mm:ss");
				record.setSend_date(senddate);
  				params.put("senddate", DateUtil.delSpaceDate(senddate));
  				params.put("batch_id", record.getBatch_id()+"");
  				params.put("save_type", record.getSave_type()+"");
  				params.put("user_type", record.getUser_type()+"");
  				params.put("lat", record.getLat()+"");
  				params.put("lng", record.getLng()+"");
 	 			if(record.getRecord_id()>0){
 	 				params.put("record_id", record.getRecord_id()+"");
 	 			}
 	 			if((record.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEXT)==Record.BATCHRECORD_TYPE_OF_TEXT){
 	 				params.put("recordtext", record.getContext());
 	 			}
 	  			else if((record.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEMPLATE)==Record.BATCHRECORD_TYPE_OF_TEMPLATE){
 	  				params.put("tablejson",  record.getContext());
 	  			}
 			}
 		HttpUtil.get(url,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				// 保存成功
					record.setRecord_id(jsonObject.getInt("recordid"));
					record.setLastoptime(jsonObject.getString("lastoptime"));
					 return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！",
							 record);
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
