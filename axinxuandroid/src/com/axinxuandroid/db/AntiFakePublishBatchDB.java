package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.data.Advocate;
import com.axinxuandroid.data.AntiFakePublishBatch;
import com.yixia.weibo.sdk.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class AntiFakePublishBatchDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_SecurityCodeBatch"; 
	private final static String ID = "id"; 
	private final static String USER_ID = "user_id"; 
	private final static String BATCHID = "batchid"; 
	private final static String BATCHTIME = "Batchtime"; 
	private final static String VARIETY_ID = "variety_id"; 
	private final static String TIMEBATCH = "timebatch"; 
	private final static String SNUM = "snum"; 
	private final static String ENDNUM = "endnum"; 
	private final static String STATUS = "status"; 
	private final static String TCOUNT = "tcount"; 
	private final static String BDESC = "bdesc"; 
	private final static String CREATETIME = "createtime"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	
	
	public AntiFakePublishBatchDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(AntiFakePublishBatch pbath) {
		if (pbath == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(ID, pbath.getId());
 		cv.put(USER_ID, pbath.getUser_id());
 		cv.put(BATCHID, pbath.getBatchid());
 		cv.put(VARIETY_ID, pbath.getVariety_id());
 		cv.put(TIMEBATCH, pbath.getTimebatch());
 		cv.put(SNUM, pbath.getSnum());
 		cv.put(ENDNUM, pbath.getEndnum());
 		cv.put(STATUS, pbath.getStatus());
 		cv.put(TCOUNT, pbath.getTcount());
 		cv.put(BATCHTIME, pbath.getBatchtime());
 		cv.put(BDESC, pbath.getBdesc());
 		cv.put(CREATETIME, pbath.getCreatetime());
 		cv.put(ISDEL, pbath.getIsdel());
 		cv.put(LASTOPTIME, pbath.getLastoptime());
    	SQLiteDatabase db = this.getWritableDatabase();
    		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
	//	pbath.setId((int)row);
		return row;
	}
	 
	public  List<AntiFakePublishBatch>  selectbyUserId(int user_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.user_id = ?  and dat.isdel="+DATA_NOT_DELETE+" order by dat.id desc";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {user_id+""});
		int number = cursor.getCount();
 		List<AntiFakePublishBatch> reds=null;
		if (number > 0) {
			reds=new ArrayList<AntiFakePublishBatch>();
			AntiFakePublishBatch red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new AntiFakePublishBatch();
 				red.setId(cursor.getInt(0));
  				red.setUser_id(cursor.getInt(1));
   				red.setBatchid(cursor.getInt(2));
   				red.setVariety_id(cursor.getInt(3));
   				red.setStatus(cursor.getInt(4));
   				red.setBatchtime(cursor.getString(5));
   				red.setTimebatch(cursor.getString(6));
   				red.setSnum(cursor.getString(7));
   				red.setEndnum(cursor.getString(8));
   				red.setTcount(cursor.getInt(9));
   				red.setBdesc(cursor.getString(10));
   				red.setCreatetime(cursor.getString(11));
   				red.setIsdel(cursor.getInt(12));
   				red.setLastoptime(cursor.getString(13));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	/**
	 * 获取批次的最后操作时间
	 * @return record_id
	 */
	public String getLatoptime( int user_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.user_id = ? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{user_id+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	
	public  AntiFakePublishBatch  selectbyId(int id) {
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
		AntiFakePublishBatch red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
 			red=new AntiFakePublishBatch();
				red.setId(cursor.getInt(0));
				red.setUser_id(cursor.getInt(1));
				red.setBatchid(cursor.getInt(2));
				red.setVariety_id(cursor.getInt(3));
				red.setStatus(cursor.getInt(4));
				red.setBatchtime(cursor.getString(5));
				red.setTimebatch(cursor.getString(6));
				red.setSnum(cursor.getString(7));
				red.setEndnum(cursor.getString(8));
				red.setTcount(cursor.getInt(9));
				red.setBdesc(cursor.getString(10));
				red.setCreatetime(cursor.getString(11));
				red.setIsdel(cursor.getInt(12));
				red.setLastoptime(cursor.getString(13));
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
 
 
	public  void update(AntiFakePublishBatch pbath) {
		if (pbath == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(pbath.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(USER_ID, pbath.getUser_id());
 		cv.put(BATCHTIME, pbath.getBatchtime());
 		cv.put(BATCHID, pbath.getBatchid());
 		cv.put(VARIETY_ID, pbath.getVariety_id());
 		cv.put(TIMEBATCH, pbath.getTimebatch());
 		cv.put(SNUM, pbath.getSnum());
 		cv.put(ENDNUM, pbath.getEndnum());
 		cv.put(STATUS, pbath.getStatus());
 		cv.put(TCOUNT, pbath.getTcount());
 		cv.put(BDESC, pbath.getBdesc());
 		cv.put(CREATETIME, pbath.getCreatetime());
 		cv.put(ISDEL, pbath.getIsdel());
 		cv.put(LASTOPTIME, pbath.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	} 
}
