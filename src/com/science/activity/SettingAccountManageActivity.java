package com.science.activity;

import com.example.science.R;
import com.example.science.R.layout;
import com.example.science.R.menu;
import com.science.view.CircularImage;
import com.science.view.MyHeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

public class SettingAccountManageActivity extends Activity {

	private ListView my_account_list_view;
	private MyHeaderView header_view;    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_account_manage);
		my_account_list_view = (ListView) findViewById(R.id.setting_account_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting_account_manage, menu);
		return true;
	}

}
