package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.CategoryProcedure;
import com.axinxuandroid.data.Intelligence;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.Variety;
import com.ncpzs.util.LogUtil;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NetUpdateRecordDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_NetUpdateRecord"; 
	private final static String ID = "id"; 
	private final static String TYPE = "type"; 
	private final static String OBJID = "objid"; 
 	private final static String STATUSJSON = "statusjson"; 
 	public NetUpdateRecordDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(NetUpdateRecord recd) {
		if (recd == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(TYPE, recd.getType());
 		cv.put(OBJID, recd.getObjid());
 		cv.put(STATUSJSON, recd.getStatusjson());
 
     	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		recd.setId((int)row);
		return row;
	}
	
	public NetUpdateRecord getByTypeWithObjId(int type,int objid){
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where  type = ? and objid=? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {type+"",objid+""});
		NetUpdateRecord recd=null;
    	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
   			recd=new NetUpdateRecord();
   			recd.setId(cursor.getInt(0));
   			recd.setType(cursor.getInt(1));
   			recd.setObjid(cursor.getInt(2));
   			recd.setStatusjson(cursor.getString(3));
 		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return recd;
	}
	
	 
	public  void update(NetUpdateRecord recd) {
		if (recd == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(recd.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(TYPE, recd.getType());
 		cv.put(OBJID, recd.getObjid());
 		cv.put(STATUSJSON, recd.getStatusjson());
   		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
