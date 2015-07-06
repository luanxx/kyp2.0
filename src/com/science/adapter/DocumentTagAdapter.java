package com.science.adapter;

import com.example.science.R;
import com.science.view.MyImageButton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class DocumentTagAdapter extends BaseAdapter{

	 private Context context;
     
	 private int tag_nums = 4;
	  public DocumentTagAdapter(Context context)
	 {
		 this.context = context;
	 }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tag_nums;
	}

	@Override
	public Object getItem(int item) {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this.context);
		WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		int spacing = 2;
		int item_width = (width - 80 - spacing*3)/4;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.documenttagitem, null);
			convertView.setLayoutParams(new GridView.LayoutParams(140,64));
		}

		return convertView;
	}
	
}
