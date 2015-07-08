package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Intelligence;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.VilleageBanner;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadVillageInfoThread extends NetThread {

	private Villeage vill;
	private String banneroptime,intelligenceoptime,varietyoptime;
	public LoadVillageInfoThread(Villeage vill,String banneroptime,String intelligenceoptime,String varietyoptime){
		this.vill=vill;
		this.banneroptime=banneroptime==null?"":banneroptime;
		this.intelligenceoptime=intelligenceoptime==null?"":intelligenceoptime;
		this.varietyoptime=varietyoptime==null?"":varietyoptime;
	}
	 
	
	private void initBanners(JSONObject jsonObject){
		List<VilleageBanner> banners = null;
		try{
			if (jsonObject.has("banners")) {
				banners = new ArrayList<VilleageBanner>();
				JSONArray array = jsonObject.getJSONArray("banners");
				VilleageBanner banner;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					banner = new VilleageBanner();
					banner.setBannerid(jo.getInt("id"));
					banner.setVilleage_id(jo.getInt("farm_id"));
					banner.setNeturl(jo.getString("banner_url"));
					banner.setThumb_url(jo.getString("thumb_url"));
					banner.setIsdel(jo.getInt("isdel"));
					banner.setLastoptime(jo.getString("lastoptime"));
					String loadurl=null;
					String localurl=AppConstant.BANNER_DIR+banner.getBannerid();
					if(FileUtil.hasFile(localurl)){
						banner.setLocalurl(localurl);
					}else{
						if(banner.getNeturl()!=null&&!"".equals(banner.getNeturl())){
							loadurl=banner.getNeturl();
						}
						if(loadurl!=null){
							Bitmap mBitmap=BitmapFactory.decodeStream(HttpUtil.getImageStream(HttpUrlConstant.URL_HEAD+loadurl));
						    if(mBitmap!=null){
		 					    	BitmapUtils.saveFile(mBitmap,banner.getBannerid()+"" , AppConstant.BANNER_DIR);
		 					    	banner.setLocalurl(localurl);
						    }
						}
					}
					  banners.add(banner);
				}
				vill.setBanners(banners);
			}
		}catch (Exception e) {
			e.printStackTrace();
 		}
 	}
	
    private void initUsers(JSONObject jsonObject){
    	List<User> users = null;
		try{
			if (jsonObject.has("users")) {
				users = new ArrayList<User>();
				JSONArray array = jsonObject.getJSONArray("users");
				User user;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					user = new User();
					user.setUser_id(jo.getInt("user_id"));
					user.setUser_name(jo.getString("nick_name"));
  					user.setPerson_imageurl(jo.getString("person_imageurl"));
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
					users.add(user);
				}
				vill.setUsers(users);
			}
		}catch (Exception e) {
			e.printStackTrace();
 		}
	}
    private void initIntelligences(JSONObject jsonObject){
    	List<Intelligence> intells = null;
		try{
			if (jsonObject.has("certificates")) {
				intells = new ArrayList<Intelligence>();
				JSONArray array = jsonObject.getJSONArray("certificates");
				Intelligence intell;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					intell = new Intelligence();
					intell.setVilleage_id(jo.getInt("farm_id"));
					intell.setCreate_time(jo.getString("create_time"));
					intell.setIntelligence_desc(jo.getString("certificate_desc"));
					String imgurl=jo.getString("certificate_imgurl");
					if(imgurl!=null&&!"".equals(imgurl)&&!"null".equals(imgurl))
					  intell.setIntelligence_imgurl(HttpUrlConstant.URL_HEAD+imgurl);
					intell.setIntelligenceid(jo.getInt("id"));
					intell.setLabel_name(jo.getString("label_name"));
					String thimgurl=jo.getString("thumbnail_url");
					if(thimgurl!=null&&!"".equals(thimgurl)&&!"null".equals(thimgurl))
 					     intell.setThumbnail_url(HttpUrlConstant.URL_HEAD+thimgurl);
 					intell.setIsdel(jo.getInt("isdel"));
 					intell.setLastoptime(jo.getString("lastoptime"));
 					intells.add(intell);
				}
				vill.setIntelligences(intells);
			}
		}catch (Exception e) {
			e.printStackTrace();
 		}
	}
    private void initVariets(JSONObject jsonObject){
    	List<Variety> varities = null;
		try{
			if (jsonObject.has("varities")) {
				varities = new ArrayList<Variety>();
				JSONArray array = jsonObject.getJSONArray("varities");
				Variety variety;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					variety = new Variety();
					variety.setVariety_id(jo.getInt("id"));
					variety.setVariety_name(jo.getString("variety_name"));
					variety.setVilleage_id(jo.getInt("farm_id"));
					variety.setUser_id(jo.getInt("user_id"));
					variety.setCategory_id(jo.getInt("category_id"));
					variety.setCreate_time(jo.getString("create_time"));
					variety.setVariety_desc(jo.getString("variety_desc"));
					variety.setIsdel(jo.getInt("isdel"));
					variety.setLastoptime(jo.getString("lastoptime"));
					varities.add(variety);
				}
				vill.setVarieties(varities);
			}
		}catch (Exception e) {
			e.printStackTrace();
 		}
	}
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.VILLEAGE_INFO ;
		RequestParams params=new RequestParams();
		params.put("vid",vill.getVilleage_id()+"" );
		params.put("banneroptime",  DateUtil.delSpaceDate(banneroptime));
		params.put("certificateoptime", DateUtil.delSpaceDate(intelligenceoptime) );
		params.put("varietyoptime",  DateUtil.delSpaceDate(varietyoptime));
 		HttpUtil.get(netdataurl,params, this.jsonhand);   
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject.getInt("hasdata")== HAS_DATA){
				initUsers(jsonObject);
				initBanners(jsonObject);
				initIntelligences(jsonObject);
				initVariets(jsonObject);
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						vill);
			}else{
 	 			return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
 			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
  	}
	
	
}
