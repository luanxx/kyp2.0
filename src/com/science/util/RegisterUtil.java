package com.science.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class RegisterUtil {

	public static RegisterUtil reg = null;
	public static final int RESULT_OK = 200;//注册成功
	public static final int RESULT_REDUPLICATED = 400;//用户存在
	public static final int RESULT_FAIL = -1;
	private int code = RESULT_FAIL;//返回的状态吗
	
	private RegisterUtil(){
		
	}
	
	public static RegisterUtil getInstance(){
		if(reg == null)
			reg = new RegisterUtil();
		return reg;
	}
	
	public  int register(final Context context,String nickname,String password,String en_password,
			             String email,final OnRegisterListener listener){
		
		if(nickname.length() < 5)
		{
			Toast.makeText(context,"用户名不能少于5位", Toast.LENGTH_SHORT).show();
			return RESULT_FAIL;
		}
		
		
		if(!password.equals(en_password))
		{
			Toast.makeText(context,"前后密码不一致", Toast.LENGTH_SHORT).show();
			return RESULT_FAIL;
		}
		
		if(!email.contains("@"))
		{
			Toast.makeText(context, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
			return RESULT_FAIL;
		}
		
		String register_url = Url.composeRegisterUrl(nickname, password,email);
		
		
		
		
		Handler handler = new Handler(){
			
			@Override
			public void handleMessage(Message msg){
				
				
				if(msg.what == RESULT_OK)
				{
					listener.onRegister(RESULT_OK);
					Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
				}
				else if(msg.what == RESULT_REDUPLICATED)
				{
					listener.onRegister(RESULT_REDUPLICATED);
					Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
        register(register_url,handler);
		
		
		return code;
		
	}
	
	
	public void register(final String str_url,final Handler handler){
		
		
		
		Runnable register_thread = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					URL url = new URL(str_url);
					Log.i("register_str_url", str_url);
					URLConnection conn = url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					
					InputStreamReader reader = new InputStreamReader(is,"UTF-8");
					BufferedReader buf_reader = new BufferedReader(reader);
					StringBuffer sb  = new StringBuffer();
					String str;
					
					while((str = buf_reader.readLine())!= null)
					{
						sb.append(str);
					}
					
					String str_temp = sb.toString();
					
					JSONObject obj = new JSONObject(str_temp);
					int a = str_temp.indexOf("{");
					str_temp = str_temp.substring(a);
					int code = Integer.parseInt(obj.getString("code"));
				    if(handler != null)
				    	handler.sendEmptyMessage(code);
						
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(NullPointerException e)
				{
					e.printStackTrace();
				}
			}
			
		};
		
		
		
		new Thread(register_thread).start();
		
	}

	
	
	
	public interface OnRegisterListener{
		
		public void onRegister(int result);
	}
}
