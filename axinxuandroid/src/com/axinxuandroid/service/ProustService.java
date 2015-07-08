package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.ProustDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.TemplateDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class ProustService {
	private ProustDB proustDB;
	public  ProustService(){
		proustDB=new ProustDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	public void clearData(){
		proustDB.clearData();
	}
    public void saveOrUpdateProust(Proust proust) { 
    	 if(proust!=null){
    		 Proust dbproust=proustDB.selectbyUserIdProustId(proust.getUser_id(), proust.getProust_id());
    		 if(dbproust==null){
    			 proustDB.insert(proust);
    		 }else{
    			 if(dbproust.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				 proust.setId(dbproust.getId());
        			 proustDB.update(proust);
    			 }
     		 }
    	 }
    		
    }
    public String getLatoptime(int userid){
    	return proustDB.getLatoptime(userid);
    }
    public List<Proust> getByUserid(int user_id) { 
       	return proustDB.selectbyUserId(user_id);
    }
	public Proust selectbyUserIdProustId(int user_id,int proust_id){
		 return proustDB.selectbyUserIdProustId(user_id, proust_id);
	}
	public Proust selectbyId(long id){
		 return proustDB.selectbyId(id);
	}
    public void deleteByUserId(int userid){
    	proustDB.deleteByUserId(userid);
    }
    public void deleteByUserIdProustId(int userid,int proust_id){
    	proustDB.deleteByUserIdProustId(userid, proust_id);
    }
}
