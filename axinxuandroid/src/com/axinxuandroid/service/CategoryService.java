package com.axinxuandroid.service;

 
 import java.util.ArrayList;
import java.util.List;

 

import com.axinxuandroid.data.Advocate;
import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Variety;

import com.axinxuandroid.db.CategoryDB;
import com.axinxuandroid.db.VarietyDB;
 
import com.axinxuandroid.sys.gloable.Gloable;

public class CategoryService {
	private CategoryDB categoryDB;
	public  CategoryService(){
		categoryDB=new CategoryDB(Gloable.getInstance().getCurContext().getApplicationContext());
 	}
	 
	public  List<Category>  selectbyParentId(long parentid){
		return categoryDB.selectbyParentId(parentid);
	}
	
	
	public Category getById(long id){
		return categoryDB.getById(id);
	}
	/**
	 * 获取最后更新时间
	 * @return record_id
	 */
	public String getLatoptime() {
		return categoryDB.getLatoptime();
	}
	
	public  void saveOrUpdate(Category cate){
		if (cate != null) {
			Category bc = this.getById(cate.getId());
			if (bc == null) {
				categoryDB.insert(cate);
			} else {
				cate.setId(bc.getId());
				categoryDB.update(cate);

			}
		}
	}
     
}
