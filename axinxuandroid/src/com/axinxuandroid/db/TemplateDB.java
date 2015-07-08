package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TemplateDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Template"; 
	private final static String ID = "id"; 
	private final static String TEMPLATEID = "template_id"; 
 	private final static String CATEGORYID = "category_id"; 
	private final static String LABELNAME = "label_name"; 
	private final static String HEAD = "head"; 
	private final static String FOOT = "foot"; 
	private final static String SIGN = "sign"; 
	private final static String CONTEXT = "context"; 
	private final static String VERSION = "version"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	public TemplateDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(Template template) {
		if (template == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(TEMPLATEID, template.getTemplate_id());
 		cv.put(CATEGORYID, template.getCategory_id());
 		cv.put(LABELNAME, template.getLabel_name());
 		cv.put(HEAD, template.getHead());
 		cv.put(FOOT, template.getFoot());
 		cv.put(SIGN,template.getSign());
		cv.put(CONTEXT, template.getContext());
		cv.put(VERSION, template.getVersion());
		cv.put(ISDEL, template.getIsdel());
		cv.put(LASTOPTIME, template.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		template.setId((int)row);
		return row;
	}
	/**
	 * 获取批次的最后更新时间
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
 		Cursor cursor = db.rawQuery(sb.toString(),null);
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	public  Template  selectByCategoryWithLabelName(int cid,String labelname) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,template_id,category_id,label_name,head,foot,sign,context,version";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.label_name = ? and isdel="+DATA_NOT_DELETE+"  order by dat.template_id desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {labelname});
		int number = cursor.getCount();
 		List<Template> reds=null;
		if (number > 0) {
			reds=new ArrayList<Template>();
			Template red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Template();
 				red.setId(cursor.getInt(0));
  				red.setTemplate_id(cursor.getInt(1));
  				red.setCategory_id(cursor.getInt(2));
  				red.setLabel_name(cursor.getString(3));
  				red.setHead(cursor.getString(4));
  				red.setFoot(cursor.getString(5));
  				red.setSign(cursor.getString(6));
  				red.setContext(cursor.getString(7));
  				red.setVersion(cursor.getInt(8));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		if(reds!=null&&reds.size()>0){
			for(Template tmp:reds){
				if(tmp.getCategory_id()==cid)
					return tmp;
			}
			return reds.get(0);
		}
 		return null;
	}
	public  List<Template>  getAllTemplate() {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,template_id,category_id,label_name,head,foot,sign,context,version";
		String from = " from "+TABLE_NAME+" dat where   isdel="+DATA_NOT_DELETE+"";
 		sb.append(select);
		sb.append(from);
    	Cursor cursor = db.rawQuery(sb.toString(), null);
		int number = cursor.getCount();
 		List<Template> reds=null;
		if (number > 0) {
			reds=new ArrayList<Template>();
			Template red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Template();
 				red.setId(cursor.getInt(0));
  				red.setTemplate_id(cursor.getInt(1));
  				red.setCategory_id(cursor.getInt(2));
  				red.setLabel_name(cursor.getString(3));
  				red.setHead(cursor.getString(4));
  				red.setFoot(cursor.getString(5));
  				red.setSign(cursor.getString(6));
  				red.setContext(cursor.getString(7));
  				red.setVersion(cursor.getInt(8));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
  		return reds;
	}
	public  Template  selectByTemplateId(int templateId) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,template_id,category_id,label_name,head,foot,sign,context,version ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.template_id = ?   and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {templateId+""});
		int number = cursor.getCount();
		Template red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Template();
			red.setId(cursor.getInt(0));
			red.setTemplate_id(cursor.getInt(1));
			red.setCategory_id(cursor.getInt(2));
			red.setLabel_name(cursor.getString(3));
			red.setHead(cursor.getString(4));
			red.setFoot(cursor.getString(5));
			red.setSign(cursor.getString(6));
			red.setContext(cursor.getString(7));
			red.setVersion(cursor.getInt(8));
  		}
		cursor.close();
		db.close();
  		return red;
	}
	 
	/**
	 *  
	 */
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(TABLE_NAME, null, null);
 		db.close();
	}
	
	
	
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
 
	public  void update(Template template) {
		if (template == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(template.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(TEMPLATEID, template.getTemplate_id());
 		cv.put(CATEGORYID, template.getCategory_id());
 		cv.put(LABELNAME, template.getLabel_name());
 		cv.put(HEAD, template.getHead());
 		cv.put(FOOT, template.getFoot());
 		cv.put(SIGN,template.getSign());
 		cv.put(VERSION, template.getVersion());
		cv.put(CONTEXT, template.getContext());
		cv.put(ISDEL, template.getIsdel());
		cv.put(LASTOPTIME, template.getLastoptime());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
