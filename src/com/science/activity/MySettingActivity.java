package com.science.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.example.science.R;
import com.science.json.JsonCommonResponseStateHandler;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.util.DefaultUtil;
import com.science.util.FileAccess;
import com.science.util.Url;
import com.science.view.CircularImage;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MySettingActivity extends Activity {

	private CircularImage  my_photo_iv;
	private TextView       header_title_tv;
	private ImageButton    back_btn;
	private MyImageButton  setting_exit_btn;
	private MyImageButton  setting_change_platform_btn;
	private String         logout_url;
	private View           setting_setting_abount_view;
    
	private final int      LOGOUT_OK = 0;
	private final int      LOGOUT_FAIL = 1;
	private FunctionManage fm;
	private MyApplication application;
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_setting);
		application = MyApplication.getInstance();
		fm = new FunctionManage(this);
		initView();
	}
	
	
	public void initView(){
		
		header_title_tv = (TextView) findViewById(R.id.settingheadertitle);
		header_title_tv.setText("设置");
		back_btn = (ImageButton) findViewById(R.id.settingback);
		setting_exit_btn = (MyImageButton) findViewById(R.id.setting_setting_exit);
		setting_change_platform_btn = (MyImageButton) findViewById(R.id.setting_setting_change_platform);
		back_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
			
		});
		
		
		my_photo_iv = (CircularImage)findViewById(R.id.setting_settings_my_photo);
		my_photo_iv.setImageResource(R.drawable.shezhitouxiang);
		
		
		setting_setting_abount_view = findViewById(R.id.setting_setting_abount);
		setting_setting_abount_view.setOnClickListener(onClickListener);
		setting_exit_btn.setOnClickListener(onClickListener);
		
		setting_change_platform_btn.setOnClickListener(onClickListener);
		
	}
	
	
	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			case R.id.setting_setting_abount:
				Intent intent = new Intent(MySettingActivity.this,SettingAbountActivity.class);
				startActivity(intent);
				break;
			case R.id.setting_setting_exit:
			    
				if(!application.IsLogin())
				{
					fm.Login();
				}
				else
				{
				  if(fm.Logout() == 204){
					  
					  handler.sendEmptyMessage(LOGOUT_OK);
					  MyApplication.sidString = "";
					  MyApplication.getInstance().changeUserInfo();
					  FileAccess.deleteFile("/sdcard/science_pie/" + MyApplication.user_name + ".jpg");
				  }
				}

				
				break;
				
			case R.id.setting_setting_change_platform://切换平台
                MyApplication.changePlatform();
				break;
			
		   }
		
		}
	};
	
	
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			
			case DefaultUtil.NETWORK_DISCONNECTED:
				Toast.makeText(MySettingActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
				break;
			case LOGOUT_OK:
				fm.Login();
				MyApplication.Logout();
				//Toast.makeText(MySettingActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
				Intent intent1 = new Intent(MySettingActivity.this, MainActivity.class);
				startActivity(intent1);
				break;
			case LOGOUT_FAIL:
				//Toast.makeText(MySettingActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}
