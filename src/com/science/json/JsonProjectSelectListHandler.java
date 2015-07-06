package com.science.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonProjectSelectListHandler {
	public String json=null;
	public String codeString=null;
	public String messageString=null;
	public String reString=null;
	public List<Map<String, String>> list=null;
	
	public JsonProjectSelectListHandler(String str)
	{
		json=str;
		list=new ArrayList<Map<String,String>>();
	}
	
	public JsonProjectSelectListHandler()
	{
		list=new ArrayList<Map<String,String>>();
	}
	
	public void SetJsonHotPage(String str)
	{
		json=str;
		list=new ArrayList<Map<String,String>>();
	}
	
	public List<Map<String, String>> getList()
	{
		return null;
	}
	
	public List<Map<String, String>> getListItems(InputStream input)
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
	        
	        Log.v("test", sb.toString());
	        String strTemp=sb.toString();
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        codeString=obj.getString("code");
	        Log.v("test",codeString);
	        messageString=obj.getString("message");
	        if (codeString.equals("200")) {
	        	Log.v("test", "parse json success");
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
		        JSONArray array = tempJsonObject.getJSONArray("project.list");
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, String> map=new HashMap<String, String>();
		        	Log.v("test",temp.getString("title"));
		        	map.put("title", temp.getString("title"));
		        	map.put("startTime",temp.getString("startTime"));
		        	map.put("endTime", temp.getString("endTime"));
		        	map.put("mark", temp.getString("mark"));
		        	list.add(map);
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
