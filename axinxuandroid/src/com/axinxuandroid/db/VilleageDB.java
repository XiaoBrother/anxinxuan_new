package com.axinxuandroid.db;

 
import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;
 
 import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class VilleageDB extends SystemDB {
	
	public final static String TABLE_NAME = "T_android_Villeage"; 
	private final static String ID = "id"; 
 	private final static String VILLEAGEID = "villeage_id"; 
	private final static String VILLEAGE_NAME = "villeage_name"; 
	private final static String COLLECT_CODE = "collect_code"; 
	private final static String MANAGER_NAME = "manager_name"; 
	private final static String TELE = "tele"; 
	private final static String ADDRESS = "address"; 
	private final static String BULID_TIME = "bulid_time"; 
	private final static String SCALE = "scale"; 
	private final static String TYPE = "type"; 
	private final static String MANAGE_SCOPE = "manage_scope"; 
	private final static String VILLEAGE_DESC = "villeage_desc"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static String LAT = "lat"; 
	private final static String LNG = "lng"; 
	public VilleageDB(Context context) {
		super(context);
	}
 	
	public long insert(Villeage uservilleage) {
		if (uservilleage == null) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
 		cv.put(VILLEAGEID, uservilleage.getVilleage_id());
		cv.put(VILLEAGE_NAME, uservilleage.getVilleage_name());
		cv.put(COLLECT_CODE, uservilleage.getCollect_code());
		cv.put(MANAGER_NAME, uservilleage.getManager_name());
		cv.put(TELE, uservilleage.getTele());
		cv.put(ADDRESS, uservilleage.getAddress());
		cv.put(BULID_TIME, uservilleage.getBulid_time());
		cv.put(SCALE, uservilleage.getScale());
		cv.put(TYPE, uservilleage.getType());
		cv.put(MANAGE_SCOPE, uservilleage.getManage_scope());
		cv.put(VILLEAGE_DESC, uservilleage.getVilleage_desc());
		cv.put(ISDEL, uservilleage.getIsdel());
		cv.put(LASTOPTIME, uservilleage.getLastoptime());
		cv.put(LAT, uservilleage.getLat());
		cv.put(LNG, uservilleage.getLng());
    	long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}
	
	public void update(Villeage villeage) {
		if (villeage == null) {
			return ;
		}
		String where = VILLEAGEID + " = ?";
		String[] whereValue = { String.valueOf(villeage.getVilleage_id())};
		ContentValues cv = new ContentValues();
		cv.put(VILLEAGEID, villeage.getVilleage_id());
		cv.put(VILLEAGE_NAME, villeage.getVilleage_name());
		cv.put(COLLECT_CODE, villeage.getCollect_code());
		cv.put(MANAGER_NAME, villeage.getManager_name());
		cv.put(TELE, villeage.getTele());
		cv.put(ADDRESS, villeage.getAddress());
		cv.put(BULID_TIME, villeage.getBulid_time());
		cv.put(SCALE, villeage.getScale());
		cv.put(TYPE, villeage.getType());
		cv.put(MANAGE_SCOPE, villeage.getManage_scope());
		cv.put(ISDEL, villeage.getIsdel());
		cv.put(LASTOPTIME, villeage.getLastoptime());
		cv.put(VILLEAGE_DESC, villeage.getVilleage_desc());
		cv.put(LAT, villeage.getLat());
		cv.put(LNG, villeage.getLng());
  		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
	}
	
//	public void insertUserVilleages(List<Villeage> rr){
//		if(rr!=null&&rr.size()>0){
//			SQLiteDatabase db = this.getWritableDatabase();
//			for(Villeage uservilleage:rr){
//				ContentValues cv = new ContentValues();
// 				cv.put(VILLEAGEID, uservilleage.getVilleage_id());
//				cv.put(VILLEAGE_NAME, uservilleage.getVilleage_name());
//				cv.put(COLLECT_CODE, uservilleage.getCollect_code());
//				cv.put(MANAGER_NAME, uservilleage.getManager_name());
//				cv.put(TELE, uservilleage.getTele());
//				cv.put(ADDRESS, uservilleage.getAddress());
//				cv.put(BULID_TIME, uservilleage.getBulid_time());
//				cv.put(SCALE, uservilleage.getScale());
//				cv.put(TYPE, uservilleage.getType());
//				cv.put(MANAGE_SCOPE, uservilleage.getManage_scope());
//				cv.put(VILLEAGE_DESC, uservilleage.getVilleage_desc());
//				cv.put(ISDEL, uservilleage.getIsdel());
//				cv.put(LASTOPTIME, uservilleage.getLastoptime());
// 				db.insert(TABLE_NAME, null, cv);
//			}
//			db.close();
//		}
//  	}
 
	/**
	 *  
	 */
	public void deleteByVilleageid(int vid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = VILLEAGEID + " = ?";
		String[] whereValue = {vid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	
	 
  
	public  Villeage selectbyVilleageid(int villeageid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where    dat.villeage_id= ? and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] { villeageid+""});
		Villeage uvilleage=null;
     	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
  		    uvilleage=new Villeage();
			uvilleage.setId(cursor.getLong(0));
 			uvilleage.setVilleage_id(cursor.getInt(1));
			uvilleage.setVilleage_name(cursor.getString(2));
			uvilleage.setCollect_code(cursor.getString(3));
			uvilleage.setManager_name(cursor.getString(4));
			uvilleage.setTele(cursor.getString(5));
			uvilleage.setAddress(cursor.getString(6));
			uvilleage.setBulid_time(cursor.getString(7));
			uvilleage.setScale(cursor.getString(8));
			uvilleage.setType(cursor.getInt(9));
			uvilleage.setManage_scope(cursor.getString(10));
			uvilleage.setVilleage_desc(cursor.getString(11));
  			uvilleage.setLat(cursor.getDouble(12));
			uvilleage.setLng(cursor.getDouble(13));
    	} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return uvilleage;
	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
