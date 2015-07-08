package com.axinxuandroid.activity.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Template;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

/**
 * 获取更新品种
 * 
 * @author Administrator
 * 
 */
public class LoadCategoryThread extends NetThread {
 	private String lastoptime;
	public LoadCategoryThread(String lastoptime){
		this.lastoptime=lastoptime;
	}
	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.LOAD_CATEGORY;
		RequestParams params=new RequestParams();
		params.put("lastoptime", DateUtil.delSpaceDate(lastoptime));
        HttpUtil.get(netdataurl,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS) {
				 List<Category> cates=null;
					if(jsonObject.getInt("hasdata")== HAS_DATA){
						cates=new ArrayList<Category>();
						JSONArray array = jsonObject.getJSONArray("category");
						Category cate;
					for (int i = 0; i < array.length(); i++) {
						cate=new Category();
						JSONObject jo=  array.getJSONObject(i);
						cate=new Category();
						cate.setId(jo.getInt("id"));
						cate.setCategory_name(jo.getString("category_name"));
						if(jo.has("cate_order")&&!jo.isNull("cate_order"))
						  cate.setCate_order(jo.getInt("cate_order"));
						cate.setClevel(jo.getInt("clevel"));
						cate.setLastoptime(jo.getString("lastoptime"));
						cate.setIsdel(jo.getInt("isdel"));
						cate.setParentid(jo.getInt("parentid"));
 						cates.add(cate);
					}
				} 
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
 						cates);
				}else{
 					return new NetResult(NetResult.RESULT_OF_ERROR, "无法获取更新！");
				}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}

 
	 
}
