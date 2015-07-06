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

public class JsonDownLoadsKeywords {
	public String json=null;
	public String codeString=null;
	public String messageString=null;
	public String reString=null;
	public String sidString=null;
	public Map<String, String> map=null;
	
	public JsonDownLoadsKeywords(String str)
	{
		json=str;
		map=new HashMap<String, String>();
	}
	
	public JsonDownLoadsKeywords()
	{
		map=new HashMap<String, String>();
	}
	
	public void SetJsonHotPage(String str)
	{
		json=str;
		map=new HashMap<String, String>();
	}
	
	public List<Map<String, String>> getList()
	{
		return null;
	}
	
	public Map<String, String> getListItems(InputStream input)
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
	        Log.v("Login", sb.toString());
	        String strTemp=sb.toString();
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        codeString=obj.getString("code");
	        Log.v("Login",codeString);
	        messageString=obj.getString("message");
	        if (codeString.equals("200")) {
	        	Log.v("Login", "parse json success");
	        	map.clear();
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
	        	JSONArray array = tempJsonObject.getJSONArray("core.list");
		        for (int i = 0; i < array.length(); ++i) {
		        	String strKeywords=array.opt(i).toString();
		        	if(strKeywords.equals(" ")){
		        		strKeywords = "";
		        	}
		        	map.put(Integer.toString(i), strKeywords);
		        }
		        return map;
			}
	        else
	        {
	        	return null;
	        }
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
