package com.axinxuandroid.service;

 
import java.util.ArrayList;
import java.util.Date;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.UserVisitHistory;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.ProustDB;
import com.axinxuandroid.db.TemplateDB;
import com.axinxuandroid.db.UserFavoriteDB;
import com.axinxuandroid.db.UserVisitHistoryDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class UserVisitHistoryService {
	private UserVisitHistoryDB visitDB;
	public  UserVisitHistoryService(){
		visitDB=new UserVisitHistoryDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	  
    public void addVisitory(int userid,int visiobjid,int type) { 
    	UserVisitHistory usv=getByTypeWithUseridAndVisitobj(type,userid,visiobjid);
    	if(usv!=null){
    		usv.setVisittime(DateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
    		visitDB.update(usv)	;
    	}else{
    		usv=new UserVisitHistory();
        	usv.setUserid(userid);
        	usv.setVisitobjid(visiobjid);
        	usv.setType(type);
        	usv.setVisittime(DateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        	visitDB.insert(usv);
    	}
    		
    		
     }
    
    
	public  List<UserVisitHistory>  getByTypeWithUser(int type,int userid,String visittime){
		 return visitDB.selectbyTypeWithUserid(type, userid, visittime);
	}
	public  UserVisitHistory   getByTypeWithUseridAndVisitobj(int type,int userid,int objid){
		return visitDB.selectbyTypeWithUseridAndVisitobj(type, userid, objid);
	}
	 
	
	public void clearData(){
		visitDB.clearData();
	}
    
}
