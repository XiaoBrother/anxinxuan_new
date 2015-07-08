package com.axinxuandroid.activity.net;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.sina.weibo.sdk.utils.MD5;

public class ResetPwdThread extends NetThread{
	private int userid;
	private String pwd;
    public ResetPwdThread(int userid,String pwd){
    	this.userid=userid;
    	this.pwd=pwd;
    }
  
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.USER_RESET_PWD;
		RequestParams params=new RequestParams();
		params.put("userid", userid+"");
		params.put("pwd", MD5.hexdigest(pwd));
 		HttpUtil.get(netdataurl,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
					return new NetResult(NetResult.RESULT_OF_SUCCESS, "设置成功！");
			}else{
				return new NetResult(NetResult.RESULT_OF_ERROR, "设置失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}
	
	  

}
