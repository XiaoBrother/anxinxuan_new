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

public class UpdateBatchStatusThread extends NetThread{
    private Batch batch;
    private int status;
    private User user;
  	public UpdateBatchStatusThread(Batch batch,int status,User user){
		this.batch=batch;
		this.status=status;
		this.user=user;
 	}
 	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.BATCH_STATUS;
 		RequestParams params=new RequestParams();
		params.put("batchid", batch.getBatch_id()+"");
		params.put("userid", user.getUser_id()+"");
		params.put("farmid", batch.getVilleage_id()+"");
		params.put("status",status+"");
 		HttpUtil.get(netdataurl,params, this.jsonhand); 
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "���³ɹ���");		
			}else{
				return new NetResult(NetResult.RESULT_OF_ERROR, "����ʧ�ܣ�");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "����ʧ�ܣ�");
		}
	}
	
	  

}
