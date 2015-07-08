package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.Variety;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VarietyDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Variety"; 
	private final static String ID = "id"; 
	private final static String VARIETYID = "variety_id"; 
	private final static String VARIETY_NAME = "variety_name"; 
 	private final static String CATEGORYID = "category_id"; 
	private final static String VILLEAGEID = "villeage_id"; 
	private final static String USERID = "user_id"; 
	private final static String CREATETIME = "create_time"; 
	private final static String VARIETY_DESC = "variety_desc"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	public VarietyDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(Variety variety) {
		if (variety == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(VARIETYID, variety.getVariety_id());
 		cv.put(VARIETY_NAME, variety.getVariety_name());
 		cv.put(CATEGORYID, variety.getCategory_id());
  		cv.put(VILLEAGEID, variety.getVilleage_id());
 		cv.put(USERID, variety.getUser_id());
 		cv.put(CREATETIME, variety.getCreate_time());
 		cv.put(VARIETY_DESC, variety.getVariety_desc());
 		cv.put(ISDEL, variety.getIsdel());
 		cv.put(LASTOPTIME, variety.getLastoptime());
   		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		variety.setId((int)row);
		return row;
	}
	/**
	 * 获取更新时间
	 * @return record_id
	 */
	public String getLatoptime( int villeageid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.villeage_id = ?  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{villeageid+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	public  List<Variety>  selectbyVilleage(int villeage_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.villeage_id = ? and isdel="+DATA_NOT_DELETE+"  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {villeage_id+""});
		int number = cursor.getCount();
 		List<Variety> reds=null;
		if (number > 0) {
			reds=new ArrayList<Variety>();
			Variety red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Variety();
 				red.setId(cursor.getInt(0));
 				red.setVariety_id(cursor.getInt(1));
 				red.setVariety_name(cursor.getString(2));
 				red.setCategory_id(cursor.getInt(3));
 				red.setVilleage_id(cursor.getInt(4));
 				red.setUser_id(cursor.getInt(5));
 				red.setCreate_time(cursor.getString(6));
 				red.setVariety_desc(cursor.getString(7));
  				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public  List<Variety>  queryByVilleageWithName(int vid,String name) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.villeage_id = ? and dat.variety_name like ? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] { vid+"","%"+name+"%"});
		int number = cursor.getCount();
 		List<Variety> reds=null;
		if (number > 0) {
			reds=new ArrayList<Variety>();
			Variety red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Variety();
 				red.setId(cursor.getInt(0));
 				red.setVariety_id(cursor.getInt(1));
 				red.setVariety_name(cursor.getString(2));
 				red.setCategory_id(cursor.getInt(3));
 				red.setVilleage_id(cursor.getInt(4));
 				red.setUser_id(cursor.getInt(5));
 				red.setCreate_time(cursor.getString(6));
 				red.setVariety_desc(cursor.getString(7));
  				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public  Variety  selectbyVarietyId(int varietyid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.variety_id=? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {varietyid+""});
		int number = cursor.getCount();
		Variety red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Variety();
			red.setId(cursor.getInt(0));
			red.setVariety_id(cursor.getInt(1));
			red.setVariety_name(cursor.getString(2));
			red.setCategory_id(cursor.getInt(3));
			red.setVilleage_id(cursor.getInt(4));
			red.setUser_id(cursor.getInt(5));
			red.setCreate_time(cursor.getString(6));
			red.setVariety_desc(cursor.getString(7));
    	}
		cursor.close();
		db.close();
		return red;
	}
	 
	 
	public void deleteByVilleageId(int villeage_id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = VILLEAGEID + " = ?";
		String[] whereValue = {villeage_id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	 
 
	public  void update(Variety variety) {
		if (variety == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(variety.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(VARIETYID, variety.getVariety_id());
 		cv.put(VARIETY_NAME, variety.getVariety_name());
 		cv.put(CATEGORYID, variety.getCategory_id());
  		cv.put(VILLEAGEID, variety.getVilleage_id());
 		cv.put(USERID, variety.getUser_id());
 		cv.put(CREATETIME, variety.getCreate_time());
 		cv.put(VARIETY_DESC, variety.getVariety_desc());
 		cv.put(ISDEL, variety.getIsdel());
 		cv.put(LASTOPTIME, variety.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
