//package com.science.activity;
//
//import android.app.Activity;
//
//public class HotPageListActivity extends Activity{
//
//}
package com.science.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.adapter.CommonFragmentPagerAdapter;
import com.science.fragment.HotpageFragment;
import com.science.json.JsonHotPageListHandler;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.AsyncImageLoader;
import com.science.util.DefaultUtil;
import com.science.util.Url;
import com.science.util.AsyncImageLoader.ImageCallback;
import com.science.view.MyHeaderView;
import com.science.view.MyPullToRefreshListView;
import com.science.view.MyPullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;

public class HotPageListActivity extends FragmentActivity {
	
	private MyApplication myApplication= null;
	
	private MyPullToRefreshListView listView;
	private ViewSwitcher viewSwitcher;
	private Button bt;
	private ProgressBar pg;
	private View moreView;
	
	private List<Map<String, Object>> list=null;
	//private MyHandler myHandler=null;
	private JsonHotPageListHandler json=null;
	
	private String strUrl=null;
	private int block=0;
	//private MyListAdapter myListAdapter=null;
	private MyHeaderView myHeader=null;
	private ViewPager view_pager;
	private CommonFragmentPagerAdapter adapter;
	private String title="";
	
	private int type_id = 1;
	private int class_id = 0;
	private int id1 = DefaultUtil.MAX_VALUE;
	private int id2 = -1;
	private int id = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myApplication=(MyApplication)this.getApplication();
		
		// 去掉顶部灰条
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hotpagelist);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		myHeader=(MyHeaderView)findViewById(R.id.hotpagelistheader);
		GetUrl();
		//ComposeUrl(0);
		strUrl = Url.composeHotPageUrl(type_id, class_id, id);
		//initVariable();
		//initViews();
		myHeader.SetHeaderText(title);
		view_pager = (ViewPager)findViewById(R.id.hotpage_view_pager);
		HotpageFragment hot_page_frag = new HotpageFragment(class_id);
		adapter = new CommonFragmentPagerAdapter(this.getSupportFragmentManager(),new Fragment[]{hot_page_frag});
		view_pager.setAdapter(adapter);
		view_pager.setCurrentItem(0);
		//requestData(); // 刚启动的时候刷新一次数据
	}
//	
	private void GetUrl()
	{
		Intent intent = this.getIntent();
		String value = intent.getStringExtra("block");
		title = intent.getStringExtra("title");
		block=Integer.parseInt(value);
		
		switch (block) {
		case 0:
			
			strUrl=Url.HOTPAGEDETIALURL0;
			class_id = 0;
			//strUrl+=Integer.toString(block);
			break;
		case 1:
			strUrl=Url.HOTPAGEDETIALURL1;
			class_id = 1;
			//strUrl+=Integer.toString(block);
			break;
		case 2:
			strUrl=Url.HOTPAGEDETIALURL2;
			class_id = 2;
			//strUrl+=Integer.toString(block);
			break;
		case 3:
			strUrl=Url.HOTPAGEDETIALURL3;
			class_id = 3;
			//strUrl+=Integer.toString(block);
			break;

		default:
			break;
		}
	}
//	
//	private void ComposeUrl(int id)
//	{
//		strUrl+="&Id=";
//	}
//	
//	private void initViews()
//	{
//		viewSwitcher = (ViewSwitcher) findViewById(R.id.hotpagelistViewSwitcher);
//		listView = new MyPullToRefreshListView(this);
//	
//		
//		listView.setDivider(getResources().getDrawable(R.color.middle_gray));
//		listView.setDividerHeight(1);
////		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
////		listView.setDivider(getResources().getDrawable(
////				R.drawable.list_divider_line));
////		listView.setDividerHeight(1);
////		listView.setSelector(R.drawable.list_item_selector);
//		listView.setonRefreshListener(refreshListener);
//		listView.setOnItemClickListener(listener);
//		listView.setBackgroundColor(getResources().getColor(R.color.white));
//		viewSwitcher.addView(listView);
//		viewSwitcher.addView(getLayoutInflater().inflate(
//				R.layout.layout_progress_page, null));
//		viewSwitcher.showNext();
//		// listView.setOnItemClickListener(listener);
//
//		// 实例化底部布局
//		moreView = getLayoutInflater().inflate(R.layout.listmoredata, null);
//
//		bt = (Button) moreView.findViewById(R.id.bt_load);
//		pg = (ProgressBar) moreView.findViewById(R.id.pg);
//
//		// 加上底部view
//		bt.setOnClickListener(bt_listener);
//		
//		myHeader=(MyHeaderView)findViewById(R.id.hotpagelistheader);
//	}
//	
//	private void initVariable()
//	{
//		myHandler=new MyHandler();
//		json=new JsonHotPageListHandler();
//		list=new ArrayList<Map<String, Object>>();
//		
//	}
//	
//	private void requestData()
//	{
//		MyThread myThread=new MyThread(0);
//		new Thread(myThread).start();
//	}
//	/**
//	 * 下拉刷新监听器
//	 */
//	private OnRefreshListener refreshListener = new OnRefreshListener() {
//		@Override
//		public void onRefresh() {
////			TabScienceNetActivity.isSearch = false;
//			refreshPage();
//		}
//		
//	};
//	
//	/**
//	 * 底部加载更多按钮的监听器
//	 */
//	private OnClickListener bt_listener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			pg.setVisibility(View.VISIBLE);
//			bt.setVisibility(View.GONE);
//			MyGetMoreData myGetMoreData=new MyGetMoreData();
//			new Thread(myGetMoreData).start();
//		}
//	};
//	
//
//	/**
//	 * ListView的每个item的点击事件
//	 */
//	private OnItemClickListener listener = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
////			if (position == 0) {
////				return;
////			} else if (position == 1 && adList != null && adList.size() != 0) { // 打开广告链接
////				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(adList
////						.get(0).getLink())));
////			} else {
////				redirectDetailActivity(position - 2); // 打开显示具体的新闻的页面
////			}
//			
//				String url=(String) list.get(position-1).get("filename");
//				Log.v("test", url); 
//				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
//				intent.setClass(HotPageListActivity.this, CommonContentActivity.class);//从哪里跳到哪里
//				intent.putExtra("url", url);//传递数据
//				intent.putExtra("title", title);
//				intent.putExtra("act_class", "热点新闻");
//				startActivity(intent);
//
//			
//
//		}
//	};
//	
//	
//	private class HotItemComparator implements Comparator<Map<String,Object>>{
//
//		@Override
//		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
//			// TODO Auto-generated method stub
//			int i1 = (Integer) lhs.get("id");
//			int i2 = (Integer) rhs.get("id");
//			if(i1 < i2)
//				return 1;
//			else if(i1 > i2)
//				return -1;
//			return 0;
//		}
//
//
//		
//	};
//	
//	private HotItemComparator hot_item_comparator = new HotItemComparator();
//	
//   	
//	/**
//	 * 打开显示具体的新闻内容的activity
//	 * 
//	 * @param listIndex
//	 *            触发点击事件的那条新闻在新闻list里面的索引
//	 */
//	private void redirectDetailActivity(int listIndex) {
//		Intent intent = new Intent();
////		try {
////			intent.setClass(ScienceNetNewsActivity.this,
////					NewsDetailActivity.class);
////			Bundle bundle = new Bundle();
////			// 将要传递给显示具体新闻页面的数据放入Bundle
////			String tmpId = list.get(listIndex).getId(); // 触发点击事件的那条新闻的ID
////			ArrayList<String> tmpNewsIdList = new ArrayList<String>(); // 当前的新闻的ID列表
////			ArrayList<String> tmpNewsDescriptionList = new ArrayList<String>();
////			for (XmlItemNews item : list) {
////				tmpNewsIdList.add(item.getId());
////				tmpNewsDescriptionList.add(item.getDescription());
////			}
////			int tmpIndex = listIndex; // 触发点击事件的那条新闻所在新闻list里面的索引
////			String tmpDescription = list.get(listIndex).getDescription(); // 触发点击事件的那条新闻的描述
////
////			bundle.putString("current_news_id", tmpId);
////			bundle.putStringArrayList("news_id_list", tmpNewsIdList);
////			bundle.putStringArrayList("news_description_list",
////					tmpNewsDescriptionList);
////			bundle.putInt("current_news_index", tmpIndex);
////			bundle.putString("current_news_description", tmpDescription);
////
////			intent.putExtras(bundle);
////
////			startActivityForResult(intent, 0);
////		} catch (Exception ex) {
////			ex.printStackTrace();
////		}
//	}
//
//	private class MyThread implements Runnable
//	{
//		private int index=0;
//		public MyThread(int temp)
//		{
//			index=temp;
//		}
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			URL url;
//			try {
//				//url = new URL(myApplication.ComposeToken(strUrl+Integer.toString(0)));
//				url = new URL(strUrl);
//				URLConnection con = url.openConnection();
//				con.connect();
//				InputStream input = con.getInputStream();
//				List<Map<String,Object>> temp = json.getListItems(input);
//				if(temp != null){
//					
//					Collections.sort(temp, hot_item_comparator);
//					list.addAll(0, temp);
//					myHandler.sendEmptyMessage(1);
//				}
//
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		
//	}
//	
//	private class MyGetMoreData implements Runnable
//	{
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			URL url;
//			try {
//				//ComposeUrl(Integer.parseInt(list.get(list.size()-1).get("id")));
//				//url = new URL(myApplication.ComposeToken(strUrl+Integer.toString(Integer.parseInt((String)list.get(list.size()-1).get("id")))));
//				
//				int oldest_id = (Integer) list.get(list.size() - 1).get("id");
//				strUrl = Url.composeHotPageUrl(type_id, class_id, oldest_id, -1);
//				url = new URL(strUrl);
//				URLConnection con = url.openConnection();
//				con.connect();
//				InputStream input = con.getInputStream();
//				List<Map<String, Object>> temp;
//				temp=json.getListItems(input);
//				if (temp!=null) {
//					list.addAll(list.size() - 1, temp);
//					
//					myHandler.sendEmptyMessage(2);
//				}
//				else {
//					myHandler.sendEmptyMessage(3);
//				}
//				
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		
//	}
//	
//	
//	
//	
//	
//	
//	/**
//	 * 刷新当前页面
//	 */
//	private void refreshPage() {
//		new AsyncTask<String, Integer, String>() {
//			@Override
//			protected String doInBackground(String... params) {
//				//getNewsList();
//				int newest_id = (Integer) list.get(0).get("id");
//				strUrl = Url.composeHotPageUrl(type_id, class_id, -1, newest_id);
//				URL url;
//				try {
//					//url = new URL(myApplication.ComposeToken(strUrl+Integer.toString(0)));
//					url = new URL(strUrl);
//					URLConnection con = url.openConnection();
//					con.connect();
//					InputStream input = con.getInputStream();
//					List<Map<String,Object>> temp  = json.getListItems(input);
//					
//					if(temp != null){
//						list.addAll(0, temp);
//						myHandler.sendEmptyMessage(1);
//					}
//					
//
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				
//				myHandler.sendEmptyMessage(4);
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				if (myListAdapter != null) {
//					myListAdapter.notifyDataSetChanged();
//					listView.onRefreshComplete();
//				}
//			}
//		}.execute("begin");
//	}
//	private class MyHandler extends Handler
//	{
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			if (msg.what==1) {
//				viewSwitcher.setDisplayedChild(0);
//				myListAdapter=new MyListAdapter(list, null);
//				listView.setAdapter(myListAdapter);
//				listView.addFooterView(moreView);
//			}
//			else if (msg.what==2) {
//				myListAdapter.notifyDataSetChanged();
//				pg.setVisibility(View.GONE);
//				bt.setVisibility(View.VISIBLE);
//			}
//			else if (msg.what==3) {
//				pg.setVisibility(View.GONE);
//				bt.setVisibility(View.GONE);
//			}
//			else if (msg.what==4) {
//				myListAdapter.notifyDataSetChanged();
//				listView.onRefreshComplete();
//			}
//			super.handleMessage(msg);
//		}
//		
//	}
//	
//	 private class MyListAdapter extends BaseAdapter
//	    {
//	    	private List<Map<String, Object>> list=null;
//	    	private String name=null;
//			AsyncImageLoader asyncImageLoader = new AsyncImageLoader(
//					AppUtil.ITEM_IMG_WIDTH, AppUtil.ITEM_IMG_HEIGHT);
//	    	
//	    	public MyListAdapter(List<Map<String, Object>> L,String N)
//	    	{
//	    		list=L;
//	    		name=N;
//	    	}
//
//			@Override
//			public int getCount() {
//				// TODO Auto-generated method stub
//				if (list!=null) {
//					return list.size();
//				}
//				else {
//					return 0;
//				}
//			}
//
//			@Override
//			public Object getItem(int position) {
//				// TODO Auto-generated method stub
//				if (list!=null) {
//					return list.get(position);
//				}
//				else {
//					return null;
//				}
//				
//			}
//
//			@Override
//			public long getItemId(int position) {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//
//			@Override
//			public View getView(final int position, View convertView, ViewGroup parent) {
//				// TODO Auto-generated method stub
////				if (position==0) {
////					convertView = getLayoutInflater().inflate(R.layout.hotpageitemsmain,
////							null);
////				}
////				else if (position>0)
//				{
//					convertView = getLayoutInflater().inflate(R.layout.hotpageitems,
//							null);
//					TextView tView=(TextView)convertView.findViewById(R.id.hotpageItemsTextView);
//					final ImageView imageView=(ImageView)convertView.findViewById(R.id.hotpageItemsImageView);
//					
//					String temp=(String) list.get(position).get("title");
//					String urlString=(String) list.get(position).get("imgurl");
//					tView.setText(temp);
//					Drawable db=(Drawable)list.get(position).get("drawable");
//					if (db!=null) {
//						imageView.setImageDrawable(db);
//					}
//					else {
//						Drawable cachedImage = asyncImageLoader.loadDrawable(
//								urlString, new ImageCallback() {
//									
//									@Override
//									public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//										// TODO Auto-generated method stub
//										if (imageView != null && imageDrawable != null) {
//											imageView.setImageDrawable(imageDrawable);
//											list.get(position).put("drawable", imageDrawable);
//											// Log.e("在回调里面设置好图片", "liushuai");
//										} else {
//											try {
//												imageView.setImageResource(R.drawable.sync);
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
//					
//				}
//				return convertView;
//			}
//	    	
//	    }
//	
}
