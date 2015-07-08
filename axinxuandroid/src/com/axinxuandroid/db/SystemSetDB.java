package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.SystemSet;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SystemSetDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_SystemSet"; 
	private final static String ID = "id"; 
	private final static String TYPE = "type"; 
	private final static String SET = "setjson"; 
  	
	public SystemSetDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(SystemSet set) {
		if (set == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(TYPE, set.getType());
 		cv.put(SET, set.getSetJsonString());
     	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		set.setId((int)row);
		return row;
	}
 
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public  SystemSet  getByType(int type) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
 		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   type = ?    ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {type+""});
		int number = cursor.getCount();
		SystemSet set=null;
 		if (number > 0) {
 		    set=new SystemSet();
			cursor.moveToFirst();
			set.setId(cursor.getInt(0));
			set.setType(cursor.getInt(1));
			set.setSet(cursor.getString(2));
    	}
		cursor.close();
		db.close();
		return set;
	}
	public  void update(SystemSet set) {
		if (set == null) {
			return ; 
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(set.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(TYPE, set.getType());
 		cv.put(SET, set.getSetJsonString());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
