package com.axinxuandroid.activity.net;

import java.io.File;
import java.io.IOException;
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
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
 
public class LoadCommentThread extends NetThread {

	private int record_id;
	private String lastoptime;
	public LoadCommentThread(int record_id,String lastoptime){
		this.record_id=record_id;
		this.lastoptime=lastoptime;
	}
	 
	@Override
	public void requestHttp() {
		String netdataurl = HttpUrlConstant.URL_HEAD
		+ HttpUrlConstant.RECORD_COMMENT ;
		RequestParams params=new RequestParams();
		params.put("recordid", record_id+"");
		params.put("lastoptime", DateUtil.delSpaceDate(lastoptime));
 		HttpUtil.get(netdataurl, params,this.jsonhand);
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		List<Comment> commments = null;
		try {
			if (jsonObject.getInt("hasdata") == HAS_DATA&&jsonObject.has("comments")) {
				commments=new ArrayList<Comment>();
				JSONArray array = jsonObject.getJSONArray("comments");
				Comment comment;
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					comment = new Comment();
					comment.setComment_id(jo.getInt("id"));
					comment.setUser_name(jo.getString("user_name"));
					comment.setContext(jo.getString("context"));
					comment.setComment_date(jo.getString("date"));
					comment.setUser_img(jo.getString("user_img"));
					comment.setParent_id(jo.getInt("parentid"));
					comment.setUser_id(jo.getInt("user_id"));
					comment.setIsdel(jo.getInt("isdel"));
					comment.setLastoptime(jo.getString("lastoptime"));
					comment.setRecordid(record_id);
					comment.setReplyUserId(jo.getString("replyuserid"));
					comment.setReplyUserName(jo.getString("replyusername"));
					comment.setVarietyName(jo.getString("varietyname"));
					comment.setBatchCode(jo.getString("batchcode"));
					commments.add(comment);
					// 用户头像
					File fl = new File(AppConstant.USERIMG_DIR
							+ comment.getUser_id());
					if (fl.exists()) {
						comment.setUser_img(AppConstant.USERIMG_DIR
								+ comment.getUser_id());
					}else{
						String userimgurl = comment.getUser_img();
						if (userimgurl != null) {
							Bitmap mBitmap = null;
							if (userimgurl.indexOf("http") >= 0)
								mBitmap = BitmapFactory.decodeStream(HttpUtil.getImageStream(userimgurl));
							else
								mBitmap = BitmapFactory.decodeStream(HttpUtil.getImageStream(HttpUrlConstant.URL_HEAD
														+ userimgurl));
							if (mBitmap != null) {
								userimgurl = comment.getUser_id()+"";
								BitmapUtils.saveFile(mBitmap, userimgurl,AppConstant.USERIMG_DIR);
								comment.setUser_img(AppConstant.USERIMG_DIR+ userimgurl);
							}
						}
				 
					}
				}
				return new NetResult(NetResult.RESULT_OF_SUCCESS, "获取数据成功！",
						commments);
 			}else {
  				return new NetResult(NetResult.RESULT_OF_ERROR, "未获取到数据！");
			}
		} catch (Exception e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
 	}

}
