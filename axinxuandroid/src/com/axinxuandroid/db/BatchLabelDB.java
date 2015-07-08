package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BatchLabelDB extends SystemDB {
	
	private final static String TABLE_NAME = "T_android_Batch_Label"; 
	private final static String ID = "id"; 
 	private final static String VARIETYID = "variety_id"; 
	private final static String LABELNAME = "label_name"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static String RECORDID = "recordid"; 

	public BatchLabelDB(Context context) {
		super(context);
	}
	/**
	 * 获取更新时间
	 * @return record_id
	 */
	public String getLatoptime( int variety_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.variety_id = ?  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{variety_id+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	  
	public List<BatchLabel> getBatchLabelsWithVariety(int variety_id) {
 		String sql = "select dat.id,dat.variety_id,dat.label_name";
		sql+= " from "+TABLE_NAME+" dat";
		sql+= " where   dat.variety_id = ? and isdel="+DATA_NOT_DELETE+" order by recordid desc ";
 		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {variety_id+""});
		int number=cursor.getCount();
		List<BatchLabel> labels=null;
	    if(number>0){
	        cursor.moveToFirst();
	        labels=new ArrayList<BatchLabel>();
	        BatchLabel lable;
	        while (cursor.getPosition() != number) {
	        	lable=new BatchLabel();
	        	lable.setId(cursor.getLong(0));
	        	lable.setVariety_id(cursor.getInt(1));
	        	lable.setLabel_name(cursor.getString(2));
  	        	labels.add(lable); 
				cursor.moveToNext();
			}
 	     }
	    cursor.close();
	    db.close();
		return labels;
		
	} 
	public  BatchLabel getBatchLabelsWithVariety(int variety_id,String labelname) {
 		String sql = "select dat.id,dat.variety_id,dat.label_name";
		sql+= " from "+TABLE_NAME+" dat";
		sql+= " where   dat.variety_id = ? and dat.label_name=? and isdel="+DATA_NOT_DELETE+" order by recordid desc";
 		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {variety_id+"",labelname});
		int number=cursor.getCount();
		BatchLabel lable=null;
 	    if(number>0){
	        cursor.moveToFirst();
 	        lable=new BatchLabel();
 	        lable.setId(cursor.getLong(0));
	        lable.setVariety_id(cursor.getInt(1));
	        lable.setLabel_name(cursor.getString(2));
   	     }
	    cursor.close();
	    db.close();
		return lable;
		
	} 
	
	public  BatchLabel getBatchLabelsWithRecordId(int recordid) {
 		String sql = "select dat.id,dat.variety_id,dat.label_name";
		sql+= " from "+TABLE_NAME+" dat";
		sql+= " where   recordid = ?  ";
 		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {recordid+""});
		int number=cursor.getCount();
		BatchLabel lable=null;
 	    if(number>0){
	        cursor.moveToFirst();
 	        lable=new BatchLabel();
 	        lable.setId(cursor.getLong(0));
	        lable.setVariety_id(cursor.getInt(1));
	        lable.setLabel_name(cursor.getString(2));
   	     }
	    cursor.close();
	    db.close();
		return lable;
		
	} 
	public List<BatchLabel> getBatchLabelsWithLabelname(int variety_id,String lablename) {
 		String sql = "select dat.id,dat.variety_id,dat.label_name";
		sql+= " from "+TABLE_NAME+" dat";
		sql+= " where   dat.label_name like ? and dat.variety_id=? and isdel="+DATA_NOT_DELETE+" order by recordid desc";
 		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {"%"+lablename+"%",variety_id+""});
		int number=cursor.getCount();
		List<BatchLabel> labels=null;
	    if(number>0){
	        cursor.moveToFirst();
	        labels=new ArrayList<BatchLabel>();
	        BatchLabel lable;
	        while (cursor.getPosition() != number) {
	        lable=new BatchLabel();
	        lable.setId(cursor.getLong(0));
	        lable.setVariety_id(cursor.getInt(1));
	        lable.setLabel_name(cursor.getString(2));
	        labels.add(lable); 
	        cursor.moveToNext();
	        }
 	     }
	    cursor.close();
	    db.close();
		return labels;
	} 
	
	public long insert(BatchLabel link) {
		if (link == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
		cv.put(VARIETYID, link.getVariety_id());
		cv.put(LABELNAME, link.getLabel_name());
		cv.put(ISDEL, link.getIsdel());
		cv.put(LASTOPTIME, link.getLastoptime());
		cv.put(RECORDID, link.getRecordid());
 		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}
	 public  void update(BatchLabel link) {
		if (link == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(link.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(VARIETYID, link.getVariety_id());
		cv.put(LABELNAME, link.getLabel_name());
		cv.put(ISDEL, link.getIsdel());
		cv.put(LASTOPTIME, link.getLastoptime());
		cv.put(RECORDID, link.getRecordid());
   		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	 
  
	
	/**
	 *  
	 */
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(TABLE_NAME, null, null);
 		db.close();
	}
	
	
	
	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
 
	public void deleteByVid(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = VARIETYID + " = ?";
		String[] whereValue = {id};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	} 
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
