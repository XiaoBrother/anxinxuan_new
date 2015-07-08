package com.axinxuandroid.activity.net;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Looper;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.net.UploadNetMediaThread.UploadMediaFinishListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.oauth.YukuOAuth.UploadStatus;
import com.axinxuandroid.oauth.YukuOAuth.UploadToken;
import com.axinxuandroid.oauth.YukuOAuth.YukuOAuthInfo;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.ImgUploadUtil;
import com.ncpzs.util.LogUtil;
import com.youku.uploader.IUploadResponseHandler;
import com.youku.uploader.YoukuUploader;

public class UploadVideoThread extends Thread {
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_ERROR = 2;
	private List<RecordResource> filepath;
	private String title;
	private String tag;
	private String recordid;
	private String batchid;
    private User user;
   // private YoukuUploader uploader;
 	//private YukuOAuth oauth = new YukuOAuth();
 	private boolean finished=false;
 	private boolean success=false;
 	private String vidoid;
 	private UploadVideoFinishListener listener;
 	public UploadVideoThread(String batchid,String recordid,String title, String tag, List<RecordResource> filepath,User user) {
		this.recordid=recordid;
		this.filepath = filepath;
		this.title = URLEncoder.encode(title);
		this.tag = URLEncoder.encode(tag);
		this.user=user;
		this.batchid=batchid;
		//this.uploader=uploader;
		  
 	}
 	@Override
	public void run() {
 		try {
 			String neturl=HttpUrlConstant.URL_HEAD+HttpUrlConstant.SAVE_VIDEO_INFO;
			String  result =null;
			List<RecordResource> success = new ArrayList<RecordResource>();
			if (filepath != null) {
				for (RecordResource rec : filepath) {
					Date date = new Date();
					String imgpath=rec.getLocalurl();
					Map<String,String> params=new HashMap<String, String>();
					params.put("batchid", batchid);
					params.put("recordid", recordid+"");
					params.put("info", date.getTime()+"");
					String filesubfix=imgpath.substring(imgpath.lastIndexOf("."));
 					//Log.e("dates", dates);
					result = ImgUploadUtil.uploadFile(imgpath, neturl, (new Date().getTime())+filesubfix,params);
  				    LogUtil.logInfo(getClass(),  "upfile:"+(result==null?"":result));
					if ( result != null) {
						JSONObject jsonObject = new JSONObject(result);
						if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
 							rec.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
							rec.setNeturl(jsonObject.getString("neturl"));
							success.add(rec);
						}
					}
				}
				if(this.listener!=null)
					this.listener.onfinish(RESULT_SUCCESS, filepath);
  			}

		} catch (Exception e) {
			e.printStackTrace();
			if(this.listener!=null)
				this.listener.onfinish(RESULT_ERROR, "视频上传失败");
		}
	}

//	@Override
//	public void run() {
//		if (filepath == null || filepath.size() < 1) {
// 			if(this.listener!=null)
//				this.listener.onfinish(RESULT_ERROR, "没有视频需要上传");
//		} else {
//			YukuOAuthInfo info = null;
//				//(YukuOAuthInfo) Session.getInstance().getAttribute(SessionAttribute.SESSION_YUKU_OAUTH);
// 			if (info == null) {
//				String refresh_token = this.getRefreshToken();
//				//SharedPreferenceService.getYukuRefreshToken();
//				// "574fa9e01758449e590e2afc527f7d20";
//				if (refresh_token != null) {
// 					info = oauth.getTokenByRefreshToken(refresh_token);
//				} else {
//					//LogUtil.logInfo(getClass(), "start login");
//				}
//				
//			}
// 			
//			if (info == null) {
//				if(this.listener!=null)
//					this.listener.onfinish(RESULT_ERROR, "暂时无法上传视频，请稍后再试");
// 			} else {
// 				LogUtil.logInfo(getClass(), "  get token:"+info.access_token);
// 				HashMap<String, String> params = new HashMap<String, String>();
//			 	HashMap<String, String> uploadInfo = new HashMap<String, String>();
//				uploadInfo.put("title",title);
//				uploadInfo.put("tags", "anxinxuan");
//				params.put("access_token", info.access_token);
// 				String vids = "";
// 				RecordResource path =filepath.get(0);//只上传一个
//			 
//				uploadInfo.put("file_name",path.getLocalurl());
// 				uploader.upload(params, uploadInfo, new IUploadResponseHandler() {
//
//						@Override
//						public void onStart() {
//							LogUtil.logInfo(getClass(), "onStart");
// 						}
//
//						@Override
//						public void onSuccess(JSONObject response) {
// 							try {
// 								vidoid=response.getString("video_id");
//							} catch (JSONException e) {
// 								e.printStackTrace();
//							}
//							finished=true;
//							success=true;
//						}
//
//						@Override
//						public void onProgressUpdate(int counter) {
//							LogUtil.logInfo(getClass(), counter + "");
// 						}
//
//						@Override
//						public void onFailure(JSONObject errorResponse) {
// 							LogUtil.logInfo(getClass(), errorResponse.toString());
// 							finished=true;
//						}
//
//						@Override
//						public void onFinished() {
// 							LogUtil.logInfo(getClass(), "video upload finished");
// 							finished=true;
// 						}
//				});
// 				//等待上传执行完成
// 				while (!finished) {
//                     try {
//						Thread.sleep(3*1000);
//					} catch (InterruptedException e) {
// 						e.printStackTrace();
//					}					
//				}
// 				if(success){
// 					path.setNeturl(vidoid);
// 					if(vidoid!=null)
// 						  vids += vidoid +"_"+path.getInfo()+ ",";
// 	 				if (vids != null)
// 						vids = vids.replaceAll(",$", "");
// 				}
// 				this.releaseRefreshToken(info.refresh_token);
// 				if(success)
//				this.uploadVideoInfo(vids);
//			}
//		}
//	}

//	private String uploadVideo(YukuOAuthInfo info, String path) {
//		UploadToken uptoken = oauth.uploadVideoInfo(info, title, tag, path);
//		if (uptoken == null) {
//			this.returnmap.put(RESULT, NetResult.RESULT_OF_ERROR);
//			this.returnmap.put(MESSAGE, "上传基本信息失败");
//		} else {
//			UploadStatus result = oauth.startUpload(uptoken, path);
//			if (result != null && result.finished) {
//				String videoid = oauth.commit(uptoken, result, info);
//				return videoid;
//			} else {
//				this.returnmap.put(RESULT, NetResult.RESULT_OF_ERROR);
//				this.returnmap.put(MESSAGE, "上传视频文件失败");
//			}
//		}
//		return null;
//	}
	
	private void uploadVideoInfo(String names){
 	 
		String netdataurl = HttpUrlConstant.URL_HEAD
				+ HttpUrlConstant.SAVE_VIDEO_INFO + "?namestr="
				+names+"&recordid="+this.recordid;
		LogUtil.logInfo(getClass(), netdataurl);
		byte[] data = HttpUtil.readStream(netdataurl);
		if (data != null) {
			try{
				JSONObject jsonObject = new JSONObject(new String(data));
				if (jsonObject != null && jsonObject.getInt("ajax_optinfo") == 1) {
					for (RecordResource path : filepath) {
						path.setState(RecordResource.RESOURCE_STATE_SYNCHRONISE);
					}
					// 将视频信息传送到服务器
  					if(this.listener!=null)
						this.listener.onfinish(RESULT_SUCCESS, filepath);
 				}
			}catch(Exception ex){
				ex.printStackTrace();
				if(this.listener!=null)
					this.listener.onfinish(RESULT_ERROR, "信息保存失败");
			}
   		}
 	}
   /**
    * 获取刷新token
    * @return
    */
	private String getRefreshToken() {
 		if (user == null)
			return null;
		String netdataurl = HttpUrlConstant.URL_HEAD
				+ HttpUrlConstant.YUKU_GET_REFRESH_TOKEN + "?phone="
				+ user.getPhone();
		LogUtil.logInfo(getClass(), netdataurl);
		byte[] data = HttpUtil.readStream(netdataurl);
		if (data != null) {
			try{
				JSONObject jsonObject = new JSONObject(new String(data));
				if (jsonObject != null && jsonObject.getInt("ajax_optinfo") == 1) {
					 return jsonObject.getString("refresh_token");
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
   		}
		return null;
	}
	 
	/**
	    * 释放刷新token
	    * @return
	    */
	private boolean releaseRefreshToken(String token) {
 		if (user == null)
			return false;
		String netdataurl = HttpUrlConstant.URL_HEAD
				+ HttpUrlConstant.YUKU_RELEASE_REFRESH_TOKEN + "?phone="
				+ user.getPhone()+"&token="+token;
		LogUtil.logInfo(getClass(), netdataurl);
		byte[] data = HttpUtil.readStream(netdataurl);
		if (data != null) {
			try{
				JSONObject jsonObject = new JSONObject(new String(data));
				if (jsonObject != null && jsonObject.getInt("ajax_optinfo") == 1) {
					 return true;
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
   		}
		return false;
	}

	public void setUploadVideoFinishListener(UploadVideoFinishListener lis){
		this.listener=lis;
	}
	public interface UploadVideoFinishListener {
		public void onfinish(int resultcode,Object resultdata);
	}
}
