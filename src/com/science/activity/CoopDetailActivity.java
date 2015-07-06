package com.science.activity;

import com.example.science.R;
import com.example.science.R.id;
import com.example.science.R.layout;
import com.example.science.R.menu;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.view.MyHeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class CoopDetailActivity extends Activity {
	private MyApplication myApplication=null;
	private FunctionManage fm ;
	private MyHeaderView header_view;
	private WebView      web_view;
	private String       url;
	private TextView     coop_detail_contact;
	private int receive_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.coop_detail);
		initViews();
		
		myApplication=(MyApplication)this.getApplication();
		fm = new FunctionManage(this);
		
		Intent intent = this.getIntent();
		url = intent.getStringExtra("url");
		receive_id = intent.getIntExtra("receive_id", 0);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coop_detail, menu);
		return true;
	}
	public void initViews()
	{
		header_view = (MyHeaderView) findViewById(R.id.coop_detail_header);
		header_view.SetHeaderText("合作研究");
		web_view = (WebView) findViewById(R.id.coop_detail_web);
		web_view.loadUrl(url);
		coop_detail_contact = (TextView)findViewById(R.id.coop_detail_contact);
		coop_detail_contact.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(myApplication.IsLogin())
				{
				Intent intent = new Intent(CoopDetailActivity.this,CoopDetailMessageActivity.class);
				intent.putExtra("receive_id", receive_id);
				startActivity(intent);
				}
				else{
					fm.Login();
				}
			}
			
		});
	}	
	
}
