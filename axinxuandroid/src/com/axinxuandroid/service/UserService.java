package com.axinxuandroid.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.axinxuandroid.data.OAuthAccount;
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

public class UserService {
  private UserDB userdb;
  private UserVilleageService uservilleageservice;
  private VilleageService villeageservice;
  private OAuthAccountService  oauthservice;
  public UserService(){
	  this.userdb=new UserDB(Gloable.getInstance().getCurContext().getApplicationContext());
	  this.uservilleageservice=new UserVilleageService();
	  villeageservice=new VilleageService();
	  oauthservice=new OAuthAccountService();
  }
  
  public void addUser(User user){
	  userdb.insert(user);
	  if(user.getUserVilleages()!=null){
		  for(UserVilleage uvil:user.getUserVilleages()){
			  uservilleageservice.saveOrUpdate(uvil);
			  if(uvil.getVilleage()!=null)
				  villeageservice.saveOrUpdate(uvil.getVilleage());
		  }
 	  }
	  if(user.getOauths()!=null){
		  for(OAuthAccount acc:user.getOauths()){
			  oauthservice.saveOrUpdate(acc);
 		  }
 	  }
   }
  public User selectbyUserid(int user_id){
	  User user= userdb.selectbyUserId(user_id);
	  if(user!=null)
	   user.setVilleages(uservilleageservice.getVilleageByUser(user_id));
	  return user;
   }
  
  private void updateUser(User user) {
	  userdb.updateUser(user);
	  if(user.getUserVilleages()!=null){
		  for(UserVilleage uvil:user.getUserVilleages()){
			  uservilleageservice.saveOrUpdate(uvil);
			  if(uvil.getVilleage()!=null)
				  villeageservice.saveOrUpdate(uvil.getVilleage());
		  }
 	  }
	  if(user.getOauths()!=null){
		  for(OAuthAccount acc:user.getOauths()){
			  oauthservice.saveOrUpdate(acc);
 		  }
 	  }
  }
  public int getUserInVilleageType(int userid,int villeagid){
	  int usertype=Record.BATCHRECORD_USERTYPE_OF_CONSUMER;
	  LogUtil.logInfo(getClass(), userid+":"+villeagid);
 	  Villeage uvil=uservilleageservice.selectByUserIdWithVilleageid(userid, villeagid);
	  if(uvil!=null)
		  usertype=Record.BATCHRECORD_USERTYPE_OF_CREATOR;
	  return usertype;
  }
  
  public Villeage getUserCreateVilleage(int user_id){
 	  Villeage uvil=villeageservice.queryUserCreateFarm(user_id);
	  return uvil;
  }
  
  public void saveOrUpdate(User user){
	  if(user==null) return ;
	  User exist=this.selectbyUserid(user.getUser_id());
	  if(exist!=null){
		  user.setId(exist.getId());
		  if(exist.getPerson_imageurl()!=null&&(exist.getPerson_imageurl().equals(user.getPerson_imageurl())))
			  user.setLocal_imgurl(exist.getLocal_imgurl());
		  this.updateUser(user);
		 
	  }else this.addUser(user);
  }
  
  public User getLastLoginUser(){
	  User user=SharedPreferenceService.getLastLoginUser();
 	  if(user!=null){
 		User suser= this.selectbyUserid(user.getUser_id());
 		if(suser!=null&&user.getLogin_time()!=null){
 			suser.setLogin_time(user.getLogin_time());
 		}
 		return suser;
 	  }
 	  return null;
  }
  

}
