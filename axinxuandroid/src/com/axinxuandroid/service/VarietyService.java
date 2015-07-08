package com.axinxuandroid.service;

 
 import java.util.ArrayList;
import java.util.List;

 

import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Variety;

import com.axinxuandroid.db.CategoryDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.VarietyDB;
 
import com.axinxuandroid.sys.gloable.Gloable;

public class VarietyService {
	private VarietyDB varietyDB;
	private CategoryDB categoryDB;
	public  VarietyService(){
		varietyDB=new VarietyDB(Gloable.getInstance().getCurContext().getApplicationContext());
		categoryDB=new CategoryDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	public void clearData(){
		varietyDB.clearData();
	}
	 //保存
    public void save(Variety variety) { 
    	if(variety!=null){
     	   varietyDB.insert(variety);
     	}
     }
	  //保存或更新
    public void saveOrUpdate(Variety variety) { 
    	if(variety!=null){
    		Variety va=this.getByVarietyId(variety.getVariety_id());
    		if(va==null){
    			varietyDB.insert(variety);
    		}else{
    			if(va.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				variety.setId(va.getId());
        			varietyDB.update(variety);
    			}
     		}
    	}
    	
    }
    
    public void deleteByVilleageid(int vid){
    	varietyDB.deleteByVilleageId(vid);
    }
    
    public Variety getByVarietyId(int varietyid) { 
       	return varietyDB.selectbyVarietyId(varietyid);
    }
	
    public List<Variety> queryByVilleageWithName(int vid,String name) { 
    	List<Variety> vars= varietyDB.queryByVilleageWithName(vid, name);
    	if(vars!=null&&vars.size()>0)
    		for(Variety var:vars)
    			setCategoryNames(var);
    	return vars;
    }
    public String getLatoptime( int villeageid){
    	return varietyDB.getLatoptime(villeageid);
    }
    public List<Variety> selectbyVilleage(int vid) { 
    	List<Variety> vars= varietyDB.selectbyVilleage(vid);
    	if(vars!=null&&vars.size()>0)
    		for(Variety var:vars)
    			setCategoryNames(var);
    	return vars;
    }
     
    public void setCategoryNames(Variety var){
    	if(var!=null){
    		List<String> names=new ArrayList<String>();
    		getParentCategorys(var.getCategory_id(),names);
    		StringBuffer sb=new StringBuffer();
    		if(names!=null){
 				for (int i = names.size() - 1; i > -1; i--) {
 					if(i==0)
					  sb.append(names.get(i) + ">");
 					else  sb.append(names.get(i) + "/");
				}
    			
    			var.setCategorynames(sb.toString());
    		}
     	}
    }
    private void getParentCategorys(long id,List<String> names){
 		if(names==null) names=new ArrayList<String>() ;
 		Category  cat=categoryDB.getById(id);
 		if(cat!=null){
 			names.add(cat.getCategory_name());
 			getParentCategorys(cat.getParentid(),names);
 		}
 	}
}
