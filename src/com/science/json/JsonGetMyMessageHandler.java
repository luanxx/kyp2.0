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

public class JsonGetMyMessageHandler {
	public String json=null;
	public String returnCode=null;
	public String message=null;
	public String reString=null;
	public List<Map<String, Object>> list=null;
	
	
	public JsonGetMyMessageHandler(String str)
	{
		json=str;
		list=new ArrayList<Map<String,Object>>();
	}
	
	public JsonGetMyMessageHandler()
	{
		list=new ArrayList<Map<String,Object>>();
	}
	
	public void SetJsonHotPage(String str)
	{
		json=str;
		list=new ArrayList<Map<String,Object>>();
	}
	
	public List<Map<String, String>> getList()
	{
		return null;
	}
	
	public List<Map<String, Object>> getListItems(InputStream input)
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
	        
	        Log.i("testaaaaaaaa", sb.toString());
	        String strTemp=sb.toString();
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        returnCode=obj.getString("code");
	        Log.v("test",returnCode);
	        message=obj.getString("message");
	        if (returnCode.equals("200")) {
	        	Log.v("test", "parse json success");
		        JSONArray array = obj.getJSONArray("result");
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	map.put("id", temp.getString("id"));
		        	map.put("sender_id",temp.getString("sender_id"));
		        	map.put("receiver_id", temp.getString("receiver_id"));
		        	map.put("message", temp.getString("message"));
		        	map.put("msg_status", temp.getString("msg_status"));
		        	map.put("date", temp.getString("date"));
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
