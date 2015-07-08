package com.axinxuandroid.activity.net;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class CheckVersionThread extends NetThread{
   

	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.CHECK_VERSION;
		HttpUtil.get(netdataurl, this.jsonhand);
	}

	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				Version version=new Version();
				version.setDownurl(jsonObject.getString("downurl"));
				version.setImportant(jsonObject.getInt("important"));
				version.setVersion((float)jsonObject.getDouble("version"));
				version.setVersiondesc(jsonObject.getString("versiondesc"));
				version.setHistory(jsonObject.getString("history"));
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "检测更新成功！", version);
			}else{
				return new NetResult(NetResult.RESULT_OF_ERROR, "检测更新失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "检测更新失败！");
		}
 	}
	
	  

}
