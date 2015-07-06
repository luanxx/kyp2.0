package com.science.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.activity.CommonContentActivity;
import com.science.adapter.TechTrendAcdmAffrListAdapter;
import com.science.adapter.SubjectPopMenuListAdapter;
import com.science.interfaces.ListComparator;
import com.science.json.JsonDcumentListHandler;
import com.science.json.JsonTechTrendListHandler;
import com.science.model.CommonItem;
import com.science.model.ResourceDefine;
import com.science.model.Subject;
import com.science.util.DefaultUtil;
import com.science.util.MergeListUtil;
import com.science.util.Url;
import com.science.view.MyImageButton;
import com.science.view.MyPullToRefreshListView;
import com.science.view.MyPullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class TechTrendAcdmAffrFagment extends Fragment {

	private View view;
	private Activity      activity;
	private TextView      tecg_trend_subject_tv;
	private MyImageButton tech_trend_subject_extend_btn;
	private PopupWindow   tech_trend_subject_pop_win;
	private View          tech_trend_subject_pop_view;
	private View          tech_trend_subject_layout;
	private View          tech_trend_footer;
	private LayoutInflater      inflater;
	private MyPullToRefreshListView            tech_trend_acdm_affr_list_view;
	private ListView            tech_trend_first_subject_list_view;
	private ListView            tech_trend_second_subject_list_view;
	private List<Subject> tech_trend_subject_list;
	private List<Map<String,Object>> tech_trend_subject_alys_list;
	private List<Map<String,Object>> tech_trend_acdm_affr_list;
	private SubjectPopMenuListAdapter tech_trend_first_subject_list_adapter;
	private SubjectPopMenuListAdapter tech_trend_second_subject_list_adapter;
	private TechTrendAcdmAffrListAdapter tech_trend_acdm_affr_list_adapter;
	private Subject curr_subject;
	
    private int     subject_src1 = 0;
    private int     subject_src2 = 0;
    private String  alys_list_url;
    private JsonTechTrendListHandler json;
    
    private int last_id;
    
    /**
     * 定义一些消息
     */
    private final int LOAD_ALYS_LIST_OK = 0;
    private final int LOAD_ALYS_LIST_FAIL = 1;
    private final int REFRESH_COMPLETE = 2;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
	
		view = inflater.inflate(R.layout.tech_trendency_fragment_academic_affair, null);
		activity = this.getActivity();
		this.inflater = inflater;
		initVariable();
		initView();
		setOnClickListener();
		setOnItemClickListener();
		requestData();
		return view;
	}
	
	
	private void initVariable(){
		
		tech_trend_acdm_affr_list = new ArrayList<Map<String,Object>>();
		tech_trend_acdm_affr_list_adapter = new TechTrendAcdmAffrListAdapter(activity,tech_trend_acdm_affr_list);
		tech_trend_subject_list = ResourceDefine.defined_resource_subject_list;
		tech_trend_first_subject_list_adapter = new SubjectPopMenuListAdapter(activity,tech_trend_subject_list);
		tech_trend_subject_alys_list = new ArrayList<Map<String,Object>>();
	

	}
	
	
	private void requestData(){
		if(tech_trend_acdm_affr_list_view != null && tech_trend_footer != null){
			tech_trend_acdm_affr_list_view.addFooterView(tech_trend_footer);
		}
		
		AlysRequestThread thread = new AlysRequestThread();
		new Thread(thread).start();
	}
	
	
	
	public void initView(){
	

		
		tecg_trend_subject_tv = (TextView) view.findViewById(R.id.tech_trend_subject_text);
		tech_trend_subject_extend_btn = (MyImageButton) view.findViewById(R.id.tech_trend_subject_extend);
		tech_trend_subject_layout = view.findViewById(R.id.tech_trend_subject_layout);
		tech_trend_subject_pop_view = inflater.inflate(R.layout.tech_trend_academic_affair_tag_pop_view, null);
		tech_trend_acdm_affr_list_view = (MyPullToRefreshListView) view.findViewById(R.id.tech_trend_acdm_affr_list);
		tech_trend_first_subject_list_view = (ListView) tech_trend_subject_pop_view.findViewById(R.id.tech_trend_acdm_affr_tag_first_list);
		tech_trend_second_subject_list_view = (ListView) tech_trend_subject_pop_view.findViewById(R.id.tech_trend_acdm_affr_tag_second_list);
		tech_trend_subject_pop_win = new PopupWindow(tech_trend_subject_pop_view,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		
		
		tech_trend_first_subject_list_view.setAdapter(tech_trend_first_subject_list_adapter);   
		tech_trend_subject_pop_win.setOutsideTouchable(true);
		tech_trend_subject_pop_win.setFocusable(true);
		tech_trend_subject_pop_win.setBackgroundDrawable(new BitmapDrawable());
		tech_trend_subject_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				float x = (float) (tech_trend_subject_extend_btn.getWidth()*0.5);
				float y = (float) (tech_trend_subject_extend_btn.getHeight()*0.5);
				RotateAnimation rot_anim = new RotateAnimation(180,0,x,y);
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(10);
				tech_trend_subject_extend_btn.startAnimation(rot_anim);
			}
		});
		
		
		
		
		
		
		/*初始化默认为某一学科，例如计算机*/
		
		alys_list_url = Url.composeTechTrendAcdmAffrUrl(tech_trend_subject_list.get(0).getSbjCode(), 
				                                         tech_trend_subject_list.get(0).getSubSbj().get(0).getSbjCode(), 
				                                         DefaultUtil.MAX_VALUE);
		subject_src1 = tech_trend_subject_list.get(0).getSbjCode();
		subject_src2 =  tech_trend_subject_list.get(0).getSubSbj().get(0).getSbjCode();
		tecg_trend_subject_tv.setText(tech_trend_subject_list.get(0).getSubSbj().get(0).getSbjName());
		
		
		tech_trend_acdm_affr_list_view.setonRefreshListener(refreshListener);
		tech_trend_acdm_affr_list_view.setOnScrollListener(new OnScrollListenerImple());
		tech_trend_acdm_affr_list_view.setAdapter(tech_trend_acdm_affr_list_adapter);
		tech_trend_footer = inflater.inflate(R.layout.comm_list_footer, null);
		
	}
	
	public void setOnClickListener(){
		
		tech_trend_subject_extend_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float x = (float) (v.getWidth()*0.5);
				float y = (float) (v.getHeight()*0.5);
				RotateAnimation rot_anim = new RotateAnimation(0,180,x,y);
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				v.startAnimation(rot_anim);
				
				tech_trend_subject_pop_win.showAsDropDown(tech_trend_subject_layout);
				

			}
			
		});
		
		
		
		
	}
	
	public void setOnItemClickListener(){
		
		tech_trend_first_subject_list_view.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				tech_trend_first_subject_list_view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                int height = tech_trend_first_subject_list_view.getHeight();
                LayoutParams params = tech_trend_second_subject_list_view.getLayoutParams();
                params.height = height - 8;
                tech_trend_second_subject_list_view.setLayoutParams(params);
                tech_trend_second_subject_list_view.setMinimumHeight(height);
				List<Subject> subject_list = tech_trend_subject_list.get(position).getSubSbj();
				tech_trend_second_subject_list_adapter = new SubjectPopMenuListAdapter(activity,subject_list);
				tech_trend_second_subject_list_view.setAdapter(tech_trend_second_subject_list_adapter);
				
				
				//设置当前一级学科
				curr_subject = tech_trend_subject_list.get(position);
				tech_trend_first_subject_list_adapter.setSelectedPosition(position);
				tech_trend_first_subject_list_adapter.notifyDataSetChanged();

				subject_src1 = curr_subject.getSbjCode();
				//tech_trend_tag_pop_win.showAsDropDown(tech_trend_tag_layout);
			}
			
		});
		
		
		tech_trend_second_subject_list_view.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				tech_trend_second_subject_list_adapter.setSelectedPosition(position);
				tech_trend_second_subject_list_adapter.notifyDataSetChanged();
				
				Subject curr_sub_subject;
				if(curr_subject.getSubSbj() != null){
					curr_sub_subject = curr_subject.getSubSbj().get(position);
					tecg_trend_subject_tv.setText(curr_sub_subject.getSbjName());
					subject_src2 = curr_sub_subject.getSbjCode();
					
					tech_trend_acdm_affr_list.clear();
					tech_trend_acdm_affr_list_adapter.updateAdapter(tech_trend_acdm_affr_list);
					
					/*构造URL*/
					alys_list_url = Url.composeTechTrendAcdmAffrUrl(subject_src1, subject_src2, DefaultUtil.MAX_VALUE);
					requestData();
					
				}
				tech_trend_subject_pop_win.dismiss();
				
			}
			
		});
		
		
		
		tech_trend_acdm_affr_list_view.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				int index = position - 1;
				String url = (String) tech_trend_acdm_affr_list.get(index).get("url");
				String act_class = "业界动态";
				String theme = (String) tech_trend_acdm_affr_list.get(index).get("title") ;
				Intent intent = new Intent();
				intent.setClass(activity, CommonContentActivity.class);
				intent.putExtra("act_class", act_class);
				intent.putExtra("url", url);
				intent.putExtra("theme", theme);
				intent.putExtra("articleType", tech_trend_acdm_affr_list.get(index).get("articleType").toString());
				intent.putExtra("id", (Integer)tech_trend_acdm_affr_list.get(index).get("id"));
				activity.startActivity(intent);
			}
			
		});
	}
	
	
	
	
	
	
	private class AlysRequestThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(200);
				json = new JsonTechTrendListHandler();
				URL url = new URL(alys_list_url);
			    URLConnection conn  = url.openConnection();
			    conn.connect();
			    InputStream is = conn.getInputStream();
			    List<Map<String,Object>> temp = null;
			    temp = json.getListItems(is);
			    if(temp != null){
			    	
			    	
			    	for(int i = 0;i < temp.size();i++){
			    		tech_trend_acdm_affr_list.add(temp.get(i));
			    	}
			    	//Collections.sort(tech_trend_acdm_affr_list,new ListComparator());
			    	MergeListUtil merger = new MergeListUtil();
			    	merger.mergeTwoListBackward(tech_trend_acdm_affr_list, temp);
			    	if(tech_trend_acdm_affr_list.size() > 0){
			    		last_id = (Integer) tech_trend_acdm_affr_list.get(tech_trend_acdm_affr_list.size() - 1).get("id");
			    	}
			    	handler.sendEmptyMessage(LOAD_ALYS_LIST_OK);
			    }
			    else{
			    	//tech_trend_acdm_affr_list = null;
			    	handler.sendEmptyMessage(LOAD_ALYS_LIST_FAIL);
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
		}
		
	}
	
	
	
	
	
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
//			if(msg.what == LOAD_ALYS_LIST_OK){
//				tech_trend_acdm_affr_list_adapter = new TechTrendAcdmAffrListAdapter(activity,tech_trend_acdm_affr_list);
//				tech_trend_acdm_affr_list_view.setAdapter(tech_trend_acdm_affr_list_adapter);
//			}
//			else if(msg.what == LOAD_ALYS_LIST_FAIL){
//				Toast.makeText(activity, "学科动态获取失败", Toast.LENGTH_SHORT).show();
//			}
		switch(msg.what){
		
		case LOAD_ALYS_LIST_OK:
			
			tech_trend_acdm_affr_list_adapter.updateAdapter(tech_trend_acdm_affr_list);
			
			
			if(tech_trend_acdm_affr_list_view != null && tech_trend_footer != null){
				if(tech_trend_acdm_affr_list_view.getFooterViewsCount() > 0)
					for(int i = 0;i < tech_trend_acdm_affr_list_view.getFooterViewsCount();i++)
				           tech_trend_acdm_affr_list_view.removeFooterView(tech_trend_footer);
			}
			
			if(tech_trend_acdm_affr_list != null)
			     if(tech_trend_acdm_affr_list.size() > 0)
			         last_id = (Integer) tech_trend_acdm_affr_list.get(tech_trend_acdm_affr_list.size() - 1).get("id");
			break;
		case LOAD_ALYS_LIST_FAIL:
			if(tech_trend_acdm_affr_list_view != null && tech_trend_footer != null){
				if(tech_trend_acdm_affr_list_view.getFooterViewsCount() > 0)
					for(int i = 0;i < tech_trend_acdm_affr_list_view.getFooterViewsCount();i++)
				           tech_trend_acdm_affr_list_view.removeFooterView(tech_trend_footer);
			}
			break;
		case REFRESH_COMPLETE:
			tech_trend_acdm_affr_list_view.onRefreshComplete();
		}
			

		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    private class OnScrollListenerImple implements OnScrollListener{ 
    	//boolean pullable = false;
        @Override 
        public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) { 
        	//if(visibleItemCount + 1 < totalItemCount)
        		//pullable = true;
        } 
   


		@Override 
        public void onScrollStateChanged(AbsListView listview, int scrollState) { 
	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
	            // 判断是否滚动到底部  
	            if (listview.getLastVisiblePosition() == listview.getCount() - 1 /*&& pullable*/) {  
	                //加载更多功能的代码  
	            	addDataForListView(); 
	            }  
	        } 
        } 
           
    }
    
    
    
    private void addDataForListView() {
		// TODO Auto-generated method stub
		alys_list_url = Url.composeTechTrendAcdmAffrUrl(subject_src1, subject_src2, last_id);
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
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				
				
				//int newest_id = (Integer) proj_list.get(0).get("id");
				alys_list_url = Url.composeTechTrendAcdmAffrUrl(subject_src1, subject_src2, DefaultUtil.MAX_VALUE);

				URL url;
				try {
					//url = new URL(myApplication.ComposeToken(strUrl+Integer.toString(0)));
					Thread.sleep(500);
					url = new URL(alys_list_url);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					json = new JsonTechTrendListHandler();
					List<Map<String,Object>> temp  = json.getListItems(input);
					
					if(temp != null){
						MergeListUtil merger = new MergeListUtil();
						if(merger.mergeTwoListForward(tech_trend_acdm_affr_list, temp)){
							handler.sendEmptyMessage(LOAD_ALYS_LIST_OK);
						}
						else{
							handler.sendEmptyMessage(LOAD_ALYS_LIST_FAIL);
						}

						
					}
					else{
						handler.sendEmptyMessage(LOAD_ALYS_LIST_FAIL);
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
	
	
	
	
	
	
	
	
	
	
	
	
}
