package com.science.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.science.model.Comment;
import com.science.util.DefaultUtil;

import android.util.Log;

public class JsonCommentListHandler {

	public String json=null;
	public String codeString=null;
	public String messageString=null;
	public String reString=null;
	public List<Comment> list=null;
	

	public JsonCommentListHandler(String str)
	{
		json=str;
		list=new ArrayList<Comment>();
	}
	
	public JsonCommentListHandler()
	{
		list=new ArrayList<Comment>();
	}
	
	public void SetJsonHotPage(String str)
	{
		json=str;
		list=new ArrayList<Comment>();
	}
	
	public List<Comment> getList()
	{
		return null;
	}
	
	public List<Comment> getListItems(InputStream input)
	{
		
		Reader reader;
		try {
			reader = new InputStreamReader(input, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
	        String str = null;  
	        StringBuffer sb = new StringBuffer();  
	        while ((str = bufferedReader.readLine()) != null) {  
	            sb.append(str);  
	        }  
	        
	        String strTemp=sb.toString();
	        Log.i("comment_list_json", sb.toString());
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        codeString=obj.getString("code");
	        Log.v("test",codeString);
	        messageString=obj.getString("message");
	        if (codeString.equals("200")) {
	        	Log.i("test", "parse json success");
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
		        JSONArray array = tempJsonObject.getJSONArray("Comment.list");
		        
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	
		        	//评论按照三级--二级--一级顺序显示
//		        	CommentUtil fir_comment = new CommentUtil(temp.getString(CommentUtil.customername),
//		        			                                  temp.getString(CommentUtil.content),
//		        			                                  temp.getString(CommentUtil.time),
//		        			                                  temp.getInt(CommentUtil.commentid),
//		        			                                  temp.getInt(CommentUtil.customerid));//第一级评论
//		        	CommentUtil sec_comment = new CommentUtil(temp.getString(CommentUtil.sec_commentname),
//		        			                                  temp.getString(CommentUtil.sec_content),
//		        			                                  temp.getString(CommentUtil.time),
//		        			                                  DefaultUtil.INAVAILABLE,
//		        			                                  DefaultUtil.INAVAILABLE
//		        			                                  );
//		        	CommentUtil thr_comment = new CommentUtil(temp.getString(CommentUtil.thir_commentname),
//		        			                                  temp.getString(CommentUtil.thir_content),
//		        			                                  temp.getString(CommentUtil.time),
//		        			                                  DefaultUtil.INAVAILABLE,
//		        			                                  DefaultUtil.INAVAILABLE
//		        			                                  );
		        	
		        	
		        	
		        	Comment comment = new Comment(temp);
		        	
//		        	map.put(CommentUtil.commentid, temp.getInt(CommentUtil.commentid));
//		        	map.put(CommentUtil.customerid, temp.getInt(CommentUtil.customerid));
//		        	map.put(CommentUtil.customername, temp.getString(CommentUtil.customername));
//		        	map.put(CommentUtil.time, temp.getString(CommentUtil.time));
//		        	map.put(CommentUtil.sec_time, temp.getString(CommentUtil.sec_time));
//		        	map.put(CommentUtil.thir_time, temp.getString(CommentUtil.thir_time));
//		        	map.put(CommentUtil.sec_commentname,temp.getString(CommentUtil.sec_commentname));
//		        	map.put(CommentUtil.sec_content, temp.getString(CommentUtil.sec_content));
//		        	map.put(CommentUtil.thir_commentname, temp.getString(CommentUtil.thir_commentname));
//		        	map.put(CommentUtil.thir_content, temp.getString(CommentUtil.thir_content));
//		        	map.put(CommentUtil.content, temp.getString(CommentUtil.content));
		        	
		        	//解析时间
//					String sec_time = (String) map.get(CommentUtil.sec_time);
//					String thir_time = (String) map.get(CommentUtil.thir_time);
//					String time = (String)map.get(CommentUtil.content);
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMMM-dddd HH:mm:ss");
//					Date date = new Date();
//					
//					
//					
//		        	if(!thir_time.equals(DefaultUtil.EMPTY))//如果第三级时间为非空
//		        	{
//		        		date = sdf.parse(thir_time);	
//		        	}
//		        	
//		        	
//		        	if(!sec_time.equals(DefaultUtil.EMPTY))
//		        	{
//		        		if(thir_time.equals(DefaultUtil.EMPTY))
//		        			date = sdf.parse(sec_time);		  		
//		        	}
//		        	
//		        	if(!time.equals(DefaultUtil.EMPTY))
//		        	{
//		        		if(sec_time.equals(DefaultUtil.EMPTY))
//		        			date = sdf.parse(time);
//		        	}
		        	
		        	
		        	
		        	//map.put("main_time", date);
		        	
		        	list.add(comment);
		        	
		        }
		        return list;
			}
	        else
	        	return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	
		return null;
	}
	
	
}
