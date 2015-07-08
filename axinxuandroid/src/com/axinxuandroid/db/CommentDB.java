package com.axinxuandroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.BatchLabel;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.Template;
import com.ncpzs.util.LogUtil;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommentDB extends SystemDB {
 
	private final static String TABLE_NAME = "T_android_Comment"; 
	private final static String ID = "id"; 
	private final static String COMMENTID = "comment_id"; 
	private final static String USERNAME = "user_name"; 
 	private final static String CONTEXT = "context"; 
	private final static String COMMENTDATE = "comment_date"; 
	private final static String USERIMG = "user_img"; 
	private final static String PARENTID = "parent_id"; 
	private final static String USERID = "user_id"; 
	private final static String RECORDID = "recordid"; 
	private final static String ISDEL = "isdel"; 
	private final static String LASTOPTIME = "lastoptime"; 
	private final static String REPLYUSERNAME = "replyUserName";
	private final static String REPLYUSERID = "replyUserId";
	private final static String VARIETYNAME = "varietyName";
	private final static String BATCHCODE = "batchCode";
	public CommentDB(Context context) {
		super(context);
	}
  	 
	
	public long insert(Comment comment) {
		if (comment == null) {
			return 0;
		}
 		ContentValues cv = new ContentValues();
 		cv.put(COMMENTID, comment.getComment_id());
 		cv.put(USERNAME, comment.getUser_name());
 		cv.put(CONTEXT, comment.getContext());
  		cv.put(COMMENTDATE, comment.getComment_date());
 		cv.put(USERIMG, comment.getUser_img());
 		cv.put(PARENTID, comment.getParent_id());
 		cv.put(USERID, comment.getUser_id());
 		cv.put(RECORDID, comment.getRecordid());
 		cv.put(ISDEL, comment.getIsdel());
 		cv.put(LASTOPTIME, comment.getLastoptime());
 		cv.put(REPLYUSERNAME, comment.getReplyUserName());
 		cv.put(REPLYUSERID, comment.getReplyUserId());
 		cv.put(VARIETYNAME, comment.getVarietyName());
 		cv.put(BATCHCODE, comment.getBatchCode());
    	SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		comment.setId((int)row);
		return row;
	}
	/**
	 * 获取记录评论的最后更新时间
	 * @return record_id
	 */
	public String getLatoptime(int recordid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat where recordid=?";
		sb.append(select);
		sb.append(from);
 		Cursor cursor = db.rawQuery(sb.toString(), new String[]{recordid+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	
	/**
	 * 获取用户发送评论的最后更新时间
	 * @return record_id
	 */
	public String getUserSendLatoptime(int userid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat where user_id=?";
		sb.append(select);
		sb.append(from);
 		Cursor cursor = db.rawQuery(sb.toString(), new String[]{userid+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	/**
	 * 获取用户收到评论的最后更新时间
	 * @return record_id
	 */
	public String getUserReceiveLatoptime(int userid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String lastoptime =null;
		StringBuffer sb = new StringBuffer();
		String select="select max(lastoptime) ";
		String from = " from "+TABLE_NAME+" dat where parent_id=?";
		sb.append(select);
		sb.append(from);
 		Cursor cursor = db.rawQuery(sb.toString(), new String[]{userid+""});
 		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			lastoptime = cursor.getString(0);
 		}
		cursor.close();
 		db.close();
		return lastoptime;
	}
	public  Comment  getByCommentid(int commentid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat where comment_id=?";
 		sb.append(select);
		sb.append(from);
    	 Cursor cursor = db.rawQuery(sb.toString(), new String[]{commentid+""});
		int number = cursor.getCount();
		Comment red=null;
		if (number > 0) {
  			cursor.moveToFirst();
  			red=new Comment();
 			red.setId(cursor.getInt(0));
 			red.setComment_id(cursor.getInt(1));
 			red.setUser_name(cursor.getString(2));
 			red.setContext(cursor.getString(3));
 			red.setComment_date(cursor.getString(4));
 			red.setUser_img(cursor.getString(5));
 			red.setParent_id(cursor.getInt(6));
 			red.setUser_id(cursor.getInt(7));
 			red.setRecordid(cursor.getInt(8));
 			red.setReplyUserName(cursor.getString(11));
 			red.setReplyUserId(cursor.getString(12));
 			red.setVarietyName(cursor.getString(13));
 			red.setBatchCode(cursor.getString(14));
 		}
		cursor.close();
		db.close();
		return red;
	}
	public  List<Comment>  getCommentByRecordid(int recordid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		StringBuffer sb = new StringBuffer();
		String select = "select *";
		String from = " from "+TABLE_NAME+" dat where recordid=? and isdel="+DATA_NOT_DELETE+" order  by comment_id desc";
 		sb.append(select);
		sb.append(from);
    	Cursor cursor = db.rawQuery(sb.toString(), new String[]{recordid+""});
		int number = cursor.getCount();
 		List<Comment> reds=null;
		if (number > 0) {
			reds=new ArrayList<Comment>();
			Comment red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Comment();
 				red.setId(cursor.getInt(0));
 				red.setComment_id(cursor.getInt(1));
 				red.setUser_name(cursor.getString(2));
 				red.setContext(cursor.getString(3));
 				red.setComment_date(cursor.getString(4));
 				red.setUser_img(cursor.getString(5));
 				red.setParent_id(cursor.getInt(6));
 				red.setUser_id(cursor.getInt(7));
 				red.setRecordid(cursor.getInt(8));
 				red.setLastoptime(cursor.getString(10));
 				red.setReplyUserName(cursor.getString(11));
 				red.setReplyUserId(cursor.getString(12));
 	 			red.setVarietyName(cursor.getString(13));
 	 			red.setBatchCode(cursor.getString(14));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	 
	public  List<Comment>  getUserSendComment(int userid,String lastoptime) {
		SQLiteDatabase db = this.getReadableDatabase();
 		String select = "select * from "+TABLE_NAME+" dat where user_id=? ";
 		if(lastoptime!=null) select+=" and lastoptime <=? ";
 		select+="  and isdel="+DATA_NOT_DELETE+"  order  by comment_date desc limit "+PAGE_LIMIT;
 		String[] params=null;
 		if(lastoptime==null)  params=new String[]{userid+""};
 		else params=new String[]{userid+"",lastoptime};
       	Cursor cursor = db.rawQuery(select, params);
    	LogUtil.logInfo(getClass(), select+","+userid+","+lastoptime);
		int number = cursor.getCount();
 		List<Comment> reds=null;
		if (number > 0) {
			reds=new ArrayList<Comment>();
			Comment red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Comment();
 				red.setId(cursor.getInt(0));
 				red.setComment_id(cursor.getInt(1));
 				red.setUser_name(cursor.getString(2));
 				red.setContext(cursor.getString(3));
 				red.setComment_date(cursor.getString(4));
 				red.setUser_img(cursor.getString(5));
 				red.setParent_id(cursor.getInt(6));
 				red.setUser_id(cursor.getInt(7));
 				red.setRecordid(cursor.getInt(8));
 				red.setLastoptime(cursor.getString(10));
 				red.setReplyUserName(cursor.getString(11));
 				red.setReplyUserId(cursor.getString(12));
 	 			red.setVarietyName(cursor.getString(13));
 	 			red.setBatchCode(cursor.getString(14));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public  List<Comment>  getUserReceiveComment(int userid,String lastoptime) {
		SQLiteDatabase db = this.getReadableDatabase();
		String select = "select * from "+TABLE_NAME+" dat where replyUserId=? ";
 		if(lastoptime!=null) select+=" and lastoptime <=? ";
 		select+="  and isdel="+DATA_NOT_DELETE+"  order  by comment_date desc limit "+PAGE_LIMIT;
 		String[] params=null;
 		if(lastoptime==null)  params=new String[]{userid+""};
 		else params=new String[]{userid+"",lastoptime};
       	Cursor cursor = db.rawQuery(select, params);
 		int number = cursor.getCount();
 		List<Comment> reds=null;
		if (number > 0) {
			reds=new ArrayList<Comment>();
			Comment red=null;
			cursor.moveToFirst();
 			while (cursor.getPosition() != number) {
 				red=new Comment();
 				red.setId(cursor.getInt(0));
 				red.setComment_id(cursor.getInt(1));
 				red.setUser_name(cursor.getString(2));
 				red.setContext(cursor.getString(3));
 				red.setComment_date(cursor.getString(4));
 				red.setUser_img(cursor.getString(5));
 				red.setParent_id(cursor.getInt(6));
 				red.setUser_id(cursor.getInt(7));
 				red.setRecordid(cursor.getInt(8));
 				red.setLastoptime(cursor.getString(10));
 				red.setReplyUserName(cursor.getString(11));
 				red.setReplyUserId(cursor.getString(12));
 	 			red.setVarietyName(cursor.getString(13));
 	 			red.setBatchCode(cursor.getString(14));
   				reds.add(red);
				cursor.moveToNext();
			}
 		}
		cursor.close();
		db.close();
		return reds;
	}
	public void deleteById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id+""};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
		
	}
 
 
	public  void update(Comment comment) {
		if (comment == null) {
			return ;
		}
		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(comment.getId()) };
 		ContentValues cv = new ContentValues();
 		cv.put(COMMENTID, comment.getComment_id());
 		cv.put(USERNAME, comment.getUser_name());
 		cv.put(CONTEXT, comment.getContext());
  		cv.put(COMMENTDATE, comment.getComment_date());
 		cv.put(USERIMG, comment.getUser_img());
 		cv.put(PARENTID, comment.getParent_id());
 		cv.put(USERID, comment.getUser_id());
 		cv.put(RECORDID, comment.getRecordid());
 		cv.put(ISDEL, comment.getIsdel());
 		cv.put(LASTOPTIME, comment.getLastoptime());
 		cv.put(REPLYUSERNAME, comment.getReplyUserName());
 		cv.put(REPLYUSERID, comment.getReplyUserId());
 		cv.put(VARIETYNAME, comment.getVarietyName());
 		cv.put(BATCHCODE, comment.getBatchCode());
  		SQLiteDatabase db = this.getWritableDatabase();
  		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
