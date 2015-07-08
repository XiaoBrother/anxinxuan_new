package com.axinxuandroid.db;

import com.axinxuandroid.data.SystemLabel;
import com.axinxuandroid.data.Villeage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SystemLabelDB extends SystemDB {
	private static final String TABLE_NAME = "T_android_System_Label";
	private static final String LABELID = "label_id";
	private static final String LABELNAME = "label_name";
	private static final String IMGURL = "label_imageurl";
	private static final String TYPE = "type";
	private static final String MODULEID = "module_id";
	private static final String INDEXORDER = "index_order";
 
	public SystemLabelDB(Context context) {
		super(context);
	}

	public  String getImageUrlByNameWithModule(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select label_imageurl ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where    dat.label_name= ? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] { name});
		String url=null;
     	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
   			url=cursor.getString(0);
      	} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return url;
	}
	
	public  SystemLabel getByName(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where    dat.label_name= ? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] { name});
		SystemLabel label=null;
     	if (cursor.getCount() > 0) {
   			cursor.moveToFirst();
   			label=new SystemLabel();
    		label.setId(cursor.getInt(0));
   			label.setLabel_id(cursor.getInt(1));
   			label.setLabel_name(cursor.getString(2));
   			label.setLabel_imageurl(cursor.getString(3));
   			label.setType(cursor.getInt(4));
   			label.setModule_id(cursor.getInt(5));
   			label.setIndex_order(cursor.getInt(6));;
      	} 
   		//LogUtil.logInfo(getClass(), cursor.getCount()+"");
		cursor.close();
		db.close();
 		return label;
	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
