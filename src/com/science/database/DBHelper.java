package com.science.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "my_collection_list.db";
	private static final int DATABASE_VERSION = 1;
	
	private String DBName;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.DBName = DATABASE_NAME;
	}
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.DBName = name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (DATABASE_NAME.equals(this.DBName)) {
			// 存放收藏记录的表,结构 �? type id title description imgs
			String sqlString_createTableCollection = "CREATE TABLE IF NOT EXISTS collection" +  
            "(account VARCHAR PRIMARY_KEY,type INTEGER PRIMARY_KEY, id INTEGER PRIMARY KEY, title VARCHAR, description VARCHAR, url VARCHARs)";
			// 存放博客和博客的作�?�对应关系的表，结构�? blog_id copyright
			String sqlString_createTableBolgCopyright = "CREATE TABLE IF NOT EXISTS blog_copyright"
					+ "(blog_id VARCHAR PRIMARY KEY, copyright VARCHAR)";
			// 存放某篇具体报刊内容的和它的评论权限字段关系的表，结构：newspaper_content_id can_comment
			String sqlString_createTableNewspapercontentCancomment = "CREATE TABLE IF NOT EXISTS newspaper_content_cancomment"
					+ "(newspaper_content_id VARCHAR PRIMARY KEY, can_comment VARCHAR)";
			
			db.execSQL(sqlString_createTableCollection);
			db.execSQL(sqlString_createTableBolgCopyright);
			db.execSQL(sqlString_createTableNewspapercontentCancomment);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
