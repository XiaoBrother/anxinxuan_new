package com.axinxuandroid.service;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
 import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.CategoryProcedure;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.CategoryProcedureDB;
import com.axinxuandroid.db.SystemDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class BatchLabelService {
	private BatchLabelDB labelDB;
	private CategoryProcedureDB catdb;
	public  BatchLabelService(){
		labelDB=new BatchLabelDB(Gloable.getInstance().getCurContext().getApplicationContext());
		catdb=new CategoryProcedureDB(Gloable.getInstance().getCurContext());
 	}
	 
	public void clearData(){
		labelDB.clearData();
	}
    
    
    public void saveOrUpdate(BatchLabel label){
    	if(label!=null){
    		BatchLabel dblabel=getBatchLabelsWithRecordId(label.getRecordid());
    		if(dblabel==null){
    			labelDB.insert(label);
    		}else{
    			if(dblabel.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				label.setId(dblabel.getId());
        			labelDB.update(label);
    			}
    			
    		}
    	}
    }
    public  BatchLabel getBatchLabelsWithRecordId(int recordid){
    	return labelDB.getBatchLabelsWithRecordId(recordid);
    }
    
    //查询某个品种下的某个环节
    public BatchLabel getBatchLabelWithVarietyAndName(int variety_id,String labelname) { 
       	return labelDB.getBatchLabelsWithVariety(variety_id, labelname);
    }

	// 获取标签
	public List getLabelsWithVarietyCategory(int varietyid, int categoryid) {
		Set<String> labels = new LinkedHashSet<String>();
		List<BatchLabel> vas = labelDB.getBatchLabelsWithVariety(varietyid);
		if (vas != null && vas.size() > 0) {
			for (BatchLabel bl : vas) {
				labels.add(bl.getLabel_name());
			}
		}
		List<CategoryProcedure> pros = catdb.getLabelsWithCategory(categoryid);
		if (pros != null && pros.size() > 0)
			for (CategoryProcedure cat : pros)
				labels.add(cat.getName());
		
		return Arrays.asList(labels.toArray());
	}
	  //查询本地数据
    public List getLabelsWithLablename(int variety_id,int categoryid,String lable_name) { 
    	Set<String> labels = new LinkedHashSet<String>();
    	List<BatchLabel> vas = labelDB.getBatchLabelsWithLabelname(variety_id,lable_name);
    	if (vas != null && vas.size() > 0) {
			for (BatchLabel bl : vas) {
				labels.add(bl.getLabel_name());
			}
		}
    	List<CategoryProcedure> pros = catdb.getLabelsWithCategoryAndName(categoryid,lable_name);
		if (pros != null && pros.size() > 0)
			for (CategoryProcedure cat : pros)
				labels.add(cat.getName());
		
		return Arrays.asList(labels.toArray());
    }
    /**
	 * 获取更新时间
	 * @return record_id
	 */
	public String getLatoptime( int variety_id){
		return labelDB.getLatoptime(variety_id);
	}
     
}
