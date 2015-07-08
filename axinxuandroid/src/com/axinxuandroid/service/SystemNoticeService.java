package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.SystemNoticeDB;
import com.axinxuandroid.db.TemplateDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SystemNoticeService {
	private SystemNoticeDB noticeDB;
	public  SystemNoticeService(){
		noticeDB=new SystemNoticeDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	public void clearData(){
		noticeDB.clearData();
	}
	  //保存或更新
    public void saveOrUpdateNotice(SystemNotice notice) { 
    	if(notice!=null){
    		SystemNotice bc=this.getByType(notice.getType());
    		if(bc==null){
    			noticeDB.insert(notice);
    		}else{
    			notice.setId(bc.getId());
    			noticeDB.update(notice);
    		}
    	}
    	
    }
    
    public SystemNotice getByType(int type) { 
       	return noticeDB.selectbyType(type);
    }
	 
    public void deleteById(int id){
    	noticeDB.deleteById(id);
    }
    public void deleteByType(int type){
    	noticeDB.deleteByType(type);
    }
}
