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
import com.axinxuandroid.data.OAuthAccount;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.sina.weibo.sdk.utils.MD5;
 
public class UserLoginThread extends NetThread {

	private String username;
	private String pwd;

	public UserLoginThread(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}
 
	@Override
	public void requestHttp() {
		String url = HttpUrlConstant.URL_HEAD + HttpUrlConstant.USER_LOGIN;
		RequestParams params=new RequestParams();
		params.put("user", username);
		params.put("pwd",MD5.hexdigest(pwd));
		HttpUtil.get(url,params, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
 			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") ==NET_RETURN_SUCCESS&&jsonObject.getInt("hasdata") == HAS_DATA) {
				// µÇÂ¼³É¹¦
					User user = new User();
				user.setUser_id(jsonObject.getInt("user_id"));
				user.setPhone(jsonObject.getString("phone"));
				user.setEmail(jsonObject.getString("email"));
				user.setPerson_desc(jsonObject.getString("person_desc"));
				user.setPerson_imageurl(jsonObject.getString("person_imageurl"));
				user.setWorktime(jsonObject.getString("work_time"));
				user.setAddress(jsonObject.getString("areaname"));
 				if(jsonObject.has("sex")){
					String sex=jsonObject.getString("sex");
					if(sex!=null){
						if(User.SEX_MAN.equals(sex))
							user.setSex("ÄÐ");
						else if(User.SEX_WOMAN.equals(sex))
							user.setSex("Å®");
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
				user.setLogin_time(DateUtil.dateToStrWithPattern(
						new Date(), "yyyy-MM-dd HH:mm:ss"));
				if (jsonObject.has("farms")) {
					JSONArray villeages = jsonObject.getJSONArray("farms");
					if (villeages != null && villeages.length() > 0) {
						List<UserVilleage> uvils = new ArrayList<UserVilleage>();
						for (int i = 0; i < villeages.length(); i++) {
							JSONObject object = villeages.getJSONObject(i);
							UserVilleage uv = new UserVilleage();
							uv.setUser_id(user.getUser_id());
								uv.setVilleage_id(object.getInt("farm_id"));
								uv.setRole(object.getInt("work_role"));
								Villeage vil=new Villeage();
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
								vil.setIsdel(object.getInt("isdel"));
								vil.setLastoptime(object.getString("lastoptime"));
								if(object.has("lat")&&!object.isNull("lat"))
								   vil.setLat(object.getDouble("lat"));
								if(object.has("lng")&&!object.isNull("lng"))
								vil.setLng(object.getDouble("lng"));
							//uv.setRole(object.getInt("work_role"));
								uv.setVilleage(vil);
							uvils.add(uv);
						}
						user.setUservilleages(uvils);
					}
				}
				if (jsonObject.has("oauths")) {
					JSONArray oauths = jsonObject.getJSONArray("oauths");
					if (oauths != null && oauths.length() > 0) {
						List<OAuthAccount> accounts = new ArrayList<OAuthAccount>();
						for (int i = 0; i < oauths.length(); i++) {
							JSONObject object = oauths.getJSONObject(i);
							OAuthAccount ac = new OAuthAccount();
							ac.setUserid(object.getInt("userid"));
							ac.setAccount_id(object.getString("account_id"));
							ac.setAuthorizeimg(object.getString("authorizeimg"));
							ac.setNick_name(object.getString("nick_name"));
							ac.setType(object.getInt("type"));
								accounts.add(ac);
						}
						user.setOauths(accounts);
					}
				}
			   	return new NetResult(NetResult.RESULT_OF_SUCCESS, "µÇÂ¼³É¹¦£¡",user);
			} else {
				// µÇÂ¼Ê§°Ü
				return new NetResult(NetResult.RESULT_OF_ERROR, "µÇÂ¼Ê§°Ü£¡");
			}
		} catch (Exception e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "²Ù×÷Ê§°Ü£¡");
 		}
	}
	
	
}
