package com.axinxuandroid.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * @author  
 *  
 *
 */
public abstract class SystemDB extends SQLiteOpenHelper {
	public static  final int DATA_HAS_DELETE=1;
    public static  final int DATA_NOT_DELETE=0;
	public final static String DATABASE_NAME = "anxinxuan"; 
	public static  final int PAGE_LIMIT=10;
    /**
     * ���ݿ�İ汾��һ������ֵ���ڴ���SQLiteOpenHelperʱ��
     * �ᴫ������ݿ�İ汾�������������ݿ�汾�ű����ݿ��ļ��д洢�İ汾�Ŵ�Ļ���
     * ��ôSQLiteOpenHelper#onUpgrade()�����ͻᱻ���ã����ǵ�����Ӧ���ڸ÷�������ɡ�

     */
	public final static int DATABASE_VERSION = 52; 
	private Context context;
 	public SystemDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
 	}
	
 	 
 	private void createAllTable(SQLiteDatabase db){
 		//T_android_Record_Resource ��¼��Դ
 		String sql = "CREATE TABLE IF NOT EXISTS T_android_Record_Resource (id INTEGER primary key autoincrement, "
 			+    "neturl  text,localurl text, type INTEGER, recordid INTEGER , info text ,state INTEGER,publishstate INTEGER)";
  	    db.execSQL(sql);
  	    //T_android_Record ��¼
  	   sql = "CREATE TABLE IF NOT EXISTS T_android_Record (id INTEGER primary key autoincrement, "
		+  "phone text, user_id text, labelname text, nick_name text,batch_id INTEGER, record_id INTEGER, trace_code text, recordtext text, save_type INTEGER, send_date text, save_date text, type INTEGER,state INTEGER,variety text,variety_id INTEGER,user_type INTEGER,villeage_id INTEGER,user_img text,isdel INTEGER,lastoptime text,savestatus INTEGER ,lng text,lat text)";
	    db.execSQL(sql);
	     
	    //T_android_Link ���ڱ�ǩ
	    sql = "CREATE TABLE IF NOT EXISTS T_android_Batch_Label (id INTEGER primary key autoincrement, "
		+   "variety_id INTEGER, label_name text,isdel INTEGER,lastoptime text,recordid INTEGER)";
        db.execSQL(sql);
        //T_android_User �û�
        sql = "CREATE TABLE IF NOT EXISTS T_android_User (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER, phone text, pwd text, user_name text, email text,  create_time text, login_time text, person_desc text,person_imageurl text,local_imgurl text,worktime text,address text,sex text)";
        db.execSQL(sql);
      //T_android_OAuthAccount OAuth�˺�
        sql = "CREATE TABLE IF NOT EXISTS T_android_OAuthAccount (id INTEGER primary key autoincrement, "
		+   "userid INTEGER, account_id text, type INTEGER, authorizeimg text, nick_name text)";
        db.execSQL(sql);
        //T_android_Villeage ũ��
        sql = "CREATE TABLE IF NOT EXISTS T_android_Villeage (id INTEGER primary key autoincrement, "
		+   "villeage_id INTEGER, villeage_name text, collect_code text, manager_name text, tele text, address text,bulid_time text,scale text,type INTEGER,manage_scope text,villeage_desc text,lat text,lng text,user_id INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_User_Villeage �û�ũ��
        sql = "CREATE TABLE IF NOT EXISTS T_android_User_Villeage (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER,villeage_id INTEGER,role INTEGER)";
        db.execSQL(sql);
        //T_android_Batch ����
        sql = "CREATE TABLE IF NOT EXISTS T_android_Batch (id INTEGER primary key autoincrement, "
		+   "batch_id INTEGER,variety_id INTEGER,villeage_id INTEGER, variety text, producecount INTEGER, code text,buylink text,status INTEGER,stage INTEGER,consumercount INTEGER,create_time text,order_index INTEGER,order_date INTEGER,villeage_name text,isdel INTEGER,lastoptime text,categoryid INTEGER,userid INTEGER)";
        db.execSQL(sql);
      //T_android_Template ģ��
        sql = "CREATE TABLE IF NOT EXISTS T_android_Template (id INTEGER primary key autoincrement, "
		+   "template_id INTEGER,category_id INTEGER,label_name text, head text, foot text, sign text,context text,version INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
      //T_android_Proust ��³˹���ʾ�
        sql = "CREATE TABLE IF NOT EXISTS T_android_Proust (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER,answer text,question text,proust_id INTEGER,create_date text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
      //T_android_Category Ʒ�������
        sql = "CREATE TABLE IF NOT EXISTS T_android_Category (id INTEGER primary key autoincrement, "
		+   "category_name text,parentid INTEGER,cate_order INTEGER,clevel INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_Variety Ʒ�ֱ�
        sql = "CREATE TABLE IF NOT EXISTS T_android_Variety (id INTEGER primary key autoincrement, "
		+   "variety_id INTEGER,variety_name text,category_id INTEGER,villeage_id INTEGER,user_id INTEGER,create_time text,variety_desc text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        
      //T_android_VilleageBanner ũ��banner
        sql = "CREATE TABLE IF NOT EXISTS T_android_VilleageBanner (id INTEGER primary key autoincrement, "
		+   "bannerid INTEGER,villeage_id INTEGER,neturl text,localurl text,thumb_url text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
      //T_android_Intelligence ũ������
        sql = "CREATE TABLE IF NOT EXISTS T_android_Intelligence (id INTEGER primary key autoincrement, "
		+   "intelligenceid INTEGER,villeage_id INTEGER,label_name text,intelligence_desc text,thumbnail_url text,create_time text,isdel INTEGER,lastoptime text,localurl text,intelligence_imgurl text)";
        db.execSQL(sql);
        //T_android_UserFavorite �û��ղ�
        sql = "CREATE TABLE IF NOT EXISTS T_android_UserFavorite (id INTEGER primary key autoincrement, "
		+   "favid INTEGER,label_name text,favorite_type INTEGER,favorite_id INTEGER,user_id INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_VilleagePhoto ũ�����
        sql = "CREATE TABLE IF NOT EXISTS T_android_VilleagePhoto (id INTEGER primary key autoincrement, "
		+   "photoid INTEGER,label_name text,image_url text,image_desc text,image_name text,villeage_id INTEGER,index_order INTEGER,thumbnail_url text,localurl text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_SystemNotice ϵͳ֪ͨ
        sql = "CREATE TABLE IF NOT EXISTS T_android_SystemNotice (id INTEGER primary key autoincrement, "
		+   "type INTEGER,notice text,jsondata text)";
        db.execSQL(sql);
        
      //T_android_SystemLog ϵͳ��־
        sql = "CREATE TABLE IF NOT EXISTS T_android_SystemLog (id INTEGER primary key autoincrement, "
		+   "actiontime text,message text,extrainfo text)";
        db.execSQL(sql);
         
      //T_android_SystemSet ϵͳ����
        sql = "CREATE TABLE IF NOT EXISTS T_android_SystemSet (id INTEGER primary key autoincrement, "
		+   "type INTEGER,setjson text)";
        db.execSQL(sql);
        //T_android_Comment ����
        sql = "CREATE TABLE IF NOT EXISTS T_android_Comment (id INTEGER primary key autoincrement, "
		+   "comment_id INTEGER,user_name text,context text,comment_date text,user_img text,parent_id INTEGER,user_id INTEGER,recordid INTEGER,isdel INTEGER,lastoptime text,replyUserName text,replyUserId INTEGER,varietyName text,batchCode text)";
        db.execSQL(sql);
        //T_android_UserVisitHistory �û������¼
        sql = "CREATE TABLE IF NOT EXISTS T_android_UserVisitHistory (id INTEGER primary key autoincrement, "
		+   "userid INTEGER,type INTEGER,visitobjid INTEGER,visittime text)";
        db.execSQL(sql);
        
        //T_android_CategoryProcedure Ʒ���������̱�
        sql = "CREATE TABLE IF NOT EXISTS T_android_CategoryProcedure (id INTEGER primary key autoincrement, "
		+   "categoryprocedureid INTEGER,category_id INTEGER,name text,index_order INTEGER,create_time text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        
      //T_android_NetUpdateRecord ���ݸ��¼�¼
        sql = "CREATE TABLE IF NOT EXISTS T_android_NetUpdateRecord (id INTEGER primary key autoincrement, "
		+   "type INTEGER,objid INTEGER,statusjson text)";
        db.execSQL(sql);
        
      //T_android_System_Label ϵͳ��ǩ
        sql = "CREATE TABLE IF NOT EXISTS T_android_System_Label (id INTEGER primary key autoincrement, "
		+   "label_id INTEGER,label_name text,label_imageurl text,type INTEGER,module_id INTEGER, index_order INTEGER)";
        db.execSQL(sql);
        
        //T_android_Advocate �û���
        sql = "CREATE TABLE IF NOT EXISTS T_android_Advocate (id INTEGER primary key autoincrement, "
		+   "userid INTEGER,recordid INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        
        
        //T_android_SecurityCodeBatch �û���α����
        sql = "CREATE TABLE IF NOT EXISTS T_android_SecurityCodeBatch (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER,batchid INTEGER, variety_id INTEGER, status INTEGER,batchtime text,timebatch text,snum text,endnum text,tcount INTEGER,bdesc text,createtime text, isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
 	}
 	private void dropAllTable(SQLiteDatabase db){
 		String sql = "DROP TABLE IF EXISTS T_android_Record_Resource " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Record " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Link " ;
		db.execSQL(sql);
	    sql = "DROP TABLE IF EXISTS T_android_User " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Villeage " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_User_Villeage " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Batch " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Template " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Proust " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Category " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Variety " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_VilleageBanner " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Intelligence " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_UserFavorite " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_SystemNotice " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_VilleagePhoto " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_SystemLog " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_SystemSet " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Comment " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_UserVisitHistory " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_CategoryProcedure " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Batch_Label " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_NetUpdateRecord " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_OAuthAccount " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_System_Label " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_Advocate " ;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_android_SecurityCodeBatch " ;
		db.execSQL(sql);
		 
 	}
	/**
	 * @param paraList
	 * @return paraArray
	 *  
	 */
	public String[] getStringArray(List<String> paraList) {
		int paraSize = paraList.size();
		int index = 0;
		String[] paraArray = new String[paraSize];
		for (String para : paraList) {
			paraArray[index] = para;
			index++;
		}
		return paraArray;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createAllTable(db);
		initSqlDatas(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropAllTable(db);
		onCreate(db);
 	}
 
	/**
	 * ��ʼ������
	 */
	public void initSqlDatas(SQLiteDatabase db){
		readSqlFile(db,"axx_systemset.sql");
		readSqlFile(db,"axx_category.sql");
		readSqlFile(db,"axx_categoryprocedure.sql");
		readSqlFile(db,"axx_recordtemplate.sql");
		readSqlFile(db,"axx_systemcertificate.sql");
 	}
	private void readSqlFile(SQLiteDatabase db,String filename){
		try {
		     InputStream in = context.getAssets().open(filename);
 		     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		     String sqlUpdate = null;
		     while ((sqlUpdate = bufferedReader.readLine()) != null) {
 		           if (!TextUtils.isEmpty(sqlUpdate)) {
		        	   db.execSQL(sqlUpdate);
		           }
		     }
		     bufferedReader.close();
		     in.close();
		} catch (Exception e) {
		      e.printStackTrace();
		}  
	}
	/**
	 * ��������
	 */
	public void clearData(){
		String tablename=getDBName();
		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(tablename, null, null);
		db.close();
	}
	
	public abstract String getDBName();
}
