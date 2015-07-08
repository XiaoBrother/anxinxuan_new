package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.VilleagePhoto;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VilleagePhotoDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_VilleagePhoto"; 
	private final static String ID = "id"; 
	private final static String PHOTOID = "photoid"; 
	private final static String LABELNAME = "label_name"; 
 	private final static String IMAGEURL = "image_url"; 
 	private final static String IMAGEDESC = "image_desc";
 	private final static String IMAGENAME = "image_name";
 	private final static String VILLEAGEID = "villeage_id";
 	private final static String INDEXORDER = "index_order";
 	private final static String THUMBURL = "thumbnail_url";
 	private final static String LOCALURL = "localurl";
 	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	public VilleagePhotoDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(VilleagePhoto photo) {
		if (photo == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(PHOTOID, photo.getPhotoid());
 		cv.put(LABELNAME, photo.getLabel_name());
 		cv.put(IMAGEURL, photo.getImage_url());
 		cv.put(IMAGEDESC, photo.getImage_desc());
 		cv.put(IMAGENAME, photo.getImage_name());
 		cv.put(VILLEAGEID, photo.getVilleage_id());
 		cv.put(INDEXORDER, photo.getIndex_order());
 		cv.put(THUMBURL, photo.getThumbnail_url());
 		cv.put(LOCALURL, photo.getLocalurl());
 		cv.put(ISDEL, photo.getIsdel());
 		cv.put(LASTOPTIME, photo.getLastoptime());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		photo.setId((int)row);
		return row;
	}

	public  List<VilleagePhoto>  selectbyVilleageId(int vid,int top) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   villeage_id = ?    and isdel="+DATA_NOT_DELETE+"  order by lastoptime desc limit "+top;
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {vid+""});
		int number = cursor.getCount();
		List<VilleagePhoto> reds=null;
		if (number > 0) {
			reds=new ArrayList<VilleagePhoto>();
			VilleagePhoto red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new VilleagePhoto();
 				red.setId(cursor.getInt(0));
 				red.setPhotoid(cursor.getInt(1));
 				red.setLabel_name(cursor.getString(2));
 				red.setImage_url(cursor.getString(3));
 				red.setImage_desc(cursor.getString(4));
 				red.setImage_name(cursor.getString(5));
 				red.setVilleage_id(cursor.getInt(6));
 				red.setIndex_order(cursor.getInt(7));
 				red.setThumbnail_url(cursor.getString(8));
 				red.setLocalurl(cursor.getString(9));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	
	public   VilleagePhoto  getByPhotoId(int photoid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   photoid = ?   and isdel="+DATA_NOT_DELETE+"  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {photoid+""});
		int number = cursor.getCount();
		VilleagePhoto red=null;
		if (number > 0) {
 			cursor.moveToFirst();
			red=new VilleagePhoto();
			red.setId(cursor.getInt(0));
			red.setPhotoid(cursor.getInt(1));
			red.setLabel_name(cursor.getString(2));
			red.setImage_url(cursor.getString(3));
			red.setImage_desc(cursor.getString(4));
			red.setImage_name(cursor.getString(5));
			red.setVilleage_id(cursor.getInt(6));
			red.setIndex_order(cursor.getInt(7));
			red.setThumbnail_url(cursor.getString(8));
			red.setLocalurl(cursor.getString(9));
  		}
		cursor.close();
		db.close();
		return red;
	}
	
	public  String  getLastoptime(int villeageid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select max(lastoptime)";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   villeage_id = ?    ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {villeageid+""});
		int number = cursor.getCount();
		String lastoptime=null;
		if (number > 0) {
 			cursor.moveToFirst();
 			lastoptime=cursor.getString(0);
    	}
		cursor.close();
		db.close();
		return lastoptime;
	}
	 
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public  void updateImageLocalUrl(int id, String url) {
		 
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(id) };
 		ContentValues cv = new ContentValues();
  		cv.put(LOCALURL, url);
   		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	public  void update(VilleagePhoto photo) {
		if (photo == null) {
			return ; 
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(photo.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(PHOTOID, photo.getPhotoid());
 		cv.put(LABELNAME, photo.getLabel_name());
 		cv.put(IMAGEURL, photo.getImage_url());
 		cv.put(IMAGEDESC, photo.getImage_desc());
 		cv.put(IMAGENAME, photo.getImage_name());
 		cv.put(VILLEAGEID, photo.getVilleage_id());
 		cv.put(INDEXORDER, photo.getIndex_order());
 		cv.put(THUMBURL, photo.getThumbnail_url());
 		cv.put(LOCALURL, photo.getLocalurl());
 		cv.put(ISDEL, photo.getIsdel());
 		cv.put(LASTOPTIME, photo.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
