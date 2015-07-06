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

import com.science.activity.DocumentExpressActivity;

import android.util.Log;

public class JsonDcumentListHandler {
	public String json=null;
	public String codeString=null;
	public String messageString=null;
	public String reString=null;
	public List<Map<String, Object>> list=null;
	public int type ;
	public JsonDcumentListHandler(String str)
	{
		
		list=new ArrayList<Map<String,Object>>();
	}
	
	public JsonDcumentListHandler(int type)
	{
		this.type = type;
		list=new ArrayList<Map<String,Object>>();
	}
	
	public JsonDcumentListHandler()
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
	        
	        String strTemp=sb.toString();
	        Log.i("tempstring", strTemp.toString());
	        int a=strTemp.indexOf("{");
	        strTemp=strTemp.substring(a);
	        JSONObject obj = new JSONObject(strTemp);
	        codeString=obj.getString("code");
	        messageString=obj.getString("message");
	        if (codeString.equals("200")) {
	        	
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
		        JSONArray array = tempJsonObject.getJSONArray("core.list");
	        	/*中文文献*/
	        	if(type == DocumentExpressActivity.DOC_CHI){
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	map.put("id", temp.getInt("cnki_id"));
		        	map.put("title",temp.getString("name"));
		        	map.put("diggtop", temp.getInt("diggtop"));
		        	map.put("pdate",temp.getString("pdate"));
		        	map.put("plnum", temp.getInt("plnum"));
		        	map.put("url", temp.getString("url"));
		        	map.put("articleType", temp.getString("articleType"));
		        	list.add(map);
		        }
	        	}
	        	
	        	
	        	/*英文文献*/
	        	else if(type == DocumentExpressActivity.DOC_ENG){
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	map.put("id", temp.getInt("sci_id"));
		        	map.put("title",temp.getString("name"));
		        	map.put("diggtop", temp.getInt("diggtop"));
		        	map.put("pdate",temp.getString("pdate"));
		        	map.put("plnum", temp.getInt("plnum"));
		        	map.put("url", temp.getString("url"));
		        	map.put("articleType", temp.getString("articleType"));
		        	list.add(map);
		        }
	        	}
		        
		        /*work文献*/
		        else if(type == DocumentExpressActivity.DOC_WORK){
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	map.put("id", temp.getInt("work_id"));
		        	map.put("title",temp.getString("name"));
		        	map.put("diggtop", temp.getInt("diggtop"));
		        	map.put("pdate",temp.getString("pdate"));
		        	map.put("plnum", temp.getInt("plnum"));
		        	map.put("url", temp.getString("url"));
		        	map.put("articleType", temp.getString("articleType"));
		        	list.add(map);
		        }
		        }
		        
		        else if(type == DocumentExpressActivity.DOC_NSF){
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	Map<String, Object> map=new HashMap<String, Object>();
		        	map.put("id", temp.getInt("nsf_id"));
		        	map.put("title",temp.getString("name"));
		        	map.put("diggtop", temp.getInt("diggtop"));
		        	map.put("age",temp.getString("awardExpirationDate"));
		        	map.put("effective_date", temp.getString("awardEffeciveDate"));
		        	map.put("plnum", temp.getInt("plnum"));
		        	map.put("url", temp.getString("url"));
		        	map.put("articleType", temp.getString("articleType"));
		        	map.put("pdate", temp.getString("pdate"));
		        	list.add(map);
		        }
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
