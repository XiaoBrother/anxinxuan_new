package com.axinxuandroid.activity.net;
 
import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Advocate;
import com.axinxuandroid.data.User;
 
 
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
 
public class AdvocateThread  extends NetThread{
    private int recordid;
    private User user;
    private int type;
    private static final int ADD=0;
    private static final int REDUCE=1;
    /**
     * 
     * @param recordid
     * @param user
     * @param type (0:添加，1：取消)
     */
  	public AdvocateThread(int recordid,User user,int type){
	   this.recordid=recordid;
	   this.user=user;
	   this.type=type;
 	}
 	@Override
	public NetResult onResponseSuccess(JSONObject jsonObject) {
		 try {
			 if (jsonObject != null&& jsonObject.getInt("ajax_optinfo") == 1) {
				    if(type==ADD){
				    	Advocate adv=new Advocate();
				    	adv.setRecordid(recordid);
				    	adv.setUserid(user.getUser_id());
				    	adv.setIsdel(0);
				    	adv.setLastoptime(jsonObject.getString("lastoptime"));
				    	return new NetResult(NetResult.RESULT_OF_SUCCESS, null,adv);
				    }else  return new NetResult(NetResult.RESULT_OF_SUCCESS, null);
			 }else{
				 NetResult nr;
				 if(type==ADD){
					 nr= new NetResult(NetResult.RESULT_OF_ERROR,"您已经点过赞了");
 				 }else  nr= new NetResult(NetResult.RESULT_OF_ERROR,"您没有点过赞");
				 if(jsonObject.getInt("hasdata")==1){
					  Advocate adv=new Advocate();
				      adv.setRecordid(jsonObject.getInt("record_id"));
				      adv.setUserid(jsonObject.getInt("user_id"));
				      adv.setIsdel(jsonObject.getInt("isdel"));
				      adv.setLastoptime(jsonObject.getString("lastoptime"));
				      nr.setData(adv);
				 }
				return nr;
			 }
			 
 		} catch (JSONException e) {
 			e.printStackTrace();
 		    return new NetResult(NetResult.RESULT_OF_ERROR, "点赞失败！");
		}
 	}
	@Override
	public void requestHttp() {
		if(user!=null){
		    String netdataurl = HttpUrlConstant.URL_HEAD+ HttpUrlConstant.ADD_ADVOCATE;
		    RequestParams params=new RequestParams();
		    params.put("userid", user.getUser_id()+"" );
		    params.put("recordid",  recordid+"");
		    params.put("type",  type+"");
 		    HttpUtil.get(netdataurl,params, this.jsonhand);
		}else this.finishNet(new NetResult(NetResult.RESULT_OF_ERROR, "请先登录！"));

	}
 
	 
}
