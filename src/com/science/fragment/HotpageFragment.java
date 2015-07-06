package com.science.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.activity.CommonContentActivity;

import com.science.json.JsonHotPageListHandler;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.AsyncImageLoader;
import com.science.util.MergeListUtil;
import com.science.util.Url;
import com.science.util.AsyncImageLoader.ImageCallback;
import com.science.view.MyHeaderView;
import com.science.view.MyPullToRefreshListView;
import com.science.view.MyPullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class HotpageFragment extends Fragment {

	private MyApplication myApplication= null;
	
	private MyPullToRefreshListView listView;
	private Button bt;
	private ProgressBar pg;
	private View moreView;
	
	private List<Map<String, Object>> list=null;
	private MyHandler myHandler=null;
	private JsonHotPageListHandler json=null;
	
	private String strUrl=null;
	private int block=0;
	private MyListAdapter myListAdapter=null;
	private MyHeaderView myHeader=null;
	private String title="";
	
	private int type_id = 1;
	private int class_id = 0;
	private int id = 0;
	private Activity activity;
	private View view;
	private View hot_list_footer;
	private LayoutInflater inflater;
	private int last_id = 0;
	private MergeListUtil merger = new MergeListUtil();
	private final boolean BACKWARD = true;
	private final boolean FORWARD = false;
	private boolean direction = BACKWARD;
	private boolean loadable = true;
	public HotpageFragment(){
		
	}
	
	public HotpageFragment(int tab){
		
		this.class_id = tab;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

		
		this.activity = this.getActivity();
		this.inflater = inflater;
		myApplication=(MyApplication)activity.getApplication();
		strUrl = Url.composeHotPageUrl(type_id, class_id, 0);
		hot_list_footer = inflater.inflate(R.layout.comm_list_footer, null);
		initVariable();
		initViews();
		requestData(); // 刚启动的时候刷新一次数据	
		return this.view;
	}
	

	

	
	private void initViews()
	{

		listView = new MyPullToRefreshListView(activity);
	
		
		listView.setDivider(getResources().getDrawable(R.color.middle_gray));
		listView.setDividerHeight(1);
		listView.setonRefreshListener(refreshListener);
		listView.setOnScrollListener(new OnScrollListenerImple());
		listView.setOnItemClickListener(listener);
		listView.setBackgroundColor(getResources().getColor(R.color.white));
		listView.setAdapter(myListAdapter);

		// 实例化底部布局
		moreView = inflater.inflate(R.layout.listmoredata, null);

		bt = (Button) moreView.findViewById(R.id.bt_load);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);


		this.view = listView;
	}
	
	private void initVariable()
	{
		myHandler=new MyHandler();
		json=new JsonHotPageListHandler();
		list=new ArrayList<Map<String, Object>>();
		myListAdapter=new MyListAdapter(list, null);
		
	}
	
	private void requestData()
	{
		if(listView != null && hot_list_footer != null){
			listView.addFooterView(hot_list_footer);
		}
		MyThread myThread=new MyThread(0);
		new Thread(myThread).start();
	}
	/**
	 * 下拉刷新监听器
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
//			TabScienceNetActivity.isSearch = false;
			refreshPage();
		}
		
	};
	


	/**
	 * ListView的每个item的点击事件
	 */
	private OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			
				String url=(String) list.get(position-1).get("filename");
				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
				intent.setClass(activity, CommonContentActivity.class);//从哪里跳到哪里
				intent.putExtra("url", url);//传递数据
				intent.putExtra("theme", title);
				String act_class = "";
				if(class_id == 4)
					act_class = "创业项目";
				else if(class_id == 3)
					act_class = "项目解读";
				intent.putExtra("act_class", act_class);
				intent.putExtra("articleType", list.get(position-1).get("articleType").toString());
				intent.putExtra("id", (Integer)list.get(position-1).get("id"));
				startActivity(intent);

			

		}
	};
	
	

	

	
   	


	private class MyThread implements Runnable
	{
		private int index=0;
		public MyThread(int temp)
		{
			index=temp;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			URL url;
			try {
				Thread.sleep(3000);
				url = new URL(strUrl);
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				List<Map<String,Object>> temp = json.getListItems(input);
				if(temp != null){
					if(merger.mergeTwoList(list, temp, direction))
						myHandler.sendEmptyMessage(1);
				}
				else
					myHandler.sendEmptyMessage(5);
				
				

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	

	
	
	
	
	
	
    private class OnScrollListenerImple implements OnScrollListener{ 
    	boolean pullable = false;
        @Override 
        public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) { 
        	if(visibleItemCount + 1 < totalItemCount)
        		pullable = true;
        } 
   


		@Override 
        public void onScrollStateChanged(AbsListView listview, int scrollState) { 
	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
	            // 判断是否滚动到底部  
	            if (listview.getLastVisiblePosition() == listview.getCount() - 1 && pullable && loadable) {  
	                //加载更多功能的代码  
	            	addDataForListView(); 
	            }  
	        } 
        } 
           
    }
    
    
    
    private void addDataForListView() {
		// TODO Auto-generated method stub
    	//MyGetMoreData();
    	
    	if(loadable)
    	{	
    	loadable = false;
    	direction = BACKWARD;
    	strUrl = Url.composeHotPageUrl(type_id, class_id, last_id);
    	requestData();
    	}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 刷新当前页面
	 */
	private void refreshPage() {
		
		if(loadable)
		{
		    direction = FORWARD;
			loadable = false;
		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				
				strUrl = Url.composeHotPageUrl(type_id, class_id, 0);
				URL url;
				try {
					//url = new URL(myApplication.ComposeToken(strUrl+Integer.toString(0)));
					Thread.sleep(3000);
					url = new URL(strUrl);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					json = new JsonHotPageListHandler();
					List<Map<String,Object>> temp  = json.getListItems(input);
					
					if(temp != null){
						if(list != null)
						//list.addAll(0, temp);
						if(merger.mergeTwoList(list,temp,direction))
							myHandler.sendEmptyMessage(1);
					}
					else
					{
						myHandler.sendEmptyMessage(5);
					}
					

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				myHandler.sendEmptyMessage(4);
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if (myListAdapter != null) {
					myListAdapter.notifyDataSetChanged();
					listView.onRefreshComplete();
				}
			}
		}.execute("begin");
	}
	}
	private class MyHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==1) {
				if(myListAdapter != null)
				{
				myListAdapter.notifyDataSetChanged();
				}
				
				if(list != null)
					if(list.size() > 0)
						last_id = (Integer) list.get(list.size() - 1).get("id");
				if(listView != null){
					if(listView.getFooterViewsCount() > 0 && hot_list_footer != null)
						listView.removeFooterView(hot_list_footer);
				}
				
				loadable = true;
			}
			else if (msg.what==2) {
				myListAdapter.notifyDataSetChanged();
				pg.setVisibility(View.GONE);
				if(listView != null){
					if(listView.getFooterViewsCount() > 0 && hot_list_footer != null)
						listView.removeFooterView(hot_list_footer);
				}
//				bt.setVisibility(View.VISIBLE);
				loadable = true;
			}
			else if (msg.what==3) {
				pg.setVisibility(View.GONE);
				bt.setVisibility(View.GONE);
				if(listView != null){
					if(listView.getFooterViewsCount() > 0 && hot_list_footer != null)
						listView.removeFooterView(hot_list_footer);
				}
				loadable = true;
			}
			else if (msg.what==4) {
				//myListAdapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				loadable = true;
			}
			else if(msg.what == 5){
				if(listView != null){
					if(listView.getFooterViewsCount() > 0 && hot_list_footer != null)
						listView.removeFooterView(hot_list_footer);
				}
				loadable = true;
			}
			super.handleMessage(msg);
		}
		
	}
	
	 private class MyListAdapter extends BaseAdapter
	    {
	    	private String name=null;
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader(
					AppUtil.ITEM_IMG_WIDTH, AppUtil.ITEM_IMG_HEIGHT);
	    	
	    	public MyListAdapter(List<Map<String, Object>> L,String N)
	    	{
	    		//list=L;
	    		name=N;
	    	}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if (list!=null) {
					return list.size();
				}
				else {
					return 0;
				}
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				if (list!=null) {
					return list.get(position);
				}
				else {
					return null;
				}
				
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				ViewHolder holder = null; 
				
				if(convertView == null)
				{
					holder = new ViewHolder();
					View view = inflater.inflate(R.layout.hotpageitems,
							null);
					holder.tView =(TextView)view.findViewById(R.id.hotpageItemsTextView);
					holder.imageView = (ImageView)view.findViewById(R.id.hotpageItemsImageView);
//					
//					String temp=(String) list.get(position).get("title");
//					String urlString=(String) list.get(position).get("imgurl");
//					holder.tView.setText(temp);
//					Drawable db=(Drawable)list.get(position).get("drawable");
//					if (db!=null) {
//						holder.imageView.setImageDrawable(db);
//					}
//					else {
//						Drawable cachedImage = asyncImageLoader.loadDrawable(
//								urlString, new ImageCallback() {
//									
//									@Override
//									public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//										// TODO Auto-generated method stub
//										if (holder.imageView != null && imageDrawable != null) {
//											holder.imageView.setImageDrawable(imageDrawable);
//											list.get(position).put("drawable", imageDrawable);
//											// Log.e("在回调里面设置好图片", "liushuai");
//										} else {
//											try {
//												holder.imageView.setImageResource(R.drawable.sync);
//												
//												// Log.e("在回调里面设置了默认的图片", "liushuai");
//											} catch (Exception e) {
//												
//											}
//										}
//									}
//								});
//					}
//					
					
					
					convertView = view;
					convertView.setTag(holder);
					
					
					
				}else{
					holder = (ViewHolder) convertView.getTag();

				}
				
				
				
				
				
				final int index = position;
				
				
				if(holder != null){
					final ViewHolder holder1 = holder;
					
					String temp=(String) list.get(index).get("title");
					final String urlString=(String) list.get(index).get("imgurl");
					holder1.imageView.setTag(urlString + index);
					holder.tView.setText(temp);
					Drawable db=(Drawable)list.get(index).get("drawable");
					if (db!=null) {
						holder.imageView.setImageDrawable(db);
					}
					else {
						
					Drawable drawable =	new AsyncImageLoader(AppUtil.ITEM_IMG_WIDTH,AppUtil.ITEM_IMG_WIDTH).loadDrawable(
								urlString, new ImageCallback() {
									
									@Override
									public void imageLoaded(Drawable imageDrawable, String imageUrl) {
										// TODO Auto-generated method stub
										
										ImageView imgViewByTag = (ImageView) (listView.findViewWithTag(imageUrl + index));
										if(imgViewByTag != null){
											imgViewByTag.setImageDrawable(imageDrawable);
											list.get(index).put("drawable", imageDrawable);
										}
										else
										{
											
										}
//										if (holder1.imageView != null && imageDrawable != null) {
//											if(holder1.imageView.getTag() != null && holder1.imageView.getTag().equals(urlString))
//											{
//											  list.get(index).put("drawable", imageDrawable);
//											  holder1.imageView.setImageDrawable(imageDrawable);
//											}
//											// Log.e("在回调里面设置好图片", "liushuai");
//										} else {
//
//												holder1.imageView.setImageResource(R.drawable.sync);
//										}
									}
								});
					
					
					   if(drawable == null){
						   holder1.imageView.setImageResource(R.drawable.icon_empty);
					   }
					   else
					   {
						   holder1.imageView.setImageDrawable(drawable);
					   }
					
					
					
					}
					
				}
				
				
				
				
				
				
				
				
				
				
				return convertView;
			}
	    	
	    }
	
	 
	 
	 class ViewHolder{
		 TextView tView;
		 ImageView imageView;
	 }
	 
	 
}
