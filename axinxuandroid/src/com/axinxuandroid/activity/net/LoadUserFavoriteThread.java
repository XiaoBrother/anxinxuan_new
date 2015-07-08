package com.axinxuandroid.activity.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.UserFavorite;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
 
public class LoadUserFavoriteThread extends NetThread {

	private int userid;
	private String lastoptime;
	public LoadUserFavoriteThread(int userid,String lastoptime){
		this.userid=userid;
		this.lastoptime=lastoptime==null?"":lastoptime;
	}
 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.USER_FAVORITE ;
		RequestParams params=new RequestParams();
		params.put("userid",  userid+"");
		params.put("lastoptime",DateUtil.delSpaceDate(lastoptime)  );
		HttpUtil.get(netdataurl,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject.getInt("hasdata")== HAS_DATA&&jsonObject.has("userfavorites")) {
				List<UserFavorite> ufs = new ArrayList<UserFavorite>();
				JSONArray array = jsonObject.getJSONArray("userfavorites");
				UserFavorite favorite;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					favorite = new UserFavorite();
					favorite.setFavid(jo.getInt("id"));
					favorite.setFavorite_id(jo.getInt("favorite_id"));
					favorite.setFavorite_type(jo.getInt("favorite_type"));
					favorite.setLabel_name(jo.getString("label_name"));
					favorite.setUser_id(jo.getInt("user_id"));
					favorite.setIsdel(jo.getInt("isdel"));
					favorite.setLastoptime(jo.getString("lastoptime"));
						ufs.add(favorite);
					}
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						ufs);
				
			}else{
  				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
  	}

}
