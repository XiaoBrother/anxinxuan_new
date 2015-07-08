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
import com.axinxuandroid.data.VilleagePhoto;
import com.axinxuandroid.sys.gloable.Gloable;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadVillagePhotoThread extends NetThread {

	private int villid;
	private String lastoptime;
	public LoadVillagePhotoThread(int villid,String lastoptime){
		this.villid=villid;
		this.lastoptime=lastoptime==null?"":lastoptime;
	}
	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.VILLEAGE_PHOTO  ;
		RequestParams params=new RequestParams();
		params.put("vid", villid+"" );
		params.put("lastoptime",DateUtil.delSpaceDate(lastoptime)  );
		HttpUtil.get(netdataurl,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject.getInt("hasdata")== HAS_DATA&&jsonObject.has("photos")) {
				List<VilleagePhoto> photos = new ArrayList<VilleagePhoto>();
				JSONArray array = jsonObject.getJSONArray("photos");
				VilleagePhoto photo;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					photo = new VilleagePhoto();
					photo.setPhotoid(jo.getInt("id"));
					photo.setImage_desc(jo.getString("image_desc"));
					photo.setImage_name(jo.getString("image_name"));
					photo.setImage_url(HttpUrlConstant.URL_HEAD+jo.getString("image_url"));
					photo.setIndex_order(jo.getInt("index_order"));
					photo.setLabel_name(jo.getString("label_name"));
					photo.setThumbnail_url(HttpUrlConstant.URL_HEAD+jo.getString("thumbnail_url"));
					photo.setVilleage_id(jo.getInt("farm_id"));
					photo.setIsdel(jo.getInt("isdel"));
					photo.setLastoptime(jo.getString("lastoptime"));
					if(photo.getImage_url()!=null){
						    int lastp=photo.getImage_url().lastIndexOf(".");
					    String img=photo.getImage_url().substring(0,lastp)+"_cutmid."+photo.getImage_url().substring(lastp+1);
					    photo.setThumbnail_url(img);
					}
					photos.add(photo);
				}
  				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						photos);
			}else{
 				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}
	
	

	
 
}
