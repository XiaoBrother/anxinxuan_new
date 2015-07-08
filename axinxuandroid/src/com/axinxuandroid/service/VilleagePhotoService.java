package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.VilleagePhoto;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.SystemNoticeDB;
import com.axinxuandroid.db.TemplateDB;
import com.axinxuandroid.db.VilleagePhotoDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class VilleagePhotoService {
	private VilleagePhotoDB photoDB;
	public  VilleagePhotoService(){
		photoDB=new VilleagePhotoDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	public void clearData(){
		photoDB.clearData();
	}
	  //保存或更新
    public void saveOrUpdateNotice(VilleagePhoto photo) { 
    	if( photo!=null){
    		VilleagePhoto bc=this.getByPhotoid(photo.getPhotoid());
    		if(bc==null){
    			photoDB.insert(photo);
    		}else{
    			if(bc.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				photo.setId(bc.getId());
        			photoDB.update(photo);
    			}
    			
    		}
    	}
    	
    }
    
    public  VilleagePhoto getByPhotoid(int pid) { 
       	return photoDB.getByPhotoId(pid);
    }
    public  List<VilleagePhoto> getByVilleageid(int vid,int top) { 
       	return photoDB.selectbyVilleageId(vid, top);
    }
    public  String  getLastoptime(int villeageid){
		return  photoDB.getLastoptime(villeageid);
	}
    public void updateImageLocalUrl(int id,String localurl){
    	photoDB.updateImageLocalUrl(id, localurl);
    }
     
}
