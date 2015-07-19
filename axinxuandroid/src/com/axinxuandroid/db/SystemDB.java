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
     * 数据库的版本是一个整型值，在创建SQLiteOpenHelper时，
     * 会传入该数据库的版本，如果传入的数据库版本号比数据库文件中存储的版本号大的话，
     * 那么SQLiteOpenHelper#onUpgrade()方法就会被调用，我们的升级应该在该方法中完成。

     */
	public final static int DATABASE_VERSION = 52; 
	private Context context;
 	public SystemDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
 	}
	
 	 
 	private void createAllTable(SQLiteDatabase db){
 		//T_android_Record_Resource 记录资源
 		String sql = "CREATE TABLE IF NOT EXISTS T_android_Record_Resource (id INTEGER primary key autoincrement, "
 			+    "neturl  text,localurl text, type INTEGER, recordid INTEGER , info text ,state INTEGER,publishstate INTEGER)";
  	    db.execSQL(sql);
  	    //T_android_Record 记录
  	   sql = "CREATE TABLE IF NOT EXISTS T_android_Record (id INTEGER primary key autoincrement, "
		+  "phone text, user_id text, labelname text, nick_name text,batch_id INTEGER, record_id INTEGER, trace_code text, recordtext text, save_type INTEGER, send_date text, save_date text, type INTEGER,state INTEGER,variety text,variety_id INTEGER,user_type INTEGER,villeage_id INTEGER,user_img text,isdel INTEGER,lastoptime text,savestatus INTEGER ,lng text,lat text)";
	    db.execSQL(sql);
	     
	    //T_android_Link 环节标签
	    sql = "CREATE TABLE IF NOT EXISTS T_android_Batch_Label (id INTEGER primary key autoincrement, "
		+   "variety_id INTEGER, label_name text,isdel INTEGER,lastoptime text,recordid INTEGER)";
        db.execSQL(sql);
        //T_android_User 用户
        sql = "CREATE TABLE IF NOT EXISTS T_android_User (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER, phone text, pwd text, user_name text, email text,  create_time text, login_time text, person_desc text,person_imageurl text,local_imgurl text,worktime text,address text,sex text)";
        db.execSQL(sql);
      //T_android_OAuthAccount OAuth账号
        sql = "CREATE TABLE IF NOT EXISTS T_android_OAuthAccount (id INTEGER primary key autoincrement, "
		+   "userid INTEGER, account_id text, type INTEGER, authorizeimg text, nick_name text)";
        db.execSQL(sql);
        //T_android_Villeage 农场
        sql = "CREATE TABLE IF NOT EXISTS T_android_Villeage (id INTEGER primary key autoincrement, "
		+   "villeage_id INTEGER, villeage_name text, collect_code text, manager_name text, tele text, address text,bulid_time text,scale text,type INTEGER,manage_scope text,villeage_desc text,lat text,lng text,user_id INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_User_Villeage 用户农场
        sql = "CREATE TABLE IF NOT EXISTS T_android_User_Villeage (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER,villeage_id INTEGER,role INTEGER)";
        db.execSQL(sql);
        //T_android_Batch 批次
        sql = "CREATE TABLE IF NOT EXISTS T_android_Batch (id INTEGER primary key autoincrement, "
		+   "batch_id INTEGER,variety_id INTEGER,villeage_id INTEGER, variety text, producecount INTEGER, code text,buylink text,status INTEGER,stage INTEGER,consumercount INTEGER,create_time text,order_index INTEGER,order_date INTEGER,villeage_name text,isdel INTEGER,lastoptime text,categoryid INTEGER,userid INTEGER)";
        db.execSQL(sql);
      //T_android_Template 模板
        sql = "CREATE TABLE IF NOT EXISTS T_android_Template (id INTEGER primary key autoincrement, "
		+   "template_id INTEGER,category_id INTEGER,label_name text, head text, foot text, sign text,context text,version INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
      //T_android_Proust 普鲁斯特问卷
        sql = "CREATE TABLE IF NOT EXISTS T_android_Proust (id INTEGER primary key autoincrement, "
		+   "user_id INTEGER,answer text,question text,proust_id INTEGER,create_date text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
      //T_android_Category 品种种类表
        sql = "CREATE TABLE IF NOT EXISTS T_android_Category (id INTEGER primary key autoincrement, "
		+   "category_name text,parentid INTEGER,cate_order INTEGER,clevel INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_Variety 品种表
        sql = "CREATE TABLE IF NOT EXISTS T_android_Variety (id INTEGER primary key autoincrement, "
		+   "variety_id INTEGER,variety_name text,category_id INTEGER,villeage_id INTEGER,user_id INTEGER,create_time text,variety_desc text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        
      //T_android_VilleageBanner 农场banner
        sql = "CREATE TABLE IF NOT EXISTS T_android_VilleageBanner (id INTEGER primary key autoincrement, "
		+   "bannerid INTEGER,villeage_id INTEGER,neturl text,localurl text,thumb_url text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
      //T_android_Intelligence 农场资质
        sql = "CREATE TABLE IF NOT EXISTS T_android_Intelligence (id INTEGER primary key autoincrement, "
		+   "intelligenceid INTEGER,villeage_id INTEGER,label_name text,intelligence_desc text,thumbnail_url text,create_time text,isdel INTEGER,lastoptime text,localurl text,intelligence_imgurl text)";
        db.execSQL(sql);
        //T_android_UserFavorite 用户收藏
        sql = "CREATE TABLE IF NOT EXISTS T_android_UserFavorite (id INTEGER primary key autoincrement, "
		+   "favid INTEGER,label_name text,favorite_type INTEGER,favorite_id INTEGER,user_id INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_VilleagePhoto 农场相册
        sql = "CREATE TABLE IF NOT EXISTS T_android_VilleagePhoto (id INTEGER primary key autoincrement, "
		+   "photoid INTEGER,label_name text,image_url text,image_desc text,image_name text,villeage_id INTEGER,index_order INTEGER,thumbnail_url text,localurl text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        //T_android_SystemNotice 系统通知
        sql = "CREATE TABLE IF NOT EXISTS T_android_SystemNotice (id INTEGER primary key autoincrement, "
		+   "type INTEGER,notice text,jsondata text)";
        db.execSQL(sql);
        
      //T_android_SystemLog 系统日志
        sql = "CREATE TABLE IF NOT EXISTS T_android_SystemLog (id INTEGER primary key autoincrement, "
		+   "actiontime text,message text,extrainfo text)";
        db.execSQL(sql);
         
      //T_android_SystemSet 系统设置
        sql = "CREATE TABLE IF NOT EXISTS T_android_SystemSet (id INTEGER primary key autoincrement, "
		+   "type INTEGER,setjson text)";
        db.execSQL(sql);
        //T_android_Comment 评论
        sql = "CREATE TABLE IF NOT EXISTS T_android_Comment (id INTEGER primary key autoincrement, "
		+   "comment_id INTEGER,user_name text,context text,comment_date text,user_img text,parent_id INTEGER,user_id INTEGER,recordid INTEGER,isdel INTEGER,lastoptime text,replyUserName text,replyUserId INTEGER,varietyName text,batchCode text)";
        db.execSQL(sql);
        //T_android_UserVisitHistory 用户浏览记录
        sql = "CREATE TABLE IF NOT EXISTS T_android_UserVisitHistory (id INTEGER primary key autoincrement, "
		+   "userid INTEGER,type INTEGER,visitobjid INTEGER,visittime text)";
        db.execSQL(sql);
        
        //T_android_CategoryProcedure 品种生产过程表
        sql = "CREATE TABLE IF NOT EXISTS T_android_CategoryProcedure (id INTEGER primary key autoincrement, "
		+   "categoryprocedureid INTEGER,category_id INTEGER,name text,index_order INTEGER,create_time text,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        
      //T_android_NetUpdateRecord 数据更新记录
        sql = "CREATE TABLE IF NOT EXISTS T_android_NetUpdateRecord (id INTEGER primary key autoincrement, "
		+   "type INTEGER,objid INTEGER,statusjson text)";
        db.execSQL(sql);
        
      //T_android_System_Label 系统标签
        sql = "CREATE TABLE IF NOT EXISTS T_android_System_Label (id INTEGER primary key autoincrement, "
		+   "label_id INTEGER,label_name text,label_imageurl text,type INTEGER,module_id INTEGER, index_order INTEGER)";
        db.execSQL(sql);
        
        //T_android_Advocate 用户赞
        sql = "CREATE TABLE IF NOT EXISTS T_android_Advocate (id INTEGER primary key autoincrement, "
		+   "userid INTEGER,recordid INTEGER,isdel INTEGER,lastoptime text)";
        db.execSQL(sql);
        
        
        //T_android_SecurityCodeBatch 用户防伪批次
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
	 * 初始化数据
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
	 * 清理数据
	 */
	public void clearData(){
		String tablename=getDBName();
		SQLiteDatabase db = this.getWritableDatabase();
 		db.delete(tablename, null, null);
		db.close();
	}
	
	public abstract String getDBName();
}
