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

import android.content.Context;
import android.util.Log;

import com.science.model.Cooper;
import com.science.model.Facility;

public class JsonCoopResearchListHandler {

	private String       type;//facility设备，labor表示人力
	private List<Object> list;
	private String                   codeString;
	private String                   messageString;
	private Context                  context;
	public JsonCoopResearchListHandler(){
		
		
	}
	
	
	public JsonCoopResearchListHandler(Context context,String type){
		
		this.context =  context;
		list = new ArrayList<Object>();
		this.type = type;
	}
	
	
	
	
	
	
	public Object getListItems(InputStream input)
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
	        codeString=obj.getString("code");
	        messageString=obj.getString("message");
	        if (codeString.equals("200")) {
	        	
	        	if("labor".equals(type))
	        	{
	        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
		        JSONArray array = tempJsonObject.getJSONArray("laborList");
		        for (int i = 0; i < array.length(); ++i) {
		        	JSONObject temp = (JSONObject) array.opt(i);
		        	
		        	Cooper cooper = new Cooper(context,(Integer)temp.getInt("posterid"),(String)temp.getString("name"),
		        			                   (String)temp.getString("facepicurl"),(String)temp.getString("resurl"),
		        			                   (Integer)temp.getInt("teamSize"),(Integer)temp.getInt("resType"),
		        			                   (Integer)temp.getInt("title"),temp.getString("organization"));
		        	list.add(cooper);
		        }
	        	}
	        	
	        	
	        	else if("facility".equals(type))
	        	{
	        		
		        	JSONObject tempJsonObject=(JSONObject) obj.get("result");
			        JSONArray array = tempJsonObject.getJSONArray("facilityList");
			        for (int i = 0; i < array.length(); ++i) {
			        	JSONObject temp = (JSONObject) array.opt(i);
			        	
			        	JSONArray arr = temp.getJSONArray("images.list");
			        	List<String> imgs_url_list = new ArrayList<String>();
			        	for(int j = 0;j < arr.length();j++){
			        		imgs_url_list.add((String) arr.get(j));
			        	}
			        	Facility faci = new Facility(temp.getString("name"),temp.getString("resurl"),
			        			                     temp.getInt("resType"),temp.getString("model"),
			        			                     temp.getInt("number"),temp.getString("owner"),
			        			                     temp.getInt("posterid"),imgs_url_list);    
 	                    list.add(faci);
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
