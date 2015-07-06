package com.science.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.science.R;
import com.science.activity.CommonContentActivity;
import com.science.adapter.TechTrendKywdAlysListAdapter;
import com.science.model.KywdAlys;
import com.science.services.MyApplication;
import com.science.util.Url;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TechTrendKywdAlysFragment extends Fragment {
    
	private View        view;
	private Activity    activity;
	private RelativeLayout kywd_alys_none_hint_layout;
	private EditText       kywd_alys_einput_et;
	
	private MyImageButton  kywd_alys_search_btn;
	private ListView    kywd_alys_list_view;
	private List<KywdAlys> kywd_alys_list;
	private TechTrendKywdAlysListAdapter kywd_alys_list_adapter;
	private LayoutInflater inflater;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		
		view = inflater.inflate(R.layout.tech_trendency_fragment_kywd_alys, null);
		activity = this.getActivity();
		this.inflater = inflater;
		initVariable();
		initView();
		return view;
	}
	
	
	public void initVariable(){
		
		kywd_alys_list = new ArrayList<KywdAlys>();
		for(int i = 0;i < MyApplication.getInstance().non_null_keywords_list.size();i++)
		kywd_alys_list.add(new KywdAlys(MyApplication.getInstance().non_null_keywords_list.get(i).toString(),
				              "城市化的关键词分析"));
	}
	public void initView(){
		//kywd_alys_none_hint_layout = (RelativeLayout) view.findViewById(R.id.tech_trend_kywd_alys_none_hint_layout);
		
		kywd_alys_list_view = (ListView) view.findViewById(R.id.tech_trend_kywd_alys_list);
		kywd_alys_einput_et = (EditText) view.findViewById(R.id.tech_trend_kywd_alys_input_box);
		kywd_alys_search_btn = (MyImageButton) view.findViewById(R.id.tech_trend_kywd_alys_search);
	    kywd_alys_list_adapter  = new TechTrendKywdAlysListAdapter(activity);
	    kywd_alys_list_adapter.updateKywdAlysList(kywd_alys_list);
	    kywd_alys_list_view.setAdapter(kywd_alys_list_adapter);
	    
	    
	    
	    kywd_alys_list_view.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				String url = Url.composeTechTrendKywdAlysUrl(kywd_alys_list.get(position).getKywd());
				String act_class = "关键词分析";
				String theme = "\"" + kywd_alys_list.get(position).getKywd() + "\"";
				Intent intent = new Intent();
				intent.setClass(activity, CommonContentActivity.class);
				intent.putExtra("act_class", act_class);
				intent.putExtra("url", url);
				intent.putExtra("theme", theme);
				activity.startActivity(intent);
				
			}
	    	
	    });
	    
	    
	    
	    
	    kywd_alys_search_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String keyword = kywd_alys_einput_et.getText().toString();
				if(!keyword.isEmpty()){
					
					String url = Url.composeTechTrendKywdAlysUrl(keyword);
					String act_class = "关键词分析";
					String theme = "\"" + keyword + "\"";
					Intent intent = new Intent();
					intent.setClass(activity, CommonContentActivity.class);
					intent.putExtra("act_class", act_class);
					intent.putExtra("url", url);
					intent.putExtra("theme", theme);
					activity.startActivity(intent);
				}
				else{
					Toast.makeText(activity, "关键词不能为空", Toast.LENGTH_SHORT).show();
				}
			}
	    	
	    });
	    
	    kywd_alys_einput_et.setOnKeyListener(new OnKeyListener(){

    		@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
    			// TODO Auto-generated method stub
				String keyword = kywd_alys_einput_et.getText().toString();
				if(!keyword.isEmpty()){
					
					String url = Url.composeTechTrendKywdAlysUrl(keyword);
					String act_class = "关键词分析";
					String theme = "\"" + keyword + "\"";
					Intent intent = new Intent();
					intent.setClass(activity, CommonContentActivity.class);
					intent.putExtra("act_class", act_class);
					intent.putExtra("url", url);
					intent.putExtra("theme", theme);
					activity.startActivity(intent);
				}
				else{
					Toast.makeText(activity, "关键词不能为空", Toast.LENGTH_SHORT).show();
				}
				return false;
			}
    		
    	});
	    
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}
