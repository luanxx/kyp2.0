package com.science.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.science.database.DBManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ShoucangUtil {
	
	private Context context;
    private final int ADD = 1;
    private final int DELETE = 2;
    
	private  final int RESULT_OK = 200;
	private  final int RESULT_FAIL = 400;
	
	public static final int RESULT_ADD_OK = 1000;
	public static final int RESULT_ADD_FAIL = 1001;
	public static final int RESULT_DELETE_OK = 1002;
	public static final int RESULT_DELETE_FAIL  = 1003;
	private OnShoucangListener listener;
	
	private  DBManager db_manager;
	
	public ShoucangUtil(Context context)
	{
		this.context = context;
		this.db_manager = new DBManager(context);
	}
	
	public void setOnShoucangListener(OnShoucangListener listener)
	{
		this.listener = listener;
	}
	
	
	public void addShoucang(String article_type,int article_id,String url,String title)
	{
		String str_url  = Url.composeAddShoucangUrl(article_type, article_id, url, title);
		Log.i("str_url_shoucang", str_url);
		//Log.i("add_shoucang_url", str_url);
		new Thread(new ShoucangRunnable(str_url,ADD)).start();
	}
	
	public void deleteShoucang(String article_type,int article_id)
	{
		String str_url  = Url.composeDeleteShoucangUrl(article_type,article_id);
		new Thread(new ShoucangRunnable(str_url,DELETE)).start();
	}
	
	
	
	
	
	
	private class ShoucangRunnable implements Runnable{

		private String str_url;
		private int    action;//表明是删除还是添加
		public ShoucangRunnable(String str_url,int action)
		{
			this.str_url = str_url;
			this.action = action;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(str_url);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				InputStreamReader reader = new InputStreamReader(is,"UTF-8");
				BufferedReader buf_reader = new BufferedReader(reader);
				StringBuffer sb = new StringBuffer();
				String str;
				while((str = buf_reader.readLine()) != null)
				{
					sb.append(str);
				}
				
				String str_temp = sb.toString();
				JSONObject obj = new JSONObject(str_temp);
				int a = str_temp.indexOf("{");
				str_temp = str_temp.substring(a);
				int code = Integer.parseInt(obj.getString("code"));
				if(action == ADD)
				{
					if(code == RESULT_OK)
					handler.sendEmptyMessage(RESULT_ADD_OK);
//					else
//					handler.sendEmptyMessage(RESULT_ADD_FAIL);
				}
				if(action == DELETE)
				{
					if(code == RESULT_OK)
					handler.sendEmptyMessage(RESULT_DELETE_OK);
					else
					handler.sendEmptyMessage(RESULT_DELETE_FAIL);
				}
						
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what){
			case RESULT_ADD_OK:
				listener.onShoucang(ShoucangUtil.RESULT_ADD_OK);
				break;
			case RESULT_ADD_FAIL:
				listener.onShoucang(ShoucangUtil.RESULT_ADD_FAIL);
				break;
			case RESULT_DELETE_OK:
				listener.onShoucang(ShoucangUtil.RESULT_DELETE_OK);
				break;
			case RESULT_DELETE_FAIL:
				listener.onShoucang(ShoucangUtil.RESULT_DELETE_FAIL);
			}
		}
	};
	
	
	
	/**
	 * 
	 * @param type 表示文章的类型
	 * @param id  表示文章的id
	 * @param title 文章的标题
	 * @param description  文章的表述，一般是时间
	 * @param url          文章的链接
	 */
	public  void addToLocalShoucang(String account,int type,int id,String title,String description,String url){
		
		db_manager.addOneCollection(account,type, id, title, description, url);
	}
	
	
	public  void dropFromLocalShoucang(String account,int type,int id){
		db_manager.dropOneCollection(account,type, id);
	}
	
	
	
	public  List<Map<String,Object>> getLocalShoucangList(){
		
		List<Map<String,Object>> list = db_manager.queryAllCollection();
		return list;
	}
	
	
	
	
	
	public  boolean containInShoucang(int article_type,int article_id){
		
		
		List<Map<String,Object>> list = getLocalShoucangList();
		if(list == null)
			return false;
		for(Map<String,Object> map : list){
			
			if((Integer)map.get("article_type") == article_type
					&&(Integer)map.get("article_id") == article_id)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public interface OnShoucangListener{
		
		public void onShoucang(int result);
		
	}
	
	
	
}
