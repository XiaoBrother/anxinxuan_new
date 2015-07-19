package com.axinxuandroid.activity.net;

 import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.AntiFakePublishBatch;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;

public class SaveAntiFakePublishBatchThread  extends NetThread{

	private  AntiFakePublishBatch pbatch ;
  	public SaveAntiFakePublishBatchThread(AntiFakePublishBatch pbatch){
		this.pbatch=pbatch;
	}
 
 
	
	@Override
	public void requestHttp() {
 			String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.SAVE_ANTIFAKE_PUBLISHBATCH;
  			RequestParams params=new RequestParams();
	   		if (pbatch != null) {
	   			params.put("user_id", pbatch.getUser_id() +"");
	   			params.put("tcount",pbatch.getTcount()+"");
  				params.put("batchtime", pbatch.getBatchtime());
  				params.put("variety_id", pbatch.getVariety_id()+"");
  				params.put("batchid", pbatch.getBatchid()+"");
  				params.put("bdesc", pbatch.getBdesc());
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
			    if(jsonObject.has("sbatch")){
			    	JSONObject jo = jsonObject.getJSONObject("sbatch");
					 pbatch.setId(jo.getInt("id"));
					 pbatch.setBatchtime(jo.getString("batchtime"));
					 pbatch.setBdesc(jo.getString("bdesc"));
					 pbatch.setCreatetime(jo.getString("createtime"));
					 pbatch.setEndnum(jo.getString("enum"));
					 pbatch.setLastoptime(jo.getString("lastoptime"));
					 pbatch.setSnum(jo.getString("snum"));
					 pbatch.setStatus(jo.getInt("status"));
					 pbatch.setTcount(jo.getInt("tcount"));
					 pbatch.setTimebatch(jo.getString("timebatch"));
					 pbatch.setUser_id(jo.getInt("user_id"));
					 if(jo.has("variety_id")&&!jo.isNull("variety_id"))
					 {
						 pbatch.setVariety_id(jo.getInt("variety_id"));
					 } 
					 if(jo.has("batchid")&&!jo.isNull("batchid"))
					 {
						 pbatch.setBatchid(jo.getInt("batchid"));
					 } 
					// pbatch.setIsdel( jo.getInt("isdel"));
					 pbatch.setLastoptime( jo.getString("lastoptime"));
				    }
					 return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！",
							 pbatch);
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
