package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Advocate;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AdvocateDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Advocate"; 
	private final static String ID = "id"; 
	private final static String USER_ID = "userid"; 
	private final static String RECORD_ID = "recordid"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	public AdvocateDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(Advocate adv) {
		if (adv == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(USER_ID, adv.getUserid());
 		cv.put(RECORD_ID, adv.getRecordid());
 		cv.put(ISDEL, adv.getIsdel());
 		cv.put(LASTOPTIME, adv.getLastoptime());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		adv.setId((int)row);
		return row;
	}
	 
	public  List<Advocate>  selectbyUserId(int user_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.userid = ?  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {user_id+""});
		int number = cursor.getCount();
 		List<Advocate> reds=null;
		if (number > 0) {
			reds=new ArrayList<Advocate>();
			Advocate red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Advocate();
 				red.setId(cursor.getInt(0));
  				red.setUserid(cursor.getInt(1));
   				red.setRecordid(cursor.getInt(2));
   				red.setIsdel(cursor.getInt(3));
   				red.setLastoptime(cursor.getString(4));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	 
	public  Advocate  selectbyUserIdRecordId(int user_id,int recordid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.userid = ? and dat.recordid=?  and dat.isdel =?";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {user_id+"",recordid+"",DATA_NOT_DELETE+""});
		int number = cursor.getCount();
		Advocate red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Advocate();
			red.setId(cursor.getInt(0));
			red.setUserid(cursor.getInt(1));
			red.setRecordid(cursor.getInt(2));
			red.setIsdel(cursor.getInt(3));
			red.setLastoptime(cursor.getString(4));
    		}
		cursor.close();
		db.close();
		return red;
	}
	public  Advocate  selectbyId(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.id = ?   ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {id+""});
		int number = cursor.getCount();
		Advocate red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Advocate();
			red.setId(cursor.getInt(0));
			red.setUserid(cursor.getInt(1));
			red.setRecordid(cursor.getInt(2));
			red.setIsdel(cursor.getInt(3));
			red.setLastoptime(cursor.getString(4));
    		}
		cursor.close();
		db.close();
		return red;
	}
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
 
 
	public  void update(Advocate adv) {
		if (adv == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(adv.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(USER_ID, adv.getUserid());
 		cv.put(RECORD_ID, adv.getRecordid());
 		cv.put(ISDEL, adv.getIsdel());
 		cv.put(LASTOPTIME, adv.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	} 
}
