package com.axinxuandroid.activity.net;
 
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Batch;
 
import com.axinxuandroid.data.User;
 
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
 
public class AddBatchThread  extends NetThread{

	private int villeageid;
	private int varietyid;
	private int status;
	private int stage;
	private User user;
	public AddBatchThread(int villeageid,int varietyid,int stage,int status,User user){
	   this.villeageid=villeageid;
	   this.varietyid=varietyid;
	   this.stage=stage;
	   this.status=status;
	   this.user=user;
	}
	 
	@Override
	public void requestHttp() {
		if(user!=null){
			String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.ADD_BATCH;
			RequestParams params=new RequestParams();
			params.put("farmid", villeageid+"");
			params.put("varietyid", varietyid+"");
			params.put("userid", user.getUser_id()+"");
			params.put("status", status+"");
			params.put("stage", stage+"");
			HttpUtil.get(url, params,this.jsonhand);
		}else this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "请先登录！"));
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
 			try {
			 if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
					 Batch btch=new Batch();
					 btch.setBatch_id(jsonObject.getLong("id"));
					 btch.setCode(jsonObject.getString("batch_code"));
					 btch.setProducecount( jsonObject.getInt("producecount"));
					 btch.setVariety(jsonObject.getString("variety_name"));
					 btch.setVariety_id(jsonObject.getLong("variety_id"));
					 btch.setVilleage_id(jsonObject.getInt("farm_id"));
					 btch.setStatus(jsonObject.getInt("status"));
					 btch.setStage(jsonObject.getInt("stage"));
					 btch.setCategoryid(jsonObject.getInt("categoryid"));
					 btch.setOrder_index(0);
					 btch.setOrder_date(btch.getBatch_id());
					 btch.setConsumercount( jsonObject.getInt("consumercount"));
					 btch.setCreate_time( jsonObject.getString("create_time"));
					// 保存成功
 					return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！", btch);
				} else {
					// 保存失败
					return new NetResult(NetResult.RESULT_OF_ERROR, "保存失败！");
				}
			} catch (JSONException e) {
 				e.printStackTrace();
 				return new NetResult(NetResult.RESULT_OF_ERROR, "系统异常！");
			}
 	}
	 
 
	 
}
