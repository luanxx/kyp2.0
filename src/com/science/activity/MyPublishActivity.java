package com.science.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.json.JsonGetCollectionHandler;
import com.science.json.JsonGetMyPublishNeedHandler;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.AsyncImageLoader;
import com.science.util.Url;
import com.science.util.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MyPublishActivity extends Activity {
	private MyApplication myApplication=null;
	private ImageButton headerback=null;
	private TextView headertitle;
	private ViewPager viewPager;  
	private ArrayList<View> pageViews;
	private View v1;
	private View v2;
	
	private ListView resourceListView=null;
	private ListView needListView=null;
	
	private ImageButton mypublishresource;
	private ImageButton mypublisneed;
	
	private List<Map<String,Object>> resourceList=null;
	private List<Map<String, Object>> needList=null;
	
	private MyHandler handler;
	private JsonGetMyPublishNeedHandler jsonGetMyPublishNeedHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myApplication=(MyApplication)this.getApplication();
		
		// 去掉顶部灰条
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mypublish);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		
		InitVariable();
		InitViews();
		InitData();
		SetListener();
	}
	
	private void InitVariable()
	{
		pageViews = new ArrayList<View>(); 
		jsonGetMyPublishNeedHandler=new JsonGetMyPublishNeedHandler(); 
		handler=new MyHandler();
		
	}
	
	private void InitViews()
	{
		headerback=(ImageButton)findViewById(R.id.settingback);
		headertitle=(TextView)findViewById(R.id.settingheadertitle);
		mypublishresource=(ImageButton)findViewById(R.id.mypublishresource);
		mypublisneed=(ImageButton)findViewById(R.id.mypublishneed);
		
        viewPager = (ViewPager)this.findViewById(R.id.mypublicviewswitch);
        
		LayoutInflater inflater = getLayoutInflater(); 
        
        v1 = (ViewGroup) inflater.inflate(R.layout.mypublishresourch, null);
        v2 = inflater.inflate(R.layout.mypublishneed, null); 
        
        resourceListView=(ListView)v1.findViewById(R.id.mypublishresoucelistview);
        needListView=(ListView)v2.findViewById(R.id.mypublishneed);
        
        pageViews.add(v1);  
        pageViews.add(v2); 
        
        viewPager.setAdapter(new GuidePageAdapter());
	}
	
	private void InitData()
	{
		headertitle.setText("我的发布");
		headerback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		mypublishresource.setImageDrawable(getResources().getDrawable(R.drawable.wofabudeziyuansel));
		mypublisneed.setImageDrawable(getResources().getDrawable(R.drawable.wofabudexuqiu));
		
		resourceList=new ArrayList<Map<String,Object>>();
//		Map<String , Object> map1=new HashMap<String, Object>();
//		map1.put("title", "北京市关于征集2014年中央文化产业发展专项资金一般项目的通知");
//		map1.put("time", "4天前");
//		map1.put("url", "http://123.57.207.9/static/news/default/news_38.jpg");
//		map1.put("abstract", "项目摘要");
//		resourceList.add(map1);
//		
//		needListView.setAdapter(new MyAdapte(resourceList));
	}
	
	private void SetListener()
	{
		viewPager.setOnPageChangeListener(onPageChangeListener);
		mypublishresource.setOnClickListener(titleOnClickListener);
		mypublisneed.setOnClickListener(titleOnClickListener);
	}
	
	private void GetMyNeed()
	{
		MyThreadGetMyNeed myThreadGetMyNeed=new MyThreadGetMyNeed();
		new Thread(myThreadGetMyNeed).start();
	}
	
	private View.OnClickListener titleOnClickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.mypublishresource:
				mypublishresource.setImageDrawable(getResources().getDrawable(R.drawable.wofabudeziyuansel));
				mypublisneed.setImageDrawable(getResources().getDrawable(R.drawable.wofabudexuqiu));
				viewPager.setCurrentItem(0);
				break;
			case R.id.mypublishneed:
				mypublishresource.setImageDrawable(getResources().getDrawable(R.drawable.wofabudeziyuansel));
				mypublisneed.setImageDrawable(getResources().getDrawable(R.drawable.wofabudexuqiu));
				viewPager.setCurrentItem(1);
				break;
			default:
				break;
			}
		}
	};
	
	private OnPageChangeListener onPageChangeListener=new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==0)
				{
					//myHeader.SetSelected(0);
					mypublishresource.setImageDrawable(getResources().getDrawable(R.drawable.wofabudeziyuansel));
					mypublisneed.setImageDrawable(getResources().getDrawable(R.drawable.wofabudexuqiu));
				}
				else if(arg0==1)
				{
					mypublishresource.setImageDrawable(getResources().getDrawable(R.drawable.wofabudeziyuan));
					mypublisneed.setImageDrawable(getResources().getDrawable(R.drawable.wofabudexuqiusel));
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				Log.v("1","onPageScrolled");
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				Log.v("1","onPageScrollStateChanged");
			}
		};
	
	 private class GuidePageAdapter extends PagerAdapter {  
   	  
	        @Override  
	        public int getCount() {  
	            return pageViews.size();  
	        }  
	  
	        @Override  
	        public boolean isViewFromObject(View arg0, Object arg1) {  
	            return arg0 == arg1;  
	        }  
	  
	        @Override  
	        public int getItemPosition(Object object) {  
	            // TODO Auto-generated method stub  
	            return super.getItemPosition(object);  
	        }  
	  
	        @Override  
	        public void destroyItem(View arg0, int arg1, Object arg2) {  
	            // TODO Auto-generated method stub  
	            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
	        }  
	  
	        @Override  
	        public Object instantiateItem(View arg0, int arg1) {  
	            // TODO Auto-generated method stub  
	            ((ViewPager) arg0).addView(pageViews.get(arg1));  
	            return pageViews.get(arg1);  
	        }  
	  
	        @Override  
	        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
	            // TODO Auto-generated method stub  
	  
	        }  
	  
	        @Override  
	        public Parcelable saveState() {  
	            // TODO Auto-generated method stub  
	            return null;  
	        }  
	  
	        @Override  
	        public void startUpdate(View arg0) {  
	            // TODO Auto-generated method stub  
	  
	        }  
	  
	        @Override  
	        public void finishUpdate(View arg0) {  
	            // TODO Auto-generated method stub  
	  
	        }  
	    } 
	 
	 private class MyAdapte extends BaseAdapter
	{
		 private List<Map<String, Object>> myList=null;
		 
		 public MyAdapte(List<Map<String, Object>> L)
		 {
			 myList=L;
		 }

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if (myList!=null) {
					return myList.size();
				}
				else {
					return 0;
				}
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				if (myList!=null) {
					return myList.get(arg0);
				}
				else {
					return null;
				}
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				convertView = getLayoutInflater().inflate(R.layout.mycollectionitem,
						null);
				TextView title=(TextView)convertView.findViewById(R.id.myCollectionItemTitle);
				TextView time=(TextView)convertView.findViewById(R.id.myCollectionItemsTime);
				
				String strtitle=(String) myList.get(position).get("title");
				String strTime=(String)myList.get(position).get("time");
				
				title.setText(strtitle);
				time.setText(strTime);
				return convertView;
			}
			
		}
	 
	 private class MyThreadGetMyNeed implements Runnable
	    {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				URL url;
				try {
					url = new URL(myApplication.ComposeToken(Url.getCollection));
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					needList=jsonGetMyPublishNeedHandler.getListItems(input);
					if (needList!=null) {
						handler.sendEmptyMessage(2);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
	    }
		
		private class MyHandler extends Handler {

	    	@Override
	    	public void dispatchMessage(Message msg) {
	    		// TODO Auto-generated method stub
	    		super.dispatchMessage(msg);
	    	}

	    	@Override
	    	public void handleMessage(Message msg) {
	    		if (msg.what == 2) {
	    			needListView.setAdapter(new MyAdapte(needList));
				} 
	    		super.handleMessage(msg);
	    	}

	    	@Override
	    	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
	    		// TODO Auto-generated method stub
	    		return super.sendMessageAtTime(msg, uptimeMillis);
	    	}

	    	@Override
	    	public String toString() {
	    		// TODO Auto-generated method stub
	    		return super.toString();
	    	}
		}
	    	
}
