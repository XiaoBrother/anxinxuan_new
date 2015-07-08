package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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

public class ProustDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Proust"; 
	private final static String ID = "id"; 
	private final static String USER_ID = "user_id"; 
	private final static String PROUST_ID = "proust_id"; 
 	private final static String ANSWER = "answer"; 
	private final static String QUESTION = "question"; 
	private final static String CREATEDATE = "create_date"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	public ProustDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(Proust proust) {
		if (proust == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(USER_ID, proust.getUser_id());
 		cv.put(PROUST_ID, proust.getProust_id());
 		cv.put(ANSWER, proust.getAnswer());
  		cv.put(QUESTION, proust.getQuestion());
 		cv.put(CREATEDATE, proust.getCreate_date());
 		cv.put(ISDEL, proust.getIsdel());
 		cv.put(LASTOPTIME, proust.getLastoptime());
   		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		proust.setId((int)row);
		return row;
	}
	/**
	 * 获取批次的最后更新时间
	 * @return record_id
	 */
	public String getLatoptime(int userid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
 		String sql="select max(lastoptime) ";
		sql+= " from "+TABLE_NAME+" dat where user_id=?";
  		Cursor cursor = db.rawQuery(sql, new String[] {userid+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	public  List<Proust>  selectbyUserId(int user_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.user_id = ?  and isdel="+DATA_NOT_DELETE+" order by proust_id";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {user_id+""});
		int number = cursor.getCount();
 		List<Proust> reds=null;
		if (number > 0) {
			reds=new ArrayList<Proust>();
			Proust red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Proust();
 				red.setId(cursor.getInt(0));
  				red.setUser_id(cursor.getInt(1));
  				red.setAnswer(cursor.getString(2));
  				red.setQuestion(cursor.getString(3));
  				red.setProust_id(cursor.getInt(4));
  				red.setCreate_date(cursor.getString(5));
  				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	 
	public  Proust  selectbyUserIdProustId(int user_id,int proust_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.user_id = ? and dat.proust_id=? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {user_id+"",proust_id+""});
		int number = cursor.getCount();
		Proust red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Proust();
			red.setId(cursor.getInt(0));
			red.setUser_id(cursor.getInt(1));
			red.setAnswer(cursor.getString(2));
			red.setQuestion(cursor.getString(3));
			red.setProust_id(cursor.getInt(4));
			red.setCreate_date(cursor.getString(5));
    		}
		cursor.close();
		db.close();
		return red;
	}
	public  Proust  selectbyId(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.id = ?  and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {id+""});
		int number = cursor.getCount();
		Proust red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Proust();
			red.setId(cursor.getInt(0));
			red.setUser_id(cursor.getInt(1));
			red.setAnswer(cursor.getString(2));
			red.setQuestion(cursor.getString(3));
			red.setProust_id(cursor.getInt(4));
			red.setCreate_date(cursor.getString(5));
    		}
		cursor.close();
		db.close();
		return red;
	}
	public void deleteByUserId(int user_id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = USER_ID + " = ?";
		String[] whereValue = {user_id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public void deleteByUserIdProustId(int user_id,int proust_id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = USER_ID + " = ? and "+PROUST_ID+" =?";
		String[] whereValue = {user_id+"",proust_id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
 
	public  void update(Proust proust) {
		if (proust == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(proust.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(USER_ID, proust.getUser_id());
 		cv.put(PROUST_ID, proust.getProust_id());
 		cv.put(ANSWER, proust.getAnswer());
  		cv.put(QUESTION, proust.getQuestion());
 		cv.put(CREATEDATE, proust.getCreate_date());
 		cv.put(ISDEL, proust.getIsdel());
 		cv.put(LASTOPTIME, proust.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	} 
}
