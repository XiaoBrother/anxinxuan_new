package com.axinxuandroid.activity.net;


import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.UserFavorite;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class AddUserFavoriteThread extends NetThread {

	private UserFavorite favorite;

	public AddUserFavoriteThread(UserFavorite favorite) {
		this.favorite = favorite;
	}

 	@Override
	public void requestHttp() {
 		if(this.favorite!=null){
 			String url = HttpUrlConstant.URL_HEAD
 			+ HttpUrlConstant.ADD_USER_FAVORITE ;
 	        LogUtil.logInfo(AddUserFavoriteThread.class, url);
 	        RequestParams params=new RequestParams();
 	        params.put("userid", favorite.getUser_id()+"");
 	        params.put("type",favorite.getFavorite_type()+"");
 	        params.put("favoriteid",favorite.getFavorite_id()+"");
  	        HttpUtil.get(url,params, this.jsonhand);
 		}else{
  			this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "无法添加用户收藏！"));
 		}
		
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				UserFavorite favorite = new UserFavorite();
				favorite.setFavid(jsonObject.getInt("id"));
				favorite.setFavorite_id(jsonObject.getInt("favorite_id"));
				favorite.setFavorite_type(jsonObject
						.getInt("favorite_type"));
				favorite.setLabel_name(jsonObject.getString("label_name"));
				favorite.setUser_id(jsonObject.getInt("user_id"));
				// 收藏成功
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "收藏成功！", favorite);
 			} else {
				// 收藏失败
  				return new NetResult(NetResult.RESULT_OF_ERROR, "收藏失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "收藏失败！", null);
		}
 	}
	
}
