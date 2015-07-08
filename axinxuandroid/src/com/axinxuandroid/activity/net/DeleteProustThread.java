package com.axinxuandroid.activity.net;
import org.json.JSONException;
import org.json.JSONObject;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;

public class DeleteProustThread extends NetThread{
    private int proust_id;
    private User user;
 	public DeleteProustThread(int proust_id,User user){
		this.proust_id=proust_id;
		this.user=user;
 	}
 	 
	@Override
	public void requestHttp() {
		if(user!=null){
			String netdataurl = HttpUrlConstant.URL_HEAD
			+ HttpUrlConstant.DELETE_RROUST ;
			RequestParams params=new RequestParams();
			params.put("proust_id",  user.getUser_id()+"");
			params.put("user_id",  proust_id+"");
 			HttpUtil.get(netdataurl, params,this.jsonhand);
		}else this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "���ȵ�¼��"));
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "ɾ���ɹ���");
			}else{
 				return new NetResult(NetResult.RESULT_OF_ERROR, "ɾ��ʧ�ܣ�");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "����ʧ�ܣ�");
		}
 	}
	
	  

}
