package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.List;


import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.ncpzs.util.LogUtil;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecordDB extends SystemDB {
	
	private final static String TABLE_NAME = "T_android_Record"; 
	private final static String ID = "id"; 
	private final static String VILLEAGE_ID = "villeage_id"; 
	private final static String TRACECODE = "trace_code"; 
	private final static String PHONE = "phone"; 
	private final static String LABELNAME = "labelname"; 
	private final static String NICKNAME = "nick_name"; 
	private final static String USERID = "user_id"; 
	private final static String RECORDTEXT = "recordtext"; 
 	private final static String SAVE_DATE = "save_date";
	private final static String BATCH_ID = "batch_id";
	private final static String RECORD_ID = "record_id";
 	private final static String SAVE_TYPE = "save_type";
	private final static String TYPE = "type";//
	private final static String SEND_DATE = "send_date";
	private final static String STATE = "state";
	private final static String VARIETY = "variety"; 
	private final static String VARIETYID = "variety_id"; 
	private final static String USERTYPE = "user_type"; 
	private final static String USERIMG = "user_img"; 
 	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static String SAVESTATUS = "savestatus"; 
	private final static String LNG = "lng"; 
	private final static String LAT = "lat"; 
	public RecordDB(Context context) {
		super(context);
	}

 

  
	public long insert(Record record) {
		if (record == null) {
			return 0;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
		cv.put(PHONE, record.getPhone());
		cv.put(USERID, record.getUser_id());
		cv.put(TRACECODE, record.getTrace_code());
		cv.put(LABELNAME, record.getLabel_name());
		cv.put(NICKNAME, record.getNick_name());
		cv.put(RECORDTEXT, record.getContext());
 		cv.put(BATCH_ID, record.getBatch_id());
		cv.put(RECORD_ID, record.getRecord_id());
		cv.put(SAVE_TYPE, record.getSave_type());
		cv.put(TYPE, record.getType());
		cv.put(SAVE_DATE, record.getSave_date());
		cv.put(SEND_DATE, record.getSend_date());
		cv.put(STATE, record.getState());
		cv.put(VARIETY, record.getVariety_name());
		cv.put(VARIETYID, record.getVariety_id());
		cv.put(USERTYPE, record.getUser_type());
		cv.put(VILLEAGE_ID, record.getVilleage_id());
		cv.put(USERIMG, record.getUser_img());
		cv.put(ISDEL, record.getIsdel());
		cv.put(LASTOPTIME, record.getLastoptime());
		cv.put(SAVESTATUS, record.getSavestatus());
		cv.put(LNG, record.getLng());
		cv.put(LAT, record.getLat());
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		record.setId(row);
 		return row;
	}

	 
 
 
	
	/**
	 * 删除表内记录
	 */
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(TABLE_NAME, null, null);
 		db.close();
	}
	
	public void deleteByRecordId(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = RECORD_ID + " = ?";
		String[] whereValue = {id};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	public void deleteById(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
	}
	
	public  List<Record>  selectbytracecode(String id,int user_type,String endtime) {
 		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select dat.id,dat.batch_id,dat.labelname,dat.save_date, dat.recordtext,dat.nick_name,dat.type,dat.record_id,dat.variety,dat.save_type,dat.send_date,dat.variety_id,dat.state,dat.trace_code,dat.villeage_id,dat.user_img,dat.user_type,dat.user_id,dat.savestatus,dat.lng,dat.lat";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.trace_code = ? and dat.user_type=? and ( (dat.state=dat.save_type " ;
		if(endtime!=null)
			where+=" and lastoptime>=?";
		where+=	" ) or (dat.savestatus ="+Record.BATCHRECORD_SAVESTATE_PREPARESAVE+" )) and isdel="+DATA_NOT_DELETE;
		
		where+=" order by dat.send_date desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
      	Cursor cursor =null;
    	if(endtime==null)
    		cursor=db.rawQuery(sb.toString(), new String[] {id,user_type+""});
    	else cursor=db.rawQuery(sb.toString(), new String[] {id,user_type+"",endtime});
		int number = cursor.getCount();
 		List<Record> reds=null;
		if (number > 0) {
			reds=new ArrayList<Record>();
			Record red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Record();
 				red.setId(cursor.getLong(0));
  				red.setBatch_id(cursor.getInt(1));
				red.setLabel_name(cursor.getString(2));
  				red.setSave_date(cursor.getString(3));
 				red.setContext(cursor.getString(4));
  				red.setNick_name(cursor.getString(5));
 				red.setType(cursor.getInt(6));
 				red.setRecord_id(cursor.getInt(7));
 				red.setVariety_name(cursor.getString(8));
 				red.setSave_type(cursor.getInt(9));
 				red.setSend_date(cursor.getString(10));
 				red.setVariety_id(cursor.getInt(11));
 				red.setState(cursor.getInt(12));
 				red.setTrace_code(cursor.getString(13));
 				red.setVilleage_id(cursor.getInt(14));
 				red.setUser_img(cursor.getString(15));
 				red.setUser_type(cursor.getInt(16));
 				red.setUser_id(cursor.getString(17));
 				red.setSavestatus(cursor.getInt(18));
 				red.setLng(cursor.getDouble(19));
 				red.setLat(cursor.getDouble(20));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	
	public  List<Record>  getUserRecords(int userid,String endtime) {
 		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select dat.id,dat.batch_id,dat.labelname,dat.save_date, dat.recordtext,dat.nick_name,dat.type,dat.record_id,dat.variety,dat.save_type,dat.send_date,dat.variety_id,dat.state,dat.trace_code,dat.villeage_id,dat.user_img,dat.user_type,dat.user_id,dat.savestatus,dat.lng,dat.lat";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.user_id = ?   and ( (dat.state=dat.save_type " ;
		if(endtime!=null)
			where+=" and lastoptime>=?";
		where+=	" ) or dat.savestatus ="+Record.BATCHRECORD_SAVESTATE_NET+") and isdel="+DATA_NOT_DELETE;
		
		where+=" order by dat.send_date desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
    	Cursor cursor =null;
    	if(endtime==null)
    		cursor=db.rawQuery(sb.toString(), new String[] {userid+""});
    	else cursor=db.rawQuery(sb.toString(), new String[] {userid+"",endtime});
		int number = cursor.getCount();
 		List<Record> reds=null;
		if (number > 0) {
			reds=new ArrayList<Record>();
			Record red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Record();
 				red.setId(cursor.getLong(0));
  				red.setBatch_id(cursor.getInt(1));
				red.setLabel_name(cursor.getString(2));
  				red.setSave_date(cursor.getString(3));
 				red.setContext(cursor.getString(4));
  				red.setNick_name(cursor.getString(5));
 				red.setType(cursor.getInt(6));
 				red.setRecord_id(cursor.getInt(7));
 				red.setVariety_name(cursor.getString(8));
 				red.setSave_type(cursor.getInt(9));
 				red.setSend_date(cursor.getString(10));
 				red.setVariety_id(cursor.getInt(11));
 				red.setState(cursor.getInt(12));
 				red.setTrace_code(cursor.getString(13));
 				red.setVilleage_id(cursor.getInt(14));
 				red.setUser_img(cursor.getString(15));
 				red.setUser_type(cursor.getInt(16));
 				red.setUser_id(cursor.getString(17));
 				red.setSavestatus(cursor.getInt(18));
 				red.setLng(cursor.getDouble(19));
 				red.setLat(cursor.getDouble(20));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	
	
	 
	
	
	/**
	 * 获取草稿箱数据
	 * @return
	 */
	public  List<Record>  getDraftRecords(int userid ) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select dat.id,dat.batch_id,dat.labelname,dat.save_date, dat.recordtext,dat.nick_name,dat.type,dat.record_id,dat.variety,dat.save_type,dat.send_date,dat.variety_id,dat.state,dat.trace_code,dat.villeage_id,dat.user_img,dat.phone,dat.user_type,dat.savestatus,dat.user_id,dat.lng,dat.lat";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where  dat.user_id = ? and  dat.savestatus  ="+Record.BATCHRECORD_SAVESTATE_DRAFT+"  order by dat.id desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(),  new String[] {userid+""});
		int number = cursor.getCount();
 		List<Record> reds=null;
		if (number > 0) {
			reds=new ArrayList<Record>();
			Record red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Record();
 				red.setId(cursor.getLong(0));
  				red.setBatch_id(cursor.getInt(1));
				red.setLabel_name(cursor.getString(2));
  				red.setSave_date(cursor.getString(3));
 				red.setContext(cursor.getString(4));
  				red.setNick_name(cursor.getString(5));
 				red.setType(cursor.getInt(6));
 				red.setRecord_id(cursor.getInt(7));
 				red.setVariety_name(cursor.getString(8));
 				red.setSave_type(cursor.getInt(9));
 				red.setSend_date(cursor.getString(10));
 				red.setVariety_id(cursor.getInt(11));
 				red.setState(cursor.getInt(12));
 				red.setTrace_code(cursor.getString(13));
 				red.setVilleage_id(cursor.getInt(14));
 				red.setUser_img(cursor.getString(15));
 				red.setPhone(cursor.getString(16));
 				red.setUser_type(cursor.getInt(17));
 				red.setSavestatus(cursor.getInt(18));
 				red.setUser_id(cursor.getString(19));
 				red.setLng(cursor.getDouble(20));
 				red.setLat(cursor.getDouble(21));
  				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	/**
	 * 获取批次记录的最后操作时间
	 * @return record_id
	 */
	public String getLastoptime(int batchid,int user_type) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime = null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.batch_id = ? and dat.user_type=?  ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{batchid+"",user_type+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	 
	 
	/**
	 * 获取报表记录
	 * @return record_id
	 */
	public Record getByBatchidWidthRecordid(int batchid,int recordid){
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select="select dat.id,dat.batch_id,dat.labelname,dat.save_date, dat.recordtext,dat.nick_name,dat.type,dat.record_id,dat.variety,dat.save_type ,dat.send_date,dat.variety_id,dat.state,dat.trace_code,dat.villeage_id,dat.user_img,dat.user_type,dat.user_id,dat.savestatus,dat.lng,dat.lat";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.batch_id=? and dat.record_id = ? and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{batchid+"",recordid+""});
 		Record red=null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
		    red=new Record();
		    red.setId(cursor.getLong(0));
 			red.setBatch_id(cursor.getInt(1));
		    red.setLabel_name(cursor.getString(2));
			red.setSave_date(cursor.getString(3));
			red.setContext(cursor.getString(4));
 			red.setNick_name(cursor.getString(5));
			red.setType(cursor.getInt(6));
			red.setRecord_id(cursor.getInt(7));
			red.setVariety_name(cursor.getString(8));
			red.setSave_type(cursor.getInt(9));
			red.setSend_date(cursor.getString(10));
			red.setVariety_id(cursor.getInt(11));
			red.setState(cursor.getInt(12));
			red.setTrace_code(cursor.getString(13));
			red.setVilleage_id(cursor.getInt(14));
			red.setUser_img(cursor.getString(15));
			red.setUser_type(cursor.getInt(16));
			red.setUser_id(cursor.getString(17));
			red.setSavestatus(cursor.getInt(18));
			red.setLng(cursor.getDouble(19));
			red.setLat(cursor.getDouble(20));
  		}
		cursor.close();
		db.close();
		return red;
	}
	/**
	 * 获取报表记录
	 * @return record_id
	 */
	public Record getById(long id){
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select="select dat.id,dat.batch_id,dat.labelname,dat.save_date, dat.recordtext,dat.nick_name,dat.type,dat.record_id,dat.variety,dat.save_type ,dat.send_date,dat.variety_id,dat.state,dat.trace_code,dat.villeage_id,dat.user_img,dat.user_type,dat.user_id,dat.savestatus,dat.lng,dat.lat";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where  dat.id = ? and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{id+""});
 		Record red=null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
		    red=new Record();
		    red.setId(cursor.getLong(0));
 			red.setBatch_id(cursor.getInt(1));
		    red.setLabel_name(cursor.getString(2));
			red.setSave_date(cursor.getString(3));
			red.setContext(cursor.getString(4));
 			red.setNick_name(cursor.getString(5));
			red.setType(cursor.getInt(6));
			red.setRecord_id(cursor.getInt(7));
			red.setVariety_name(cursor.getString(8));
			red.setSave_type(cursor.getInt(9));
			red.setSend_date(cursor.getString(10));
			red.setVariety_id(cursor.getInt(11));
			red.setState(cursor.getInt(12));
			red.setTrace_code(cursor.getString(13));
			red.setVilleage_id(cursor.getInt(14));
			red.setUser_img(cursor.getString(15));
			red.setUser_type(cursor.getInt(16));
			red.setUser_id(cursor.getString(17));
			red.setSavestatus(cursor.getInt(18));
			red.setLng(cursor.getDouble(19));
			red.setLat(cursor.getDouble(20));
  		}
		cursor.close();
		db.close();
		return red;
	}
	
	/**
	 * 获取报表记录
	 * @return record_id
	 */
	public Record getByRecordId(int record_id){
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select="select dat.id,dat.batch_id,dat.labelname,dat.save_date, dat.recordtext,dat.nick_name,dat.type,dat.record_id,dat.variety,dat.save_type ,dat.send_date,dat.variety_id,dat.state,dat.trace_code,dat.villeage_id,dat.user_img,dat.user_type,dat.user_id,dat.savestatus,dat.lng,dat.lat";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where  dat.record_id = ? and isdel="+DATA_NOT_DELETE+"";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{record_id+""});
 		Record red=null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
		    red=new Record();
		    red.setId(cursor.getLong(0));
 			red.setBatch_id(cursor.getInt(1));
		    red.setLabel_name(cursor.getString(2));
			red.setSave_date(cursor.getString(3));
			red.setContext(cursor.getString(4));
 			red.setNick_name(cursor.getString(5));
			red.setType(cursor.getInt(6));
			red.setRecord_id(cursor.getInt(7));
			red.setVariety_name(cursor.getString(8));
			red.setSave_type(cursor.getInt(9));
			red.setSend_date(cursor.getString(10));
			red.setVariety_id(cursor.getInt(11));
			red.setState(cursor.getInt(12));
			red.setTrace_code(cursor.getString(13));
			red.setVilleage_id(cursor.getInt(14));
			red.setUser_img(cursor.getString(15));
			red.setUser_type(cursor.getInt(16));
			red.setUser_id(cursor.getString(17));
			red.setSavestatus(cursor.getInt(18));
			red.setLng(cursor.getDouble(19));
			red.setLat(cursor.getDouble(20));
  		}
		cursor.close();
		db.close();
		return red;
	}
	
	public void updateSaveState(Record record){
		if (record == null) {
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(record.getId()) };
		ContentValues cv = new ContentValues();
 		cv.put(SAVESTATUS, record.getSavestatus());
		db.update(TABLE_NAME, cv, where, whereValue);
   		db.close();
	}
	
	/**
	 * 更新
	 * @param record
	 */
	public void update(Record record) {
		if (record == null) {
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(record.getId()) };
		ContentValues cv = new ContentValues();
		cv.put(USERID, record.getUser_id());
		cv.put(PHONE, record.getPhone());
		cv.put(TRACECODE, record.getTrace_code());
		cv.put(LABELNAME, record.getLabel_name());
		cv.put(NICKNAME, record.getNick_name());
		cv.put(RECORDTEXT, record.getContext());
 		cv.put(BATCH_ID, record.getBatch_id());
		cv.put(RECORD_ID, record.getRecord_id());
		cv.put(SAVE_TYPE, record.getSave_type());
		cv.put(TYPE, record.getType());
		cv.put(SAVE_DATE, record.getSave_date());
		cv.put(SEND_DATE, record.getSend_date());
		cv.put(STATE, record.getState());
		cv.put(VARIETY, record.getVariety_name());
		cv.put(VARIETYID, record.getVariety_id());
		cv.put(USERTYPE, record.getUser_type());
		cv.put(VILLEAGE_ID, record.getVilleage_id());
		cv.put(USERIMG, record.getUser_img());
		cv.put(ISDEL, record.getIsdel());
		cv.put(LASTOPTIME, record.getLastoptime());
		cv.put(SAVESTATUS, record.getSavestatus());
		cv.put(LNG, record.getLng());
		cv.put(LAT, record.getLat());
		db.update(TABLE_NAME, cv, where, whereValue);
		
 		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
