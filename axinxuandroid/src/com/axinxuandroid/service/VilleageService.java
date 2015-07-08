package com.axinxuandroid.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.UserDB;
import com.axinxuandroid.db.UserVilleageDB;
import com.axinxuandroid.db.VilleageDB;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.UserCookie;
import com.ncpzs.util.LogUtil;

public class VilleageService {
  private VilleageDB villeagedb;
  public VilleageService(){
 	  villeagedb=new VilleageDB(Gloable.getInstance().getCurContext().getApplicationContext());
  }
  
  public Villeage getByVilleageid(int villeageid){
	  return villeagedb.selectbyVilleageid(villeageid);
   }
  
  
  public void  saveOrUpdate(Villeage villeage){
	  if(villeage!=null){
		  Villeage oldvil=getByVilleageid(villeage.getVilleage_id());
		  if(oldvil==null){
			  villeagedb.insert(villeage);
		  }else{
			  if(oldvil.getIsdel()!=SystemDB.DATA_HAS_DELETE){
				  villeage.setId(oldvil.getId());
				  villeagedb.update(villeage);
			  }
			
		  }
	  }
  }
   

}
