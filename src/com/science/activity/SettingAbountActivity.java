package com.science.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.science.R;
import com.science.json.JsonCheckUpdateHandler;
import com.science.services.DownloadFile;
import com.science.services.MyApplication;
import com.science.util.Url;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingAbountActivity extends Activity {

	private View check_update_view;
	private View version_info_view;
	private TextView version_name_tv;
	private ProgressDialog pd;
	
	private ImageButton back_ib;
	
	private View feedback_view;
	private EditText editText;
	private String fankui_str;
	private final int UPDATE_PROGRESS = 0;
	private final int DOWNLOAD_APK_OK = 1;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_setting_about);
		initView();
	}
	
	
	public void initView(){
		
		check_update_view = findViewById(R.id.setting_setting_version_check);
		version_name_tv = (TextView) findViewById(R.id.setting_setting_about_version_number);
		version_name_tv.setText("版本号    " + MyApplication.local_version_name);
		check_update_view.setClickable(true);
		check_update_view.setOnClickListener(onClickListener);
		back_ib = (ImageButton)findViewById(R.id.settingback);
		
		back_ib.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
			
		});
		
	//feedback
		feedback_view = findViewById(R.id.setting_setting_fankui);
		feedback_view.setClickable(true);
		feedback_view.setOnClickListener(onClickListener);
	}
	
	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			
			case R.id.setting_setting_fankui:
				
//				LayoutInflater layoutInflater = LayoutInflater.from(SettingAbountActivity.this);
//				View myLoginView = layoutInflater.inflate(R.id.release_experience, null);
				editText = new EditText(SettingAbountActivity.this);
				AlertDialog builder_fankui =new AlertDialog.Builder(SettingAbountActivity.this).setTitle("请输入您要反馈的信息").setIcon(android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("确定", listener).setNegativeButton("取消", null).show(); 
                
				
				break;
			
			
			case R.id.setting_setting_version_check:
				
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingAbountActivity.this);
				
				
                MyApplication.checkVersionUpdate();
				
				
				
				
				if(MyApplication.local_version < MyApplication.server_version)
				{
				builder.setTitle("软件更新");
				builder.setMessage("新版本  ：" + MyApplication.server_version_name);
				builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface v, int arg1) {
						// TODO Auto-generated method stub
					}
				});
				
				
				builder.setPositiveButton("马上更新",new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						new DownloadFile(SettingAbountActivity.this).downFile(MyApplication.version_info.get("dlurl").toString());
					}
					
				});
				}
				else
				{
					
					builder.setTitle("软件更新");
					builder.setMessage("当前已经是最新版本");
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface v, int arg1) {
							// TODO Auto-generated method stub
						}
					});
					
					
					builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
			                 
						}
						
					});
					
					
					
					
				}
				builder.create();
				builder.show();
				break;
			}
		}
		
	};
	
	 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  

	    {  

	        public void onClick(DialogInterface dialog, int which)  

	        {  

	            switch (which)  

	            {  

	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  

	            	fankui_str = editText.getText().toString();
	            	if(fankui_str!=null)
	            	{
	            		
	            		String str_url = Url.composeSubmitFeedbackUrl(fankui_str, MyApplication.eid);
	            	    new Thread(new SubmmitFeedbackThread(str_url)).start();
	            	}
	            	else Toast.makeText(getApplicationContext(), "您还没有填写哦", 0).show();

	                break;  

	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  

	                break;  

	            default:  

	                break;  

	            }  

	        }  

	    };    

	    private class SubmmitFeedbackThread implements Runnable{

			private String str_url;
			public SubmmitFeedbackThread(String url){
				
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

					if(code == 200)
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								 Toast.makeText(SettingAbountActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
								  
							}
							
						});
					else
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								 Toast.makeText(SettingAbountActivity.this, "反馈失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();
								  
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

	
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch(msg.what){
            
            case DOWNLOAD_APK_OK:
                pd.cancel();
                update();
            	break;
            case UPDATE_PROGRESS:
            	
            	int progress = (Integer) msg.obj;
            	Log.i("progress", "" + progress);
            	pd.setProgress(progress);
            	pd.setMessage("请稍候: " + progress + "%");
            	break;
            }

            
            
        }
    };
	
	
    
    
	
	   public void downFile(final String url){
		   pd.show();
	        new Thread(){
	            public void run(){
	                HttpClient client = new DefaultHttpClient();
	                HttpGet get = new HttpGet(url);
	                HttpResponse response;
	                try {
	                    response = client.execute(get);
	                    HttpEntity entity = response.getEntity();
	                    long length = entity.getContentLength();
	                    InputStream is =  entity.getContent();
	                    FileOutputStream fileOutputStream = null;
	                    if(is != null){
	                        File file = new File(Environment.getExternalStorageDirectory(),"science_pie.apk");
	                        fileOutputStream = new FileOutputStream(file);
	                        byte[] b = new byte[1024];
	                        int charb = -1;
	                        int count = 0;
	                        while((charb = is.read(b))!=-1){
	                            fileOutputStream.write(b, 0, charb);
	                            count += charb;
	                            int progress = (int) ((double)count/length * 100);
	                            Message msg = handler.obtainMessage();
	                            msg.what = UPDATE_PROGRESS;
	                            msg.obj = progress;
	                            handler.sendMessage(msg);
	                        }
	                    }
	                    fileOutputStream.flush();
	                    if(fileOutputStream!=null){
	                        fileOutputStream.close();
	                    }
	                    down();
	                 
	                }  catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }.start();
	    }
	
	   
	   
	   
	    /**
	     * 下载完成，通过handler将下载对话框取消
	     */
	    public void down(){
	        new Thread(){
	            public void run(){
	                Message message = handler.obtainMessage();
	                message.what = DOWNLOAD_APK_OK; 
	                handler.sendMessage(message);
	            }
	        }.start();
	    }
	   
	   
	   
	   
	   /**
	     * 安装应用
	     */
	    public void update(){
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"science_pie.apk"))
	                , "application/vnd.android.package-archive");
	        startActivity(intent);
	    }
	  
		
}
