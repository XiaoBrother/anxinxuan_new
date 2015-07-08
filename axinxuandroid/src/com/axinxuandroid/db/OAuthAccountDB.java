package com.axinxuandroid.db;

 
import com.axinxuandroid.data.OAuthAccount;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.UserService;
import com.ncpzs.util.LogUtil;

 import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class OAuthAccountDB extends SystemDB {
	
	public final static String TABLE_NAME = "T_android_OAuthAccount"; 
	private final static String ID = "id"; 
	private final static String USERID = "userid"; 
	private final static String ACCOUNTID = "account_id"; 
	private final static String TYPE = "type"; 
	private final static String AUTHORIZEIMG = "authorizeimg"; 
	private final static String NICKNAME = "nick_name"; 
 	 

	public OAuthAccountDB(Context context) {
		super(context);
	}

	  
	
	
	public void insert(OAuthAccount account) {
		if (account == null) return;
		SQLiteDatabase db = this.getWritableDatabase();
 		ContentValues cv = new ContentValues();
 		cv.put(USERID, account.getUserid());
		cv.put(ACCOUNTID, account.getAccount_id());
		cv.put(TYPE, account.getType());
		cv.put(AUTHORIZEIMG, account.getAuthorizeimg());
 		cv.put(NICKNAME, account.getNick_name());
   		long row = db.insert(TABLE_NAME, null, cv);
   		account.setId((int)row);
		db.close();
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
	
	 
	
	
	public OAuthAccount selectbyAccountWithType(String accountid,int type) {
		SQLiteDatabase db = this.getReadableDatabase();
		OAuthAccount oath =null;
		StringBuffer sb = new StringBuffer();
		String select = "select * ";
		String from = " from "+TABLE_NAME+" dat";
		String where = " where dat.account_id = ? and dat.type=?";
		sb.append(select);
		sb.append(from);
		sb.append(where);
		Cursor cursor = db.rawQuery(sb.toString(), new String[] {accountid,type+""});
   		if (cursor.getCount() > 0) {
  			cursor.moveToFirst();
  			oath = new OAuthAccount();
  			oath.setId(cursor.getInt(0));
  			oath.setUserid(cursor.getInt(1));
  			oath.setAccount_id(cursor.getString(2));
  			oath.setType(cursor.getInt(3));
  			oath.setAuthorizeimg(cursor.getString(4));
  			oath.setNick_name(cursor.getString(5));
  		} 
		cursor.close();
		db.close();
 		return oath;
	}
  
	
	public void update(OAuthAccount account) {
		if (account == null) {
			return;
		}
 		String where = USERID + " = ?";
		String[] whereValue = { String.valueOf(account.getId()) };
		ContentValues cv = new ContentValues();
		cv.put(USERID, account.getUserid());
		cv.put(ACCOUNTID, account.getAccount_id());
		cv.put(TYPE, account.getType());
		cv.put(AUTHORIZEIMG, account.getAuthorizeimg());
 		cv.put(NICKNAME, account.getNick_name());
 		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
 	}
	
	 
	@Override
	public String getDBName() {
 		return TABLE_NAME;
	}
}
