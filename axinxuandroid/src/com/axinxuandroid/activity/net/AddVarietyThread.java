package com.axinxuandroid.activity.net;

import java.net.URLEncoder;
import java.util.List;
 
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Comment;
 import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
 
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class AddVarietyThread  extends NetThread{

    private Variety variety;
    private User user;
	public AddVarietyThread(Variety variety,User user){
	   this.variety=variety;
	   this.user=user;
 	}
	 
	@Override
	public void requestHttp() {
		if(user!=null){
			String name=variety.getVariety_name()==null?"":variety.getVariety_name();
			String desc=variety.getVariety_desc()==null?"":variety.getVariety_desc();
			String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.ADD_VARIETY;
 			RequestParams params=new RequestParams();
			params.put("userid",variety.getUser_id()+""  );
			params.put("categoryid",variety.getCategory_id()+""  );
			params.put("farmid",variety.getVilleage_id() +"");
			params.put("name",name  );
			params.put("desc", desc );
 			HttpUtil.get(url, params,jsonhand);
		}else this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "请先登录！"));
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				variety.setVariety_id(jsonObject.getInt("id"));
				variety.setVariety_name(jsonObject.getString("variety_name"));
				variety.setVilleage_id(jsonObject.getInt("farm_id"));
				variety.setUser_id(jsonObject.getInt("user_id"));
				variety.setCategory_id(jsonObject.getInt("category_id"));
				variety.setCreate_time(jsonObject.getString("create_time"));
				variety.setVariety_desc(jsonObject.getString("variety_desc"));
					// 保存成功
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "收藏成功！", variety);
			} else {
					// 保存失败
				return new NetResult(NetResult.RESULT_OF_ERROR, "保存失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "保存失败！");
		}
 	}
 
	 
}
