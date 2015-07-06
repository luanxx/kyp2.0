package com.science.activity;
import com.example.science.R;

import com.science.adapter.CommonFragmentPagerAdapter;
import com.science.fragment.CoopPaperFragment;
import com.science.fragment.CoopResourceFragment;
import com.science.view.MyHeaderView;
import com.science.view.MyImageButton;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CoopResearchActivity extends FragmentActivity{
	


	
	
	private LinearLayout main=null;

	private MyImageButton mTabResource;  
	private MyImageButton mTabPaper;  
	private CoopResourceFragment fragment_resource;  
	private CoopPaperFragment fragment_paper;
	
	private MyHeaderView coop_header = null;

    
    private FragmentManager fm;
    
    private ViewPager view_pager;
    private  CommonFragmentPagerAdapter fragment_adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LayoutInflater inflater = getLayoutInflater();  
		main=(LinearLayout)inflater.inflate(R.layout.coop_research, null); 
		setContentView(main);
		//setContentView(R.layout.coop_research);
        
       // 初始化控件和声明事件  
		//mTabResource = (MyImageButton) findViewById(R.id.coop_resource);  
		//mTabPaper = (MyImageButton) findViewById(R.id.coop_paper);  
        //mTabResource.setOnClickListener(this);  
        //mTabPaper.setOnClickListener(this);  
 
        coop_header = (MyHeaderView) findViewById(R.id.coop_header);
        OnClickListener on_header_tab_listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//表明选择资源列表
				if(v.getId() == 0)
				{
					coop_header.SetSelected(0);
					view_pager.setCurrentItem(0);
				}
				else if(v.getId() == 1)
				{
					coop_header.SetSelected(1);
					view_pager.setCurrentItem(1);
				}
				
			}
        	
        };
        coop_header.SetHeaderText("合作研究");
        coop_header.SetHeaderButtons(new String[]{"资源","论文"});
        coop_header.SetOnHeadButtonClickListener(on_header_tab_listener, 0);
        coop_header.SetOnHeadButtonClickListener(on_header_tab_listener, 1);
        coop_header.SetSelected(0);
       // 设置默认的Fragment  
        setDefaultFragment();  

        fragment_adapter = new CommonFragmentPagerAdapter(fm,new Fragment[]{fragment_resource,fragment_paper});
		view_pager = (ViewPager)findViewById(R.id.coop_view_pager);
		view_pager.setAdapter(fragment_adapter);
		view_pager.setCurrentItem(0);
		
		view_pager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				
				coop_header.SetSelected(position);
			}
			
		});
		
		
	}
	
	private void setDefaultFragment()
	{
		fm = this.getSupportFragmentManager();
		fragment_resource = new CoopResourceFragment();
		fragment_paper = new CoopPaperFragment();
	}

	

	
	


	/*
	@Override
	public void onClick(View v)
	{
		FragmentManager fm = getFragmentManager();
		// 开启Fragment事务
		FragmentTransaction transaction = fm.beginTransaction();

		switch (v.getId())
		{
		case R.id.coop_resource:
			if (mResource == null)
			{
				mResource = new FragmentOfCoopResource();
				
			}
			
			// 使用当前Fragment的布局替代id_content的控件
			transaction.replace(R.id.id_content, mResource);
			
			Toast.makeText(CoopResearchActivity.this,"click",Toast.LENGTH_SHORT).show();
			break;
		case R.id.coop_paper:
			if (mPaper == null)
			{
				mPaper = new FragmentOfCoopPaper();
				Log.i("mresource","isNULL");
			}
			else Log.i("mresource","!isNULL");
			transaction.replace(R.id.id_content, mPaper);
			
			break;
		}
		// transaction.addToBackStack();
		// 事务提交
		transaction.commit();
	}
*/
	


}
