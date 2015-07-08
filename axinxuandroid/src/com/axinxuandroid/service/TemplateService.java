package com.axinxuandroid.service;

 
import java.util.ArrayList;
 import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
 

import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.db.BatchDB;
import com.axinxuandroid.db.BatchLabelDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.TemplateDB;
 
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class TemplateService {
	private TemplateDB templateDB;
	public  TemplateService(){
		templateDB=new TemplateDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	  //保存或更新
    public void saveOrUpdateTemplate(Template template) { 
    	if(template!=null){
    		Template bc=this.getByTemplateId(template.getTemplate_id());
    		if(bc==null){
    			templateDB.insert(template);
    		}else{
    			if(bc.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				template.setId(bc.getId());
        			templateDB.update(template);
    			}
     		}
    	}
    	
    }
    public String getLatoptime(){
    	return templateDB.getLatoptime();
    }
    public Template getByTemplateId(int template_id) { 
       	return templateDB.selectByTemplateId(template_id);
    }
	 
    public Template selectByCategoryWithLabelName(int cid,String labelname) { 
       	return templateDB.selectByCategoryWithLabelName(cid, labelname);
    }
    public List<String> selectByCategoryWithLabelNameToList(int cid,String labelname) { 
    	Template temp=templateDB.selectByCategoryWithLabelName(cid, labelname);
    	List<String> rddata=null;
    	try{
    		if(temp!=null){
        		rddata=new ArrayList<String>();
        		JSONObject contextjson = new JSONObject(temp.getContext());
    			JSONArray titles=contextjson.getJSONArray("titles");
    			for (int i = 0; i < titles.length(); i++) {
    					rddata.add(titles.getJSONObject(i).getString("name"));
    			}
        	}
    	}catch (Exception e) {
    		
 		}
    	return rddata;
    }
    public List<Template> getAllTemplate(){
    	return templateDB.getAllTemplate();
    }
}
