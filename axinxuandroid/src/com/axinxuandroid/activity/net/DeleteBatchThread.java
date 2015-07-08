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

public class DeleteBatchThread extends NetThread{
    private Batch batch;
    private User user;
 	public DeleteBatchThread(Batch batch,User user){
		this.batch=batch;
		this.user=user;
 	}
 	 
	@Override
	public void requestHttp() {
		if(user!=null){
			String netdataurl = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.DELETE_BATCH ;
			 RequestParams params=new RequestParams();
			 params.put("userid", user.getUser_id()+"" );
			 params.put("farmid",  batch.getVilleage_id()+"");
			 params.put("batchid",  batch.getBatch_id()+"");
			HttpUtil.get(netdataurl, params,this.jsonhand);
		}else  this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "ÇëÏÈµÇÂ¼£¡"));
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "É¾³ý³É¹¦£¡");
			}else{
 				return new NetResult(NetResult.RESULT_OF_ERROR, "É¾³ýÊ§°Ü£¡");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "ÏµÍ³Òì³££¡");
		}
 	}
	
	  

}
