package com.science.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.activity.CommonContentActivity;
import com.science.activity.DocumentExpressActivity;
import com.science.activity.MainActivity;
import com.science.activity.ProjectApplyActivity;
import com.science.adapter.DocumentListAdapter;
import com.science.interfaces.ListComparator;
import com.science.interfaces.OnLoadingStateChangedListener;
import com.science.json.JsonDcumentListHandler;
import com.science.json.JsonProgramListHandler;
import com.science.services.DataCache;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.DefaultUtil;
import com.science.util.MergeListUtil;
import com.science.util.Url;
import com.science.view.MyHeaderView;
import com.science.view.MyImageButton;
import com.science.view.MyPullToRefreshListView;
import com.science.view.MyPullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class DocumentExpressFragment extends Fragment  implements OnLoadingStateChangedListener{

	
	private Activity activity;

	private MyPullToRefreshListView  doc_list_view;
	
	
	/*与数据解析有关*/
	private JsonDcumentListHandler json = null;
	private String str_url = null;
	private List<Map<String,Object>> doc_list;
    private MyApplication application;
    private DocListAdapter doc_list_adapter;
    private View view;
    private View doc_footer_view;
    //表示文献的类型、id等信息
    private Integer last_id = 0;
    private String  last_pdate = "";
    private int id;
    private int type;
    private int tab ;
    private StringBuffer keywords = new StringBuffer("");
    
    private boolean loadable = true;
    private  final int UPDATE_TAG_VIEW = 0x00; 
    private  final int UPDATE_DOC_LIST = 0x01;
    private  final int UPDATE_DOC_LIST_FAIL = 0x02;
    private final int REFRESH_COMPLETE = 0x03;
    
    private List<Map<String,Object> > saved_doc_list;
    private MergeListUtil merger = new MergeListUtil();
    
    public DocumentExpressFragment(){
    	
    }
    
    
    public DocumentExpressFragment(int type){
    	
    }
    
    
    public DocumentExpressFragment(int id,int type,StringBuffer keywords,List<Map<String,Object>> list){
    	if(last_id <= id)
    	this.id = id;
    	this.type = type;
    	this.keywords = keywords;
    	this.saved_doc_list = list;
    	
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    	
    	this.activity = this.getActivity();
    	view = inflater.inflate(R.layout.document_fragment_list, container, false);  
        doc_list = new ArrayList<Map<String,Object>>();

        this.application = MyApplication.getInstance();
        doc_list_view = (MyPullToRefreshListView) view.findViewById(R.id.doc_list_view);
        doc_list_adapter = new DocListAdapter();
        doc_list_view.setAdapter(doc_list_adapter);
        doc_list_view.setOnItemClickListener(doc_item_click_listener);
        doc_list_view.setOnScrollListener(new OnScrollListenerImple());
        doc_list_view.setonRefreshListener(refreshListener);
        doc_footer_view = inflater.inflate(R.layout.comm_list_footer, null);
        doc_list_view.addFooterView(doc_footer_view);
        return view;
    }
    
    @Override
    public void onDestroyView(){
    	super.onDestroyView();
    	doc_list = null;
    }
 
    
    
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	

    }
    
   
    
    
    
    public void loadDocument(String pdate,int id, int type, StringBuffer sb)
    {


    	
    	
    	if(sb == null)
    		sb = new StringBuffer(DefaultUtil.EMPTY);

    	int tab = type - DocumentExpressActivity.DOC_CHI;
    	if(tab > 3 && tab < 0)
    		return ;
    	

    	
    	this.tab = tab;
    	if(DataCache.doc_lists == null)
    	{
    		DataCache.doc_lists = new ArrayList<List<Map<String,Object>>>();
    		for(int i = 0;i < 4;i++)
    		DataCache.doc_lists.add(new ArrayList<Map<String,Object>>());
    	}
    	
    	
    	//改变关键词或者类型
    	if(this.type != type || !this.keywords.equals(sb))
    	{
    		if(doc_list != null)
    		{
    			doc_list.clear();
    			
    			if(DataCache.doc_lists.get(tab)!=null)
    				DataCache.doc_lists.get(tab).clear();
    			
    			if(doc_list_adapter != null)
    			doc_list_adapter.notifyDataSetChanged();
    		}
    		loadable = true;
    	}

    	this.type = type;
    	this.keywords = null;
    	this.keywords = new StringBuffer(sb.toString());
    	if(!loadable)
    		return;
    	
        if(DataCache.doc_lists.get(tab) != null){
//        	str_url = Url.composeDocListUrl(pdate,id, type, sb.toString());
//        	requestData();
        	if(!DataCache.doc_lists.get(tab).isEmpty()){
        		if(doc_list != null){
        			merger.mergeTwoListBackward(doc_list, DataCache.doc_lists.get(tab));
        		}
        		if(doc_list_adapter != null)
        			doc_list_adapter.notifyDataSetChanged();
        	}
//        	else
//        	{
            	str_url = Url.composeDocListUrl(pdate,id, type, sb.toString());
            	Log.i("doc_urllllllllll", str_url);
            	if(doc_list_view != null && doc_footer_view != null)
				if(doc_list_view.getFooterViewsCount() <= 0)
					doc_list_view.addFooterView(doc_footer_view);
            	 
            	
            	//Log.i("doc_str_url", str_url);
            	requestData();
//        	}
        }
    	
    	

    	
    }
    
    
    
    
    

    
    
    @Override
    public void onStart()
    {
    	super.onStart();

    }
    
    
    public void saveDocFragment(List<Map<String,Object>> list){
    	if(list != null) 	
    		list.clear();
    	else
    		list = new ArrayList<Map<String,Object>>();
    	if(doc_list != null){
    		list.addAll(doc_list);
    	}
    }
    
    public void reLoadDocFragment(List<Map<String,Object>> list){
      //  doc_list = new ArrayList<Map<String,Object>>();
    	if(doc_list!=null && list!=null)
    	{
    		doc_list.clear();
    		doc_list.addAll(list);
    	}
    	
        handler.sendEmptyMessage(UPDATE_DOC_LIST);
        //updateDocListFragment(this.last_id,this.type,this.keywords);
    }
    
    
    
    /**
     * 
     * @param type  表明自己是属于哪一类型的文献
     * @param keywords  表明关键词序列
     */
    public void updateDocListFragment(int id,int type,StringBuffer sb){
    	if(sb == null)
    		sb = new StringBuffer("null");
    	str_url = Url.composeDocListUrl(DefaultUtil.EMPTY,id, type, sb.toString());
    	requestData();
    }
    
    
    
    
    
    public void updateDocList(String pdate,int id,int type,String[] strs){
    	
    	StringBuffer kywds = new StringBuffer("");
    	int i = 0;
    	for(i = 0;i < strs.length - 1;i++){
    		kywds.append(strs[i]);
    		kywds.append("||");
    	}
    	
    	kywds.append(strs[i]);
    	str_url = Url.composeDocListUrl(DefaultUtil.EMPTY,id, type, kywds.toString());
    	requestData();
    	
    }
    
    

	
	private void requestData()
	{
		
        MyThread thread = new MyThread(0);
        new Thread(thread).start(); 
	}
	

	
	
	
	
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case UPDATE_DOC_LIST:
				//onLoadingSucceed();
				if(doc_list_adapter != null)
				{
				doc_list_adapter.notifyDataSetChanged();
				}
				if(doc_list != null)
					if(doc_list.size() > 0)
					{
						last_id = (Integer) doc_list.get(doc_list.size() - 1).get("id");
						last_pdate = (String) doc_list.get(doc_list.size() - 1).get("pdate");
					}
				if(doc_list_view.getFooterViewsCount() > 0)
					for(int i = 0;i < doc_list_view.getFooterViewsCount();i++)
					doc_list_view.removeFooterView(doc_footer_view);
				if(doc_list_view != null)
				doc_list_view.onRefreshComplete();
				loadable = true;
				break;
			case UPDATE_DOC_LIST_FAIL:
				if(doc_list_view.getFooterViewsCount() > 0)
					for(int i = 0;i < doc_list_view.getFooterViewsCount();i++)
					doc_list_view.removeFooterView(doc_footer_view);
				if(doc_list_view != null)
				doc_list_view.onRefreshComplete();
				loadable = true;
				break;
				
			case REFRESH_COMPLETE:
				if(doc_list_view != null)
				doc_list_view.onRefreshComplete();
				loadable = true;
			}
		}
	};

	
	
	
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
			loadable = false;
			URL url;
			try{
                
				Thread.sleep(500);
				url = new URL(str_url);
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				List<Map<String,Object>> temp = null;
				json = new JsonDcumentListHandler(type);
				temp = json.getListItems(input);
				if(temp != null )
				{
					


				  if(DataCache.doc_lists.get(tab) != null)
				  {		
						merger.mergeTwoListBackward(DataCache.doc_lists.get(tab), temp);
						if(doc_list != null)
						{
						  if(merger.mergeTwoListBackward(doc_list, DataCache.doc_lists.get(tab))){
							  handler.sendEmptyMessage(UPDATE_DOC_LIST);
						  }
					     else{
					    		handler.sendEmptyMessage(UPDATE_DOC_LIST_FAIL);
					     }
						
					    }
				  }
				}else
				{
					handler.sendEmptyMessage(UPDATE_DOC_LIST_FAIL);
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
		
	}
	
	
	
	
	
	static class ViewHolder{
		
		public TextView title_tv;
		public TextView like_num_tv;
		public TextView comment_num_tv;
	}
	
	
	
	private class DocListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(doc_list != null)
			return doc_list.size();
			else
			return 0;
		}

		@Override
		public Object getItem(int item) {
			// TODO Auto-generated method stub
			return doc_list.get(item);
		}

		@Override
		public long getItemId(int id) {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.doc_list_item, null);
			holder = new ViewHolder();
			holder.title_tv = (TextView) view.findViewById(R.id.doc_title);
			holder.like_num_tv = (TextView) view.findViewById(R.id.doc_like_num);
			holder.comment_num_tv = (TextView) view.findViewById(R.id.doc_comment_num);
			String title = (String)doc_list.get(position).get("title");
			int  like_num = (Integer)doc_list.get(position).get("diggtop");
			int  comment_num = (Integer)doc_list.get(position).get("plnum");
			holder.title_tv.setText(title);
			holder.like_num_tv.setText("" + like_num);
			holder.comment_num_tv.setText("" + comment_num);

			
			convertView = view;
			convertView.setTag(holder);
			}
			
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.title_tv.setText(doc_list.get(position).get("title").toString());
			holder.like_num_tv.setText(doc_list.get(position).get("diggtop").toString());
			holder.comment_num_tv.setText(doc_list.get(position).get("plnum").toString());
			return convertView;
		}
		
	}
	
	
	
	//为ListView每一个item设置OnClick事件
	private OnItemClickListener doc_item_click_listener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			int index = position - 1;
			String url = application.ComposeToken(Url.DocumentDetailBase) + "&id=" + doc_list.get(index).get("id");
			
			
			Intent intent = new Intent();//用以传递数据
			
			intent.setClass(activity, CommonContentActivity.class);
			intent.putExtra("articleType", doc_list.get(index).get("articleType").toString());
			intent.putExtra("id", (Integer)doc_list.get(index).get("id"));
			intent.putExtra("url", doc_list.get(index).get("url").toString());
			String act_class = AppUtil.BlockCodeToBlockText(""+type);
			intent.putExtra("act_class", act_class);
			intent.putExtra("theme",doc_list.get(index).get("title").toString());
			startActivity(intent);
		}
		
	};
	
	
	

	
	

	
	
	
	
	
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
	            if(( (listview.getLastVisiblePosition() + 1 == listview.getCount()) || listview.getLastVisiblePosition() == listview.getCount() - 2)&& loadable) {  
	                //加载更多功能的代码  
	            	addDataForListView(); 
	            }  
	        } 
        } 
           
    }
    
    
    
    private void addDataForListView() {
		// TODO Auto-generated method stub
    	doc_list_view.addFooterView(doc_footer_view);
		str_url = Url.composeDocListUrl(last_pdate,last_id, type, keywords.toString());
    	requestData();
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
	 * 底部加载更多按钮的监听器
	 */
	
	
	
	/**
	 * 刷新当前页面
	 */
	private void refreshPage() {
		if(!loadable)
			return;

		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				loadable = false;
			    str_url = Url.composeDocListUrl(DefaultUtil.EMPTY,0,type, keywords.toString());

				URL url;
				try {
					
					Thread.sleep(500);
					url = new URL(str_url);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					json = new JsonDcumentListHandler();
					List<Map<String,Object>> temp  = json.getListItems(input);
					
					if(temp != null){
						
						merger.mergeTwoListForward(DataCache.doc_lists.get(tab), temp);
						if(merger.mergeTwoListForward(doc_list, DataCache.doc_lists.get(tab)))
						handler.sendEmptyMessage(UPDATE_DOC_LIST);
						else
							handler.sendEmptyMessage(UPDATE_DOC_LIST_FAIL);	
					}else{
						handler.sendEmptyMessage(UPDATE_DOC_LIST_FAIL);
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
				
				
				
				
				
				handler.sendEmptyMessage(REFRESH_COMPLETE);
				
				return null;
				
				
			}
			
			

		
			

	 }.execute("begin");
	}
	
	
	
	
	
	
	
	

	//设置加载事件
	@Override
	public void onLoadingSucceed() {
		// TODO Auto-generated method stub
		//view.findViewById(R.id.doc_loading_mask_layout).setVisibility(View.GONE);
		//Toast.makeText(activity, "加载成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingFail() {
		// TODO Auto-generated method stub
		//view.findViewById(R.id.doc_loading_mask_layout).setVisibility(View.GONE);
		//Toast.makeText(activity, "加载失败！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingBegin() {
		// TODO Auto-generated method stub
		//view.findViewById(R.id.doc_loading_mask_layout).setVisibility(View.VISIBLE);
	}


	@Override
	public void setOnLoadingStateChangedListener(OnLoadingStateChangedListener l) {
		// TODO Auto-generated method stub
		l = this;
	}
	
	
	
	
	

    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
}
