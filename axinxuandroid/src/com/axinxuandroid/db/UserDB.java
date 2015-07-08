package com.axinxuandroid.db;

 
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;

 import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class UserDB extends SystemDB {
	
	public final static String TABLE_NAME = "T_android_User"; 
	private final static String ID = "id"; 
	private final static String USERID = "user_id"; 
	private final static String PHONE = "phone"; 
	private final static String PWD = "pwd"; 
	private final static String USERNAME = "user_name"; 
	private final static String EMAIL = "email"; 
 	private final static String CREATETIME = "create_time";
	private final static String LOGINTIME = "login_time";
	private final static String PERSONDESC = "person_desc";
	private final static String PERSONIMAGEURL = "person_imageurl";
	private final static String LOCALIMGURL = "local_imgurl";
	private final static String WORKTIME = "worktime";
	private final static String ADDRESS = "address";
	private final static String SEX = "sex";
	public UserDB(Context context) {
		super(context);
	}

	  
	
	
	public long insert(User user) {
		if (user == null) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
 		cv.put(USERID, user.getUser_id());
		cv.put(PHONE, user.getPhone());
		cv.put(USERNAME, user.getUser_name());
		cv.put(EMAIL, user.getEmail());
 		cv.put(CREATETIME, user.getCreate_time());
		cv.put(LOGINTIME, user.getLogin_time());
		cv.put(PERSONDESC, user.getPerson_desc());
		cv.put(PERSONIMAGEURL, user.getPerson_imageurl());
		cv.put(LOCALIMGURL, user.getLocal_imgurl());
		cv.put(WORKTIME, user.getWorktime());
		cv.put(ADDRESS, user.getAddress());
 		cv.put(PWD, user.getPwd());
 		cv.put(SEX, user.getSex());
  		long row = db.insert(TABLE_NAME, null, cv);
		db.close();
		return row;
	}
 
 
	/**
	 *  
	 */
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(TABLE_NAME, null, null);
 		db.close();
	}
	
	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
 		String where = ID + " = ?";
		String[] whereValue = {id};
		db.delete(TABLE_NAME, where, whereValue);
		db.close();
 	}
	
	 
	
	
	public User selectbyUserId(int userid) {
		SQLiteDatabase db = this.getReadableDatabase();
 		User newUSer =null;
		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.user_id = ?";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {userid+""});
   		if (cursor.getCount() > 0) {
  			cursor.moveToFirst();
			newUSer = new User();
			newUSer.setId(cursor.getInt(0));
			newUSer.setUser_id(cursor.getInt(1));
			newUSer.setPhone(cursor.getString(2));
			newUSer.setPwd(cursor.getString(3));
			newUSer.setUser_name(cursor.getString(4));
			newUSer.setEmail(cursor.getString(5));
 			newUSer.setCreate_time(cursor.getString(6));
			newUSer.setLogin_time(cursor.getString(7));
			newUSer.setPerson_desc(cursor.getString(8));
			newUSer.setPerson_imageurl(cursor.getString(9));
			newUSer.setLocal_imgurl(cursor.getString(10));
			newUSer.setWorktime(cursor.getString(11));
			newUSer.setAddress(cursor.getString(12));
			newUSer.setSex(cursor.getString(13));
  		} 
		cursor.close();
		db.close();
 		return newUSer;
	}
  
	
	public void updateUser(User user) {
		if (user == null) {
			return;
		}
 		String where = USERID + " = ?";
		String[] whereValue = { String.valueOf(user.getUser_id()) };
		ContentValues cv = new ContentValues();
 		cv.put(PHONE, user.getPhone());
		cv.put(USERNAME, user.getUser_name());
		cv.put(EMAIL, user.getEmail());
	 
		cv.put(CREATETIME, user.getCreate_time());
		cv.put(LOGINTIME, user.getLogin_time());
		cv.put(PERSONDESC, user.getPerson_desc());
		cv.put(PERSONIMAGEURL, user.getPerson_imageurl());
		cv.put(LOCALIMGURL, user.getLocal_imgurl());
		cv.put(WORKTIME, user.getWorktime());
		cv.put(ADDRESS, user.getAddress());
		cv.put(SEX, user.getSex());
 		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	
	public void updateLogintime(User user,String imei) {
		if (user == null) {
			return;
		}
 		String where = ID + " = ?";
		String[] whereValue = { String.valueOf(user.getUser_id()) };
		ContentValues cv = new ContentValues();
		cv.put(LOGINTIME, user.getLogin_time());
		 
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
	}
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
