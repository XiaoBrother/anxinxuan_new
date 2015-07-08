package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

 

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.db.RecordDB;
import com.axinxuandroid.db.RecordResourcesDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
 
public class RecordService {
 	private RecordDB recordDB;
 	private RecordResourcesDB resourcedb;
	public  RecordService(){
		recordDB=new RecordDB(Gloable.getInstance().getCurContext().getApplicationContext());
		resourcedb=new RecordResourcesDB(Gloable.getInstance().getCurContext().getApplicationContext());
	}
	public void clearData(){
		recordDB.clearData();
	}

	// 获取本地数据
	public List<Record> getLocalDatas( String trace_code,int user_type,String endtime) {
		List<Record> records=recordDB.selectbytracecode(trace_code,user_type,endtime);
		if(records!=null&&records.size()>0){
			for(Record rec:records){
				rec.setResources(resourcedb.getResourcesByRecordId(rec.getId()));
			}
		}
		return records;
  	}

	/**
	 * 获取用户创建的记录
	 * @param userid
	 * @param endtime
	 * @return
	 */
	public  List<Record>  getUserRecords(int userid,String endtime)  {
		List<Record> records=recordDB.getUserRecords(userid,endtime);
		if(records!=null&&records.size()>0){
			for(Record rec:records){
				rec.setResources(resourcedb.getResourcesByRecordId(rec.getId()));
			}
		}
		return records;
  	}
	 
	/**
	 *  获取草稿记录
	 * @return
	 */
	public List<Record> getDraftRecords(int userid) {
		List<Record> records=recordDB.getDraftRecords(userid);
		if(records!=null&&records.size()>0){
			for(Record rec:records){
				rec.setResources(resourcedb.getResourcesByRecordId(rec.getId()));
			}
		}
		return records;
  	}
	/**
	 * 添加或更新记录到数据库
	 * @param rec
	 */
	public void saveOrUpdate(Record rec){
		if(rec!=null){
			Record dbred=getByRecordId(rec.getRecord_id());
			if(dbred==null){
				recordDB.insert(rec);
				List<RecordResource> resources=rec.getResources();
				if(resources!=null&&resources.size()>0){
					//保存资源信息
		 			for(RecordResource res:resources){
						res.setRecordid(rec.getId());
		 			}
		 			resourcedb.insertResources(resources);
				}
			}else{
				if(dbred.getIsdel()!=SystemDB.DATA_HAS_DELETE){
					rec.setId(dbred.getId());
					recordDB.update(rec);
				}
				
			}
   		}
  	}
	
	/**
	 * 添加记录到数据库
	 * @param rec
	 */
	public void saveRecord(Record rec){
		if(rec!=null){
			recordDB.insert(rec);
    	}
  	}
	
	public String getLastoptime(int batchid,int user_type){
		return recordDB.getLastoptime(batchid,user_type);
	}
	 
 
	 
	public Record getByRecordId(int rid){
		Record red= recordDB.getByRecordId(rid);
		if(red!=null){
			red.setResources(resourcedb.getResourcesByRecordId(red.getId()));
  		}
		return red;
	}
	
	public Record getById(long id){
		Record red= recordDB.getById(id);
		if(red!=null){
			red.setResources(resourcedb.getResourcesByRecordId(red.getId()));
  		}
		return red;
	}
	public void update(Record record){
		recordDB.update(record);
	}
	public void updateResourceLocalUrl(long id,String localurl){
		resourcedb.updateResourceLocalUrl(id, localurl);
	}
	public void deleteRecord(long id){
		recordDB.deleteById(id);
 	}
	 
}
