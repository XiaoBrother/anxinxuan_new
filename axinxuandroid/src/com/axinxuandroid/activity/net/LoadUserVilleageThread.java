package com.axinxuandroid.activity.net;

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
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
 
public class LoadUserVilleageThread extends NetThread {

	private int userid;
	public LoadUserVilleageThread(int userid) {
		this.userid = userid;
  	}

	 
	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD + HttpUrlConstant.USER_VILLEAGE;
 		RequestParams params=new RequestParams();
		params.put("userid", userid+"");
		HttpUtil.get(url,params, this.jsonhand);
 	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				List<UserVilleage> uvils = new ArrayList<UserVilleage>();
					if (jsonObject.has("farms")) {
					JSONArray villeages = jsonObject.getJSONArray("farms");
					if (villeages != null && villeages.length() > 0) {
							for (int i = 0; i < villeages.length(); i++) {
							JSONObject object = villeages.getJSONObject(i);
							UserVilleage uv = new UserVilleage();
							uv.setUser_id(userid);
								uv.setVilleage_id(object.getInt("farm_id"));
								uv.setRole(object.getInt("work_role"));
							Villeage vil = new Villeage();
							vil.setVilleage_id(object.getInt("farm_id"));
							vil.setVilleage_name(object.getString("farm_name"));
							vil.setManager_name(object.getString("manager_name"));
							vil.setTele(object.getString("tele"));
							vil.setAddress(object.getString("address"));
							vil.setCollect_code(object.getString("collect_code"));
							vil.setManage_scope(object.getString("manage_scope"));
							vil.setScale(object.getString("scale"));
							vil.setType(object.getInt("type"));
							vil.setBulid_time(object.getString("bulid_time"));
							vil.setVilleage_desc(object.getString("farm_desc"));
							vil.setBulid_time(object.getString("bulid_time"));
							vil.setIsdel(object.getInt("isdel"));
							vil.setLastoptime(object.getString("lastoptime"));
  							if(object.has("lat")&&!object.isNull("lat"))
								vil.setLat(object.getDouble("lat"));
							if(object.has("lng")&&!object.isNull("lng"))
								vil.setLng(object.getDouble("lng"));
							uv.setVilleage(vil);
							//uv.setRole(object.getInt("work_role"));
								uvils.add(uv);
						}
						}
				}
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						uvils);
			} else {
  				return new NetResult(NetResult.RESULT_OF_ERROR, "数据加载失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}
	
}
