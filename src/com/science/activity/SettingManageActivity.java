package com.science.activity;

import com.example.science.R;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.view.CircularImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingManageActivity extends Activity{
	
	private MyApplication myApplication=null;
	private FunctionManage fm ;
	private ImageButton headerback=null;
	private TextView headertitle;
	private LinearLayout myInfo=null;
	private LinearLayout myCollection=null;
	private LinearLayout myPublish=null;
	private LinearLayout myMessage=null;
	private LinearLayout mySetting = null;
	private CircularImage    my_photo;
	private TextView         my_nickname = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myApplication=(MyApplication)this.getApplication();
		
		// 去掉顶部灰条
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settingmanage);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		fm = new FunctionManage(this);
		InitVariable();
		InitViews();
		InitData();
		SetListener();
	}
	
	private void InitVariable()
	{
		
	}
	
	private void InitViews()
	{
		headerback=(ImageButton)findViewById(R.id.settingback);
		headertitle=(TextView)findViewById(R.id.settingheadertitle);
		myInfo=(LinearLayout)findViewById(R.id.settingmyinfo);
		myCollection=(LinearLayout)findViewById(R.id.settingmycollection);
		myPublish=(LinearLayout)findViewById(R.id.settingpublish);
		myMessage=(LinearLayout)findViewById(R.id.settingmymessage);
		mySetting = (LinearLayout)findViewById(R.id.setting_settings);
		my_photo = (CircularImage) findViewById(R.id.setting_my_photo);
		my_photo.setImageResource(R.drawable.shezhitouxiang);
		my_nickname = (TextView) findViewById(R.id.setting_info_my_name);

	}
	
	private void InitData()
	{
		headertitle.setText("个人设置");
		headerback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		if(myApplication.IsLogin())
		{
			if(myApplication.user.userName == null || myApplication.user.userName.isEmpty())
				my_nickname.setText("未设置昵称");
			else
		        my_nickname.setText(MyApplication.getInstance().user.userNickName);
		}else{
			my_nickname.setText("未登录");
		}
		
		//Log.i("user_name",myApplication.user_name);
	}
	
	private void SetListener()
	{
		myInfo.setOnClickListener(onClickListener);
		myCollection.setOnClickListener(onClickListener);
		myPublish.setOnClickListener(onClickListener);
		myMessage.setOnClickListener(onClickListener);
		mySetting.setOnClickListener(onClickListener);
		my_photo.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.settingmyinfo:
				if(myApplication.IsLogin())
				{
				Intent intent=new Intent(SettingManageActivity.this,MyInfoActivity.class);
				startActivity(intent);
				}
				else{
					fm.Login();
				}
				break;
			case R.id.settingmycollection:
				if(myApplication.IsLogin())
				{
				Intent collection=new Intent(SettingManageActivity.this,MyCollectionActivity.class);
				startActivity(collection);
				}
				else{
					
					fm.Login();
				}
				break;
			case R.id.settingpublish:
				if(myApplication.IsLogin())
				{
				Intent publish=new Intent(SettingManageActivity.this,MyPublishActivity.class);
				startActivity(publish);
				}
				else{
					fm.Login();
				}
				break;
			case R.id.settingmymessage:
//				Intent message=new Intent(SettingManageActivity.this,MyMessageActivity.class);
//				startActivity(message);
				if(myApplication.IsLogin())
				{
				   Intent message=new Intent(SettingManageActivity.this,MyMessageActivity.class);
				  startActivity(message);
				}
				else{
					fm.Login();
				}
				break;
			case R.id.setting_settings:
				Intent setting =new Intent(SettingManageActivity.this,MySettingActivity.class);
				startActivity(setting);
				break;
			case R.id.setting_my_photo:
				if(myApplication.IsLogin())
				{
				
				}
				else{
					
					fm.Login();
				}
				break;
			default:
				break;
			}
		}
	};
	
	
	@Override
	public void onResume(){
		super.onResume();
		if(myApplication.IsLogin() && my_nickname != null){
			if(myApplication.user.userNickName == null || myApplication.user.userNickName.isEmpty())
				my_nickname.setText("未设置昵称");
			else
		        my_nickname.setText(MyApplication.getInstance().user.userNickName);
			
			if(myApplication.user.userAvatar != null){
				if(myApplication.user.userAvatar.get() != null){
					my_photo.setImageBitmap(myApplication.user.userAvatar.get());
				}
			}
			
		}
	}

}
