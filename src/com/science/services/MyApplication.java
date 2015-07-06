package com.science.services;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.igexin.sdk.PushManager;

import com.science.activity.DocumentExpressActivity;
import com.science.activity.MainActivity;

import com.science.activity.Android_DialogActivity.MyThread;

import com.science.json.JsonCheckUpdateHandler;
import com.science.json.JsonGetMyInfoHandler;
import com.science.json.JsonLoginHandler;
import com.science.model.ResourceDefine;
import com.science.model.User;
import com.science.util.DefaultUtil;
import com.science.util.FileAccess;
import com.science.util.Url;

import android.R.string;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application{

	public static String loginurl=null;
	public static String loginState=null;
	public  static String sidString="";
	public static String result=null;
	public static JsonLoginHandler jsonLoginHandler=new JsonLoginHandler();
	public static Map<String,String> my_keywords = null;
	static MyApplication instance;
	public static  String [] keywords=null;
	public static boolean hasLauncher = false;;
	public static String user_name = DefaultUtil.EMPTY;	//存储当前的用户名
	public static String password = "";//最后肯定要改成加密的形式
    public static String nickname = "";
    public static User   user ;
    public static SoftReference<Bitmap> user_avatar;
	public static SharedPreferences shared_prefs;
	public static SharedPreferences user_info_prefs;
	public static Editor            editor;
	public static Editor            user_info_editor;
	public static String eid;
	public static String cid;
	public  static List<StringBuffer> non_null_keywords_list = new ArrayList<StringBuffer>();
	/*存储当前版本的号*/
	public static int local_version = 1;
	public static int server_version = 1;
	public static String local_version_name = "";
	public static String server_version_name = "";
	public static Map<String,Object> version_info;
	
	
	
	public static final int CHECK_UPDATE_OK = 0;
	public static final int CHECK_UPDATE_HAVE_NEW_VERSION = 1;
	public static final String platform1 = "http://123.57.207.9/";
	public static final String platform2 = "http://123.56.155.78/";
	
	public static String platform;//用来平台,也就是是URL
	
	
	
	public static MyApplication getInstance() {
	return	instance;

	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (keywords==null) {
			keywords=new String[8];
			for (int i = 0; i < keywords.length; i++) {
				keywords[i]="";
			}
		}
		instance= this;
		hasLauncher = true;
		
		
		/*生成ResourceDefine 很重要*/
		ResourceDefine.deriveResourceDefine(getApplicationContext());

		eid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID); 
        
		
		/*获取版本号*/
		local_version = getVersionCode();
		local_version_name = getVersionName();
		shared_prefs = getSharedPreferences("data_base",Activity.MODE_PRIVATE);
		
		if(shared_prefs != null){
			
			editor = shared_prefs.edit();
			platform = shared_prefs.getString("platform",platform1 );

			
		}
		

        
		//SharedPreferences mySharedPreferences= getSharedPreferences("LoginInfo", Activity.MODE_PRIVATE); 
		//user_name = mySharedPreferences.getString("name", "");
		//password = mySharedPreferences.getString("pass", "");

		//user_info_prefs= getSharedPreferences("LoginInfo", Activity.MODE_PRIVATE); 
		//user_info_editor = shared_prefs.edit();
		user_name = shared_prefs.getString("name", "");
		password = shared_prefs.getString("password", "");
		sidString = shared_prefs.getString("sid", "");

		//initUser();
		
		checkVersionUpdate();
	}

	public  String Login(String name,String pass)
	{
		if (name==null||pass==null) {
			return null;
		}
		else {
			loginurl=Url.LOGINURL;
			loginurl+="name=";
			loginurl+=name;
			loginurl+="&";
			loginurl+="pass=";
			loginurl+=pass;
			
			URL url;
			try {
				url = new URL(loginurl);
				Log.v("MyApplicationLogin", loginurl);
				URLConnection con;
				con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				sidString=jsonLoginHandler.getListItems(input);
				user_name = name;	
				password = pass;
				editor.putString("sid", sidString);
				return sidString;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
//			MyThread myThread=new MyThread();
//			new Thread(myThread).start();
		}
		
		return null;
	}
	
	
	
	
	
	public static void Logout(){
        

		if(keywords != null)
		{
			for(int i = 0;i < keywords.length;i++)
				keywords[i] = "";
		}
		sidString = null;
		//my_keywords.clear();
		if(non_null_keywords_list != null)
		non_null_keywords_list.clear();
		
	}
	
	
	public int getVersionCode(){
		
		int verCode = -1;
		try {
			verCode = this.getApplicationContext().getPackageManager().getPackageInfo("com.example.science", 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return verCode;
	}
	
	
	public String getVersionName(){
		
		String versionName = "";
		try {
			versionName = this.getApplicationContext().getPackageManager().getPackageInfo("com.example.science", 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return versionName;
	}
	
	
	
	public static void  checkVersionUpdate(){
		
		new Thread(new CheckUpdateThread()).start();
	}
	
	
	
	
	public  String ComposeToken(String str)
	{
		if (str!=null) {
			if (str.charAt(str.length()-1)!='?') {
				str+="&";
			}
			str+="sid=";
			str+=this.sidString;
			str+="&";
			str+="eid=";
			str+=this.eid;
			return str;
		}
		else {
			return null;
		}
	}
	
	public  Boolean IsLogin()
	{
		if (sidString!=null&&!sidString.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void  updateNonNullKeywordsList()
	{
		non_null_keywords_list.clear();
		for(int i = 0;i < keywords.length;i++)
		{
			if(!keywords[i].isEmpty())
			{
				StringBuffer sb = new StringBuffer(keywords[i]);
				non_null_keywords_list.add(sb);
			}
		}
	}
	
	
	
	public class MyThread implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(loginurl);
				Log.v("MyApplicationLogin", loginurl);
				URLConnection con;
				con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				sidString=jsonLoginHandler.getListItems(input);
				//Log.v("MyApplication", sidString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				
			}
		}
		
	}
	
	
	public static  Handler handler = new Handler(){
	
		@Override
		public void handleMessage(Message msg){
			
			if(msg.what == CHECK_UPDATE_OK)
			{
				server_version = (Integer) version_info.get("version_id");
				server_version_name = version_info.get("version_name").toString();
				if(local_version < server_version){
					
					//Toast.makeText(getApplicationContext(), "有新版本更新", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	
	
	
	

	
	
	
	
	public  static class CheckUpdateThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String str_url = Url.composeCheckUpdateUrl(MyApplication.local_version);
			try {
				URL url = new URL(str_url);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				JsonCheckUpdateHandler json = new JsonCheckUpdateHandler();
				Map<String,Object> temp = json.getItemList(is);
				if(temp != null){
					
					version_info = temp;
					handler.sendEmptyMessage(CHECK_UPDATE_OK);
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public static void initUser(){
		user = new User();
		if(sidString != null && !sidString.isEmpty()){
		     
		     user.sidString = shared_prefs.getString("sid", "");
		     user.userIdentity = shared_prefs.getString("userIdentity", "");
		     user.userArea = shared_prefs.getString("userArea", "");
		     user.userName = user_name;
		     user.userNickName = shared_prefs.getString("userNickName", "");
		     user.userAvatar = new SoftReference<Bitmap>(FileAccess.getBitmapByFolder("/sdcard/science_pie/" ,MyApplication.user_name + ".jpg"));
		     user.userEmail = shared_prefs.getString("userEmail", "");
		     user.userOrganization = shared_prefs.getString("userOrganization", "");
		     
		}

	}
	
	
	
	
	
	public static void initUserInfoFromRemote(final Handler handler,final int msg){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				URL url;
				String my_info_str_url = Url.composeGetMyInfoUrl();
				try {
					url = new URL(my_info_str_url);
					URLConnection conn = url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					JsonGetMyInfoHandler json = new JsonGetMyInfoHandler();
					List<Map<String,Object>> temp = json.getListItems(is);
					if(temp != null){
						Map<String,Object>  userInfo= temp.get(0);
						user.userNickName = (String)userInfo.get("nickname");
						user.userArea = (String) userInfo.get("area");
						user.userIdentity = (String) userInfo.get("identity");
						user.userOrganization = (String) userInfo.get("organization");
						user.userEmail = (String) userInfo.get("email");
						String avatarUrl = (String) userInfo.get("img_url");
	
				        try {
				        	
							URL url1 = new URL(avatarUrl);  
					        HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();  
					        conn1.setConnectTimeout(10 * 1000);  
							conn1.setRequestMethod("GET");
					        if(conn1.getResponseCode() == HttpURLConnection.HTTP_OK){  
					            //return conn.getInputStream(); 
					          Bitmap  mBitmap = BitmapFactory.decodeStream(conn1.getInputStream());
					          user.userAvatar = new SoftReference<Bitmap>(mBitmap);
					         //Log.i("jjjjjjjj", avatarUrl);
					          FileAccess.saveBitmapToFile(mBitmap, "/sdcard/science_pie/" + MyApplication.user_name + ".jpg");
					            
					        } 
						} catch (ProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						changeUserInfo();
						handler.sendEmptyMessage(msg);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}).start();
	}
	
	
	
	
	
	public static void changePlatform(){
		
		if(platform.equals(platform1)){
			
			platform = platform2;
		}
		else if(platform.equals(platform2)){
			platform = platform1;
		}
		
		
		Toast.makeText(getInstance(), "成功切换到平台" + platform, Toast.LENGTH_SHORT).show();
		
		
		Url.BASEURL = platform;
		if(shared_prefs != null){
			editor.putString("platform", platform);
		}
		editor.commit();
	}
	
	
	public static void changeUserInfo(){
		
		editor.putString("name", user_name);
		editor.putString("password", password);
		editor.putString("sid", sidString);
		editor.putString("userNickName", user.userNickName);
		editor.putString("userIdentity", user.userIdentity);
		editor.putString("userOrganization", user.userOrganization);
		editor.putString("userEmail", user.userEmail);
		editor.putString("userArea", user.userArea);
		editor.commit();
	}
	
	
	public  void uploadDeviceId(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String strUrl = Url.composeUploadDevideId(MyApplication.eid);
				try {
					URL url = new URL(strUrl);
					URLConnection conn = url.openConnection();
					conn.connect();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}
	
	
	
	public  void uploadCid(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String cidString=PushManager.getInstance().getClientid(MyApplication.getInstance().getBaseContext());
					MyApplication.cid = cidString;
					URL url = new URL(Url.composeUploadCid(cidString));
					URLConnection con = url.openConnection();
					con.connect();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}
	
	
	
    
	
	
}
