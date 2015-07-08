package com.axinxuandroid.service;

 
 import java.util.ArrayList;
import java.util.List;

 

import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.Variety;

import com.axinxuandroid.db.CategoryDB;
import com.axinxuandroid.db.CommentDB;
import com.axinxuandroid.db.NetUpdateRecordDB;
import com.axinxuandroid.db.VarietyDB;
 
import com.axinxuandroid.sys.gloable.Gloable;

public class NetUpdateRecordService {
	private NetUpdateRecordDB nrecDB;
 	public  NetUpdateRecordService(){
 		nrecDB=new NetUpdateRecordDB(Gloable.getInstance().getCurContext().getApplicationContext());
  	}
 	public void clearData(){
 		nrecDB.clearData();
	}
     
 	public void saveOrUpdate(NetUpdateRecord rec){
 		if(rec!=null){
 			NetUpdateRecord dbdata=getByTypeWithObjId(rec.getType(), rec.getObjid());
 			if(dbdata==null){
 				nrecDB.insert(rec);
 			}else{
  				rec.setId(dbdata.getId());
 				nrecDB.update(rec);
 			}
 		}
 	}
 	public NetUpdateRecord getByTypeWithObjId(int type,int objid){
 		return nrecDB.getByTypeWithObjId(type, objid);
 	}
}
