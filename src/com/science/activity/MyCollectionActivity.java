package com.science.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.interfaces.ListComparator;
import com.science.json.JsonGetCollectionHandler;
import com.science.services.MyApplication;
import com.science.util.ShoucangUtil;
import com.science.util.Url;
import com.science.view.MyListView;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView; 
import android.widget.AbsListView.OnScrollListener; 
import android.widget.ListView; 
import android.widget.SimpleAdapter; 

public class MyCollectionActivity extends Activity {


	private MyApplication myApplication=null;
	private ImageButton headerback=null;
	private TextView headertitle;
	private ListView myListView=null;
	private List<Map<String, Object>> myList;
	private JsonGetCollectionHandler jsonGetCollectionHandler;
	private MyHandler handler;
	private MyAdapter myAdapter=null;
	private Integer lastId=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myApplication=(MyApplication)this.getApplication();
		
		// 去掉顶部灰条
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mycollection);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		
		InitVariable();
		InitViews();
		InitData();
		SetListener();
		
		GetMyCollection();
	}
	
	private void InitVariable()
	{
		myList=new ArrayList<Map<String,Object>>();
		myAdapter = new MyAdapter();
		jsonGetCollectionHandler=new JsonGetCollectionHandler();
		handler=new MyHandler();


	}
	
	private void InitViews()
	{
		headerback=(ImageButton)findViewById(R.id.settingback);
		headertitle=(TextView)findViewById(R.id.settingheadertitle);
		myListView=(ListView)findViewById(R.id.myCollectionList);
		myListView.setAdapter(myAdapter);
		myListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyCollectionActivity.this,CommonContentActivity.class);
				intent.putExtra("theme",myList.get(position).get("title").toString());
				intent.putExtra("act_class", "我的收藏");
				intent.putExtra("url", myList.get(position).get("url").toString());
				startActivity(intent);
			}
			
		});
	}
	
	private void InitData()
	{
		
		headertitle.setText("我的收藏");
		headerback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	
	
	
	private void SetListener()
	{
		//myInfo.setOnClickListener(onClickListener);
		myListView.setOnScrollListener(new OnScrollListenerImple()); 
	}
	
	private void GetMyCollection()
	{
		MyThreadGetMyCollection myThreadGetMyCollection=new MyThreadGetMyCollection();
		new Thread(myThreadGetMyCollection).start();
	}
	
	private class MyAdapter extends BaseAdapter
	{

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
			ViewHolder holder = null;
			
			if(convertView == null){
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.mycollectionitem,
						null);
				holder.titleTv =(TextView)convertView.findViewById(R.id.myCollectionItemTitle);
				holder.timeTv =(TextView)convertView.findViewById(R.id.myCollectionItemsTime);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(holder != null){
				
				String strtitle=(String) myList.get(position).get("title");
				String strTime=(String)myList.get(position).get("description");
				holder.titleTv.setText(strtitle);
				holder.timeTv.setText(strTime);
			}
			

			return convertView;
		}
		
		
		
		
		class ViewHolder{
			TextView titleTv;
			TextView timeTv;
		}
		
		
		
		
		
		
	}

	private class MyThreadGetMyCollection implements Runnable
    {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			URL url;
			try {
				//String strUrl=Url.getCollection+"&id=";
				String tempLastId=lastId.toString();
				//strUrl+=tempLastId;
				String strUrl ;
				strUrl = Url.composeGetCollectionUrl(tempLastId);
				url = new URL(strUrl);
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				List<Map<String, Object>> tempList=null;
				jsonGetCollectionHandler = new JsonGetCollectionHandler();
				tempList=jsonGetCollectionHandler.getListItems(input);
				
				if (tempList!=null) {
					for (int i = 0; i < tempList.size(); i++) {
						myList.add(tempList.get(i));
					}

					lastId= (Integer) myList.get(myList.size()-1).get("id");
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
    			if (myAdapter==null) {
    				
        			myAdapter=new MyAdapter();
        			myListView.setAdapter(myAdapter);
        			
        			//更新本地数据库中的收藏信息
        			ShoucangUtil su = new ShoucangUtil(MyCollectionActivity.this);
//        			for(int i = 0;i < myList.size();i++){
//        				
//        				su.addToLocalShoucang(myApplication.user_name, (Integer)myList.get(i).get("article_type"), 
//        						(Integer)myList.get(i).get("article_id"), (String)myList.get(i).get("title"),(String)myList.get(i).get("time"),(String)myList.get(i).get("url"));
//        			}
				}
    			else
    			{
    				myAdapter.notifyDataSetChanged();
    			}

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

    private class OnScrollListenerImple implements OnScrollListener{ 
        @Override 
        public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) { 
            
        } 
   


		@Override 
        public void onScrollStateChanged(AbsListView listview, int scrollState) { 
	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
	            // 判断是否滚动到底部  
	            if (listview.getLastVisiblePosition() == listview.getCount() - 1) {  
	                //加载更多功能的代码  
	            	addDataForListView(); 
	            }  
	        } 
        } 
           
    }

    private void addDataForListView() {
		// TODO Auto-generated method stub
    	GetMyCollection();
	}
}
