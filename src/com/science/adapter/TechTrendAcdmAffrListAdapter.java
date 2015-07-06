package com.science.adapter;

import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.model.CommonItem;
import com.science.util.AsyncImageLoader;
import com.science.util.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TechTrendAcdmAffrListAdapter extends BaseAdapter {

	private List<Map<String,Object>> tech_trend_acdm_affr_list;
	private Context context;
	public TechTrendAcdmAffrListAdapter(Context context,List<Map<String,Object>> list){
		this.context = context;
		this.tech_trend_acdm_affr_list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(tech_trend_acdm_affr_list != null)
			return tech_trend_acdm_affr_list.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tech_trend_acdm_affr_list.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		

		if(convertView == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater().from(context);
			View view = inflater.inflate(R.layout.tech_trend_acdm_affr_list_item, null);
			holder.title_tv = (TextView) view.findViewById(R.id.tech_trend_acdm_affr_item_title);
//			TextView like_num_tv = (TextView) view.findViewById(R.id.tech_trend_acdm_affr_item_like_num);
//			TextView comment_num_tv = (TextView) view.findViewById(R.id.tech_trend_acdm_affr_item_comment_num);
			//final ImageView photo_iv = (ImageView)view.findViewById(R.id.tech_trend_acdm_affr_item_photo);
			holder.title_tv.setText(tech_trend_acdm_affr_list.get(position).get("title").toString());
			
			convertView = view;
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title_tv.setText(tech_trend_acdm_affr_list.get(position).get("title").toString());
		
		//like_num_tv.setText("" + tech_trend_acdm_affr_list.get(position).getLikeNum());
		//comment_num_tv.setText("" + tech_trend_acdm_affr_list.get(position).getCommentNum());
		
		
//		String img_url = (String) tech_trend_acdm_affr_list.get(position).get("img_url");
//		AsyncImageLoader asycn_image_loader = new AsyncImageLoader(100,100);
//		asycn_image_loader.loadDrawable(img_url, new ImageCallback(){
//
//			@Override
//			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//				// TODO Auto-generated method stub
//				photo_iv.setImageDrawable(imageDrawable);
//			}
//			
//		});
		
		
		
		
		
		
		return convertView;
	}

	
	class ViewHolder {
		TextView title_tv;
		ImageView photo_iv;
	}
	
	
	
	public void updateAdapter(List<Map<String,Object>> list){
		
		this.tech_trend_acdm_affr_list = list;
		this.notifyDataSetChanged();
	}
}
