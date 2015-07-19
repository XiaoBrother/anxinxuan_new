package com.axinxuandroid.activity.net;

 import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.AntiFakeQuerySet;
import com.axinxuandroid.data.Record;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;

public class SaveAntiFakeQuerySetThread  extends NetThread{

	private AntiFakeQuerySet qset;
  	public SaveAntiFakeQuerySetThread(AntiFakeQuerySet qset){
		this.qset=qset;
	}
 
 
	
	@Override
	public void requestHttp() {
 			String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.SAVE_ANTIFAKE_SAVESET;
  			RequestParams params=new RequestParams();
	   		if (qset != null) {
	   			params.put("id", qset.getId()+"");
	   			params.put("user_id", qset.getUser_id()+"");
	   			params.put("stitle",qset.getStitle());
  				params.put("bottominf", qset.getBottominf());
  				params.put("sinaweibo", qset.getSinaweibo());
  				params.put("weixin", qset.getWeixin());
  				params.put("weidian", qset.getWeidian());
  				params.put("tel", qset.getTel());
  				/*	String senddate= DateUtil.dateToStrWithPattern(new Date(),
				"yyyy-MM-dd HH:mm:ss");
				qset.setSend_date(senddate);
  				params.put("senddate", DateUtil.delSpaceDate(senddate));*/
 			}
 		HttpUtil.get(url,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				// 保存成功
					qset.setId(jsonObject.getInt("id"));
					qset.setLastoptime(jsonObject.getString("lastoptime"));
					 return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！",
							 qset);
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
