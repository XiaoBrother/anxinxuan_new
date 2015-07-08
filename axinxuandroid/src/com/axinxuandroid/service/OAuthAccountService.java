package com.axinxuandroid.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.axinxuandroid.data.OAuthAccount;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.db.OAuthAccountDB;
import com.axinxuandroid.db.UserDB;
import com.axinxuandroid.db.UserVilleageDB;
import com.axinxuandroid.db.VilleageDB;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.UserCookie;
import com.ncpzs.util.LogUtil;

public class OAuthAccountService {
  private OAuthAccountDB oauthdb;
   public OAuthAccountService(){
	  this.oauthdb=new OAuthAccountDB(Gloable.getInstance().getCurContext().getApplicationContext());
    }
  
  public void saveOrUpdate(OAuthAccount account){
	  if(account!=null){
		  OAuthAccount dbaccount=getbyAccountWithType(account.getAccount_id(), account.getType());
		  if(dbaccount==null)
			  oauthdb.insert(account);
		  else{
 			  account.setId(dbaccount.getId());
			  oauthdb.update(account);
		  }
	  }
   }
  
  public OAuthAccount getbyAccountWithType(String account,int type){
	 return oauthdb.selectbyAccountWithType(account, type);
  }
  
  
  

}
