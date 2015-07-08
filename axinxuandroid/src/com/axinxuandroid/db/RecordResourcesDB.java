package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.List;

 import com.axinxuandroid.data.RecordResource;
import com.ncpzs.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class RecordResourcesDB extends SystemDB{
    private static final String TABLE_NAME="T_android_Record_Resource";
    private static final String NETURL="neturl";
    private static final String LOCALURL="localurl";
    private static final String TYPE="type";
    private static final String INFO="info";
    private static final String RECORDID="recordid";
    private static final String ID="id";
    private static final String STATE="state";
    private static final String PUBLISHSTATE="publishstate";
 	public RecordResourcesDB(Context context) {
		super(context);
 	}
 
	
	public long insertResource(RecordResource rr){
		if(rr!=null){
			SQLiteDatabase db = this.getWritableDatabase();
 	 		ContentValues cv = new ContentValues();
			cv.put(NETURL, rr.getNeturl());
			cv.put(LOCALURL, rr.getLocalurl());
			cv.put(TYPE, rr.getType());
			cv.put(RECORDID, rr.getRecordid());
			cv.put(INFO, rr.getInfo());
			cv.put(STATE, rr.getState());
			cv.put(PUBLISHSTATE, rr.getPublishstate());
 	 		long row = db.insert(TABLE_NAME, null, cv);
	 		db.close();
	 		rr.setId(row);
	 		return row;
		}
		return -1;
 	}
	
	public void insertResources(List<RecordResource> rr){
		if(rr!=null&&rr.size()>0){
			SQLiteDatabase db = this.getWritableDatabase();
			for(RecordResource rec:rr){
				ContentValues cv = new ContentValues();
				cv.put(NETURL, rec.getNeturl());
				cv.put(LOCALURL, rec.getLocalurl());
				cv.put(TYPE, rec.getType());
				cv.put(RECORDID, rec.getRecordid());
				cv.put(STATE, rec.getState());
				cv.put(INFO, rec.getInfo());
				cv.put(PUBLISHSTATE, rec.getPublishstate());
 				rec.setId(db.insert(TABLE_NAME, null, cv));
			}
			db.close();
		}
  	}
	
	public List<RecordResource> getResourcesByRecordId(long recordid){
		SQLiteDatabase db = this.getReadableDatabase();
 		String sql = "select * ";
 		sql+= " from "+TABLE_NAME+" dat";
 		sql+= " where dat."+RECORDID+"= ?  order by id desc";
 		Cursor cursor = db.rawQuery(sql, new String[] {recordid+""});
		int number = cursor.getCount();
 		List<RecordResource> reds=null;
		if (number > 0) {
			reds=new ArrayList<RecordResource>();
			RecordResource red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new RecordResource();
 				red.setId(cursor.getLong(0));
 				red.setNeturl(cursor.getString(1));
 				red.setLocalurl(cursor.getString(2));
 				red.setType(cursor.getInt(3));
 				red.setRecordid(cursor.getLong(4));
 				red.setInfo(cursor.getString(5));
 				red.setState(cursor.getInt(6));
 				red.setPublishstate(cursor.getInt(7));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public List<RecordResource> getAllResources(){
		SQLiteDatabase db = this.getReadableDatabase();
 		String sql = "select * ";
 		sql+= " from "+TABLE_NAME+" dat order by id desc ";
 		Cursor cursor = db.rawQuery(sql, null);
		int number = cursor.getCount();
 		List<RecordResource> reds=null;
		if (number > 0) {
			reds=new ArrayList<RecordResource>();
			RecordResource red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new RecordResource();
 				red.setId(cursor.getLong(0));
 				red.setNeturl(cursor.getString(1));
 				red.setLocalurl(cursor.getString(2));
 				red.setType(cursor.getInt(3));
 				red.setRecordid(cursor.getLong(4));
 				red.setInfo(cursor.getString(5));
 				red.setState(cursor.getInt(6));
 				red.setPublishstate(cursor.getInt(7));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
    public void updateResourceLocalUrl(long id,String localurl){
    	SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(id) };
		ContentValues cv = new ContentValues();
		cv.put(LOCALURL, localurl);
 		db.update(TABLE_NAME, cv, where, whereValue);
 		db.close();
	}
    
    
    public void updateVideoStatusWithTime(int id,int status,int time){
    	SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(id) };
		ContentValues cv = new ContentValues();
		cv.put(PUBLISHSTATE, status);
		cv.put(INFO, time);
 		db.update(TABLE_NAME, cv, where, whereValue);
 		db.close();
	}
     
    public void updateResource(RecordResource rec){
    	if(rec==null) return ;
    	SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(rec.getId()) };
		ContentValues cv = new ContentValues();
		cv.put(NETURL, rec.getNeturl());
		cv.put(LOCALURL, rec.getLocalurl());
		cv.put(TYPE, rec.getType());
		cv.put(RECORDID, rec.getRecordid());
		cv.put(STATE, rec.getState());
		cv.put(INFO, rec.getInfo());
  		db.update(TABLE_NAME, cv, where, whereValue);
  		db.close();
	}
	public void deleteByRecord(long recordid){
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = RECORDID + " = ?";
		String[] whereValue = { String.valueOf(recordid) };
  		db.delete(TABLE_NAME, where, whereValue);
 		db.close();
	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
