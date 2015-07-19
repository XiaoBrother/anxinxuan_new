package com.axinxuandroid.service;

import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.VilleageDB;
import com.axinxuandroid.sys.gloable.Gloable;

public class VilleageService {
  private VilleageDB villeagedb;
  public VilleageService(){
 	  villeagedb=new VilleageDB(Gloable.getInstance().getCurContext().getApplicationContext());
  }
  
  public Villeage getByVilleageid(int villeageid){
	  return villeagedb.selectbyVilleageid(villeageid);
   }
  
  public Villeage queryUserCreateFarm(int user_id){
	  return villeagedb.queryUserCreateFarm(user_id);
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
