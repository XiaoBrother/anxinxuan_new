package com.axinxuandroid.db;

 
import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.data.Intelligence;
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
 
public class IntelligenceDB extends SystemDB {
	
	private final static String TABLE_NAME = "T_android_Intelligence"; 
	private final static String ID = "id"; 
	private final static String INTELLIGENCEID = "intelligenceid"; 
 	private final static String VILLEAGEID = "villeage_id"; 
	private final static String LABELNAME = "label_name"; 
	private final static String DESC = "intelligence_desc"; 
	private final static String THUMBURL = "thumbnail_url"; 
	private final static String CREATETIME = "create_time"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static String LOCALURL = "localurl"; 
	private final static String IMGURL = "intelligence_imgurl"; 
	public IntelligenceDB(Context context) {
		super(context);
	}
 	
	public long insert(Intelligence intell) {
		if (intell == null) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
 		cv.put(INTELLIGENCEID, intell.getIntelligenceid());
 		cv.put(VILLEAGEID, intell.getVilleage_id());
 		cv.put(LABELNAME, intell.getLabel_name());
		cv.put(DESC, intell.getIntelligence_desc());
		cv.put(THUMBURL, intell.getThumbnail_url());
		cv.put(CREATETIME, intell.getCreate_time());
		cv.put(ISDEL, intell.getIsdel());
		cv.put(LASTOPTIME, intell.getLastoptime());
		cv.put(IMGURL, intell.getIntelligence_imgurl());
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
	public void insertIntelligences(List<Intelligence> rr){
		if(rr!=null&&rr.size()>0){
			SQLiteDatabase db = this.getWritableDatabase();
			for(Intelligence intell:rr){
				ContentValues cv = new ContentValues();
				cv.put(INTELLIGENCEID, intell.getIntelligenceid());
		 		cv.put(VILLEAGEID, intell.getVilleage_id());
		 		cv.put(LABELNAME, intell.getLabel_name());
				cv.put(DESC, intell.getIntelligence_desc());
				cv.put(THUMBURL, intell.getThumbnail_url());
				cv.put(CREATETIME, intell.getCreate_time());
				cv.put(ISDEL, intell.getIsdel());
				cv.put(LASTOPTIME, intell.getLastoptime());
				cv.put(IMGURL, intell.getIntelligence_imgurl());
				db.insert(TABLE_NAME, null, cv);
			}
			db.close();
		}
  	}
 
	/**
	 *  
	 */
	public void deleteByIntelligenceid(int intellid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = intellid + " = ?";
		String[] whereValue = {intellid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	
	 
	
	 
	
	
	public List<Intelligence> selectbyVilleageid(int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.villeage_id = ? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {vid+""});
		int number = cursor.getCount();
 		List<Intelligence> reds=null;
   		if (cursor.getCount() > 0) {
   			reds=new ArrayList<Intelligence>();
  			cursor.moveToFirst();
  			Intelligence intell=null;
 			while (cursor.getPosition() != number) {
 				intell=new Intelligence();
 				intell.setId(cursor.getLong(0));
 				intell.setIntelligenceid(cursor.getInt(1));
 				intell.setVilleage_id(cursor.getInt(2));
 				intell.setLabel_name(cursor.getString(3));
 				intell.setIntelligence_desc(cursor.getString(4));
 				intell.setThumbnail_url(cursor.getString(5));
 				intell.setCreate_time(cursor.getString(6));
 				intell.setIsdel(cursor.getInt(7));
 				intell.setLastoptime(cursor.getString(8));
 				intell.setLocalurl(cursor.getString(9));
 				intell.setIntelligence_imgurl(cursor.getString(10));
 				reds.add(intell);
				cursor.moveToNext();
			}
   		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return reds;
	}
  
	public Intelligence selectbyIntelligenceid(int intellid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.intelligenceid = ? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {intellid+""});
		Intelligence intell=null;
    	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
   			intell=new Intelligence();
   			intell.setId(cursor.getLong(0));
			intell.setIntelligenceid(cursor.getInt(1));
			intell.setVilleage_id(cursor.getInt(2));
			intell.setLabel_name(cursor.getString(3));
			intell.setIntelligence_desc(cursor.getString(4));
			intell.setThumbnail_url(cursor.getString(5));
			intell.setCreate_time(cursor.getString(6));
			intell.setIsdel(cursor.getInt(7));
			intell.setLastoptime(cursor.getString(8));
			intell.setLocalurl(cursor.getString(9));
			intell.setIntelligence_imgurl(cursor.getString(10));
 		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return intell;
	}
	
	
	 
	
	public List<Intelligence> selectbyNameWithVilleageId(String name,int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.villeage_id = ? and dat.label_name = ? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {vid+"",name});
		int number = cursor.getCount();
 		List<Intelligence> reds=null;
   		if (cursor.getCount() > 0) {
   			reds=new ArrayList<Intelligence>();
  			cursor.moveToFirst();
  			Intelligence intell=null;
 			while (cursor.getPosition() != number) {
 				intell=new Intelligence();
 				intell.setId(cursor.getLong(0));
 				intell.setIntelligenceid(cursor.getInt(1));
 				intell.setVilleage_id(cursor.getInt(2));
 				intell.setLabel_name(cursor.getString(3));
 				intell.setIntelligence_desc(cursor.getString(4));
 				intell.setThumbnail_url(cursor.getString(5));
 				intell.setCreate_time(cursor.getString(6));
 				intell.setIsdel(cursor.getInt(7));
 				intell.setLastoptime(cursor.getString(8));
 				intell.setLocalurl(cursor.getString(9));
 				intell.setIntelligence_imgurl(cursor.getString(10));
 				reds.add(intell);
				cursor.moveToNext();
			}
   		} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return reds;
	}
	public  void update(Intelligence intell) {
		if (intell == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(intell.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(INTELLIGENCEID, intell.getIntelligenceid());
 		cv.put(VILLEAGEID, intell.getVilleage_id());
 		cv.put(LABELNAME, intell.getLabel_name());
		cv.put(DESC, intell.getIntelligence_desc());
		cv.put(THUMBURL, intell.getThumbnail_url());
		cv.put(CREATETIME, intell.getCreate_time());
		cv.put(ISDEL, intell.getIsdel());
		cv.put(LASTOPTIME, intell.getLastoptime());
		cv.put(IMGURL, intell.getIntelligence_imgurl());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	
	public  void  updateImgLocalUrl(int id,String url) {
		 
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(id) };
 		ContentValues cv = new ContentValues();
 		cv.put(LOCALURL, url);
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
