package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.SystemLogDB;
import com.axinxuandroid.db.SystemNoticeDB;
import com.axinxuandroid.db.TemplateDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class SystemLogService {
	private SystemLogDB logDB;
	public  SystemLogService(){
		logDB=new SystemLogDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	  //保存或更新
    public void save(SystemLog log) { 
    	if(log!=null){
     		logDB.insert(log);
     	}
    	
    }
    public  List<SystemLog>  getAllLog(){
    	return logDB.getAllLog();
    }
    public void deleteById(int id){
    	logDB.deleteById(id);
    }
     
}
