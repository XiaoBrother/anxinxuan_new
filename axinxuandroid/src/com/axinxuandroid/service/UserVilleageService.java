package com.axinxuandroid.service;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.db.UserDB;
import com.axinxuandroid.db.UserVilleageDB;
import com.axinxuandroid.db.VilleageDB;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.UserCookie;
import com.ncpzs.util.LogUtil;

public class UserVilleageService {
  private UserVilleageDB uservilleagedb;
  public UserVilleageService(){
	  uservilleagedb=new UserVilleageDB(Gloable.getInstance().getCurContext().getApplicationContext());
  }
  
  public List<Villeage> getVilleageByUser(int uid){
	  return uservilleagedb.selectVilleagebyUserId(uid);
   }
   
  public List<User> getUserByVilleage(int vid){
	  return uservilleagedb.selectUserbyVilleage(vid);
   }
  public  Villeage  selectByUserIdWithVilleageid(int userid,int vid){
	  return uservilleagedb.selectByUserIdWithVilleageid(userid, vid);
  }
  public  int  getUserRoleInVilleage(int userid,int vid){
	   UserVilleage uvil= uservilleagedb.selectUserVilleage(userid, vid);
	   if(uvil!=null)
		   return uvil.getRole();
	   return -1;
  }
  public void saveOrUpdate(UserVilleage uv){
	    if(uv!=null){
	    	UserVilleage dbuv=uservilleagedb.selectUserVilleage(uv.getUser_id(),uv.getVilleage_id());
	    	if(dbuv!=null){
	    		uv.setId(dbuv.getId());
	    	    uservilleagedb.update(uv);
	    	}else{
	    		uservilleagedb.insert(uv);
	    	}
	    }
	   
   }
}
