package com.axinxuandroid.service;

import com.axinxuandroid.data.SystemLabel;
import com.axinxuandroid.db.RecordResourcesDB;
import com.axinxuandroid.db.SystemLabelDB;
import com.axinxuandroid.sys.gloable.Gloable;

public class SystemLabelService {
	private SystemLabelDB slabeldb;
	public SystemLabelService(){
		slabeldb=new SystemLabelDB(Gloable.getInstance().getCurContext().getApplicationContext());
	}
	
	public  String getImageUrlByNameWithModule(String name){
		return slabeldb.getImageUrlByNameWithModule(name);
	}
	
	public  SystemLabel getByName(String name) {
		return slabeldb.getByName(name);
	}
}
