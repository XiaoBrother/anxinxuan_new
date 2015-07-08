package com.axinxuandroid.service;

import java.util.List;

import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.db.RecordResourcesDB;
import com.axinxuandroid.sys.gloable.Gloable;

public class RecordResourceService {
  
  	private RecordResourcesDB resourcedb;
	public  RecordResourceService(){
 		resourcedb=new RecordResourcesDB(Gloable.getInstance().getCurContext().getApplicationContext());
	}
	
	public void clearData(){
		resourcedb.clearData();
	}
	public void insertResources(List<RecordResource> rrs){
		resourcedb.insertResources(rrs);
	}
	
	public long insertResource(RecordResource rr){
		return resourcedb.insertResource(rr);
	}
	public void updateResource(RecordResource rec){
		resourcedb.updateResource(rec);
	}
 
	public void updateResourceLocalUrl(int id,String localurl){
		resourcedb.updateResourceLocalUrl(id, localurl);
	}
	public void updateResources(List<RecordResource> recs){
		if(recs!=null&&recs.size()>0)
			for(RecordResource rr:recs)
				this.updateResource(rr);
 	}
	public void updateVideoStatusWithTime(int id,int status,int time){
		resourcedb.updateVideoStatusWithTime(id, status,time);
	}
	public void deleteByRecord(long recordid){
		resourcedb.deleteByRecord(recordid);
	}
	public List<RecordResource> getAllResources(){
		return resourcedb.getAllResources();
	}
}
