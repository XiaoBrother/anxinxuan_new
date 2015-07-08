package com.axinxuandroid.db;

 
import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;
 
 import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class UserVilleageDB extends SystemDB {
	
	private final static String TABLE_NAME = "T_android_User_Villeage"; 
	private final static String ID = "id"; 
 	private final static String VILLEAGEID = "villeage_id"; 
	private final static String USERID = "user_id"; 
	private final static String ROLE = "role"; 
	public UserVilleageDB(Context context) {
		super(context);
	}
 	
	public long insert(UserVilleage uservilleage) {
		if (uservilleage == null) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
 		cv.put(VILLEAGEID, uservilleage.getVilleage_id());
		cv.put(USERID, uservilleage.getUser_id());
		cv.put(ROLE, uservilleage.getRole());
    	long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}
	public void update(UserVilleage uservilleage) {
		if (uservilleage == null) {
			return;
		}
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(uservilleage.getUser_id()) };
		ContentValues cv = new ContentValues();
		cv.put(VILLEAGEID, uservilleage.getVilleage_id());
		cv.put(USERID, uservilleage.getUser_id());
		cv.put(ROLE, uservilleage.getRole());
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
	}
	 
	 
 	/**
	 *  
	 */
	public void deleteByUserid(int uid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = USERID+" = ?";
		String[] whereValue = {uid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	
	 
	
	 
	
	
	public List<Villeage> selectVilleagebyUserId(int userid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select vil.* ";
		String from = " from "+TABLE_NAME+" dat,"+VilleageDB.TABLE_NAME+" vil ";
		String where = " where dat.user_id = ? and dat.villeage_id= vil.villeage_id";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {userid+""});
		int number = cursor.getCount();
 		List<Villeage> reds=null;
   		if (cursor.getCount() > 0) {
   			reds=new ArrayList<Villeage>();
  			cursor.moveToFirst();
  			Villeage uvilleage=null;
 			while (cursor.getPosition() != number) {
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
  				reds.add(uvilleage);
				cursor.moveToNext();
			}
			 
  		} 
   		//LogUtil.logInfo(getClass(), "villeage count:"+cursor.getCount()+"");
		cursor.close();
		db.close();
 		return reds;
	}
	
	public List<User> selectUserbyVilleage(int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select user.* ";
		String from = " from "+TABLE_NAME+" dat,"+UserDB.TABLE_NAME+" user ";
		String where = " where dat.villeage_id = ? and dat.user_id= user.user_id";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {vid+""});
		int number = cursor.getCount();
 		List<User> reds=null;
   		if (cursor.getCount() > 0) {
   			reds=new ArrayList<User>();
  			cursor.moveToFirst();
  			User user=null;
 			while (cursor.getPosition() != number) {
 				user=new User();
 				user.setId(cursor.getInt(0));
 				user.setUser_id(cursor.getInt(1));
 				user.setPhone(cursor.getString(2));
 				user.setPwd(cursor.getString(3));
 				user.setUser_name(cursor.getString(4));
 				user.setEmail(cursor.getString(5));
  				user.setCreate_time(cursor.getString(6));
 				user.setLogin_time(cursor.getString(7));
 				user.setPerson_desc(cursor.getString(8));
 				user.setPerson_imageurl(cursor.getString(9));
 				user.setLocal_imgurl(cursor.getString(10));
 				reds.add(user);
				cursor.moveToNext();
			}
			 
  		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return reds;
	}
	
	public  Villeage  selectByUserIdWithVilleageid(int userid,int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select vil.* ";
		String from = " from "+TABLE_NAME+" dat,"+VilleageDB.TABLE_NAME+" vil ";
		String where = " where dat.user_id = ? and dat.dat.villeage_id=? and dat.villeage_id= vil.villeage_id";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Villeage uvilleage=null;
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {userid+"",vid+""});
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
 		cursor.close();
		db.close();
 		return uvilleage;
	}
	
	public  UserVilleage  selectUserVilleage(int userid,int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select  * ";
		String from = " from "+TABLE_NAME ;
		String where = " where  user_id = ? and   villeage_id=? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		UserVilleage uvilleage=null;
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {userid+"",vid+""});
    	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
			uvilleage=new UserVilleage();
			uvilleage.setId(cursor.getLong(0));
			uvilleage.setUser_id(cursor.getInt(1));
			uvilleage.setVilleage_id(cursor.getInt(2));
			uvilleage.setRole(cursor.getInt(3));
			 
    	} 
 		cursor.close();
		db.close();
 		return uvilleage;
	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
