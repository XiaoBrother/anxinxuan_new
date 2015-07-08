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
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SystemLogDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_SystemLog"; 
	private final static String ID = "id"; 
	private final static String MESSAGE = "message"; 
	private final static String ACTIONTIME = "actiontime"; 
 	private final static String EXTRAINFO = "extrainfo"; 
 	
	public SystemLogDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(SystemLog log) {
		if (log == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(MESSAGE, log.getMessage());
 		cv.put(ACTIONTIME, log.getActiontime());
 		cv.put(EXTRAINFO, log.getExtrainfo());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		log.setId((int)row);
		return row;
	}
 
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public  List<SystemLog>  getAllLog() {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
 		sb.append(select);
		sb.append(from);
    	Cursor cursor = db.rawQuery(sb.toString(), null);
		int number = cursor.getCount();
 		List<SystemLog> reds=null;
		if (number > 0) {
			reds=new ArrayList<SystemLog>();
			SystemLog red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new SystemLog();
 				red.setId(cursor.getInt(0));
   			    red.setActiontime(cursor.getString(1));
  			    red.setMessage(cursor.getString(2));
  			    red.setExtrainfo(cursor.getString(3));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public  void update(SystemLog log) {
		if (log == null) {
			return ; 
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(log.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(MESSAGE, log.getMessage());
 		cv.put(ACTIONTIME, log.getActiontime());
 		cv.put(EXTRAINFO, log.getExtrainfo());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
