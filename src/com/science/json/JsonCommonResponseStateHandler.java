package com.science.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonCommonResponseStateHandler {

	
	int code = 0;
	public JsonCommonResponseStateHandler(){
		
		
	}
	
	
	public int getResponseStateCode(final InputStream is){
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(is,"UTF-8");
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
			code = Integer.parseInt(obj.getString("code"));
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

		
		
		return code;
	}
	
	
	
	
	public void request(final String str_url){
		class GetResponseStateThread implements Runnable{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				URL url;
			
					try {
						url = new URL(str_url);
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
						code = Integer.parseInt(obj.getString("code"));
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
	}
	
	
	
}
