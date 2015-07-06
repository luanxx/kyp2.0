package com.science.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.science.R;

import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.util.Url;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class CoopDetailMessageActivity extends Activity {

	private EditText message_et;
	private MyImageButton send_btn;
	private int receive_id;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.coop_detail_message);
		initViews();
		receive_id = this.getIntent().getIntExtra("receive_id", 0);
		
		
		
	}
	public void initViews()
	{
		message_et = (EditText)findViewById(R.id.coop_detail_message);
		send_btn = (MyImageButton)findViewById(R.id.coop_detail_send);
		send_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message_str = message_et.getText().toString();
				if(message_str!=null)
            	{
            		
            		String str_url = Url.composeSendMessageUrl(receive_id, message_str);
            	    new Thread(new SendMessageThread(str_url)).start();
            	}
            	else Toast.makeText(getApplicationContext(), "您还没有填写哦", 0).show();

			}
			
		});
	}
	 private class SendMessageThread implements Runnable{

			private String str_url;
			public SendMessageThread(String url){
				
				this.str_url = url;
			}
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
					int code = Integer.parseInt(obj.getString("code"));
                     System.out.println("codeddd"+code+"");
					if(code == 200)
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								 Toast.makeText(CoopDetailMessageActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
								  Intent intent = new Intent(CoopDetailMessageActivity.this,CoopDetailActivity.class);
								  startActivity(intent);
							}
							
						});
					else
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								 Toast.makeText(CoopDetailMessageActivity.this, "发送失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();
								 Intent intent = new Intent(CoopDetailMessageActivity.this,CoopDetailActivity.class);
								  startActivity(intent);
							}
							
						});
						
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
			
		};
}
