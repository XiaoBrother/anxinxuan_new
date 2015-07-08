package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.SystemDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class BatchService {
	private BatchDB batchDB;
	public  BatchService(){
		batchDB=new BatchDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	public void clearData(){
		batchDB.clearData();
	}
	  //保存或更新
    public void saveOrUpdateBatch(Batch batch) { 
    	if(batch!=null){
    		Batch bc=this.getByBatchId((int)batch.getBatch_id());
     		if(bc==null){
    			batchDB.insert(batch);
    		}else{
    			if(bc.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				batch.setId(bc.getId());
         			batchDB.update(batch);
    			}
    			
    		}
    	}
    	
    }
  //保存的时候不保存lastoptime
    public void tmpSave(Batch batch) { 
    	if(batch!=null){
      			batch.setLastoptime(null);
    			batchDB.insert(batch);
     	}
    }
    public void update(Batch batch){
    	if(batch!=null){
    		Batch bc=this.getByBatchId((int)batch.getBatch_id());
    		if(bc!=null){
    			batch.setId(bc.getId());
     			batchDB.update(batch);
    		}
    	}
     }
    /**
     * 在原有的记录数上增加或减少count个记录数
     * @param count
     * @param type
     * @param batchid
     */
    public void updateBatchRecordCount(int count,int type,int batchid){
    	Batch batch=getByBatchId(batchid);
    	int ncount=0;
     	if(batch!=null){
    		if(type==Record.BATCHRECORD_USERTYPE_OF_CREATOR)
    			ncount=batch.getProducecount()+count;
     		else if(type==Record.BATCHRECORD_USERTYPE_OF_CONSUMER)
     			ncount=batch.getConsumercount()+count;
    		batchDB.updateBatchRecordCount(ncount, type, batchid);

    	}
    	
    }
    public Batch getByBatchId(int batchid) { 
       	return batchDB.selectbyBatchId(batchid);
    }
    public Batch getByBatchCode(String batchcode) { 
       	return batchDB.selectbyBatchCode(batchcode);
    }
    public  void deleteByBatchId(int batchid) { 
       	  batchDB.deleteByBatchId(batchid);
    }
    public void deleteByVilleageId(int villeageid){
    	batchDB.deleteByVilleageId(villeageid);
    }
    public List<Batch> getByVilleageid(int vid,int stage) { 
       	return batchDB.selectbyVilleageId(vid,stage);
    }
    
    public List<Batch> serachByVarietyId(int varietyid) { 
       	return batchDB.serachByVarietyId(varietyid);
    }
    
    public List<Batch> serachByVariety(String variety,int stage) { 
       	return batchDB.serachByVariety(variety,stage);
    }
    public List<Batch> serachByCode(String code,int stage) { 
       	return batchDB.serachByCode(code,stage);
    }
    public void deleteNoCodeBatch(){
    	batchDB.deleteNoCodeBatch();
    }
    /**
	 * 获取批次的最后操作时间
	 * @return record_id
	 */
	public String getLatoptime( int villeage_id,int stage){
		return batchDB.getLatoptime(villeage_id,stage);
	}
	 
	/**
	 * 获取置顶的批次
	 * @return record_id
	 */
	public Batch getTopBatch( ){
		return batchDB.getTopBatch();
	}
}
