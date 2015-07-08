package com.axinxuandroid.activity.net;
import org.json.JSONException;
import org.json.JSONObject;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.User;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class UpdateBatchStageThread extends NetThread{
    private Batch batch;
    private int newstage;
    private User user;
  	public UpdateBatchStageThread(Batch batch,int newstage,User user){
		this.batch=batch;
		this.newstage=newstage;
		this.user=user;
 	}
  
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.BATCH_STAGE ;
		RequestParams params=new RequestParams();
		params.put("batchid",  batch.getBatch_id()+"");
		params.put("userid",user.getUser_id()+"");
		params.put("farmid",  batch.getVilleage_id()+"");
		params.put("stage",  newstage+"");
 		HttpUtil.get(netdataurl,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "更新成功！");		
			}else{
				return new NetResult(NetResult.RESULT_OF_ERROR, "更新失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
	}
	
	  

}
