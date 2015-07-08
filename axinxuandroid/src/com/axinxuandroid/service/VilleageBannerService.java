package com.axinxuandroid.service;

 
 import java.util.ArrayList;
import java.util.List;

 

import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.VilleageBanner;

import com.axinxuandroid.db.CategoryDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.VarietyDB;
import com.axinxuandroid.db.VilleageBannerDB;
 
import com.axinxuandroid.sys.gloable.Gloable;

public class VilleageBannerService {
	private VilleageBannerDB bannerdb;
 	public  VilleageBannerService(){
 		bannerdb=new VilleageBannerDB(Gloable.getInstance().getCurContext().getApplicationContext());
  	}
 	public void clearData(){
 		bannerdb.clearData();
	}
	  //保存或更新
    public void saveOrUpdate(VilleageBanner banner) { 
    	if(banner!=null){
    		VilleageBanner va=this.getByBanner(banner.getBannerid());
    		if(va==null){
    			bannerdb.insert(banner);
    		}else{
    			if(va.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				banner.setId(va.getId());
        			bannerdb.update(banner);
    			}
     		}
    	}
     }
    public String getLatoptime(int vid) {
    	return bannerdb.getLatoptime(vid);
    }
    public VilleageBanner getByBanner(int bid) { 
       	return bannerdb.selectbyBannerid(bid);
    }
	
    public List<VilleageBanner> getByVilleage(int vid) { 
    	return bannerdb.selectbyVilleageid(vid);
    }
    
     
}
