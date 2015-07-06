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

public class JsonGetMyInfoHandler {
	public String json=null;
	public String returnCode=null;
	public String message=null;
	public String reString=null;
	public List<Map<String, Object>> list=null;
	public Map<String,Object> myInfo;
	
	public JsonGetMyInfoHandler(String str)
	{
		json=str;
		list=new ArrayList<Map<String,Object>>();
		myInfo = new HashMap<String,Object>();
	}
	
	public JsonGetMyInfoHandler()
	{
		list=new ArrayList<Map<String,Object>>();
		myInfo = new HashMap<String,Object>();
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
	        
	        String strTemp=sb.toString();
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        returnCode=obj.getString("code");
	        message=obj.getString("message");
	        if (returnCode.equals("200")) {
	        	JSONObject resultJSonObj = obj.getJSONObject("result");
		        JSONArray array = resultJSonObj.getJSONArray("myData.list");
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	
		        	map.put("nickname",temp.getString("nickname"));
		        	map.put("identity", temp.getString("profession"));
		        	map.put("organization", temp.getString("unit"));
		        	map.put("area", temp.getString("location"));
		        	map.put("email", temp.getString("email"));
		        	map.put("img_url", temp.getString("Image"));
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
