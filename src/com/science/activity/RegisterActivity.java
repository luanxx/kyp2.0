package com.science.activity;

import com.example.science.R;
import com.science.services.MyApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends Activity{
	
	private MyApplication myApplication=null;
	private EditText nickName=null;
	private EditText userName=null;
	private EditText password=null;
	private EditText repassword=null;
	private Spinner diyu=null;
	private Spinner zhiye=null;
	private static final String[] strDiYu={"河北","山东","海南","河南","山西"};
	private static final String[] strZhiYe={"学生","学者","科研人员"};
	private ArrayAdapter<String> adapterDiYu;
	private ArrayAdapter<String> adapterZhiYe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myApplication=(MyApplication)this.getApplication();
		
		// 去掉顶部灰条
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		
		InitVariable();
		InitViews();
		InitDate();
	}
	
	private void InitVariable()
	{
		
	}
	
	private void InitViews()
	{
		userName=(EditText)findViewById(R.id.registerusername);
		nickName=(EditText)findViewById(R.id.registernickname);
		password=(EditText)findViewById(R.id.registerrepasswrod);
		repassword=(EditText)findViewById(R.id.registerrepasswrod);
		diyu=(Spinner)findViewById(R.id.registerdiyu);
		zhiye=(Spinner)findViewById(R.id.registerzhiye);
	}
	
	private void InitDate()
	{
		adapterDiYu=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strDiYu);
		adapterZhiYe=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strZhiYe);
		adapterDiYu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterZhiYe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		diyu.setAdapter(adapterDiYu);
		zhiye.setAdapter(adapterZhiYe);
	}
}
