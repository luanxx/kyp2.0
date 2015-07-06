package com.science.activity;

import com.example.science.R;
import com.science.adapter.CommonFragmentPagerAdapter;
import com.science.adapter.SubjectPopMenuListAdapter;
import com.science.fragment.TechTrendAcdmAffrFagment;
import com.science.fragment.TechTrendKywdAlysFragment;
import com.science.view.MyHeaderView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;

public class TechTrendActivity extends FragmentActivity{

	private CommonFragmentPagerAdapter tech_trend_pager_adapter;
	private ViewPager                  tech_trend_view_pager;
	private MyHeaderView               tech_trend_header_view;
	private TechTrendKywdAlysFragment  tech_trend_kywd_alys_fragment;
	private TechTrendAcdmAffrFagment tech_trend_acdm_affr_fragment;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.tech_trendency);
        initView();	
    }
    
    
    public void initView(){
    	
    	tech_trend_kywd_alys_fragment = new TechTrendKywdAlysFragment();
    	tech_trend_acdm_affr_fragment = new TechTrendAcdmAffrFagment();
    	tech_trend_view_pager = (ViewPager) findViewById(R.id.tech_trend_view_pager);
    	tech_trend_header_view = (MyHeaderView) findViewById(R.id.tech_trend_header);
    	tech_trend_pager_adapter = new CommonFragmentPagerAdapter(this.getSupportFragmentManager(),
    			                                                  new Fragment[]{tech_trend_kywd_alys_fragment,
    		                                                                     tech_trend_acdm_affr_fragment});
    	               
    	tech_trend_view_pager.setAdapter(tech_trend_pager_adapter);
    	tech_trend_view_pager.setCurrentItem(0);
    	tech_trend_view_pager.setOnPageChangeListener(new OnPageChangeListener(){

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
				tech_trend_header_view.SetSelected(position);
			}
    		
    	});
    	
    	
    	tech_trend_header_view.SetHeaderText("科技趋势");
    	tech_trend_header_view.SetHeaderButtons(new String[]{"关键词分析","业界动态"});
    	tech_trend_header_view.SetSelected(0);
    	OnClickListener on_tech_trend_tab_listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tech_trend_view_pager.setCurrentItem(v.getId());
			}
    		
    	};
    	
    	tech_trend_header_view.SetOnHeadButtonClickListener(on_tech_trend_tab_listener, 0);
    	tech_trend_header_view.SetOnHeadButtonClickListener(on_tech_trend_tab_listener, 1);
    	
    	
    }
}
