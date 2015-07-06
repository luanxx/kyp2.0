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

public class JsonCheckUpdateHandler {

	private Map<String,Object> version_info;
	
	public JsonCheckUpdateHandler(){
		
		version_info = new HashMap<String,Object>();
	}
	
	
	
	public Map<String,Object> getItemList(InputStream is){
		
		Reader reader;
		try {
			reader = new InputStreamReader(is, "UTF-8");
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
	        String codeString=obj.getString("code");
	        if (codeString.equals("200")) {
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
	            JSONObject tteempJsonObject = (JSONObject) tempJsonObject.get("check.list");
		        	//Map<String, Object> map=new HashMap<String, Object>();
		        	version_info.put("version_id", tteempJsonObject.getInt("version_id"));
		        	version_info.put("version_content",tteempJsonObject.getString("version_content"));
		        	version_info.put("version_date", tteempJsonObject.getString("version_date"));
		        	version_info.put("dlurl", tteempJsonObject.getString("dlurl"));
                    version_info.put("version_name", tteempJsonObject.getString("version_name"));
		        	return version_info;
	        }
		       
			
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
