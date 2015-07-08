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
import com.axinxuandroid.data.Template;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

/**
 * 获取更新模板
 * 
 * @author Administrator
 * 
 */
public class LoadTemplateThread extends NetThread {
 	private String lastoptime;
	public LoadTemplateThread(String lastoptime){
		this.lastoptime=lastoptime;
	}
	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.LOAD_TEMPLATE;
		RequestParams params=new RequestParams();
		params.put("lastoptime", DateUtil.delSpaceDate(lastoptime));
        HttpUtil.get(netdataurl,params, this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == NET_RETURN_SUCCESS) {
				List<Template> templates=null;
					if(jsonObject.getInt("hasdata")== HAS_DATA){
						templates=new ArrayList<Template>();
						JSONArray array = jsonObject.getJSONArray("templates");
						Template template;
					for (int i = 0; i < array.length(); i++) {
						template=new Template();
						JSONObject jo=  array.getJSONObject(i);
							template.setTemplate_id(jo.getInt("id"));
						if(!jo.isNull("category_id"))
							template.setCategory_id(jo.getInt("category_id"));
						template.setLabel_name(jo.getString("label_name"));
						template.setContext(jo.getString("context"));
						template.setFoot(jo.getString("foot"));
						template.setHead(jo.getString("head"));
						template.setSign(jo.getString("sign"));
						template.setVersion(jo.getInt("version"));
						template.setIsdel(jo.getInt("isdel"));
						template.setLastoptime(jo.getString("lastoptime"));
						templates.add(template);
					}
				} 
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						templates);
				}else{
 					return new NetResult(NetResult.RESULT_OF_ERROR, "无法获取更新！");
				}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
 		}
 	}

 
	 
}
