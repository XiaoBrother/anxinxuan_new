package com.axinxuandroid.activity.net;

import java.net.URLEncoder;
import java.util.List;
 
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Comment;
 import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
 
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SaveCommentThread  extends NetThread{

	private int recordid;
	private String context;
    private User user;
    private int parentid;
 	public SaveCommentThread(int recordid,int parentid,String context,User user){
	   this.recordid=recordid;
	   this.context=context;
	   this.parentid=parentid;
	   this.user=user;
	}
 	@Override
	public void requestHttp() {
		if(user!=null){
			String url = HttpUrlConstant.URL_HEAD+HttpUrlConstant.SAVE_RECORD_COMMENT;
 			RequestParams params=new RequestParams();
			params.put("recordid", recordid+"");
			params.put("context",  context);
			params.put("phone", user.getPhone());
			params.put("parentid", parentid+"");
			HttpUtil.get(url,params, this.jsonhand);
		}else{
			 this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "请先登录！"));
		}
	}
	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		try {
			if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
					Comment comment=new Comment();
					comment.setComment_id(jsonObject.getInt("id"));
					comment.setContext(context);
					comment.setUser_name(user.getUser_name());
					comment.setComment_date(jsonObject.getString("comment_date"));
					comment.setUser_img(user.getLocal_imgurl());
					comment.setUser_id(user.getUser_id());
					comment.setParent_id(jsonObject.getInt("parent_id"));
					comment.setRecordid(recordid);
					// 保存成功
					 return new NetResult(NetResult.RESULT_OF_SUCCESS, "保存成功！",
							 comment);
				} else {
					// 保存失败
					return new NetResult(NetResult.RESULT_OF_ERROR, "保存失败！");
				}
		} catch (JSONException e) {
 			e.printStackTrace();
 			return new NetResult(NetResult.RESULT_OF_ERROR, "操作失败！");
		}
 	}
 
	 
}
