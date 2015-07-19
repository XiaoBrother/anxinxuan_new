package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Record;
import com.ncpzs.util.LogUtil;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BatchDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Batch"; 
	private final static String ID = "id"; 
	private final static String BATCHID = "batch_id"; 
 	private final static String VARIETYID = "variety_id"; 
	private final static String VILLEAGEID = "villeage_id";
	private final static String VILLEAGENAME = "villeage_name";
	private final static String PRODUCECOUNT = "producecount"; 
	private final static String VARIETY = "variety"; 
	private final static String CODE = "code"; 
	private final static String BUYLINK = "buylink"; 
	private final static String STATUS = "status"; 
	private final static String STAGE = "stage"; 
	private final static String CONSUMERCOUNT = "consumercount"; 
	private final static String CREATETIME = "create_time"; 
	private final static String ORDERINDEX = "order_index"; 
	private final static String ORDERDATE = "order_date"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static String CATEGORYID="categoryid";
	private final static String USERID="userid";
	public BatchDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(Batch batch) {
		if (batch == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(BATCHID, batch.getBatch_id());
 		cv.put(VARIETYID, batch.getVariety_id());
 		cv.put(VILLEAGEID, batch.getVilleage_id());
 		cv.put(VILLEAGENAME, batch.getVilleage_name());
 		cv.put(PRODUCECOUNT, batch.getProducecount());
 		cv.put(VARIETY, batch.getVariety());
 		cv.put(CODE, batch.getCode());
		cv.put(BUYLINK, batch.getBuylink());
		cv.put(STATUS, batch.getStatus());
		cv.put(STAGE, batch.getStage());
		cv.put(CONSUMERCOUNT, batch.getConsumercount());
		cv.put(CREATETIME, batch.getCreate_time());
		cv.put(ORDERINDEX, batch.getOrder_index());
		cv.put(ORDERDATE, batch.getOrder_date());
		cv.put(ISDEL, batch.getIsdel());
		cv.put(LASTOPTIME, batch.getLastoptime());
		cv.put(CATEGORYID, batch.getCategoryid());
		cv.put(USERID, batch.getUserid());
  		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		batch.setId((int)row);
		return row;
	}
	public  List<Batch>  selectbyVilleageIdVarietyId(int villeage_id,int variety_id,int stage) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,categoryid,lastoptime,userid";
		String from = " from "+TABLE_NAME+" dat ";
		String where = " where   dat.villeage_id = ? and dat.variety_id =?  and dat.stage=? and isdel="+DATA_NOT_DELETE+" order by lastoptime desc";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {villeage_id+"",variety_id+"",stage+""});
		int number = cursor.getCount();
 		List<Batch> reds=null;
		if (number > 0) {
			reds=new ArrayList<Batch>();
			Batch red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Batch();
 				red.setId(cursor.getInt(0));
  				red.setBatch_id(cursor.getInt(1));
  				red.setVariety_id(cursor.getLong(2));
  				red.setVilleage_id(cursor.getInt(3));
  				red.setProducecount(cursor.getInt(4));
  				red.setVariety(cursor.getString(5));
  				red.setCode(cursor.getString(6));
  				red.setBuylink(cursor.getString(7));
  				red.setStatus(cursor.getInt(8));
  				red.setStage(cursor.getInt(9));
  				red.setConsumercount(cursor.getInt(10));
  				red.setCreate_time(cursor.getString(11));
  				red.setOrder_date(cursor.getLong(12));
  				red.setOrder_index(cursor.getInt(13));
  				red.setCategoryid(cursor.getInt(14));
  				red.setLastoptime(cursor.getString(15));
  				red.setUserid(cursor.getInt(16));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}

	public  List<Batch>  selectbyVilleageId(int villeage_id,int stage) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,categoryid,lastoptime,userid";
		String from = " from "+TABLE_NAME+" dat ";
		String where = " where   dat.villeage_id = ? and dat.stage=? and isdel="+DATA_NOT_DELETE+" order by lastoptime desc";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {villeage_id+"",stage+""});
		int number = cursor.getCount();
 		List<Batch> reds=null;
		if (number > 0) {
			reds=new ArrayList<Batch>();
			Batch red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Batch();
 				red.setId(cursor.getInt(0));
  				red.setBatch_id(cursor.getInt(1));
  				red.setVariety_id(cursor.getLong(2));
  				red.setVilleage_id(cursor.getInt(3));
  				red.setProducecount(cursor.getInt(4));
  				red.setVariety(cursor.getString(5));
  				red.setCode(cursor.getString(6));
  				red.setBuylink(cursor.getString(7));
  				red.setStatus(cursor.getInt(8));
  				red.setStage(cursor.getInt(9));
  				red.setConsumercount(cursor.getInt(10));
  				red.setCreate_time(cursor.getString(11));
  				red.setOrder_date(cursor.getLong(12));
  				red.setOrder_index(cursor.getInt(13));
  				red.setCategoryid(cursor.getInt(14));
  				red.setLastoptime(cursor.getString(15));
  				red.setUserid(cursor.getInt(16));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	
	public  List<Batch>  serachByVariety(String variety,int stage) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,villeage_name,categoryid,userid";
		String from = " from "+TABLE_NAME+" dat  ";
		String where = " where   dat.variety like ? and dat.stage=? and isdel="+DATA_NOT_DELETE+" order by order_date desc,order_index  desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {"%"+variety+"%",stage+""});
		int number = cursor.getCount();
 		List<Batch> reds=null;
		if (number > 0) {
			reds=new ArrayList<Batch>();
			Batch red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Batch();
 				red.setId(cursor.getInt(0));
  				red.setBatch_id(cursor.getInt(1));
  				red.setVariety_id(cursor.getLong(2));
  				red.setVilleage_id(cursor.getInt(3));
  				red.setProducecount(cursor.getInt(4));
  				red.setVariety(cursor.getString(5));
  				red.setCode(cursor.getString(6));
  				red.setBuylink(cursor.getString(7));
  				red.setStatus(cursor.getInt(8));
  				red.setStage(cursor.getInt(9));
  				red.setConsumercount(cursor.getInt(10));
  				red.setCreate_time(cursor.getString(11));
  				red.setOrder_date(cursor.getLong(12));
  				red.setOrder_index(cursor.getInt(13));
  				red.setVilleage_name(cursor.getString(14));
  				red.setCategoryid(cursor.getInt(15));
  				red.setUserid(cursor.getInt(16));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	
	
	
	public  List<Batch>  serachByVarietyId(int varietyid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,villeage_name,categoryid,userid";
		String from = " from "+TABLE_NAME+" dat  ";
		String where = " where   dat.variety_id=?   and isdel="+DATA_NOT_DELETE+" order by order_date desc,order_index  desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {varietyid+""});
		int number = cursor.getCount();
 		List<Batch> reds=null;
		if (number > 0) {
			reds=new ArrayList<Batch>();
			Batch red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Batch();
 				red.setId(cursor.getInt(0));
  				red.setBatch_id(cursor.getInt(1));
  				red.setVariety_id(cursor.getLong(2));
  				red.setVilleage_id(cursor.getInt(3));
  				red.setProducecount(cursor.getInt(4));
  				red.setVariety(cursor.getString(5));
  				red.setCode(cursor.getString(6));
  				red.setBuylink(cursor.getString(7));
  				red.setStatus(cursor.getInt(8));
  				red.setStage(cursor.getInt(9));
  				red.setConsumercount(cursor.getInt(10));
  				red.setCreate_time(cursor.getString(11));
  				red.setOrder_date(cursor.getLong(12));
  				red.setOrder_index(cursor.getInt(13));
  				red.setVilleage_name(cursor.getString(14));
  				red.setCategoryid(cursor.getInt(15));
  				red.setUserid(cursor.getInt(16));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	
	public  List<Batch>  serachByCode(String code,int stage) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,villeage_name,categoryid,userid";
		String from = " from "+TABLE_NAME+" dat   ";
		String where = " where   dat.code like ? and dat.stage=? and isdel="+DATA_NOT_DELETE+" order by order_date desc,order_index  desc ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {"%"+code+"%",stage+""});
		int number = cursor.getCount();
 		List<Batch> reds=null;
		if (number > 0) {
			reds=new ArrayList<Batch>();
			Batch red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Batch();
 				red.setId(cursor.getInt(0));
  				red.setBatch_id(cursor.getInt(1));
  				red.setVariety_id(cursor.getLong(2));
  				red.setVilleage_id(cursor.getInt(3));
  				red.setProducecount(cursor.getInt(4));
  				red.setVariety(cursor.getString(5));
  				red.setCode(cursor.getString(6));
  				red.setBuylink(cursor.getString(7));
  				red.setStatus(cursor.getInt(8));
  				red.setStage(cursor.getInt(9));
  				red.setConsumercount(cursor.getInt(10));
  				red.setCreate_time(cursor.getString(11));
  				red.setOrder_date(cursor.getLong(12));
  				red.setOrder_index(cursor.getInt(13));
  				red.setVilleage_name(cursor.getString(14));
  				red.setCategoryid(cursor.getInt(15));
  				red.setUserid(cursor.getInt(16));
 				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public  Batch selectbyBatchId(int batchid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,villeage_name,categoryid,userid";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.batch_id = ?    ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {batchid+""});
		int number = cursor.getCount();
		Batch red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Batch();
			red.setId(cursor.getInt(0));
			red.setBatch_id(cursor.getInt(1));
			red.setVariety_id(cursor.getLong(2));
			red.setVilleage_id(cursor.getInt(3));
			red.setProducecount(cursor.getInt(4));
			red.setVariety(cursor.getString(5));
			red.setCode(cursor.getString(6));
			red.setBuylink(cursor.getString(7));
			red.setStatus(cursor.getInt(8));
			red.setStage(cursor.getInt(9));
			red.setConsumercount(cursor.getInt(10));
			red.setCreate_time(cursor.getString(11));
			red.setOrder_date(cursor.getLong(12));
			red.setOrder_index(cursor.getInt(13));
			red.setVilleage_name(cursor.getString(14));
			red.setCategoryid(cursor.getInt(15));
			red.setUserid(cursor.getInt(16));
   		}
		cursor.close();
		db.close();
		return red;
	}
	
	public  Batch selectbyBatchCode(String batchcode) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,villeage_name,categoryid,userid";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.code= ?   and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), new String[] {batchcode});
		int number = cursor.getCount();
		Batch red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Batch();
			red.setId(cursor.getInt(0));
			red.setBatch_id(cursor.getInt(1));
			red.setVariety_id(cursor.getLong(2));
			red.setVilleage_id(cursor.getInt(3));
			red.setProducecount(cursor.getInt(4));
			red.setVariety(cursor.getString(5));
			red.setCode(cursor.getString(6));
			red.setBuylink(cursor.getString(7));
			red.setStatus(cursor.getInt(8));
			red.setStage(cursor.getInt(9));
			red.setConsumercount(cursor.getInt(10));
			red.setCreate_time(cursor.getString(11));
			red.setOrder_date(cursor.getLong(12));
			red.setOrder_index(cursor.getInt(13));
			red.setVilleage_name(cursor.getString(14));
			red.setCategoryid(cursor.getInt(15));
			red.setUserid(cursor.getInt(16));
   		}
		cursor.close();
		db.close();
		return red;
	}
	
	/**
	 * 获取批次的最后操作时间
	 * @return record_id
	 */
	public String getLatoptime( int villeage_id,int stage) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where   dat.villeage_id = ? and dat.stage=? ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[]{villeage_id+"",stage+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	 
	public  Batch getTopBatch() {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select dat.id,batch_id,variety_id,villeage_id,producecount,variety,code,buylink,status,stage,consumercount,create_time,order_date,order_index,categoryid,userid";
		String from = " from "+TABLE_NAME+" dat,(select id, max(order_index) from T_android_Batch where order_date=(select   max(order_date) mdate from  T_android_Batch)) t ";
		String where = " where dat.id=t.id   and isdel="+DATA_NOT_DELETE+" ";
		sb.append(select);
		sb.append(from);
		sb.append(where);
   		Cursor cursor = db.rawQuery(sb.toString(), null);
		int number = cursor.getCount();
		Batch red=null;
 		if (number > 0) {
 			cursor.moveToFirst();
			red=new Batch();
			red.setId(cursor.getInt(0));
			red.setBatch_id(cursor.getInt(1));
			red.setVariety_id(cursor.getLong(2));
			red.setVilleage_id(cursor.getInt(3));
			red.setProducecount(cursor.getInt(4));
			red.setVariety(cursor.getString(5));
			red.setCode(cursor.getString(6));
			red.setBuylink(cursor.getString(7));
			red.setStatus(cursor.getInt(8));
			red.setStage(cursor.getInt(9));
			red.setConsumercount(cursor.getInt(10));
			red.setCreate_time(cursor.getString(11));
			red.setOrder_date(cursor.getLong(12));
			red.setOrder_index(cursor.getInt(13));
			red.setCategoryid(cursor.getInt(14));
			red.setUserid(cursor.getInt(15));
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
	public void deleteByBatchId(int batchid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = BATCHID + " = ?";
		String[] whereValue = {batchid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public void deleteByVilleageId(int villeageid) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = VILLEAGEID + " = ?";
		String[] whereValue = {villeageid+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
	public void deleteNoCodeBatch() {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = CODE + " is null";
 		db.delete(TABLE_NAME, where, null);
		db.close();
		
	}
	public  void update(Batch batch) {
		if (batch == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(batch.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(BATCHID, batch.getBatch_id());
 		cv.put(VARIETYID, batch.getVariety_id());
 		cv.put(VILLEAGEID, batch.getVilleage_id());
 		cv.put(VILLEAGENAME, batch.getVilleage_name());
 		cv.put(PRODUCECOUNT, batch.getProducecount());
 		cv.put(VARIETY, batch.getVariety());
 		cv.put(CODE, batch.getCode());
		cv.put(BUYLINK, batch.getBuylink());
		cv.put(STATUS, batch.getStatus());
		cv.put(STAGE, batch.getStage());
		cv.put(CONSUMERCOUNT, batch.getConsumercount());
		cv.put(CREATETIME, batch.getCreate_time());
		//cv.put(ORDERINDEX, batch.getOrder_index());
		//cv.put(ORDERDATE, batch.getOrder_date());
		cv.put(ISDEL, batch.getIsdel());
		cv.put(LASTOPTIME, batch.getLastoptime());
		cv.put(CATEGORYID, batch.getCategoryid());
		cv.put(USERID, batch.getUserid());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	 
	/**
	 * 更新批次记录数
	 * @param count
	 * @param type
	 * @param batchid
	 */
	public  void updateBatchRecordCount(int count,int type,int batchid) {
		 
		String where = BATCHID + " = ?";
		String[] whereValue = { batchid+""};
 		ContentValues cv = new ContentValues();
 		if(type==Record.BATCHRECORD_USERTYPE_OF_CREATOR)
 			cv.put(PRODUCECOUNT, count);
 		else if(type==Record.BATCHRECORD_USERTYPE_OF_CONSUMER)
 		    cv.put(CONSUMERCOUNT, count);
   		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
//	/**
//	 * 根据本地数据更新批次记录数
//	 * @param count
//	 * @param type
//	 * @param batchid
//	 */
//	public  void updateBatchCountWithLocalRecord(int batchid) {
//		
//		String sql="update "+TABLE_NAME+"   ";
//		sql+=" set "+PRODUCECOUNT+"="+"(select count(*) from T_android_Record   where batch_id="+batchid+" and  isdel="+DATA_NOT_DELETE+" and  user_type="+Record.BATCHRECORD_USERTYPE_OF_CREATOR+"),";
//		sql+="  "+CONSUMERCOUNT+"="+"(select count(*) from T_android_Record   where batch_id="+batchid+" and  isdel="+DATA_NOT_DELETE+" and  user_type="+Record.BATCHRECORD_USERTYPE_OF_CONSUMER+")";
//		sql+=" where "+BATCHID+"="+batchid;
//    	SQLiteDatabase db = this.getWritableDatabase();
//    	//LogUtil.logInfo(getClass(), "sql:"+sql);
//   		db.execSQL(sql);
//		db.close();
// 	}

	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
