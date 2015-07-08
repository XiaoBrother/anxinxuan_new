package com.axinxuandroid.activity.net;

import java.net.URLEncoder;
import java.util.List;
 
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Proust;
 import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
 
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SaveProustThread  extends NetThread{

	private Proust pro;
    private User user;
  	public SaveProustThread(Proust pro,User user){
	   this.pro=pro;
	   this.user=user;
 	}
	 
	@Override
	public void requestHttp() {
		if(user!=null){
				String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.UPDATE_RROUST;
 				RequestParams params=new RequestParams();
				params.put("user_id", pro.getUser_id()+"");
				params.put("answer",  pro.getAnswer()  );
				params.put("proust_id",pro.getProust_id()+"" );
				HttpUtil.get(url, params,this.jsonhand);
		}else{
			this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "请先登录！"));
		}
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
					pro.setCreate_date(jsonObject.getString("create_time"));
					pro.setIsdel(jsonObject.getInt("isdel"));
					pro.setLastoptime(jsonObject.getString("lastoptime"));
					// 保存成功
				    return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！",
				    		pro);
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
