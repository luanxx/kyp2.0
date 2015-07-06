package com.science.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.science.R;
import com.science.activity.CommonContentActivity;
import com.science.activity.ProjectApplyActivity;
import com.science.fragment.ProjectApplyFragment.ViewHolder;
import com.science.json.JsonDcumentListHandler;
import com.science.json.JsonProgramListHandler;
import com.science.json.JsonProjectSelectListHandler;
import com.science.services.DataCache;
import com.science.services.MyApplication;
import com.science.util.DefaultUtil;
import com.science.util.MergeListUtil;
import com.science.util.Url;
import com.science.view.MyPullToRefreshListView;
import com.science.view.MyPullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class MainProjectApplyFragment extends Fragment {

	
	
	private MyApplication myApplication=null;

	private String str_url_proj_src;


	private WindowManager wm;

	
	private MyPullToRefreshListView  proj_list_view;
	private ProjListAdapter proj_list_adapter;

	private View proj_more_view;
	private View proj_footer_view = null;
	private JsonProgramListHandler json = null;
	private JsonProjectSelectListHandler json_select = null;
	private MyApplication application;
	private static final int UPDATE_TAG_VIEW = 0x00; 
	private static final int UPDATE_TAG_VIEW2 = 0x01; 
    private static final int UPDATE_USUAL_PROJ_LIST = 0x02;
    private static final int UPDATE_HOT_PROJ_LIST = 0x03;
    private static final int UPDATE_PROJ_LIST_FAIL = 0x04;
    private static final int REFRESH_COMPLETE = 0x05;
    private static final int UPDATE_EXPIRE_PROJ_LIST = 0x06;
    private static final int UPDATE_PROJ_LIST = 0x07;
	private List<Map<String,Object>> proj_list;
    
    private Activity activity;
    private View view;
    
    //表示项目类型
    //private int type;
    private int source;
    private int tab;
    private String str_url;//表示当前获取项目列表的url,由它的activity传
    
    
    private int proj_src1 = -1;
    private int proj_src2 = -1;
    private int proj_type = -1;
    private int proj_id = DefaultUtil.MAX_VALUE;
    private String proj_date = "";
    private int last_id = 0;
    private String last_date = "";
    private List<Map<String,Object>> saved_proj_list;
    boolean pullable = false;//防止下拉刷新时加载更多
    
    boolean loadable = true;//判断当前是否能够进行重新更新，防止用户反复点击
    
    
    private MergeListUtil merger = new MergeListUtil();
    
    private LayoutInflater inflater;
    
    private final boolean BACKWARD = true;
    private final boolean FORWARD = false;
    private boolean direction = BACKWARD;//表示是下拉刷新还是加载更多,true表示加载更多，false表示下拉刷新
	

    
    public MainProjectApplyFragment(int tab){
    	this.tab = tab;
    }
    
    
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    	
    	this.activity = this.getActivity();
    	view = inflater.inflate(R.layout.project_fragment_list, null);
    	this.inflater = inflater;
		initVariable();
		initView();
	    if(DataCache.usual_project_list!=null){
	    	if(!DataCache.usual_project_list.isEmpty()){
	    		DataCache.usual_project_list.clear();
	    	}
	    }
		//setOnClickListener();
    	return view;
    }
    
    
	private void initVariable()
	{
		
		
		str_url_proj_src = Url.ProjectUsual;
		json = new JsonProgramListHandler();
		json_select = new JsonProjectSelectListHandler();
		application = (MyApplication) activity.getApplication();
		proj_list = new ArrayList<Map<String,Object>>();
        
		new Timer().schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!loadable && direction == BACKWARD){
					handler.sendEmptyMessage(ADD_FOOTER_VIEW);
				}
			}
			
		}, 0,100);

	}
    
	private void initView()
	{
		proj_list_view = (MyPullToRefreshListView) view.findViewById(R.id.proj_list_view);
		proj_list_view.setonRefreshListener(refreshListener);
		proj_list_adapter = new ProjListAdapter();
		proj_list_view.setAdapter(proj_list_adapter);
		proj_list_view.setCacheColorHint(Color.argb(255, 0, 0, 0));
		proj_list_view.setSelector(R.drawable.list_item_selector);
		proj_list_view.setOnItemClickListener(proj_item_click_listener);
		proj_list_view.setOnScrollListener(new OnScrollListenerImple());
		/*加载更多按钮*/
		proj_more_view = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.listmoredata, null);
		proj_footer_view =  inflater.inflate(R.layout.comm_list_footer, null);
		
	}
    
    
    
	
	
	
	
	private OnItemClickListener proj_item_click_listener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			int index = position - 1;
			String url = proj_list.get(index).get("url").toString();
			Intent intent = new Intent();//用以传递数据
			intent.setClass(activity, CommonContentActivity.class);
			intent.putExtra("url", url);
			
			String act_class = "";
			if(tab == ProjectApplyActivity.PROJ_HOT)
			   act_class = "热点项目";
			else if(tab == ProjectApplyActivity.PROJ_EXPIRE)
			   act_class = "即将到期";
			intent.putExtra("act_class", act_class);
			intent.putExtra("articleType", proj_list.get(index).get("articleType").toString());
			intent.putExtra("id", (Integer)proj_list.get(index).get("id"));
			intent.putExtra("theme", proj_list.get(index).get("title").toString());
			startActivity(intent);
		}
		
	};
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param id
	 * 每次loadHotProject之前先看暂存中hot_project_list是否为空，如果为空，则申请一个
	 */
	
	public void loadHotProject(int id){
		
		if(DataCache.hot_project_list == null){
			DataCache.hot_project_list = new ArrayList<Map<String,Object>>();
		}
		if(!DataCache.hot_project_list.isEmpty())
		{
			if(proj_list != null)
			{
				merger.mergeTwoListBackward(proj_list, DataCache.hot_project_list);

			}
			else
			{
				proj_list = new ArrayList<Map<String,Object>>();
				proj_list.addAll(DataCache.hot_project_list);
			}
			if(proj_list_adapter != null)
			{
			  proj_list_adapter.notifyDataSetChanged();
			  //proj_list_view.setAdapter(proj_list_adapter);
			}
		}	
//		else
//		{
//		if(proj_list != null)
//		{
//			proj_list.clear();
//			if(proj_list_adapter != null){
//				proj_list_adapter.notifyDataSetChanged();
//			}
//		}
		final String strUrl = Url.composeHotProjectUrl(id);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try{
			    Thread.sleep(200);
				URL url = new URL(strUrl);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
			    json = new JsonProgramListHandler();
			    List<Map<String,Object>> temp = null;
			    temp = json.getListItems(is);
			    if(temp != null){	
			    	for(int i = 0;i < temp.size();i++){
			    		String title = "[置顶]" + temp.get(i).get("title");
			    		temp.get(i).put("title", title);
			    	}
			    	merger.mergeTwoList(DataCache.hot_project_list, temp,direction);
			    	if(merger.mergeTwoList(proj_list, DataCache.hot_project_list,direction))
			    	handler.sendEmptyMessage(LOAD_PROJECT_OK);
			    	else{
			    		handler.sendEmptyMessage(LOAD_PROJECT_FAIL);
			    	}
			    }
			    else
			    {
			    	handler.sendEmptyMessage(LOAD_PROJECT_FAIL);
			    }
			   
			 }catch(MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			
			
		}).start();	
	
		//}
	}
	
	
	
	public void loadUsualProject(int project_src1,int project_src2,int project_type,int id,String date)
	{

		if(DataCache.usual_project_list == null){
			DataCache.usual_project_list = new ArrayList<Map<String,Object>>();
		}
//		if(!DataCache.usual_project_list.isEmpty()){
			
			if(proj_list != null)
			{
				merger.mergeTwoListBackward(proj_list, DataCache.usual_project_list);
			}
//			else
//			{
//				proj_list = new ArrayList<Map<String,Object>>();
//				proj_list.addAll(DataCache.usual_project_list);
//			}	
			if(proj_list_adapter != null)
			{
			proj_list_adapter.notifyDataSetChanged();
			//proj_list_view.setAdapter(proj_list_adapter);
			}
	//	}

		str_url = Url.composeUsualProject(proj_src1, proj_src2, proj_type, id, date);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try{
			    
				Thread.sleep(500);	
				URL url = new URL(str_url);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
			    json = new JsonProgramListHandler();
			    List<Map<String,Object>> temp = null;
			    temp = json.getListItems(is);
			    if(temp != null){	    	
			    	merger.mergeTwoList(DataCache.usual_project_list, temp,direction);
			    	if(merger.mergeTwoList(proj_list, DataCache.usual_project_list,direction))
			    	{
			    	   handler.sendEmptyMessage(LOAD_PROJECT_OK);
			    	}
			    	else{
			    		handler.sendEmptyMessage(LOAD_PROJECT_FAIL);
			    	}
			    }
			    else
			    {
			    	handler.sendEmptyMessage(LOAD_PROJECT_FAIL);
			    }
			   
			 }catch(MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}).start();
		
		
	}
	
	
	
	public void loadExpireProject(int project_src1,int project_src2,int project_type,int id,String date)
	{
		if(DataCache.expire_project_list == null){
			DataCache.expire_project_list = new ArrayList<Map<String,Object>>();
		}
		if(DataCache.expire_project_list.isEmpty()){
			
			if(proj_list != null)
			{
				merger.mergeTwoListBackward(proj_list, DataCache.expire_project_list);
			}
			else
			{
				proj_list = new ArrayList<Map<String,Object>>();
				proj_list.addAll(DataCache.expire_project_list);
			}	
			if(proj_list_adapter != null)
			{
			proj_list_adapter.notifyDataSetChanged();
			//proj_list_view.setAdapter(proj_list_adapter);
			}
		}
		

		str_url = Url.composeExpireProjectUrl(proj_src1, proj_src2, proj_type, id, date);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try{
				
			    Thread.sleep(500);	
				URL url = new URL(str_url);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
			    json = new JsonProgramListHandler();
			    List<Map<String,Object>> temp = null;
			    temp = json.getListItems(is);
			    if(temp != null){	    	
			    	merger.mergeTwoList(DataCache.expire_project_list, temp,direction);
			    	if(merger.mergeTwoList(proj_list, DataCache.expire_project_list,direction))
			    	handler.sendEmptyMessage(LOAD_PROJECT_OK);
			    	else{
			    		handler.sendEmptyMessage(LOAD_PROJECT_FAIL);
			    	}
			    }
			    else
			    {
			    	handler.sendEmptyMessage(LOAD_PROJECT_FAIL);
			    }
			   
			 }catch(MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}).start();
	}
	
	
	
	/***
	 * 
	 * @param proj_src1
	 * @param proj_src2
	 * @param proj_type
	 * @param proj_id
	 * @param date
	 *
	 */
	
	
	public void loadProject(int proj_src1,int proj_src2,int proj_type,int proj_id,String date)
	{

		
		//项目类型或者来源改变了
        if(this.proj_src1 != proj_src1 || this.proj_src2 != proj_src2 || this.proj_type != proj_type)
        {
        	if(proj_list != null)
        	{
        		proj_list.clear();
        		if(this.tab == ProjectApplyActivity.PROJ_HOT)
        		{
        		  if(DataCache.usual_project_list != null)
        			   DataCache.usual_project_list.clear();
        		}
        		else if(this.tab == ProjectApplyActivity.PROJ_EXPIRE)
        		{
        		if(DataCache.expire_project_list != null)
        			DataCache.expire_project_list.clear();
        		}
        		if(proj_list_adapter != null)
        		{
        			proj_list_adapter.notifyDataSetChanged();
        			proj_list_view.setAdapter(proj_list_adapter);
        		}
        	}
        	
        	loadable = true;
        }
        	
		this.proj_src1 = proj_src1;
		this.proj_src2 = proj_src2;
		this.proj_type = proj_type;
		this.proj_id = proj_id;
		this.proj_date = date;
		
		if(!loadable)
		return;
		

		
		if(tab == ProjectApplyActivity.PROJ_HOT){
			
			loadable = false;
			//loadHotProject(0);
			loadUsualProject(proj_src1,proj_src2,proj_type,proj_id,date);
			
		}else if(tab == ProjectApplyActivity.PROJ_EXPIRE)
		{
			loadable = false;
			loadExpireProject(proj_src1,proj_src2,proj_type,proj_id,date);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	private final int LOAD_HOT_PROJECT_OK = -1;
	private final int LOAD_PROJECT_OK = 0;
	private final int LOAD_PROJECT_FAIL = 1;
	private final int ONREFRESHCOMPLETE = 2;
	private final int ADD_FOOTER_VIEW = 3;
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			  
			
			case LOAD_PROJECT_OK:
				if(proj_list_adapter != null)
				{
				proj_list_adapter.notifyDataSetChanged();
				//proj_list_view.setAdapter(proj_list_adapter);
				
				}
				if(proj_list != null)
					if(proj_list.size() > 0)
					{
						last_id = (Integer) proj_list.get(proj_list.size() - 1).get("id");
						last_date = (String) proj_list.get(proj_list.size() - 1).get("startTime");
					}
				
				
				if(proj_list_view.getFooterViewsCount() > 0)
					for(int  i =0;i < proj_list_view.getFooterViewsCount();i++)
					proj_list_view.removeFooterView(proj_footer_view);
				loadable = true;
				handler.sendEmptyMessage(ONREFRESHCOMPLETE);
				break;		
			case LOAD_PROJECT_FAIL:
				if(proj_list_adapter != null)
				{
				proj_list_adapter.notifyDataSetChanged();
				//proj_list_view.setAdapter(proj_list_adapter);
				if(proj_list_view.getFooterViewsCount() > 0)
					for(int  i =0;i < proj_list_view.getFooterViewsCount();i++)
					proj_list_view.removeFooterView(proj_footer_view);
				}
				//Toast.makeText(activity, "暂无相关信息", Toast.LENGTH_SHORT).show();
				loadable = true;
				handler.sendEmptyMessage(ONREFRESHCOMPLETE);
				break;
			case ONREFRESHCOMPLETE:
				//proj_list_adapter.notifyDataSetChanged();
				proj_list_view.onRefreshComplete();
				loadable = true;
				break;
				
			case ADD_FOOTER_VIEW:
				if(proj_list_view != null && proj_footer_view != null&& direction == BACKWARD)
				{
					if(proj_list_view.getFooterViewsCount() <= 0)
						proj_list_view.addFooterView(proj_footer_view);
				}
				break;
			default:
				break;
			}
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
//			TabScienceNetActivity.isSearch = false;
			
			refreshPage();
		}
		
	};
    
	
	
	
	
	
	
	private void refreshPage(){
		if(loadable)
		{
		direction = FORWARD;
        loadProject(this.proj_src1,this.proj_src2,this.proj_type,0,DefaultUtil.EMPTY);
		}
	}
	
	
	
	
	
	  class ViewHolder{
		public   TextView title_tv;
		public   TextView like_num_tv;
		public   TextView comment_num_tv;
		public   TextView start_time_tv;
		public   TextView end_time_tv;
		public   ImageView project_flag_iv;
	  }
	
	
	

	private class ProjListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return proj_list.size();
		}

		@Override
		public Object getItem(int item) {
			// TODO Auto-generated method stub
			return proj_list.get(item);
		}

		@Override
		public long getItemId(int id) {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view =  inflater.inflate(R.layout.proj_list_item, null);
				//LinearLayout proj_item_root_view = (LinearLayout) view.findViewById(R.id.proj_item_root);
				holder.title_tv = (TextView) view.findViewById(R.id.proj_title);
				holder.like_num_tv = (TextView) view.findViewById(R.id.proj_like_num_text);
				holder.comment_num_tv = (TextView)view.findViewById(R.id.proj_comment_num_text);
				holder.start_time_tv = (TextView) view.findViewById(R.id.start_time);
				holder.end_time_tv = (TextView) view.findViewById(R.id.end_time);
				holder.project_flag_iv = (ImageView) view.findViewById(R.id.proj_expire_flag);
				

				String title = (String) proj_list.get(position).get("title");
				holder.title_tv.setText(title);
				
				
				String like_num = "" + proj_list.get(position).get("diggtop");
				holder.like_num_tv.setText(like_num);
				
				String comment_num = "" + proj_list.get(position).get("plnum");		
				holder.comment_num_tv.setText(comment_num);
				
				
				String start_time = (String) proj_list.get(position).get("startTime");
				holder.start_time_tv.setText(start_time);
				
				
				String end_time = (String) proj_list.get(position).get("endTime");
				holder.end_time_tv.setText(end_time);
				
				convertView = view;
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title_tv.setText((String) proj_list.get(position).get("title"));
			holder.like_num_tv.setText("" + proj_list.get(position).get("diggtop"));
			holder.comment_num_tv.setText("" + proj_list.get(position).get("plnum"));
			holder.start_time_tv.setText((String) proj_list.get(position).get("startTime"));
			holder.end_time_tv.setText((String) proj_list.get(position).get("endTime"));
			

			switch((Integer)proj_list.get(position).get("mark")){
		     
			case 0://置顶
				holder.project_flag_iv.setBackgroundResource(R.drawable.icon_top);
				break;
			case 1://新开始
				holder.project_flag_iv.setBackgroundResource(R.drawable.shape_icon_dot_green);
				break;
			case 2://正申报
				holder.project_flag_iv.setBackgroundResource(R.drawable.shape_icon_dot_green);
				break;
			case 3://即将到期
				holder.project_flag_iv.setBackgroundResource(R.drawable.shape_icon_dot_red);
				break;
			case 4://到期
				holder.project_flag_iv.setBackgroundResource(R.drawable.shape_icon_dot_gray);
				break;
			default:
				break;
			}
			
			
			/*如果是置顶（热门）项目就去掉前面的过期标志*/
//			if(tab == ProjectApplyActivity.PROJ_HOT)
//				
//				
//				
//				holder.expire_flag_iv.setVisibility(View.INVISIBLE);
//			else{
//				//int mark = (Integer) proj_list.get(position).get("mark");
//				//if(mark == -1)
//				//	holder.expire_flag_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_green_point));
//				//else if(mark == 1)
//					holder.expire_flag_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_red_point));
//			}
			
			
			return convertView;
		}
		public void clearList(List<File> f) {
            int size = f.size();
            if (size > 0) {
                    f.removeAll(f);
                    proj_list_adapter.notifyDataSetChanged();
            }
    }
	}
	
    
    
	
	
	

	
	
	
	
	
	
    private class OnScrollListenerImple implements OnScrollListener{ 
    	
    	
    	
        @Override 
        public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) { 
            
//        	if(visibleItemCount + 1 < totalItemCount)
//        		pullable = true;

        } 
   


		@Override 
        public void onScrollStateChanged(AbsListView listview, int scrollState) { 
	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
	            // 判断是否滚动到底部  
	            if(( (listview.getLastVisiblePosition() + 1 == listview.getCount()) || listview.getLastVisiblePosition() == listview.getCount() - 2)&& loadable) {  
	                //加载更多功能的代码  
	            	addDataForListView(); 
	            }  
	        } 
        } 
           
    }
	
	
	
    private void addDataForListView() {
    	//proj_list_view.addFooterView(proj_footer_view);
    	direction = BACKWARD;
    	loadProject(this.proj_src1,this.proj_src2,this.proj_type,this.last_id,this.last_date);
    	
    }
	
	
	
}
