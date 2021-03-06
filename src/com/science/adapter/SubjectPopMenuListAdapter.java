package com.science.adapter;

import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.activity.ProjectApplyActivity;
import com.science.model.ResourceDefine;
import com.science.model.Subject;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubjectPopMenuListAdapter extends BaseAdapter {

	private List<Subject> subject_list;
	private Context context;
	
	public SubjectPopMenuListAdapter(Context context){
		this.context = context;
	}
	
	public SubjectPopMenuListAdapter(Context context,List<Subject> subject_list){
		this.context = context;
		this.subject_list = subject_list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(subject_list != null)
			return subject_list.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subject_list.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LinearLayout layout = new LinearLayout(context);
		
		
		if(position == selected_position){
			
			ImageView iv = new ImageView(context);
			iv.setImageResource(R.drawable.icon_green_point);
			layout.addView(iv);
			 layout.setBackground(context.getResources().getDrawable(R.drawable.shape_dark_blue));
		}
		else
		{
			 layout.setBackground(context.getResources().getDrawable(R.drawable.shape_light_blue));
		}
	   
	    layout.setGravity(Gravity.CENTER);
		TextView tv = new TextView(context);
		Subject sbj = subject_list.get(position);
		String item = sbj.getSbjName();
		tv.setText(item);
		tv.setPadding(5, 10, 0, 10);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(15);
		layout.addView(tv);
		return layout;
	}

	
	private int selected_position = -1;
	public void setSelectedPosition(int selected_position){
		this.selected_position = selected_position;
	}
	
	public int getSelectedPosition(){
		
		return this.selected_position;
	}
	
}
