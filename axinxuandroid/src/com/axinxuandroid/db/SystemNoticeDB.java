package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SystemNoticeDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_SystemNotice"; 
	private final static String ID = "id"; 
	private final static String TYPE = "type"; 
	private final static String NOTICE = "notice"; 
 	private final static String JSONDATA = "jsondata"; 
 	
	public SystemNoticeDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(SystemNotice notice) {
		if (notice == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(TYPE, notice.getType());
 		cv.put(NOTICE, notice.getNotice());
 		cv.put(JSONDATA, notice.getJsondata());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		notice.setId((int)row);
		return row;
	}

	public  SystemNotice  selectbyId(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   id = ?    ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {id+""});
		int number = cursor.getCount();
		SystemNotice notice=null;
		if (number > 0) {
 			cursor.moveToFirst();
 			notice=new SystemNotice();
 			notice.setId(cursor.getInt(0));
 			notice.setType(cursor.getInt(1));
 			notice.setNotice(cursor.getString(2));
 			notice.setJsondata(cursor.getString(3));
   		}
		cursor.close();
		db.close();
		return notice;
	}
	public  SystemNotice  selectbyType(int type) {
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
		SystemNotice notice=null;
		if (number > 0) {
 			cursor.moveToFirst();
 			notice=new SystemNotice();
 			notice.setId(cursor.getInt(0));
 			notice.setType(cursor.getInt(1));
 			notice.setNotice(cursor.getString(2));
 			notice.setJsondata(cursor.getString(3));
   		}
		cursor.close();
		db.close();
		return notice;
	}
	 
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public void deleteByType(int type) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = TYPE + " = ?";
		String[] whereValue = {type+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public  void update(SystemNotice notice) {
		if (notice == null) {
			return ; 
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(notice.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(TYPE, notice.getType());
 		cv.put(NOTICE, notice.getNotice());
 		cv.put(JSONDATA, notice.getJsondata());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
