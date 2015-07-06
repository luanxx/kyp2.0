package com.science.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.model.KywdAlys;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TechTrendKywdAlysListAdapter extends BaseAdapter{

	private List<KywdAlys> kywd_alys_list;
	private Context context;
	public TechTrendKywdAlysListAdapter(Context context){
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(kywd_alys_list != null)
			return kywd_alys_list.size();
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return kywd_alys_list.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = ((Activity)context).getLayoutInflater().from(context);
		View view = inflater.inflate(R.layout.tech_trendency_kywd_alys_list_item, null);
		TextView kywd_tv = (TextView) view.findViewById(R.id.tech_trend_kywd_alys_keywords);
		//TextView alys_tv = (TextView) view.findViewById(R.id.tech_trend_kywd_alys_breviary);
		//MyImageButton kywd_alys_shoucang_btn = (MyImageButton) view.findViewById(R.id.tech_trend_kywd_alys_shoucang);
		
		KywdAlys kywd_alys_node = kywd_alys_list.get(position);
		kywd_tv.setText("\"" + kywd_alys_node.getKywd() + "\"" + "的关键词分析");
		//alys_tv.setText(kywd_alys_node.getAlys());
		
		return view;
	}

	
	
	public void updateKywdAlysList(List<KywdAlys> list){
		
		this.kywd_alys_list = list;
		this.notifyDataSetChanged();
	}
}
