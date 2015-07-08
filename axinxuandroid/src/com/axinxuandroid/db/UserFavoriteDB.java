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
import com.ncpzs.util.LogUtil;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserFavoriteDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_UserFavorite"; 
	private final static String ID = "id"; 
	private final static String FAV_ID = "favid"; 
	private final static String LABELNAME = "label_name"; 
 	private final static String FAVORITETYPE = "favorite_type"; 
	private final static String FAVORITEID = "favorite_id"; 
	private final static String USER_ID = "user_id"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static int PER_QUERY_COUNT=10;//每次查询10条
	public UserFavoriteDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(UserFavorite uf) {
		if (uf == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(FAV_ID, uf.getFavid());
 		cv.put(LABELNAME, uf.getLabel_name());
 		cv.put(FAVORITETYPE, uf.getFavorite_type());
  		cv.put(FAVORITEID, uf.getFavorite_id());
 		cv.put(USER_ID, uf.getUser_id());
 		cv.put(ISDEL, uf.getIsdel());
 		cv.put(LASTOPTIME, uf.getLastoptime());
   		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		uf.setId((int)row);
		return row;
	}
	/**
	 * 获取批次的最后操作时间
	 * @return record_id
	 */
	public String getLatoptime() {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat";
		sb.append(select);
		sb.append(from);
 		Cursor cursor = db.rawQuery(sb.toString(), null);
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	public  UserFavorite  selectbyUserFavoriteTypeWithId(int favoriteid,int favoritetype) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.favorite_id = ?  and dat.favorite_type = ? and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {favoriteid+"",favoritetype+""});
		int number = cursor.getCount();
		UserFavorite uf=null;
		if (number > 0) {
 			cursor.moveToFirst();
			uf=new UserFavorite();
			uf.setId(cursor.getInt(0));
			uf.setFavid(cursor.getInt(1));
			uf.setLabel_name(cursor.getString(2));
			uf.setFavorite_type(cursor.getInt(3));
			uf.setFavorite_id(cursor.getInt(4));
			uf.setUser_id(cursor.getInt(5));
			uf.setLastoptime(cursor.getString(7));
   		}
		cursor.close();
		db.close();
		return uf;
	}
	public  List<UserFavorite>  selectbyUserFavoriteType(int favoritetype,int uid,String lastoptime,int favid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * from "+TABLE_NAME+" dat";
 		String where = " where dat.favorite_type = ? and dat.user_id=? and isdel="+DATA_NOT_DELETE+" and dat.lastoptime <= ? and dat.favid<? order by lastoptime desc,favid desc limit "+PER_QUERY_COUNT;
 		sb.append(select);
 		sb.append(where);
		LogUtil.logInfo(getClass(), select+where+","+favoritetype+","+uid+","+lastoptime+","+favid);
    	Cursor cursor = db.rawQuery(sb.toString(), new String[] {favoritetype+"",uid+"",lastoptime,favid+""});
		int number = cursor.getCount();
		List<UserFavorite> favs=null;
		if (number > 0) { 
			favs=new ArrayList<UserFavorite>();
 			while(cursor.moveToNext()){
				UserFavorite uf=null;
				uf=new UserFavorite();
				uf.setId(cursor.getInt(0));
				uf.setFavid(cursor.getInt(1));
				uf.setLabel_name(cursor.getString(2));
				uf.setFavorite_type(cursor.getInt(3));
				uf.setFavorite_id(cursor.getInt(4));
				uf.setUser_id(cursor.getInt(5));
				uf.setLastoptime(cursor.getString(7));
				favs.add(uf);
			}
  			
   		}
		cursor.close();
		db.close();
		return favs;
	}
	 
	
	public  List<UserFavorite>  serchByBatchCode(int favoritetype,String batchcode) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select dat.*";
		String from = " from "+TABLE_NAME+" dat,T_android_Batch batch";
		String where = " where     dat.favorite_type = ? and dat.isdel="+DATA_NOT_DELETE+" and batch.batch_id= dat.favorite_id and batch.code like ? order by lastoptime desc";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {favoritetype+"","%"+batchcode+"%"});
		int number = cursor.getCount();
		List<UserFavorite> favs=null;
		if (number > 0) { 
			favs=new ArrayList<UserFavorite>();
 			while(cursor.moveToNext()){
				UserFavorite uf=null;
				uf=new UserFavorite();
				uf.setId(cursor.getInt(0));
				uf.setFavid(cursor.getInt(1));
				uf.setLabel_name(cursor.getString(2));
				uf.setFavorite_type(cursor.getInt(3));
				uf.setFavorite_id(cursor.getInt(4));
				uf.setUser_id(cursor.getInt(5));
				uf.setLastoptime(cursor.getString(7));
				favs.add(uf);
			}
  			
   		}
		cursor.close();
		db.close();
		return favs;
	}
	
	public  List<UserFavorite>  serchByName(int favoritetype,String name) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
 		String select = "select dat.*";
		String from = " from "+TABLE_NAME+" dat,T_android_Batch batch";
		String where = " where     dat.favorite_type = ? and dat.isdel="+DATA_NOT_DELETE+" and batch.batch_id= dat.favorite_id and ( batch.variety like ? or batch.villeage_name like ? ) order by lastoptime desc";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {favoritetype+"","%"+name+"%","%"+name+"%"});
		int number = cursor.getCount();
  		List<UserFavorite> favs=null;
		if (number > 0) { 
			favs=new ArrayList<UserFavorite>();
 			while(cursor.moveToNext()){
				UserFavorite uf=null;
				uf=new UserFavorite();
				uf.setId(cursor.getInt(0));
				uf.setFavid(cursor.getInt(1));
				uf.setLabel_name(cursor.getString(2));
				uf.setFavorite_type(cursor.getInt(3));
				uf.setFavorite_id(cursor.getInt(4));
				uf.setUser_id(cursor.getInt(5));
				uf.setLastoptime(cursor.getString(7));
				favs.add(uf);
			}
  			
   		}
		cursor.close();
		db.close();
		return favs;
	}
	public  int  selectMaxFavid(int user_id) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select max(favid)";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.user_id = ?   and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {user_id+""});
		int number = cursor.getCount();
		int maxid=0;
  		if (number > 0) {
 			cursor.moveToFirst();
 			maxid=cursor.getInt(0);
     	}
		cursor.close();
		db.close();
		return maxid;
	}
	 
	public void deleteByFavId(long favid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = FAV_ID + " = ?";
		String[] whereValue = {favid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	public  void update(UserFavorite uf) {
		if (uf == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(uf.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(FAV_ID, uf.getFavid());
 		cv.put(LABELNAME, uf.getLabel_name());
 		cv.put(FAVORITETYPE, uf.getFavorite_type());
  		cv.put(FAVORITEID, uf.getFavorite_id());
 		cv.put(USER_ID, uf.getUser_id());
 		cv.put(ISDEL, uf.getIsdel());
 		cv.put(LASTOPTIME, uf.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
