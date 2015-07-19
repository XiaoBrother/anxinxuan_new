package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.AntiFakePublishBatch;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;


public class LoadAntiFakeBatchListThread extends NetThread{
	private int user_id;
	private String lastoptime;
 	public LoadAntiFakeBatchListThread(int user_id,String lastoptime){
		this.user_id=user_id;
		this.lastoptime=lastoptime==null?"":lastoptime;
 	}
	 
	@Override
	public void requestHttp() {
		String url=HttpUrlConstant.URL_HEAD+HttpUrlConstant.Load_ANTIFAKE_BATCHLIST;
		RequestParams params=new RequestParams();
		params.put("user_id", user_id+"" );
		params.put("endtime", DateUtil.delSpaceDate(lastoptime) );
 		HttpUtil.get(url,params, this.jsonhand);
 		
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
 			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS&&jsonObject.getInt("hasdata") == HAS_DATA) {
				    List<AntiFakePublishBatch> pbts=new ArrayList<AntiFakePublishBatch>();
				   // int count=jsonObject.getInt("totalcount");
				    if(jsonObject.has("batchlists")){
 				    	JSONArray array = jsonObject.getJSONArray("batchlists");
						for (int i = 0; i < array.length(); i++) {
						JSONObject jo = array.getJSONObject(i);
						AntiFakePublishBatch pbatch=new AntiFakePublishBatch();
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
 						 pbts.add(pbatch);
					}
 				    }
				    Map<String,Object> rdata=new HashMap<String, Object>();
				//	rdata.put("datacount", count);
					rdata.put("datas", pbts);
 			        return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
 			        		rdata);
				}else{
 					 return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
				}
		} catch (Exception e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}
	
}
