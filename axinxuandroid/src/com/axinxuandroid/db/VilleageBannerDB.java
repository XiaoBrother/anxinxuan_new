package com.axinxuandroid.db;

 
import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.VilleageBanner;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;
 
 import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class VilleageBannerDB extends SystemDB {
	
	private final static String TABLE_NAME = "T_android_VilleageBanner"; 
	private final static String ID = "id"; 
	private final static String BANNERID = "bannerid"; 
 	private final static String VILLEAGEID = "villeage_id"; 
	private final static String NETURL = "neturl"; 
	private final static String LOCALURL = "localurl"; 
	private final static String THUMBURL = "thumb_url"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	 

	public VilleageBannerDB(Context context) {
		super(context);
	}
 	
	public long insert(VilleageBanner banner) {
		if (banner == null) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
 		cv.put(BANNERID, banner.getBannerid());
 		cv.put(VILLEAGEID, banner.getVilleage_id());
 		cv.put(NETURL, banner.getNeturl());
		cv.put(LOCALURL, banner.getLocalurl());
		cv.put(THUMBURL, banner.getThumb_url());
		cv.put(ISDEL, banner.getIsdel());
		cv.put(LASTOPTIME, banner.getLastoptime());
   		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}
	
	/**
	 * 获取批次的最后更新时间
	 * @return record_id
	 */
	public String getLatoptime(int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat where villeage_id=?";
		sb.append(select);
		sb.append(from);
 		Cursor cursor = db.rawQuery(sb.toString(), new String[]{vid+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	public void insertVilleageBanners(List<VilleageBanner> rr){
		if(rr!=null&&rr.size()>0){
			SQLiteDatabase db = this.getWritableDatabase();
			for(VilleageBanner banner:rr){
				ContentValues cv = new ContentValues();
				cv.put(BANNERID, banner.getBannerid());
				cv.put(VILLEAGEID, banner.getVilleage_id());
		 		cv.put(NETURL, banner.getNeturl());
				cv.put(LOCALURL, banner.getLocalurl());
				cv.put(THUMBURL, banner.getThumb_url());
				cv.put(ISDEL, banner.getIsdel());
				cv.put(LASTOPTIME, banner.getLastoptime());
				db.insert(TABLE_NAME, null, cv);
			}
			db.close();
		}
  	}
 
	/**
	 *  
	 */
	public void deleteByBannerid(int bid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = BANNERID + " = ?";
		String[] whereValue = {bid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	
	 
	
	 
	
	
	public List<VilleageBanner> selectbyVilleageid(int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.villeage_id = ? and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {vid+""});
		int number = cursor.getCount();
 		List<VilleageBanner> reds=null;
   		if (cursor.getCount() > 0) {
   			reds=new ArrayList<VilleageBanner>();
  			cursor.moveToFirst();
  			VilleageBanner banner=null;
 			while (cursor.getPosition() != number) {
 				banner=new VilleageBanner();
 				banner.setId(cursor.getLong(0));
 				banner.setBannerid(cursor.getInt(1));
 				banner.setVilleage_id(cursor.getInt(2));
 				banner.setNeturl(cursor.getString(3));
 				banner.setLocalurl(cursor.getString(4));
 				banner.setThumb_url(cursor.getString(5));
 				reds.add(banner);
				cursor.moveToNext();
			}
   		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return reds;
	}
  
	public VilleageBanner selectbyBannerid(int bid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.bannerid = ? and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {bid+""});
 		VilleageBanner banner=null;
    	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
  		    banner=new VilleageBanner();
			banner.setId(cursor.getLong(0));
			banner.setBannerid(cursor.getInt(1));
			banner.setVilleage_id(cursor.getInt(2));
			banner.setNeturl(cursor.getString(3));
			banner.setLocalurl(cursor.getString(4));
			banner.setThumb_url(cursor.getString(5));
 		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return banner;
	}
	
	public  void update(VilleageBanner banner) {
		if (banner == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(banner.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(BANNERID, banner.getBannerid());
 		cv.put(VILLEAGEID, banner.getVilleage_id());
 		cv.put(NETURL, banner.getNeturl());
		cv.put(LOCALURL, banner.getLocalurl());
		cv.put(THUMBURL, banner.getThumb_url());
		cv.put(ISDEL, banner.getIsdel());
		cv.put(LASTOPTIME, banner.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
