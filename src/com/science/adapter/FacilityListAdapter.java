package com.science.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.science.R;
import com.science.model.Cooper;
import com.science.model.Facility;
import com.science.util.AsyncImageLoader;
import com.science.util.AsyncImageLoader.ImageCallback;






	public class FacilityListAdapter extends BaseAdapter
	{

		private Context context;
		private List<Facility> faci_list;
		public FacilityListAdapter(Context context,List<Facility> list)
		{
			this.context = context;
			this.faci_list = list;
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(faci_list != null)
			return faci_list.size();
			
			return 0;
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
			final int index = position;
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.coop_fragment_common_list_item, null);
            final ImageView photo_iv = (ImageView) view.findViewById(R.id.coop_cooper_photo);
            TextView base_info_tv = (TextView)view.findViewById(R.id.coop_cooper_base_info);
            TextView brief_intro_tv = (TextView)view.findViewById(R.id.coop_cooper_brief_introduction);
            
            
            Facility faci = faci_list.get(position);
            String base_info =   "名称" + faci.getFaciName();
            int pos1 = 0,pos2 = base_info.length();   
            //4个空格
            base_info += "    " + "型号" + faci.getFaciModel();
            int pos3 = pos2 + 4,pos4 = base_info.length();
            base_info += "    " + "数量" + faci.getFaciNumber() + "台";
            int pos5 = pos4 + 4,pos6 = base_info.length();
            
            SpannableString span_base_info = new SpannableString(base_info);
            
            span_base_info.setSpan(new TextAppearanceSpan(context,R.style.coop_cooper_text_name_style), pos1, pos2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            span_base_info.setSpan(new TextAppearanceSpan(context,R.style.coop_cooper_text_identity_style), pos3, pos4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           
            span_base_info.setSpan(new TextAppearanceSpan(context,R.style.coop_cooper_text_additional_style), pos5, pos6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            try{
            
            String brief_intro = faci_list.get(position).getFaciOwner();
            base_info_tv.setText(span_base_info);
            brief_intro_tv.setText(brief_intro);
            String pic_url = faci_list.get(position).getFaciImgUrlList().get(0);
            Drawable db = faci_list.get(position).getFaciDrawable();
            
            if(db != null){
            	photo_iv.setImageDrawable(db);
            }
            
            else{
   
            	
            new AsyncImageLoader(40,40){
            	
            }.loadDrawable(pic_url, new ImageCallback(){

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					// TODO Auto-generated method stub
					if(imageDrawable != null && photo_iv != null)
					{
					   faci_list.get(index).putFaciDrawable(imageDrawable);
					   photo_iv.setImageDrawable(imageDrawable);
					}
				}
            	
            });
           
          
            }
            }catch(Exception e){
            	e.printStackTrace();
            }
            
            
            convertView = view;
            
            
            
            return convertView;
		}

		
	  public void updateCooperList(List<Facility> list)
	  {
		  this.faci_list = list;
		  this.notifyDataSetChanged();
		  
	  }
		

      public void clearList(List<File> f) {
          int size = f.size();
          if (size > 0) {
                  f.removeAll(f);
                 // coop_list_adapter.notifyDataSetChanged();
          }
          
      
          
  }
 
	
	
}
