package com.axinxuandroid.service;

 
 import java.util.ArrayList;
import java.util.List;

 

import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Intelligence;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.VilleageBanner;

import com.axinxuandroid.db.CategoryDB;
import com.axinxuandroid.db.IntelligenceDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.VarietyDB;
import com.axinxuandroid.db.VilleageBannerDB;
 
import com.axinxuandroid.sys.gloable.Gloable;

public class IntelligenceService {
	private IntelligenceDB intelldb;
 	public  IntelligenceService(){
 		intelldb=new IntelligenceDB(Gloable.getInstance().getCurContext().getApplicationContext());
  	}
 	public void clearData(){
 		intelldb.clearData();
	}
	  //保存或更新
    public void saveOrUpdate(Intelligence intell) { 
    	if(intell!=null){
    		Intelligence va=this.getByIntelligenceid(intell.getIntelligenceid());
    		if(va==null){
    			intelldb.insert(intell);
    		}else{
    			if(va.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				intell.setId(va.getId());
        			intelldb.update(intell);
    			}
     		}
    	}
     }
    public String getLatoptime(int vid) {
    	return intelldb.getLatoptime(vid);
    }
    public Intelligence getByIntelligenceid(int intellid) { 
       	return intelldb.selectbyIntelligenceid(intellid);
    }
    public List<Intelligence> getByNameWithVilleage(String name,int vid) { 
       	return intelldb.selectbyNameWithVilleageId(name,vid);
    }
    public List<Intelligence> getByVilleage(int vid) { 
    	return intelldb.selectbyVilleageid(vid);
    }
    
    public  void  updateImgLocalUrl(int id,String url) {
    	intelldb.updateImgLocalUrl(id, url);
    }
}
