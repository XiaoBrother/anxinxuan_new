package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.CategoryProcedure;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.Variety;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryProcedureDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_CategoryProcedure"; 
	private final static String ID = "id"; 
	private final static String CATEGORY_PROCEDUREID = "categoryprocedureid"; 
	private final static String CATEGORYID = "category_id"; 
 	private final static String NAME = "name"; 
	private final static String INDEX_ORDER = "index_order"; 
	private final static String CREATE_TIME = "create_time"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
 
	public CategoryProcedureDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(CategoryProcedure catp) {
		if (catp == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(CATEGORY_PROCEDUREID, catp.getCategoryprocedureid());
 		cv.put(CATEGORYID, catp.getCategory_id());
 		cv.put(NAME, catp.getName());
  		cv.put(INDEX_ORDER,catp.getIndex_order());
 		cv.put(CREATE_TIME, catp.getCreate_time());
 		cv.put(ISDEL, catp.getIsdel());
 		cv.put(LASTOPTIME, catp.getLastoptime());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		catp.setId((int)row);
		return row;
	}
	
	public List<CategoryProcedure> getLabelsWithCategory(int categoryid) {
 		String sql = "select *  ";
		sql+= " from "+TABLE_NAME+" dat";
		sql+= " where  ( category_id = ? or category_id is null) and isdel="+DATA_NOT_DELETE;
 		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {categoryid+""});
		int number=cursor.getCount();
		List<CategoryProcedure> cats=null;
	    if(number>0){
	        cursor.moveToFirst();
	        cats=new ArrayList<CategoryProcedure>();
	        CategoryProcedure cat;
	        while (cursor.getPosition() != number) {
	        	cat=new CategoryProcedure();
	        	cat.setId(cursor.getInt(0));
	        	cat.setCategoryprocedureid(cursor.getInt(1));
	        	cat.setCategory_id(cursor.getInt(2));
	        	cat.setName(cursor.getString(3));
	        	cat.setIndex_order(cursor.getInt(4));
	        	cat.setCreate_time(cursor.getString(5));
	        	cat.setIsdel(cursor.getInt(6));;
	        	cat.setLastoptime(cursor.getString(7));
	        	cats.add(cat); 
				cursor.moveToNext();
			}
 	     }
	    cursor.close();
	    db.close();
		return cats;
		
	} 
 
	public List<CategoryProcedure> getLabelsWithCategoryAndName(int categoryid,String name) {
 		String sql = "select *  ";
		sql+= " from "+TABLE_NAME+" dat";
		sql+= " where   ( category_id = ? or category_id is null) and name like ? and isdel="+DATA_NOT_DELETE;
 		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {categoryid+""});
		int number=cursor.getCount();
		List<CategoryProcedure> cats=null;
	    if(number>0){
	        cursor.moveToFirst();
	        cats=new ArrayList<CategoryProcedure>();
	        CategoryProcedure cat;
	        while (cursor.getPosition() != number) {
	        	cat=new CategoryProcedure();
	        	cat.setId(cursor.getInt(0));
	        	cat.setCategoryprocedureid(cursor.getInt(1));
	        	cat.setCategory_id(cursor.getInt(2));
	        	cat.setName(cursor.getString(3));
	        	cat.setIndex_order(cursor.getInt(4));
	        	cat.setCreate_time(cursor.getString(5));
	        	cat.setIsdel(cursor.getInt(6));;
	        	cat.setLastoptime(cursor.getString(7));
	        	cats.add(cat); 
				cursor.moveToNext();
			}
 	     }
	    cursor.close();
	    db.close();
		return cats;
		
	} 
	public  void update(CategoryProcedure catp) {
		if (catp == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(catp.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(CATEGORY_PROCEDUREID, catp.getCategoryprocedureid());
 		cv.put(CATEGORYID, catp.getCategory_id());
 		cv.put(NAME, catp.getName());
  		cv.put(INDEX_ORDER,catp.getIndex_order());
 		cv.put(CREATE_TIME, catp.getCreate_time());
 		cv.put(ISDEL, catp.getIsdel());
 		cv.put(LASTOPTIME, catp.getLastoptime());
   		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
