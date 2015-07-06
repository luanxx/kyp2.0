package com.science.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 查询�?有的收藏记录
	 * 
	 * @return 携带�?有收藏记录的Cursor
	 */
	public Cursor queryAllCollections() {
		Cursor c = db.rawQuery("SELECT * FROM collection;", null);
		return c;
	}

	
	
	
	/**
	 * 执行�?次带条件的查�?
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @return 条件查询的Cursor
	 */
	public Cursor getExecSqlCursor(String table, String[] columns,
			String selection, String[] selectionArgs) {
		return db.query(table, columns, selection, selectionArgs, null, null,
				null);
	}

	/**
	 * 通过博客的Id来获取博客的作�??
	 * 
	 * @param blogId
	 *            博客的ID
	 * @return 博客的作�?
	 */
	public String getCopyrightByBlogId(String blogId) {
		Cursor c = getExecSqlCursor("blog_copyright",
				new String[] { "copyright" }, "blog_id=?",
				new String[] { blogId });
		while (c.moveToNext()) {
			String tmpCopyright = c.getString(c.getColumnIndex("copyright"));
			return tmpCopyright;
		}
		return "未知";
	}

	/**
	 * 通过报刊具体新闻的Id来获取该文章评论权限情况
	 * 
	 * @param npContentId
	 *            报刊具体新闻的Id
	 * @return 报刊具体新闻的评论权限情�?
	 */
	public String getCanCommentByNpContentId(String npContentId) {
		Cursor c = getExecSqlCursor("newspaper_content_cancomment",
				new String[] { "can_comment" }, "newspaper_content_id=?",
				new String[] { npContentId });
		while (c.moveToNext()) {
			String tmpCanComment = c.getString(c.getColumnIndex("can_comment"));
			return tmpCanComment;
		}
		return "0";
	}

	/**
	 * 向收藏的数据库中添加�?条新的收�?
	 * 
	 * @param type
	 *            该收藏的种类
	 * @param id
	 *            该收藏记录的ID
	 * @param description
	 *            该收藏记录的描述信息
	 * @param imgs
	 *            该收藏记录的图片情况
	 * @param content_name
	 *            该收藏记录的详细内容的html在什么地�?
	 */
	public void addOneCollection(String account,int type, int id, String title,
			String description, String url) {
		String sqlString = "INSERT INTO collection VALUES(?, ?, ?, ?, ?, ?)";
		Object[] projection = new Object[] {account, type, id, title, description, url };
		db.execSQL(sqlString, projection);
	}

	
	
	
	public List<Map<String,Object>> queryAllCollection()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Cursor c = queryAllCollections();
		while(c.moveToNext())
		{
			String account = c.getString(c.getColumnIndex("account"));
			int type = c.getInt(c.getColumnIndex("type"));
			int id = c.getInt(c.getColumnIndex("id"));
			String title = c.getString(c.getColumnIndex("title"));
			String description = c.getString(c.getColumnIndex("description"));
			String url = c.getString(c.getColumnIndex("url"));
			Map map = new HashMap<String,Object>();
			map.put("account", account);
			map.put("article_type", type);
			map.put("article_id", id);
			map.put("title", title);
			map.put("description", description);
			map.put("url", url);
			list.add(map);
		}
		
		return list;
		
	}
	
	
	
	/**
	 * 向存放博客和博客的作者关系的表中添加�?条新的记�?
	 * 
	 * @param blogId
	 *            博客的ID
	 * @param blogCopyright
	 *            博客的作�?
	 */
	public void addCopyrightOfBlog(String blogId, String blogCopyright) {
		String sqlString = "INSERT INTO blog_copyright VALUES(?, ?)";
		Object[] projection = new Object[] { blogId, blogCopyright };
		db.execSQL(sqlString, projection);
	}

	/**
	 * 向存放某�?篇具体报刊内容和它的评论权限的表中添加一条新的记�?
	 * 
	 * @param NpcontentId
	 *            该报刊具体内容的ID
	 * @param canComment
	 *            该报刊的评论权限情况字段
	 */
	public void addNpcontentCancomment(String NpcontentId, String canComment) {
		String sqlString = "INSERT INTO newspaper_content_cancomment VALUES(?, ?)";
		Object[] projection = new Object[] { NpcontentId, canComment };
		db.execSQL(sqlString, projection);
	}

	/**
	 * 在收藏的数据库中删除�?条收藏，在删除收藏的时�?�不要忘了删除这个收藏所保存的对应的图片
	 * 
	 * @param type
	 *            收藏的种�?
	 * @param id
	 *            收藏的ID
	 */
	public void dropOneCollection(String account,int type, int id) {
//		if (type == CollectionItem.TYPE_BLOG) {
//			db.delete("blog_copyright", "blog_id=?", new String[] { id });
//		} else if (type == CollectionItem.TYPE_NEWSPAPER) {
//			db.delete("newspaper_content_cancomment", "newspaper_content_id=?",
//					new String[] { id });
//		}
//		Cursor c = getExecSqlCursor("collection", new String[] { "imgs" },
//				"id=?", new String[] { id });
//		String imgPath = FileAccess.getFolderByCollectionType(type);
//		while (c.moveToNext()) {
//			imgPath += c.getString(c.getColumnIndex("imgs"));
//			Log.i("--collection img path--", imgPath);
//		}
//		FileAccess.deleteFile(imgPath);
		db.delete("collection", "account=? AND type=? AND id=?",
				new String[] { account,String.valueOf(type), Integer.toString(id) });
	}

	/**
	 * 删除�?有的收藏记录
	 */
	public void deleteAllCollections() {
		db.execSQL("delete from collection");
		db.execSQL("delete from blog_copyright");
	}

	/**
	 * 关闭数据�?
	 */
	public void closeDB() {
		if (db != null) {
			db.close();
		}
	}
}
