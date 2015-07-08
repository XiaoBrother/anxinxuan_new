package com.axinxuandroid.activity.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
 
public class LoadUserInfoThread extends NetThread {

	private int  userid;
 
	public LoadUserInfoThread(int userid) {
		this.userid = userid;
 	}
 

	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD + HttpUrlConstant.USER_INFO;
		RequestParams params=new RequestParams();
		params.put("userid", userid+"");
		HttpUtil.get(url, params,this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				User user = new User();
				user.setUser_id(jsonObject.getInt("user_id"));
					user.setEmail(jsonObject.getString("email"));
				user.setPerson_desc(jsonObject.getString("person_desc"));
				user.setPerson_imageurl(jsonObject
						.getString("person_imageurl"));
				if(jsonObject.has("sex")){
					String sex=jsonObject.getString("sex");
					if(sex!=null){
						if(User.SEX_MAN.equals(sex))
							user.setSex("男");
						else if(User.SEX_WOMAN.equals(sex))
							user.setSex("女");
					}
				}
				if(user.getPerson_imageurl()!=null){
					String userurl=user.getPerson_imageurl();
					Bitmap mBitmap =null;
					if(user.getPerson_imageurl().indexOf("http")>=0)
						mBitmap=BitmapFactory.decodeStream(HttpUtil.getImageStream(userurl));
					else mBitmap=BitmapFactory.decodeStream(HttpUtil.getImageStream(HttpUrlConstant.URL_HEAD+userurl));
				    if(mBitmap!=null){
				    	userurl=user.getUser_id()+"";
					    	BitmapUtils.saveFile(mBitmap,userurl , AppConstant.USERIMG_DIR);
				    	user.setLocal_imgurl(AppConstant.USERIMG_DIR+userurl);
				    }
					}
					user.setUser_name(jsonObject.getString("nick_name"));
					return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
							user);
  			} else {
				// 查询失败
  	 			return new NetResult(NetResult.RESULT_OF_ERROR, "获取用户信息失败！");
 			}
		} catch (Exception e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}
	
}
