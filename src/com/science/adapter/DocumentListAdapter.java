package com.science.adapter;

import java.util.List;
import java.util.Map;

import com.example.science.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DocumentListAdapter extends BaseAdapter{

	private Context context;
	private int doc_nums = 10;
	private List<Map<String,Object>> doc_list;
	public DocumentListAdapter(Context context,List<Map<String,Object>> list)
	{
		this.context = context;
		doc_list = list;
		if(doc_list == null)
			doc_nums = 0;
		else
			doc_nums = doc_list.size();
			
	}
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return doc_nums;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.doc_list_item, null);
		TextView doc_title_text = (TextView) view.findViewById(R.id.doc_title);
		TextView doc_like_num_text = (TextView) view.findViewById(R.id.doc_like_num);
		TextView doc_comment_num_text = (TextView) view.findViewById(R.id.doc_comment_num);
		String title = (String)doc_list.get(position).get("title");
		int  like_num = (Integer)doc_list.get(position).get("diggtop");
		int  comment_num = (Integer)doc_list.get(position).get("plnum");
		doc_title_text.setText(title);
		doc_like_num_text.setText("" + like_num);
		doc_comment_num_text.setText("" + comment_num);
		if(convertView == null)
		{
			convertView = view;
		}
		return convertView;
	}

}
