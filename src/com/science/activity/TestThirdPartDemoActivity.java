package com.science.activity;

import java.util.HashMap;

import com.example.science.R;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.sharesdk.framework.FakeActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

public class TestThirdPartDemoActivity extends FakeActivity implements OnClickListener, Callback, PlatformActionListener{
    private Context context;
	public void show(Context context){
		//ShareSDK.initSDK(context);
		this.context = context;
		Toast.makeText(context, "call this", Toast.LENGTH_SHORT).show();
		super.show(context, null);
	}
	
	
	@Override
	public void onCreate(){
		
		activity.setContentView(R.layout.logindialog);
		
		(activity.findViewById(R.id.logindialogqqlogin)).setOnClickListener(this);
		(activity.findViewById(R.id.logindialogweibologin)).setOnClickListener(this);
		(activity.findViewById(R.id.logindialogweixinlogin)).setOnClickListener(this);
	}
	
	
	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.logindialogweibologin:
			Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
			authorize(sina);
			break;
		case R.id.logindialogweixinlogin:
			Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
			authorize(weixin);
			break;
		}
	}

	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}
		
		plat.setPlatformActionListener(this);
		//关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}
	
}
