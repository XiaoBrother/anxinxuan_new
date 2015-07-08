package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Category"; 
	private final static String ID = "id"; 
	private final static String CATEGORY_NAME = "category_name"; 
	private final static String PARENTID = "parentid"; 
 	private final static String CATEORDER = "cate_order"; 
	private final static String CLEVEL = "clevel"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	public CategoryDB(Context context) {
		super(context);
	}
  	 
	public long insert(Category cate) {
		if (cate == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
		cv.put(ID, cate.getId());
		cv.put(CATEGORY_NAME, cate.getCategory_name());
		cv.put(PARENTID, cate.getParentid());
 		cv.put(CATEORDER, cate.getCate_order());
		cv.put(ISDEL, cate.getIsdel());
		cv.put(CLEVEL, cate.getClevel());
		cv.put(LASTOPTIME, cate.getLastoptime());
 		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}
	 public  void update(Category cate) {
		if (cate == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(cate.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(CATEGORY_NAME, cate.getCategory_name());
		cv.put(PARENTID, cate.getParentid());
 		cv.put(CATEORDER, cate.getCate_order());
		cv.put(ISDEL, cate.getIsdel());
		cv.put(CLEVEL, cate.getClevel());
		cv.put(LASTOPTIME, cate.getLastoptime());
   		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	 
	/**
	 * 获取最后更新时间
	 * @return record_id
	 */
	public String getLatoptime() {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
 		String sql="select max(lastoptime) ";
		sql+= " from "+TABLE_NAME+" dat ";
  		Cursor cursor = db.rawQuery(sql, null);
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}

	public  List<Category>  selectbyParentId(long parentid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   parentid = ?  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {parentid+""});
		int number = cursor.getCount();
 		List<Category> reds=null;
		if (number > 0) {
			reds=new ArrayList<Category>();
			Category red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Category();
 				red.setId(cursor.getInt(0));
 				red.setCategory_name(cursor.getString(1));
 				red.setParentid(cursor.getInt(2));
 				red.setCate_order(cursor.getInt(3));
 				red.setClevel(cursor.getInt(4));
 				red.setIsdel(cursor.getInt(5));
 				red.setLastoptime(cursor.getString(6));
  				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	 
	public   Category  getById(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   id = ?  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {id+""});
		int number = cursor.getCount();
		Category red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
  			red=new Category();
 			red.setId(cursor.getInt(0));
 			red.setCategory_name(cursor.getString(1));
 			red.setParentid(cursor.getInt(2));
 			red.setCate_order(cursor.getInt(3));
 			red.setClevel(cursor.getInt(4));
 			red.setIsdel(cursor.getInt(5));
			red.setLastoptime(cursor.getString(6));
   		}
		cursor.close();
		db.close();
		return red;
	}
	
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
