package com.science.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.science.json.JsonLoginHandler;

import android.R.string;

public class LoginManager {
	
	public static String userName=null;
	public static String userPass=null;
	public static String loginState=null;
	public static String sidString=null;
	public static String result=null;
	public static JsonLoginHandler jsonLoginHandler=new JsonLoginHandler();
	
	
	public static String Login()
	{
		if (userName==null||userPass==null) {
			return null;
		}
		else {
			String strUrlString=Url.LOGINURL+userName;
			strUrlString+="&";
			strUrlString+="pass=";
			strUrlString+=userPass;
			

			try {
				URL url = new URL(strUrlString);
//				Log.i("NewsListUrl", DataUrlKeys.NEWS_LIST_URL + pass);
				URLConnection con;
				con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				sidString=jsonLoginHandler.getListItems(input);
				
				return sidString;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				return null;
			}

		}
	}
}
