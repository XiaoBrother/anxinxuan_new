package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.SystemSet;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.SystemLogDB;
import com.axinxuandroid.db.SystemNoticeDB;
import com.axinxuandroid.db.SystemSetDB;
import com.axinxuandroid.db.TemplateDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SystemSetService {
	private SystemSetDB setDB;
	public  SystemSetService(){
		setDB=new SystemSetDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	  //保存或更新
    public void saveOrUpdate(SystemSet set) { 
    	if(set!=null){
    		SystemSet dbset=getByType(set.getType());
    		if(dbset==null)
    			setDB.insert(set);
    		else{
    			set.setId(dbset.getId());
    			setDB.update(set);
    		}
     	}
    	
    }
    public  SystemSet  getByType(int type){
    	return setDB.getByType(type);
    }
    public void deleteById(int id){
    	setDB.deleteById(id);
    }
     
}
