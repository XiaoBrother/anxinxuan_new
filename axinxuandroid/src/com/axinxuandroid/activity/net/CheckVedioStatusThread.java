package com.axinxuandroid.activity.net;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.VideoInfo;
import com.axinxuandroid.oauth.YukuOAuth;
 import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.youku.uploader.YoukuUploader;

public class CheckVedioStatusThread extends NetThread{
   
	private String video_id;
	public CheckVedioStatusThread(String vid){
		this.video_id=vid;
	}
	
 	@Override
	public void requestHttp() {
		String netdataurl = YukuOAuth.SINGLE_VIDEO_INFO;
		RequestParams params=new RequestParams();
		params.put("client_id", YukuOAuth.CLIENT_ID);
		params.put("video_id",  video_id);
 		HttpUtil.get(netdataurl,params, this.jsonhand);
	}
 
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null) {
				VideoInfo vinfo=new  VideoInfo();
				vinfo.videoid=jsonObject.getString("id");
				vinfo.title=jsonObject.getString("title");
				vinfo.link=jsonObject.getString("link");
				if(jsonObject.has("thumbnail"))
				  vinfo.thumbnail=jsonObject.getString("thumbnail");
				if(jsonObject.has("duration"))
				  vinfo.duration=jsonObject.getInt("duration");
				vinfo.category=jsonObject.getString("category");
				String stat=jsonObject.getString("state");
				if("normal".equals(stat)){
					vinfo.state=RecordResource.VideoState.RESOURCE_VIDEO_STATE_NORMAL;
				}else if("encoding".equals(stat)){
					vinfo.state=RecordResource.VideoState.RESOURCE_VIDEO_STATE_ENCODING;
				}else if("fail".equals(stat)){
					vinfo.state=RecordResource.VideoState.RESOURCE_VIDEO_STATE_FAIL;
				}else if("in_review".equals(stat)){
					vinfo.state=RecordResource.VideoState.RESOURCE_VIDEO_STATE_INREVIEW;
				}else if("blocked".equals(stat)){
					vinfo.state=RecordResource.VideoState.RESOURCE_VIDEO_STATE_BLOCKED;
				}
				
				vinfo.published=jsonObject.getString("published");
				vinfo.description=jsonObject.getString("description");
 				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取视频信息成功！", vinfo);
			}else{
				return new NetResult(NetResult.RESULT_OF_ERROR, "获取视频信息失败！");
			}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "获取视频信息失败！");
		}
 	}
	
	  

}
