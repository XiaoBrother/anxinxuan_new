package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.UserVisitHistory;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserVisitHistoryDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_UserVisitHistory"; 
	private final static String ID = "id"; 
	private final static String USERID = "userid"; 
	private final static String TYPE = "type"; 
 	private final static String VISITOBJID = "visitobjid"; 
	private final static String VISITTIME = "visittime"; 
	private final static int PER_PAGE_COUNT=10;//每页查询个数
 
	public UserVisitHistoryDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(UserVisitHistory history) {
		if (history == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(USERID, history.getUserid());
 		cv.put(TYPE, history.getType());
 		cv.put(VISITOBJID, history.getVisitobjid());
  		cv.put(VISITTIME,history.getVisittime());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		history.setId((int)row);
		return row;
	}
	public  UserVisitHistory   selectbyTypeWithUseridAndVisitobj(int type,int userid,int objid) {
  		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where     dat.type = ? and userid= ? and visitobjid= ? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {type+"",userid+"",objid+""});
		int number = cursor.getCount();
		UserVisitHistory uf = null;
		if (number > 0) {
			cursor.moveToFirst();
		    uf = new UserVisitHistory();
			uf.setId(cursor.getInt(0));
			uf.setUserid(cursor.getInt(1));
			uf.setType(cursor.getInt(2));
			uf.setVisitobjid(cursor.getInt(3));
			uf.setVisittime(cursor.getString(4));
 
		}
		cursor.close();
		db.close();
		return uf;
	}
	public  List<UserVisitHistory>  selectbyTypeWithUserid(int type,int userid,String visittime) {
  		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where     dat.type = ? and userid= ? and dat.visittime < ? order by visittime desc limit  "+PER_PAGE_COUNT;
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {type+"",userid+"",visittime});
		int number = cursor.getCount();
		List<UserVisitHistory> favs=null;
		if (number > 0) { 
			favs=new ArrayList<UserVisitHistory>();
 			while(cursor.moveToNext()){
 				UserVisitHistory uf=null;
				uf=new UserVisitHistory();
				uf.setId(cursor.getInt(0));
				uf.setUserid(cursor.getInt(1));
				uf.setType(cursor.getInt(2));
				uf.setVisitobjid(cursor.getInt(3));
				uf.setVisittime(cursor.getString(4));
 				favs.add(uf);
			}
  			
   		}
		cursor.close();
		db.close();
		return favs;
	}
	public  void update(UserVisitHistory history) {
		if (history == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(history.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(USERID, history.getUserid());
 		cv.put(TYPE, history.getType());
 		cv.put(VISITOBJID, history.getVisitobjid());
  		cv.put(VISITTIME,history.getVisittime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
 
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
