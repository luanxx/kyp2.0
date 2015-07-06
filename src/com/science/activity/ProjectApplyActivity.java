package com.science.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.science.R;
import com.science.adapter.CommonFragmentPagerAdapter;
import com.science.adapter.CommonPopMenuListAdapter;
import com.science.adapter.SubjectPopMenuListAdapter;
import com.science.fragment.HotpageFragment;
import com.science.fragment.MainProjectApplyFragment;
import com.science.fragment.ProjectApplyFragment;

import com.science.model.FirstClassItem;
import com.science.model.Resource;
import com.science.model.ResourceDefine;
import com.science.model.SecondClassItem;
import com.science.services.DataCache;
import com.science.util.DefaultUtil;
import com.science.view.MyHeaderView;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectApplyActivity extends FragmentActivity {
	


	
	
	private LinearLayout main=null;
    private MyHeaderView proj_apply_header;
    private MainProjectApplyFragment proj_fragment_hot;//热点项目
	private MainProjectApplyFragment proj_fragment_expire;//即将到期
	
    private HotpageFragment proj_fragment_poineer;//创业项目
	private HotpageFragment proj_fragment_interpretation;//解读项目
	private Fragment[]           proj_fragments;             
	private ViewPager            proj_view_pager;
	private CommonFragmentPagerAdapter proj_fragment_adapter;
    private int                        curr_fragment_index;
	private CommonPopMenuListAdapter proj_src_first_list_adapter;
	private CommonPopMenuListAdapter proj_src_second_list_adapter;
	private CommonPopMenuListAdapter proj_type_list_adapter;
	private Resource                 curr_proj_src;
	private List<Resource> proj_src_all_list;
	private List<Resource> proj_type_list;
	public static final int PROJ_HOT = 0;
	public static final int PROJ_EXPIRE = 1;
	public static final int PROJ_POINEER = 2;
	public static final int PROJ_INTERPRETATION = 3;
	
	private int             proj_src1 = -1;
	private int             proj_src2 = -1;
	private int             proj_type = -1;
	private int             proj_id = 0;
	
	
	public static final int DEFAULT_FLAG = -1;
	private final   int        ADD_SELECT_VIEW = 0;
	private final    int       RM_SELECT_VIEW = 2;
	private final   int        ALL_PROJECT = -1;
	private final   int        NEW_PROJECT = 0;
	private boolean                removed;//标记select_view是否被移除
	
	
	
	
	
	private View            proj_select_view;
    private RelativeLayout  proj_main_layout;
    private TextView        proj_src_text;
    private TextView        proj_type_text;
    private List<List<Map<String,Object>>> frag_saved_data;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		LayoutInflater inflater = getLayoutInflater();  
		main=(LinearLayout)inflater.inflate(R.layout.project_apply, null); 
		
		setContentView(main);
		initVariable();
		initView();

	}
	

	
    
    private void initVariable(){
        DataCache.cache.clear();
        proj_src_all_list = ResourceDefine.defined_resource_proj_src_list;
        proj_type_list = ResourceDefine.defined_resource_proj_type_list;
        proj_src_first_list_adapter = new CommonPopMenuListAdapter(this,proj_src_all_list);
        proj_type_list_adapter = new CommonPopMenuListAdapter(this,proj_type_list);
   
        proj_src1 = -1;
        proj_src2 = -1;
        proj_type = proj_type_list.get(0).getResId();
        /*设置select_view被移除的状态*/
        removed = false;
        frag_saved_data = new ArrayList<List<Map<String,Object>>>();
        frag_saved_data.add(new ArrayList<Map<String,Object>>());
        frag_saved_data.add(new ArrayList<Map<String,Object>>());
    }
    
    
    

	private void initView()
	{

		initPopMenuView();
		proj_select_view = main.findViewById(R.id.proj_select_layout);
		proj_main_layout = (RelativeLayout) main.findViewById(R.id.proj_main_layout);
		/*-1表示全部,null也默认表示全部*/
	   // proj_fragment_hot = new MainProjectApplyFragment(PROJ_HOT,DefaultUtil.INAVAILABLE,-1,-1,-1,DefaultUtil.EMPTY,frag_saved_data.get(0));//热点项目
	    //proj_fragment_expire = new ProjectApplyFragment(PROJ_EXPIRE,proj_src_all_list.get(0).getResId(),-1,-1,-1,DefaultUtil.EMPTY,frag_saved_data.get(1)); //即将到期
		proj_fragment_hot = new MainProjectApplyFragment(PROJ_HOT);
		proj_fragment_expire = new MainProjectApplyFragment(PROJ_EXPIRE);
		
	    proj_fragment_poineer = new HotpageFragment(4);//创业项目
		proj_fragment_interpretation = new HotpageFragment(3);//解读项目
		proj_apply_header = (MyHeaderView) findViewById(R.id.proj_apply_header);
		
		proj_fragments = new Fragment[]{proj_fragment_hot,proj_fragment_expire,
				                        proj_fragment_poineer,proj_fragment_interpretation};
		proj_fragment_adapter = new CommonFragmentPagerAdapter(this.getSupportFragmentManager(),proj_fragments);

		/**/
		
		proj_src_text = (TextView) findViewById(R.id.proj_src_text);
		proj_type_text = (TextView) findViewById(R.id.proj_type_text);
		
		proj_view_pager = (ViewPager) findViewById(R.id.proj_view_pager);
		proj_view_pager.setAdapter(proj_fragment_adapter);
		proj_view_pager.setCurrentItem(0);
		proj_fragment_hot.loadProject(proj_src1, proj_src2, proj_type, 0, DefaultUtil.EMPTY);
		//设置proj_apply的header
		proj_apply_header.SetHeaderText("项目申报");
		String[] header_btn_strs = {"热点项目","即将到期","创业项目","项目解读"};
		proj_apply_header.SetHeaderButtons(header_btn_strs);
		
		proj_apply_header.SetSelected(0);
		OnClickListener proj_header_tab_listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				proj_view_pager.setCurrentItem(v.getId());
				curr_fragment_index = v.getId();
				
				if(v.getId() == PROJ_HOT ){
					
					
				}
				
				if(v.getId() == PROJ_POINEER - PROJ_HOT || v.getId() == PROJ_INTERPRETATION - PROJ_HOT)
				{
					handler.sendEmptyMessage(RM_SELECT_VIEW);
				}
				else{
					
					handler.sendEmptyMessage(ADD_SELECT_VIEW);
					MainProjectApplyFragment frag = (MainProjectApplyFragment) proj_fragments[curr_fragment_index];
				    frag.loadProject(proj_src1, proj_src2, proj_type, 0, DefaultUtil.EMPTY);
					//frag.reLoadProjFragment(frag_saved_data.get(curr_fragment_index));
					//frag.updateProjectFragment(proj_src1, proj_src2, proj_type, -1,DefaultUtil.EMPTY);
				}
			}
			
		};
		
		proj_apply_header.SetOnHeadButtonClickListener(proj_header_tab_listener, 0);
		proj_apply_header.SetOnHeadButtonClickListener(proj_header_tab_listener, 1);
		proj_apply_header.SetOnHeadButtonClickListener(proj_header_tab_listener, 2);
		proj_apply_header.SetOnHeadButtonClickListener(proj_header_tab_listener, 3);
		
		
		proj_view_pager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				
				proj_apply_header.SetSelected(position);
				curr_fragment_index = position;
				if(position == PROJ_POINEER - PROJ_HOT || position == PROJ_INTERPRETATION - PROJ_HOT)
				{

					handler.sendEmptyMessage(RM_SELECT_VIEW);
				}
				else{
					handler.sendEmptyMessage(ADD_SELECT_VIEW);
					MainProjectApplyFragment frag = (MainProjectApplyFragment) proj_fragments[curr_fragment_index];
					frag.loadProject(proj_src1, proj_src2, proj_type, 0, DefaultUtil.EMPTY);
					// frag.reLoadProjFragment(frag_saved_data.get(curr_fragment_index));
				   // frag.updateProjectFragment(proj_src1, proj_src2, proj_type, -1,DefaultUtil.EMPTY);
				}
			}
			
		});
		
		
	}
	

	
	
	
	
	/**
	 * 初始化ProjSourcePopView和ProjTypePopView
	 */
	 ListView  proj_src_list_view;
	 ListView  proj_src_next_list_view;
	 ListView  proj_type_list_view;
	 View      proj_src_pop_view;
	 View      proj_type_pop_view;
	 View      proj_src_btn;
	 View      proj_type_btn;
	 PopupWindow  proj_src_pop_win;
	 PopupWindow   proj_type_pop_win;
     String  str_url_proj_src;
	
    
    
    
    
	public void initPopMenuView(){
		
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		final int screen_width = wm.getDefaultDisplay().getWidth();
		final int screen_height = wm.getDefaultDisplay().getHeight();
		
		/**
		 * 项目来源和项目类型菜单设置
		 */
		proj_src_btn = findViewById(R.id.proj_src_layout);
		proj_type_btn = findViewById(R.id.proj_type_layout);
		
		proj_src_pop_view = getLayoutInflater().inflate(R.layout.common_secondary_pop_view, null);
		proj_type_pop_view = getLayoutInflater().inflate(R.layout.common_stair_pop_view, null);
		proj_src_list_view = (ListView) proj_src_pop_view.findViewById(R.id.common_secondary_pop_list);
	    
		proj_src_list_view.setAdapter(proj_src_first_list_adapter);
	    proj_src_next_list_view = (ListView) proj_src_pop_view.findViewById(R.id.common_secondary_pop_next_list);

	     
	     //proj_type处理
	     proj_type_list_view = (ListView)proj_type_pop_view.findViewById(R.id.common_stair_pop_view_list);
	     proj_type_list_adapter.setSelectedPosition(0);
	     proj_type_list_view.setAdapter(proj_type_list_adapter);
	   //左侧ListView点击事件
	     proj_src_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	
	            	
	            	proj_src_first_list_adapter.setSelectedPosition(position);
	            	proj_src_first_list_adapter.notifyDataSetChanged();
	            	curr_proj_src = proj_src_all_list.get(position);
	            	proj_src1 = curr_proj_src.getResId();
	            	List<Resource> sub_proj_src_list;
	            	if(curr_proj_src != null)
	            	{
	            	  sub_proj_src_list = curr_proj_src.getSubResList();
	            	  proj_src_second_list_adapter = new CommonPopMenuListAdapter(ProjectApplyActivity.this,sub_proj_src_list);
	            	  proj_src_next_list_view.setAdapter(proj_src_second_list_adapter);
	            	}
          
                    

	            	/*设置二级菜单的高度*/
                    int height = proj_src_list_view.getHeight();
    	                LayoutParams params = proj_src_next_list_view.getLayoutParams();
    	                params.height = (int) (2*height) ;
    	                proj_src_next_list_view.setLayoutParams(params);
    	                proj_src_next_list_view.setMinimumHeight(height);
	            }
	        });

	     
	     
	   //右侧ListView点击事件
	     proj_src_next_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //关闭popupWindow，显示用户选择的分类
	            	
	            	
	            	proj_src_second_list_adapter.setSelectedPosition(position);
	            	proj_src_second_list_adapter.notifyDataSetChanged();
	            	
	                proj_src_pop_win.dismiss();

	                
	                if(curr_proj_src.getSubResList() != null){

                          Resource sub_proj_src = curr_proj_src.getSubResList().get(position);
                          proj_src2 = sub_proj_src.getResId()%1000;
                          if(proj_src2 == 0)
                        	  proj_src2 = -1;
                          MainProjectApplyFragment frag = (MainProjectApplyFragment) proj_fragments[curr_fragment_index];
	                      frag.loadProject(proj_src1, proj_src2, proj_type, 0, DefaultUtil.EMPTY);
                          //frag.updateProjectFragment(proj_src1, proj_src2, proj_type, -1,DefaultUtil.EMPTY);
	                      proj_src_text.setText(curr_proj_src.getResName() + "/" + sub_proj_src.getResName());
	                }
	               
	                
	                

	            }
	        });
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
		   //项目类型ListView点击事件
	     proj_type_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    

	                proj_type_list_adapter.setSelectedPosition(position);
	                proj_type_list_adapter.notifyDataSetChanged();
	                //关闭popupWindow，显示用户选择的分类
	                proj_type_pop_win.dismiss();       
	                proj_type = position - 1;
	                proj_type_text.setText(proj_type_list.get(position).getResName());
                    MainProjectApplyFragment frag = (MainProjectApplyFragment) proj_fragments[curr_fragment_index];
                    frag.loadProject(proj_src1, proj_src2, proj_type, 0, DefaultUtil.EMPTY);
                    //frag.updateProjectFragment(proj_src1, proj_src2, proj_type, -1,DefaultUtil.EMPTY);
	                
	            }
	        });
	     
	     
	     
	     
	     
	    
	     
		//项目来源popupwindow
		proj_src_pop_win = new PopupWindow(proj_src_pop_view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		proj_src_pop_win.setOutsideTouchable(true); 
		
		proj_src_pop_win.setBackgroundDrawable(new BitmapDrawable());
		proj_src_pop_win.setFocusable(true);
		proj_src_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener(){

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				proj_src_list_view.setSelection(0);
				proj_src_next_list_view.setSelection(0);
				View vv = findViewById(R.id.proj_src_arrow);
				RotateAnimation rot_anim = new RotateAnimation(180,0,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
			}
			
		});
	
		
		
		//项目类型popupwindow
		proj_type_pop_win = new PopupWindow(proj_type_pop_view,screen_width/2,LayoutParams.WRAP_CONTENT);
		proj_type_pop_win.setOutsideTouchable(true); 
		
		proj_type_pop_win.setBackgroundDrawable(new BitmapDrawable());
		proj_type_pop_win.setFocusable(true);
		proj_type_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener(){

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				proj_type_list_view.setSelection(0);
				View vv = findViewById(R.id.proj_type_arrow);
				RotateAnimation rot_anim = new RotateAnimation(180,0,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
			}
			
		});
		
		
		setOnMenuClickListener();
	}
	
	
	
	


	









private void setOnMenuClickListener(){
	   
    //设置proj_src_btn点击弹出事件
    proj_src_btn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final View vv = main.findViewById(R.id.proj_src_arrow);
			
			if (proj_src_pop_win.isShowing()) {

				proj_src_pop_win.dismiss();		
	        }
			else{
			
				RotateAnimation rot_anim = new RotateAnimation(0,180,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
				proj_src_pop_win.showAsDropDown(main.findViewById(R.id.proj_src_layout));
			}
			

		}
    	
    });
    
    
    
    
    
    
    //设置proj_type_btn点击弹出事件
    proj_type_btn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final View vv = main.findViewById(R.id.proj_type_arrow);
			
			if (proj_type_pop_win.isShowing()) {

				proj_type_pop_win.dismiss();		
	        }
			else{
			
				RotateAnimation rot_anim = new RotateAnimation(0,180,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
				proj_type_pop_win.showAsDropDown(main.findViewById(R.id.proj_type_layout));
			}
			

		}
    	
    });
}




	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			
			switch(msg.what){
			
			case ADD_SELECT_VIEW:
				if(removed)
				{
					proj_main_layout.addView(proj_select_view);
				    removed = false;
				}
				break;
			case RM_SELECT_VIEW:
				if(!removed)
				{
					proj_main_layout.removeView(proj_select_view);
				    removed = true;
				}
				break;
			}
		}
	};


	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if(DataCache.hot_project_list != null)
			DataCache.hot_project_list.clear();
			
			if(DataCache.usual_project_list != null)
			DataCache.usual_project_list.clear();
			
			if(DataCache.expire_project_list != null)
			DataCache.expire_project_list.clear();
			this.finish();
			return false;
		} else {
			return super.dispatchKeyEvent(event);
		}
	}
}
