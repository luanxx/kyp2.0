package com.science.fragment;



import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.activity.CommonContentActivity;
import com.science.activity.CoopDetailActivity;
import com.science.activity.CoopReleaseActivity;
import com.science.activity.CoopResearchActivity;


import com.science.json.JsonCoopResearchListHandler;
import com.science.json.JsonProgramListHandler;
import com.science.json.JsonProjectSelectListHandler;
import com.science.model.Cooper;
import com.science.model.Facility;
import com.science.model.FirstClassItem;
import com.science.model.Resource;
import com.science.model.ResourceDefine;
import com.science.model.SecondClassItem;
import com.science.services.MyApplication;
import com.science.util.NetWorkState;
import com.science.util.Url;
import com.science.view.MyImageButton;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.science.adapter.CommonPopMenuListAdapter;
import com.science.adapter.CoopListAdapter;
import com.science.adapter.FacilityListAdapter;



public class CoopResourceFragment extends Fragment{
	
	private View view;

	private LinearLayout main=null;
	private String strUrl=null;
	private String str_url_proj_src;

	private WindowManager wm;
	private Context context;

	private int screen_width;
	private int screen_height;
	
	private ListView  coop_list_view;
	private ListView  coop_all_list_view;
	private ListView  coop_all_next_list_view;
	private ListView  coop_area_list_view;
	private View      coop_all_pop_view;
	private View      coop_area_pop_view;
	private View      coop_all_btn;
	private View      coop_area_btn;
	private View      coop_i_offer_btn;
	private View      coop_i_need_btn;
	private View      coop_empty_content_hint_view;
	private TextView  coop_area_select_text;
	private TextView  coop_all_select_text;
	private EditText  coop_search_et;
	private MyImageButton coop_search_btn;
	private PopupWindow  coop_all_pop_win;
	private PopupWindow   coop_area_pop_win;
	//private CoopListAdapter coop_list_adapter;
	private CoopListAdapter coop_list_adapter;
	private FacilityListAdapter faci_list_adapter;
	private CommonPopMenuListAdapter first_adapter;
	private CommonPopMenuListAdapter second_adapter;
    private CommonPopMenuListAdapter coop_area_adapter;
	private View cooper_more_view;
	
	/*底部加号按钮*/
	private MyImageButton  coop_add_release_btn;
	
	private JsonCoopResearchListHandler json = null;
	private MyApplication application;

	
    private static final int UPDATE_COOP_LIST = 0x02;
    
	private List<Cooper> coop_list;
	private List<Facility> faci_list;
	private List<Resource> coop_all_list;
	private List<Resource> coop_all_next_list;
    private List<Resource> coop_area_list;
    private Resource coop_curr_all_resource;
    private Activity activity;
    
    
    /*资源的一些属性*/
    private int location;
    private int res_type;
    private int req_type;
    private String keyword;
    private int page;
    private String  cooper_type;//人力还是设备
    private int coop_relation;//标记合作关系，0表示我提供（即别人的需求信息），1表示我要找（即别人发布的资源信息）
	@Override  
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	           Bundle savedInstanceState)  
	    {  
		    this.activity = this.getActivity();
	        view = inflater.inflate(R.layout.coop_fragment_resource, container, false);  


	        initVariable();
	        initView();
	        setOnClickListener();
	        requestData();
	        return view;
	    }  

	public View getFragmentView(){
		
		return view;
	}
	
	
	
	
	private void initVariable()
	{
		
		
		
		strUrl=Url.ProjectTop;
		str_url_proj_src = Url.ProjectUsual;
		context = activity;
		application = (MyApplication) activity.getApplication();
		coop_list = null;
		screen_width = 0;
		screen_height = 0;
		coop_relation = 0;
		coop_area_list = ResourceDefine.defined_resource_coop_area_list;
		coop_all_list = ResourceDefine.defined_resource_coop_all_list;
	    /*设置coop_list_adapter*/
	    coop_list = new ArrayList<Cooper>();
	    
	    
	    
	    /*初始化资源信息*/
	    location = 0;
	    res_type = 1;
	    keyword = null;
	    page = 1;
	    cooper_type = "人力";
	    
	    
	}
	
	
	
private void requestData(){
	MyThread thread = new MyThread(0);
	new Thread(thread).start();
}
	private void initView()
	{
		
		/*先判断网络是否连接*/
		if(!NetWorkState.isNetworkAvailable(activity)){
			
			handler.sendEmptyMessage(3);
		}
		
		
		wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		screen_width = wm.getDefaultDisplay().getWidth();
		screen_height = wm.getDefaultDisplay().getHeight();
		
		
		//findViewById
		coop_search_et = (EditText) view.findViewById(R.id.coop_resource_search_box);
		coop_search_btn = (MyImageButton) view.findViewById(R.id.coop_resource_search_btn);
		coop_empty_content_hint_view = view.findViewById(R.id.coop_empty_content_hint_text);
		
		coop_all_btn = (LinearLayout)view.findViewById(R.id.coop_all_layout);
		coop_area_btn = (LinearLayout)view.findViewById(R.id.coop_area_layout);

		coop_list_view = (ListView)view.findViewById(R.id.coop_fragment_resource_list);
		
		coop_add_release_btn = (MyImageButton)view.findViewById(R.id.coop_add_release);
		
		coop_i_offer_btn = view.findViewById(R.id.coop_resource_i_offer);
		coop_i_need_btn = view.findViewById(R.id.coop_resource_i_need);
		
		coop_list_view.setCacheColorHint(Color.argb(255, 0, 0, 0));
		coop_list_view.setSelector(R.drawable.list_item_selector);
		coop_list_view.setOnItemClickListener(coop_item_click_listener);
		//coop_list_view.setAdapter(coop_list_adapter);
		
		coop_all_pop_view = activity.getLayoutInflater().inflate(R.layout.common_secondary_pop_view, null);
		coop_area_pop_view = activity.getLayoutInflater().inflate(R.layout.common_stair_pop_view, null);
		coop_all_list_view = (ListView) coop_all_pop_view.findViewById(R.id.common_secondary_pop_list);
		first_adapter = new CommonPopMenuListAdapter(activity,coop_all_list);
		first_adapter.setSelectedPosition(0);
		coop_all_list_view.setAdapter(first_adapter);
		
		
	    coop_all_next_list_view = (ListView) coop_all_pop_view.findViewById(R.id.common_secondary_pop_next_list);
	     
	     //设置coop_all_list_view和coop_all_new_list_view的比例
	     LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	     params.weight = 1.0f;
	     coop_all_list_view.setLayoutParams(params);
	     coop_all_next_list_view.setLayoutParams(params);
	     
	     
	     coop_area_select_text = (TextView) view.findViewById(R.id.coop_resource_area_select_text);
	     coop_all_select_text = (TextView)view.findViewById(R.id.coop_resource_all_select_text);
	     coop_area_select_text.setText("地域/全部");
	     coop_all_select_text.setText("人力/全部");
	     
	     
	     
	     
//	     second_adapter = new CommonPopMenuListAdapter(activity);
//	     coop_all_next_list_view.setAdapter(second_adapter);
	     
         /*合作地域来源的设置*/
	     coop_area_list_view = (ListView)coop_area_pop_view.findViewById(R.id.common_stair_pop_view_list);
	     coop_area_adapter = new CommonPopMenuListAdapter(activity,coop_area_list);
	     coop_area_list_view.setAdapter(coop_area_adapter);
	     coop_area_adapter.setSelectedPosition(0);
	     
	     
	     
	     
	   //左侧ListView点击事件
	     coop_all_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //二级数据
	            	coop_curr_all_resource = coop_all_list.get(position);
	            	first_adapter.setSelectedPosition(position);
	            	first_adapter.notifyDataSetChanged();
	            	if(coop_curr_all_resource != null)
	            	{
	            	second_adapter = new CommonPopMenuListAdapter(activity,coop_curr_all_resource.getSubResList());
	            	second_adapter.setSelectedPosition(0);
	            	coop_all_next_list_view.setAdapter(second_adapter);
	            	}
	            }
	        });

	     
	     
	   //右侧ListView点击事件
	     coop_all_next_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //关闭popupWindow，显示用户选择的分类
	            	second_adapter.setSelectedPosition(position);
	            	second_adapter.notifyDataSetChanged();
	                coop_all_pop_win.dismiss();
                    
	                List<Resource> sub_list = coop_curr_all_resource.getSubResList();
                    if(sub_list != null)
                    {
                    	res_type = sub_list.get(position).getResId();
                    	//strUrl = Url.composeCoopResourceListUrl(location, res_type, keyword, page);
                    	requestData();
                    	coop_all_select_text.setText( coop_curr_all_resource.getResName() + "/" + sub_list.get(position).getResName());
                    	cooper_type = coop_curr_all_resource.getResName();
                    }
	                

	            }
	        });
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
		   
	     coop_area_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //关闭popupWindow，显示用户选择的分类
	            	coop_area_adapter.setSelectedPosition(position);
	            	
	                coop_area_pop_win.dismiss();             
	                
	                location = coop_area_list.get(position).getResId()%1000;
	                coop_area_select_text.setText("地域/" + coop_area_list.get(position).getResName());
	                requestData();
                   
	            }
	        });
	     
	     
	     
	     

	     
	     

	    
	     
		//项目来源popupwindow
		coop_all_pop_win = new PopupWindow(coop_all_pop_view,screen_width/2,LayoutParams.WRAP_CONTENT);
		coop_all_pop_win.setOutsideTouchable(true); 
		
		coop_all_pop_win.setBackgroundDrawable(new BitmapDrawable());
		coop_all_pop_win.setFocusable(true);
		coop_all_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener(){

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				coop_all_list_view.setSelection(0);
				coop_all_next_list_view.setSelection(0);
				View vv = view.findViewById(R.id.coop_all_arrow);
				RotateAnimation rot_anim = new RotateAnimation(180,0,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
			}
			
		});
	
		
		
		//项目类型popupwindow
		coop_area_pop_win = new PopupWindow(coop_area_pop_view,screen_width/2,screen_height/2);
		coop_area_pop_win.setOutsideTouchable(true); 
		
		coop_area_pop_win.setBackgroundDrawable(new BitmapDrawable());
		coop_area_pop_win.setFocusable(true);
		coop_area_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener(){

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				coop_area_list_view.setSelection(0);
				View vv = view.findViewById(R.id.coop_area_arrow);
				RotateAnimation rot_anim = new RotateAnimation(180,0,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
			}
			
		});
		
		
		
		coop_i_offer_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				coop_relation = 0;
				TextView tv_need = (TextView) view.findViewById(R.id.coop_resource_i_need_text);
				TextView tv_offer = (TextView) view.findViewById(R.id.coop_resource_i_offer_text);
				tv_need.setTextColor(getResources().getColor(R.color.white));
				tv_offer.setTextColor(getResources().getColor(R.color.light_blue));
				requestData();
			}
			
		});
		
		
		coop_i_need_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				coop_relation = 1;
				TextView tv_need = (TextView) view.findViewById(R.id.coop_resource_i_need_text);
				TextView tv_offer = (TextView) view.findViewById(R.id.coop_resource_i_offer_text);
				tv_offer.setTextColor(getResources().getColor(R.color.white));
				tv_need.setTextColor(getResources().getColor(R.color.light_blue));
				requestData();
			}
			
		});
		

		
		coop_search_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        if(coop_search_et.getText().toString().isEmpty()){
		        	
		        	Toast.makeText(activity, "关键词不能为空",Toast.LENGTH_SHORT).show();
		        }
		        
		        else
		        {
		        	keyword = coop_search_et.getText().toString();
		        	requestData();
		        	InputMethodManager imm = (InputMethodManager)activity.
			                  getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.hideSoftInputFromWindow(v.getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);
		        }
			}
			
		});
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

				    strUrl = Url.composeCoopResourceListUrl(location, res_type, keyword, page);
					Log.i("coop_resource_url", strUrl);
				    url = new URL(strUrl);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					
					
					
					if("人力".equals(cooper_type))
					{   List<Cooper> temp = null;
						json = new JsonCoopResearchListHandler(activity,"labor");
						temp = (List<Cooper>) json.getListItems(input);
						if(temp != null)
						{
							coop_list = temp;
							handler.sendEmptyMessage(UPDATE_COOP_LIST);
							
						}else
						{
							Log.i("temp?==0","temp==0");
							handler.sendEmptyMessage(3);
						}
					}
					
					else if("设备".equals(cooper_type))
					{
					    json = json = new JsonCoopResearchListHandler(activity,"facility"); 
					    List<Facility> temp = null;
					    temp = (List<Facility>) json.getListItems(input);
					    if(temp != null)
					    {
						  faci_list = temp;
						  handler.sendEmptyMessage(UPDATE_COOP_LIST);
						
					   }else
					{
						Log.i("temp?==0","temp==0");
						handler.sendEmptyMessage(3);
					}
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
				  
					
				case UPDATE_COOP_LIST:
					if("人力".equals(cooper_type))
					{
					  coop_list_adapter = new CoopListAdapter(activity,coop_list);
					  coop_list_adapter.notifyDataSetChanged();
					  coop_list_view.setAdapter(coop_list_adapter);
					}
					
					else if("设备".equals(cooper_type))
					{
						faci_list_adapter = new FacilityListAdapter(activity,faci_list);
						faci_list_adapter.notifyDataSetChanged();
					    coop_list_view.setAdapter(faci_list_adapter);
					}

					coop_empty_content_hint_view.setVisibility(View.GONE);
					//Toast.makeText(activity, "加载成功", Toast.LENGTH_SHORT).show();
					//coop_list_view.addFooterView(cooper_more_view);
					break;
				case 3:
//					Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
					coop_list.clear();
					coop_list_adapter.notifyDataSetChanged();
					coop_empty_content_hint_view.setVisibility(View.VISIBLE);
					break;
				case 6:
					
					coop_list_adapter.notifyDataSetChanged();
					coop_list_adapter = new CoopListAdapter(activity,coop_list);
					coop_list_view.setAdapter(coop_list_adapter);
					coop_list_view.addFooterView(cooper_more_view);
					break;
					
				}
			}
		};

	  public void setOnClickListener()
		{


		   
	        //设置coop_all_btn点击弹出事件
	        coop_all_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					final View vv = (ImageView)view.findViewById(R.id.coop_all_arrow);
				
					if (coop_all_pop_win.isShowing()) {

						coop_all_pop_win.dismiss();		
			        }
					else{
					
						RotateAnimation rot_anim = new RotateAnimation(0,180,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
						rot_anim.setFillAfter(true);
						rot_anim.setDuration(100);
						vv.startAnimation(rot_anim);
						coop_all_pop_win.showAsDropDown((LinearLayout)view.findViewById(R.id.coop_all_layout));
					}
					

				}
	        	
	        });
	        
	        
	        
	        
	        
	        
	        //设置coop_area_btn点击弹出事件
	        coop_area_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final View vv = (ImageView)view.findViewById(R.id.coop_area_arrow);
					
					if (coop_area_pop_win.isShowing()) {

						coop_area_pop_win.dismiss();		
			        }
					else{
					
						RotateAnimation rot_anim = new RotateAnimation(0,180,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
						rot_anim.setFillAfter(true);
						rot_anim.setDuration(100);
						vv.startAnimation(rot_anim);
						coop_area_pop_win.showAsDropDown((LinearLayout)view.findViewById(R.id.coop_area_layout));
					}
					

				}
	        	
	        });
	        
	        
	        
	        
	        //底部增加发布按钮
	        coop_add_release_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent();
					intent.setClass(activity,CoopReleaseActivity.class);
					startActivity(intent);
				}
	        	
	        });
	        
	        
	        
	        
		}
		
		
		

		
		
		
		
	private OnItemClickListener coop_item_click_listener = new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				int receive_id = 0;
				String url = "";
				if("人力".equals(cooper_type)){
					url = coop_list.get(position).getCooperResUrl();
					receive_id = coop_list.get(position).getCooperId();
				}else if("设备".equals(cooper_type)){
					url = faci_list.get(position).getFaciResUrl();
				}
				coop_list.get(position).getCooperId();
				Intent intent = new Intent(activity,CoopDetailActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("receive_id", receive_id);
				activity.startActivity(intent);
//				String url = application.ComposeToken(Url.ProjectContentBaseUrl) + "&id=" + coop_list.get(position).get("id");
//				Log.i("proj_item_url", url);
//				Intent intent = new Intent();//用以传递数据
//				intent.setClass(activity, CommonContentActivity.class);
//				intent.putExtra("url", url);
//				intent.putExtra("act_class", "project");
//				startActivity(intent);
			}
			
		};
		

		
		
		

	
	
	
}
