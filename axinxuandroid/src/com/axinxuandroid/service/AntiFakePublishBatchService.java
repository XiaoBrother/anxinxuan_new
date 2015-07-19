package com.axinxuandroid.service;

 
import java.util.List;

import com.axinxuandroid.data.AntiFakePublishBatch;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.db.AntiFakePublishBatchDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.sys.gloable.Gloable;

public class AntiFakePublishBatchService {
	private AntiFakePublishBatchDB pbatchDB;
	public  AntiFakePublishBatchService(){
		pbatchDB=new AntiFakePublishBatchDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	public void clearData(){
		pbatchDB.clearData();
	}
	  //保存或更新
    public void saveOrUpdateBatch(AntiFakePublishBatch pbatch) { 
    	if(pbatch!=null){
    		AntiFakePublishBatch bc=this.getById((int)pbatch.getId());
     		if(bc==null){
     			pbatchDB.insert(pbatch);
    		}else{
    			if(bc.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				pbatch.setId(bc.getId());
    				pbatchDB.update(pbatch);
    			}
    			
    		}
    	}
    	
    }
  //保存的时候不保存lastoptime
    public void tmpSave(AntiFakePublishBatch pbatch) { 
    	if(pbatch!=null){
      			pbatch.setLastoptime(null);
      			pbatchDB.insert(pbatch);
     	}
    }
    public void update(AntiFakePublishBatch pbatch){
    	if(pbatch!=null){
    		AntiFakePublishBatch bc=this.getById((int)pbatch.getId());
    		if(bc!=null){
    			pbatch.setId(bc.getId());
    			pbatchDB.update(pbatch);
    		}
    	}
     }
    public AntiFakePublishBatch getById(int batchid) { 
       	return pbatchDB.selectbyId(batchid);
    }
    public  void deleteByBatchId(int id) { 
       	  pbatchDB.deleteById(id);
    }
    public List<AntiFakePublishBatch> getByUserId(int user_id) { 
       	return pbatchDB.selectbyUserId(user_id);
    }
    
    /**
	 * 获取批次的最后操作时间
	 * @return record_id
	 */
	public String getLatoptime( int user_id){
		return pbatchDB.getLatoptime(user_id);
	}
	 
}
