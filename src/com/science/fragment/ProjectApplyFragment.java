package com.science.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.science.R;
import com.science.activity.CommonContentActivity;
import com.science.activity.ProjectApplyActivity;
import com.science.interfaces.ListComparator;
import com.science.json.JsonHotPageListHandler;
import com.science.json.JsonProgramListHandler;
import com.science.json.JsonProjectSelectListHandler;
import com.science.model.FirstClassItem;
import com.science.model.SecondClassItem;
import com.science.services.DataCache;
import com.science.services.MyApplication;
import com.science.util.DefaultUtil;
import com.science.util.Url;
import com.science.view.MyHeaderView;
import com.science.view.MyImageButton;
import com.science.view.MyPullToRefreshListView;
import com.science.view.MyPullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class ProjectApplyFragment extends Fragment {

	private MyApplication myApplication=null;

	private String str_url_proj_src;


	private WindowManager wm;

	
	private MyPullToRefreshListView  proj_list_view;
	private ProjListAdapter proj_list_adapter;

	private View proj_more_view;
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
    
    public ProjectApplyFragment(){
    	
    }
    
    
    
//    public void resetFragment(int tab){
//    	
//    	if(tab == ProjectApplyActivity.PROJ_HOT){
//    		if(proj_list != null){
//    		    proj_list.clear();
//    		    
//    		}
//    	}
//    }
    
    
    public ProjectApplyFragment(int tab,int src1,int src2,int type,int id,String date,List<Map<String,Object>> list){
    	this.tab = tab;
    	this.proj_src1 = src1;
    	this.proj_src2 = src2;
    	this.proj_type = type;
    	this.proj_id = id;
    	this.proj_date = date;
    	this.saved_proj_list = list;

    	

    }

    

    
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    	
    	
    	this.activity = this.getActivity();
    	view = inflater.inflate(R.layout.project_fragment_list, null);
		initVariable();
		initView();
	   
		//setOnClickListener();
    	return view;
    }
    
    
    

    
    public void updateProjectFragment(int src1 ,int src2,int type,int id,String date){
    	/*不更新*/
    	if(proj_src1 == src1 && proj_src2 == src2 && proj_type == type)
    	   return;
    	
        if(DataCache.usual_project_list != null){
        	DataCache.usual_project_list.clear();
        }
    	
    	if(proj_list != null){
    		proj_list.clear();
    		proj_list_adapter.notifyDataSetChanged();
    	   // handler.sendEmptyMessage(UPDATE_PROJ_LIST);
    	}
    	
        
		this.proj_src1 = src1;
		this.proj_src2 = src2;
		this.proj_type = type;
		this.proj_id = id;
		this.proj_date = date;
    	
    	
    	
    	if(loadable)
    	{
    		
    	
    	if(tab == ProjectApplyActivity.PROJ_HOT){//默认状态下是热点项目
    		if(DataCache.hot_project_list  != null){
    			
    	        proj_list.addAll(DataCache.hot_project_list);
    	       // proj_list_adapter.notifyDataSetChanged();
    			handler.sendEmptyMessage(UPDATE_HOT_PROJ_LIST);
    		}
    		else{
    			 str_url =Url.composeHotProjectUrl(0);
    			 loadable = false;//正在更新数据不允许更新数据	
    			 new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try{
						URL url = new URL(str_url);
						URLConnection conn = url.openConnection();
						conn.connect();
						InputStream is = conn.getInputStream();
					    json = new JsonProgramListHandler();
					    List<Map<String,Object>> temp = null;
					    temp = json.getListItems(is);
					    if(temp != null){	    	
					    	proj_list = temp;	
					    	handler.sendEmptyMessage(UPDATE_HOT_PROJ_LIST);
					    }
					    else
					    {
					    	handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
					    }
					    
					    
					 }catch(MalformedURLException e)
						{
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
    				 
    			 }).start();
    		}
    		
    		
    	}
    	else{
    		str_url = Url.composeExpireProjectUrl(this.proj_src1,this.proj_src2,this.proj_type,-1,DefaultUtil.EMPTY);
			loadable = false;
    		new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
					URL url = new URL(str_url);
					URLConnection conn = url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
				    json = new JsonProgramListHandler();
				    List<Map<String,Object>> temp = null;
				    temp = json.getListItems(is);
				    if(temp != null){	    	
				    	proj_list = temp;
				    	handler.sendEmptyMessage(UPDATE_EXPIRE_PROJ_LIST);
				    }
				    else{
				    	handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
				    }
				 }catch(MalformedURLException e)
					{
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				 
			 }).start();
    	}
		// requestData();//刚启动的时候刷新一次数据
        

    	
    	
    	
    	

   }
		//requestData();
 }
    
    
    
    public void saveProjFragment(List<Map<String,Object>> list){
    	
    	if(list != null) 	
    		list.clear();
    	else
    		list = new ArrayList<Map<String,Object>>();
    	if(proj_list != null){
    		list.addAll(proj_list);
    	}
    }
    
    
    
    public void reLoadProjFragment(List<Map<String,Object>> list){
        //  doc_list = new ArrayList<Map<String,Object>>();
      	if(proj_list!=null && list!=null)
      	{
      		proj_list.clear();
      		proj_list.addAll(list);
      	}
      	
          handler.sendEmptyMessage(UPDATE_USUAL_PROJ_LIST);
      }
    
    
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	
//    	if(loadable){
//
//    	if(tab == ProjectApplyActivity.PROJ_HOT){//默认状态下是热点项目
//    		if(DataCache.usual_project_list == null)
//    			DataCache.usual_project_list = new ArrayList<Map<String,Object>>();
//    		if(DataCache.hot_project_list != null){
//    			proj_list.addAll(DataCache.hot_project_list);
//    		    handler.sendEmptyMessage(UPDATE_HOT_PROJ_LIST);
//    		}
//    		else{
//    			 str_url =Url.composeHotProjectUrl(0);
//    			 loadable = false;
//    			 new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						try{
//						URL url = new URL(str_url);
//						URLConnection conn = url.openConnection();
//						conn.connect();
//						InputStream is = conn.getInputStream();
//					    json = new JsonProgramListHandler();
//					    List<Map<String,Object>> temp = null;
//					    temp = json.getListItems(is);
//					    if(temp != null){	    	
//					    	proj_list = temp;
//					    	handler.sendEmptyMessage(UPDATE_HOT_PROJ_LIST);
//					    }
//					    else
//					    {
//					    	handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
//					    }
//					   
//					 }catch(MalformedURLException e)
//						{
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//    				 
//    			 }).start();
//    		}
//    		
//    		
//    	}
//    	else{
//    		
//    		str_url = Url.composeExpireProjectUrl(this.proj_src1,this.proj_src2,this.proj_type,-1,DefaultUtil.EMPTY);
//			loadable = false;
//    		new Thread(new Runnable(){
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					try{
//					URL url = new URL(str_url);
//					URLConnection conn = url.openConnection();
//					conn.connect();
//					InputStream is = conn.getInputStream();
//				    json = new JsonProgramListHandler();
//				    List<Map<String,Object>> temp = null;
//				    temp = json.getListItems(is);
//				    if(temp != null){	    	
//				    	proj_list = temp;
//				    	handler.sendEmptyMessage(UPDATE_EXPIRE_PROJ_LIST);
//				    }
//				 }catch(MalformedURLException e)
//					{
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				 
//			 }).start();
//    	}
//    }
    
    }
    
    
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
    	super.setUserVisibleHint(isVisibleToUser);
    		if(isVisibleToUser){
    	    	if(loadable){
    			
    			    	if(tab == ProjectApplyActivity.PROJ_HOT){//默认状态下是热点项目
    			    		if(DataCache.usual_project_list == null)
    			    			DataCache.usual_project_list = new ArrayList<Map<String,Object>>();
    			    		if(DataCache.hot_project_list != null){
    			    			proj_list.addAll(DataCache.hot_project_list);
    			    		    handler.sendEmptyMessage(UPDATE_HOT_PROJ_LIST);
    			    		}
    			    		else{
    			    			 str_url =Url.composeHotProjectUrl(0);
    			    			 loadable = false;
    			    			 new Thread(new Runnable(){
    			
    								@Override
    								public void run() {
    									// TODO Auto-generated method stub
    									try{
    									URL url = new URL(str_url);
    									URLConnection conn = url.openConnection();
    									conn.connect();
    									InputStream is = conn.getInputStream();
    								    json = new JsonProgramListHandler();
    								    List<Map<String,Object>> temp = null;
    								    temp = json.getListItems(is);
    								    if(temp != null){	    	
    								    	proj_list = temp;
    								    	handler.sendEmptyMessage(UPDATE_HOT_PROJ_LIST);
    								    }
    								    else
    								    {
    								    	handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
    								    }
    								   
    								 }catch(MalformedURLException e)
    									{
    										e.printStackTrace();
    									} catch (IOException e) {
    										// TODO Auto-generated catch block
    										e.printStackTrace();
    									}
    								}
    			    				 
    			    			 }).start();
    			    		}
    			    		
    			    		
    			    	}
    			    	else{
    			    		
    			    		str_url = Url.composeExpireProjectUrl(this.proj_src1,this.proj_src2,this.proj_type,-1,DefaultUtil.EMPTY);
    						loadable = false;
    			    		new Thread(new Runnable(){
    			
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								try{
    								URL url = new URL(str_url);
    								URLConnection conn = url.openConnection();
    								conn.connect();
    								InputStream is = conn.getInputStream();
    							    json = new JsonProgramListHandler();
    							    List<Map<String,Object>> temp = null;
    							    temp = json.getListItems(is);
    							    if(temp != null){	    	
    							    	proj_list = temp;
    							    	handler.sendEmptyMessage(UPDATE_EXPIRE_PROJ_LIST);
    							    }
    							 }catch(MalformedURLException e)
    								{
    									e.printStackTrace();
    								} catch (IOException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								}
    							}
    							 
    						 }).start();
    			    	}
    			    }
    		}
    	
    }
    
    
    
	private void initVariable()
	{
		
		
		str_url_proj_src = Url.ProjectUsual;
		json = new JsonProgramListHandler();
		json_select = new JsonProjectSelectListHandler();
		application = (MyApplication) activity.getApplication();
		proj_list = new ArrayList<Map<String,Object>>();


	}
	
	
	
private void requestData(){
	MyThread thread = new MyThread(0);
    new Thread(thread).start();
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
	
		


	}
	
	
	
	
	
	  private class MyThread implements Runnable
		{
			private int index = 0;
			
			public MyThread(int temp)
			{
				index = temp;
			}
			

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				URL url;
				try{
					//strUrl += "&date=2014-1-1" + "&class=" + URLEncoder.encode("企业","utf-8");
					url = new URL(str_url);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					List<Map<String,Object>> temp = null;
				    
				    json = new JsonProgramListHandler();
				    temp = json.getListItems(input);
					//Log.i("strUrl", strUrl);
					if(temp != null)
					{
						//proj_list = temp;
						for(int i = 0;i < temp.size();i++){
							if(proj_list == null)
								proj_list = new ArrayList<Map<String,Object>>();
							proj_list.add(temp.get(i));
						}
						//Collections.sort(proj_list, new ListComparator());
						if(proj_list.size() > 0)
						{
						last_id = (Integer) proj_list.get(proj_list.size() - 1).get("id");
						last_date = proj_list.get(proj_list.size() - 1).get("startTime").toString();
						}
						handler.sendEmptyMessage(UPDATE_USUAL_PROJ_LIST);
						
					}else
					{
						handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
					}
				}catch(MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	  
	  


	 
	  
	  private Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				 
				//更新TAG_VIEW
				case UPDATE_TAG_VIEW:
					
					break;
				
				case UPDATE_TAG_VIEW2:
					
					proj_list_adapter.notifyDataSetChanged();
				    break;    
					
				case UPDATE_HOT_PROJ_LIST:
					proj_list_adapter.notifyDataSetChanged();
					if(DataCache.hot_project_list  == null)
					{
						DataCache.hot_project_list = new ArrayList<Map<String,Object>>();
						if(proj_list != null)
						{
						   for(int i = 0;i < proj_list.size();i++){
							String title = "[置顶]" + (String) proj_list.get(i).get("title");
							proj_list.get(i).put("title", title);
							Set<String>keys = proj_list.get(i).keySet();
							Map<String,Object> map = new HashMap<String,Object>();
							for(String key:keys){
								map.put(key, proj_list.get(i).get(key));
							}
							DataCache.hot_project_list.add(map);
							//temp_proj_list.add(proj_list.get(i));
						   }
						}
					  // DataCache.hot_project_list = temp_proj_list;
					}
					
					
					if(DataCache.usual_project_list != null){
						str_url = Url.composeUsualProject(proj_src1, proj_src2, proj_type, -1, DefaultUtil.EMPTY);
   	    			    new Thread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try{
								URL url = new URL(str_url);
								URLConnection conn = url.openConnection();
								conn.connect();
								InputStream is = conn.getInputStream();
							    json = new JsonProgramListHandler();
							    List<Map<String,Object>> temp = null;
							    temp = json.getListItems(is);
							    if(temp != null){	 
							    	if(proj_list != null)
							    	{
							    	  DataCache.usual_project_list.addAll(temp);
							    	  proj_list.addAll(temp);
							    	  handler.sendEmptyMessage(UPDATE_USUAL_PROJ_LIST);
							    	}else{
							    		proj_list = temp;
							    	}
							    }
							    else
							    {
							    	 handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
							    }
							 }catch(MalformedURLException e)
								{
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
   	    				 
   	    			 }).start();
					}
					
					break;
				case UPDATE_USUAL_PROJ_LIST:
					
					
					/*如果是即将到期的项目就将项目中的未过期的删掉*/
					//saveProjFragment(saved_proj_list);
					proj_list_adapter.notifyDataSetChanged();
					loadable = true;

					break;
				case UPDATE_PROJ_LIST_FAIL:
					
					loadable = true;
//					if(proj_list.isEmpty())
//					{
//						//proj_list_adapter.notifyDataSetChanged();
//					}

					break;
					
				case REFRESH_COMPLETE:
					//proj_list_adapter.notifyDataSetChanged();
					//proj_list_view.setAdapter(proj_list_adapter);
					proj_list_view.onRefreshComplete();
					loadable = true;
					break;
					
				case UPDATE_EXPIRE_PROJ_LIST:
					proj_list_adapter.notifyDataSetChanged();
					loadable = true;
					break;
				case UPDATE_PROJ_LIST:
					proj_list_adapter.notifyDataSetChanged();
					loadable = true;
					break;

					
				}
			}
		};

	  public void setOnClickListener()
		{

	      //设置头部按钮的点击事件
		   OnClickListener onHeaderButtonClickListener = new OnClickListener()
		   {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			   
		   };	   

		   
		}
		
		
		
	  
	  class ViewHolder{
		public   TextView title_tv;
		public   TextView like_num_tv;
		public   TextView comment_num_tv;
		public   TextView start_time_tv;
		public   TextView end_time_tv;
		public   ImageView expire_flag_iv;
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
					holder.expire_flag_iv = (ImageView) view.findViewById(R.id.proj_expire_flag);
					
					
					/*如果是置顶（热门）项目就去掉前面的过期标志*/
					if(tab == ProjectApplyActivity.PROJ_HOT)
						holder.expire_flag_iv.setVisibility(View.INVISIBLE);
					else{
						//int mark = (Integer) proj_list.get(position).get("mark");
					//	if(mark == -1)
							//holder.expire_flag_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_green_point));
						//else if(mark == 1)
							holder.expire_flag_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_red_point));
					}
					
					
					
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
				/*如果是置顶（热门）项目就去掉前面的过期标志*/
				if(tab == ProjectApplyActivity.PROJ_HOT)
					holder.expire_flag_iv.setVisibility(View.INVISIBLE);
				else{
					//int mark = (Integer) proj_list.get(position).get("mark");
					//if(mark == -1)
					//	holder.expire_flag_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_green_point));
					//else if(mark == 1)
						holder.expire_flag_iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_red_point));
				}
				
				
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
				intent.putExtra("act_class", "project");
				intent.putExtra("articleType", proj_list.get(index).get("articleType").toString());
				//intent.putExtra("articleType1", proj_src1);
				//intent.putExtra("articleType2", proj_src2);
				intent.putExtra("id", (Integer)proj_list.get(index).get("id"));
				intent.putExtra("theme", proj_list.get(index).get("title").toString());
				startActivity(intent);
			}
			
		};
		


    
    
		
		public void updateProjectList(int tab,int src1,int src2,int type,int id,String date){
			
			str_url = Url.composeUsualProject(src1, src2, type, id,date);
			requestData();
		}
    
    
		
		
		
		
		
		/**
		 * 下拉刷新监听器
		 */
		private OnRefreshListener refreshListener = new OnRefreshListener() {
			@Override
			public void onRefresh() {
//				TabScienceNetActivity.isSearch = false;
				
				refreshPage();
			}
			
		};
		
		/**
		 * 底部加载更多按钮的监听器
		 */
		
		
		
		/**
		 * 刷新当前页面
		 */
		private void refreshPage() {
			pullable = false;
			new AsyncTask<String, Integer, String>() {

				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					
					
					//int newest_id = (Integer) proj_list.get(0).get("id");
                   if(loadable)
                   {
					if(tab == ProjectApplyActivity.PROJ_HOT)
					{
	
						if(DataCache.cache.get("UsualProject") == null){
							
							str_url = Url.composeUsualProject(proj_src1, proj_src2, proj_type, 0, DefaultUtil.EMPTY);
							loadable = false;		
							try{
									URL url = new URL(str_url);
									URLConnection conn = url.openConnection();
									conn.connect();
									InputStream is = conn.getInputStream();
								    json = new JsonProgramListHandler();
								    List<Map<String,Object>> temp = null;
								    temp = json.getListItems(is);
								    if(temp != null){
								    
								    	List<Map<String,Object>> usual_proj_list = DataCache.usual_project_list;
								    	if(DataCache.usual_project_list.size() <= 0)
								    		return null;
								    	if(temp.get(0).get("id").equals(usual_proj_list.get(0).get("id")) )
								    		return null;
								    	DataCache.usual_project_list.addAll(0,temp);
								    	//DataCache.cache.put("UsualProject",temp);
								    	proj_list = temp;
								    	if(DataCache.hot_project_list != null)
								    	proj_list.addAll(0,DataCache.hot_project_list);
								    	handler.sendEmptyMessage(UPDATE_USUAL_PROJ_LIST);
								    }
								 }catch(MalformedURLException e)
									{
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
				}
				else
				{
					str_url = Url.composeExpireProjectUrl(proj_src1, proj_src2, proj_type,-1,DefaultUtil.EMPTY);
					
					URL url;
					loadable = false;
					try {
						//url = new URL(myApplication.ComposeToken(strUrl+Integer.toString(0)));
						url = new URL(str_url);
						URLConnection con = url.openConnection();
						con.connect();
						InputStream input = con.getInputStream();
						json = new JsonProgramListHandler();
						List<Map<String,Object>> temp  = json.getListItems(input);
						
						if(temp != null){
							//Collections.sort(temp, new ListComparator());
							
							if(proj_list == null){
								proj_list = temp;
								handler.sendEmptyMessage(UPDATE_USUAL_PROJ_LIST);
							}
						}
						

					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
					
             }	//if(loadable)
                   handler.sendEmptyMessage(REFRESH_COMPLETE);
					return null;
			}
//				handler.sendEmptyMessage(REFRESH_COMPLETE);
//				
//				return null;
//	
				@Override
				protected void onPostExecute(String result) {
					if (proj_list_adapter != null) {
						proj_list_adapter.notifyDataSetChanged();
						proj_list_view.onRefreshComplete();
					}
				}	
				
				
				
				
				
				
				
				
				
		 }.execute("begin");
		 
		 //handler.sendEmptyMessage(REFRESH_COMPLETE);
		 
		 

		 
		}
		
		
		
		
		
		
		
	    private class OnScrollListenerImple implements OnScrollListener{ 
	    	
	    	
	    	
	        @Override 
	        public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) { 
	            
	        	if(visibleItemCount + 1 < totalItemCount)
	        		pullable = true;

	        } 
	   


			@Override 
	        public void onScrollStateChanged(AbsListView listview, int scrollState) { 
		        if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {  
		            // 判断是否滚动到底部  
		            if (listview.getLastVisiblePosition() == listview.getCount() - 1 && pullable) {  
		                //加载更多功能的代码  
		            	addDataForListView(); 
		            }  
		        } 
	        } 
	           
	    }
	    
	    
	    
	    private void addDataForListView() {
			// TODO Auto-generated method stub
	    	//Toast.makeText(activity, "addMore", Toast.LENGTH_SHORT).show();
	    	
	    	if(loadable)
	    	{
	    	if(tab == ProjectApplyActivity.PROJ_HOT){//默认状态下是热点项目
	                 if(DataCache.usual_project_list != null){	 
	                 List<Map<String,Object>> list = DataCache.usual_project_list;	 
	                 if(list.size() > 0){
		    			 last_id = (Integer) list.get(list.size() - 1).get("id");
		    			 last_date = (String) list.get(list.size() - 1).get("startTime");
	                 
	         

	                 str_url =Url.composeUsualProject(proj_src1, proj_src2, proj_type, last_id, last_date);
	    			 //Log.i("str_urlllllll", str_url);
	                 loadable = false;
	                 new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
							URL url = new URL(str_url);
							URLConnection conn = url.openConnection();
							conn.connect();
							InputStream is = conn.getInputStream();
						    json = new JsonProgramListHandler();
						    List<Map<String,Object>> temp = null;
						    temp = json.getListItems(is);
						    if(temp != null){	    	
						    	DataCache.usual_project_list.addAll(temp);
						    	if(proj_list != null)
						    	proj_list.addAll(temp);
						    	else
						    	proj_list = temp;
						    	
						    	handler.sendEmptyMessage(UPDATE_USUAL_PROJ_LIST);
						    }
						    else
						    {
						    	handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
						    }
						 }catch(MalformedURLException e)
							{
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
	    				 
	    			 }).start();
	               }//if(list.size() > 0)
	    			 
	             }
	    		
	    		
	    	}
	    	else{
	    		str_url = Url.composeExpireProjectUrl(this.proj_src1,this.proj_src2,this.proj_type,last_id,last_date);
   			    loadable = false;
	    		new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try{
						URL url = new URL(str_url);
						URLConnection conn = url.openConnection();
						conn.connect();
						InputStream is = conn.getInputStream();
					    json = new JsonProgramListHandler();
					    List<Map<String,Object>> temp = null;
					    temp = json.getListItems(is);
					    if(temp != null){	    	
					    	proj_list = temp;
					    	handler.sendEmptyMessage(UPDATE_EXPIRE_PROJ_LIST);
					    }
					    else
					    {
					    	handler.sendEmptyMessage(UPDATE_PROJ_LIST_FAIL);
					    }
					 }catch(MalformedURLException e)
						{
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
   				 
   			 }).start();
	    	}
		}
		
		
	    }
		
		
    
}
