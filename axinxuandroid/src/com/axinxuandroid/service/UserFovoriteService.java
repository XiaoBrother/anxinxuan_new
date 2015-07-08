package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.ProustDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.TemplateDB;
import com.axinxuandroid.db.UserFavoriteDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class UserFovoriteService {
	private UserFavoriteDB favDB;
	public  UserFovoriteService(){
		favDB=new UserFavoriteDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	  
    public void saveOrUpdate(UserFavorite uf) { 
    	 if(uf!=null){
    		 UserFavorite dbuf=favDB.selectbyUserFavoriteTypeWithId(uf.getFavorite_id(), uf.getFavorite_type());
    		 if(dbuf==null){
    			 favDB.insert(uf);
    		 }else{
    			 if(dbuf.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				 uf.setId(dbuf.getId());
        			 favDB.update(uf);
    			 }
    			 
    		 }
    	 }
    		
    }
    public  List<UserFavorite>  serchByName(int favoritetype,String name){
    	return favDB.serchByName(favoritetype, name);
    }
    
    public  List<UserFavorite>  serchByBatchCode(int favoritetype,String batchcode){
    	return favDB.serchByBatchCode(favoritetype, batchcode);
    }
    public String getLatoptime(){
    	return favDB.getLatoptime();
    }
    public void deleteByFavId(long favid) {
    	favDB.deleteByFavId(favid);
    }
	public UserFavorite selectbyUserFavoriteTypeWithId(int fid,int ftype){
		 return favDB.selectbyUserFavoriteTypeWithId(fid, ftype);
	}
	public  List<UserFavorite>  selectbyUserFavoriteType(int favoritetype,int uid,String lastoptime,int favid) {
		 return favDB.selectbyUserFavoriteType( favoritetype,uid,lastoptime,favid);
	}
	public int selectMaxFavid(int userid){
		 return favDB.selectMaxFavid(userid);
	}
    
}
