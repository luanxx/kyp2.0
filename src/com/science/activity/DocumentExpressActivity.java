package com.science.activity;

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
import java.util.zip.Inflater;

import com.example.science.R;
import com.science.adapter.CommonFragmentPagerAdapter;
import com.science.adapter.DocumentListAdapter;
import com.science.adapter.DocumentTagAdapter;
import com.science.fragment.DocumentExpressFragment;
import com.science.interfaces.OnLoadingStateChangedListener;
import com.science.json.JsonDcumentListHandler;
import com.science.services.DataCache;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.DefaultUtil;
import com.science.util.Url;
import com.science.view.MyHeaderView;
import com.science.view.MyImageButton;
import com.science.view.MyTagView;
import com.science.view.MyTagView.OnTagChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Message;
public class DocumentExpressActivity extends FragmentActivity {

	private MyHeaderView doc_header_view;

	private TableLayout tag_tab_layout;
	private RelativeLayout other_tag_layout;
	private MyImageButton fold_btn;
	private WindowManager wm;
	private TextView other_tag_tv;
	private MyTagView tag_view;
	private int rows;
	private int num_cols;//表示一行有几个
	private boolean  hide;//表示折叠按钮是否处于折叠状态
	private int tag_tv_width;
	private int tag_tv_height;
	private int[] tags_state;//记录8个标签的状态
	private String other_tag_str;
	/*与数据解析有关*/
	private JsonDcumentListHandler json = null;
	private String strUrl = null;
	private List<Map<String,String>> doc_list;
	
	
	

    private MyApplication application;
    
    
    /*定义一些常参数*/         
    private final int       UPDATE_TAG_VIEW = 0;
    public static final int       DOC_CHI = 201;
    public static final int       DOC_ENG = 202;
    public static final int       DOC_WORK = 203;
    public static final int       DOC_NSF = 204;
    private final String    DEFAULT_DOC_KEYWORD = "-1";
    
    private int              doc_id = 0;
    private int              doc_type = DOC_CHI;
    private String            doc_keywords = "";
    private String            pdate = "";
    public static  final boolean HIDE = false;
    public static final boolean SHOW = true;
    private StringBuffer current_keyword = new StringBuffer("");;
    
    
    private StringBuffer doc_list_url;
    
    private int all_keywords_num;
    
    
    //定义四个fragment
    private DocumentExpressFragment doc_fragment_chi;//中文文献
    private DocumentExpressFragment doc_fragment_eng;//中文文献
    private DocumentExpressFragment doc_fragment_work;//中文文献
    private DocumentExpressFragment doc_fragment_nsf;//中文文献
    private ViewPager doc_view_pager;
    private CommonFragmentPagerAdapter doc_fragment_adapter;
    private FragmentManager fm;
    
    
    private Fragment[] fragments;
    
    private List<List<Map<String,Object>>> frag_saved_data;
    private int curr_fragment_index;
    private String[] tag_words;//记录标签的关键词数组 
    private OnTagChangeListener on_tag_change_listener;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.document_express);
		initVariable();
		initViews();
		
	   // requestData();//刚启动的时候刷新一次数据
		//setOnClickListener();
	}
	
	
	public void initVariable() 
	{
		num_cols = 4;
		hide = true;
		tag_tv_width = 0;
		tag_tv_height = 0;

		application = (MyApplication) this.getApplication();
		doc_list = null;
		tags_state = new int[8];
		tag_words = new String[8];
		for(int i = 0;i < 8;i++)
		{
			tags_state[i] = 0;
		}
		other_tag_str = "";

		
		all_keywords_num = application.non_null_keywords_list.size();
	    rows = (all_keywords_num + 3)/4;
	    
	    //表示当前的关键词
	    
	    current_keyword = new StringBuffer("");
	    current_keyword.append(DEFAULT_DOC_KEYWORD);
	    on_tag_change_listener = new OnTagChangeListener(){

			@Override
			public void onTagChange(StringBuffer sb) {
				// TODO Auto-generated method stub
				DocumentExpressFragment frag = (DocumentExpressFragment) fragments[curr_fragment_index];
				doc_type = DOC_CHI + curr_fragment_index;
				frag.loadDocument(pdate, doc_id, doc_type, sb);
				//frag.updateDocListFragment(doc_id, doc_type, sb);
			}
	    	
	    };
	    
	    
	    
	    frag_saved_data = new ArrayList<List<Map<String,Object>>>();
	    frag_saved_data.add(new ArrayList<Map<String,Object>>());
	    frag_saved_data.add(new ArrayList<Map<String,Object>>());
	    frag_saved_data.add(new ArrayList<Map<String,Object>>());
	    frag_saved_data.add(new ArrayList<Map<String,Object>>());
	    
	    
	    
	    
	}
	

	
	
	public void initViews()
	{

		
		wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int screen_width = wm.getDefaultDisplay().getWidth();
		int screen_height = wm.getDefaultDisplay().getHeight();
		
		//获取文献速递页面的header并设置
		doc_header_view = (MyHeaderView) findViewById(R.id.doc_express_common_header_layout);
		doc_header_view.SetHeaderText("文献速递");
		String[] header_button_strs = {"中文文献","英文文献","工作文献","NSF"};
		doc_header_view.SetHeaderButtons(header_button_strs);

		tag_view = (MyTagView) findViewById(R.id.doc_tag_view);
		tag_view.setOnTagChangeListener(on_tag_change_listener);
		//设置fragment
		fm = this.getSupportFragmentManager();
		
		doc_view_pager = (ViewPager) findViewById(R.id.doc_view_pager);
		
		
		//设置当前关键词
		current_keyword = tag_view.getTagKeywords();
		//Log.i("关键词", current_keyword.toString());
		doc_fragment_chi = new DocumentExpressFragment();
		doc_fragment_eng = new DocumentExpressFragment();
		//doc_fragment_chi = new DocumentExpressFragment(0,DOC_CHI, current_keyword,frag_saved_data.get(0));
		//doc_fragment_eng = new DocumentExpressFragment(0,DOC_ENG, current_keyword,frag_saved_data.get(1));	
		doc_fragment_work = new DocumentExpressFragment(0,DOC_WORK, current_keyword,frag_saved_data.get(2));
		doc_fragment_nsf = new DocumentExpressFragment(0,DOC_NSF, current_keyword,frag_saved_data.get(3));
		
		
		fragments = new Fragment[]{doc_fragment_chi,doc_fragment_eng,doc_fragment_work,doc_fragment_nsf};
		
		
		doc_fragment_adapter = new CommonFragmentPagerAdapter(fm,fragments );
		doc_view_pager.setAdapter(doc_fragment_adapter);
		doc_view_pager.setCurrentItem(0);
		doc_fragment_chi.loadDocument(pdate, doc_id, doc_type, current_keyword);
		doc_view_pager.setOnPageChangeListener(new OnPageChangeListener(){

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
				doc_header_view.SetSelected(position);	
				//DocumentExpressFragment last_frag = (DocumentExpressFragment) fragments[curr_fragment_index];
				//last_frag.saveDocFragment(frag_saved_data.get(curr_fragment_index));
				curr_fragment_index = position;
				DocumentExpressFragment frag = (DocumentExpressFragment) fragments[curr_fragment_index];
				doc_type = DOC_CHI + curr_fragment_index;
				frag.loadDocument(pdate, doc_id, doc_type, current_keyword);
				//frag.reLoadDocFragment(frag_saved_data.get(position));
				//frag.updateDocListFragment(doc_id, doc_type, current_keyword);
				//frag.reLoadDocFragment();
				
			}
			
		});
		
		
		
		OnClickListener doc_header_tab_listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				doc_view_pager.setCurrentItem(v.getId());
				//DocumentExpressFragment last_frag = (DocumentExpressFragment) fragments[curr_fragment_index];
				//last_frag.saveDocFragment(frag_saved_data.get(curr_fragment_index));
				curr_fragment_index = v.getId();
				DocumentExpressFragment frag = (DocumentExpressFragment) fragments[curr_fragment_index];
				doc_type = DOC_CHI + curr_fragment_index;
				frag.loadDocument(pdate, doc_id, doc_type, current_keyword);
				//frag.reLoadDocFragment(frag_saved_data.get(v.getId()));
				//frag.updateDocListFragment(doc_id, doc_type, current_keyword);
				//frag.reLoadDocFragment();
			}
			
		};
		
		doc_header_view.SetOnHeadButtonClickListener(doc_header_tab_listener, 0);
		doc_header_view.SetOnHeadButtonClickListener(doc_header_tab_listener, 1);
		doc_header_view.SetOnHeadButtonClickListener(doc_header_tab_listener, 2);
		doc_header_view.SetOnHeadButtonClickListener(doc_header_tab_listener, 3);
		doc_header_view.SetSelected(0);
		
		
		
		
		
	}
	
	
	
	private void setTagLayout(boolean hide)
	{
		if(tag_tab_layout.getChildCount() != 0)
			tag_tab_layout.removeAllViews();
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		fold_btn.measure(w, h);
		if(all_keywords_num != 0)
		tag_tv_width = (getWindowManager().getDefaultDisplay().getWidth() - fold_btn.getMeasuredWidth())/(all_keywords_num < 4?all_keywords_num:4);
		else
		tag_tv_width = 0;
		

		
		int temp_rows = rows;
		
		if(hide)
			temp_rows = 1;
		else
			temp_rows = rows;
		
		
		if(hide)
			tag_tv_height = fold_btn.getMeasuredHeight();
		else if(temp_rows == 1)
			tag_tv_height = fold_btn.getMeasuredHeight();
		else if(temp_rows == 2)
			tag_tv_height = fold_btn.getMeasuredHeight()/2 + 1;
		for(int j = 0;j < temp_rows;j++)
		{
			TableRow tr = new TableRow(this);
		  for(int i = 0;i < num_cols;i++)
		  {
			  if(j*num_cols + i >= all_keywords_num)
				  break;
			TextView tv = new TextView(this);
			if(tags_state[j*num_cols + i] == 0)	
				tv.setBackground(getResources().getDrawable(R.drawable.shape_light_blue));
			else
				tv.setBackground(getResources().getDrawable(R.drawable.shape_dark_blue));
			tv.setText(application.non_null_keywords_list.get(j*num_cols + i));
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(15.0f);
			tv.setTextColor(Color.WHITE);
			tv.setMinimumWidth(tag_tv_width);
			tv.setMinimumHeight(tag_tv_height);
			int tag_id = j*num_cols + i;
			tv.setTag(tag_id);
			tv.setOnClickListener(tag_click_listener);
			tr.addView(tv);
			tag_words[j*num_cols + i] = tv.getText().toString();
		  }
		  
		  tag_tab_layout.addView(tr);
		}
	}
	
	

	
	
	public void setOnClickListener()
	{


		//设置标题上的三个按钮的监听事件

		

        doc_header_view.SetSelected(0);
        
        //设置fold按钮的监听事件

	}
	
	
	

    private String composeDocListUrl(int doc_id,int doc_type,StringBuffer doc_keywords)
    {
    	
    	StringBuffer sb = new StringBuffer(application.ComposeToken(Url.DocumentLIST));
    	try {
			sb.append("&Id=" + doc_id +"&" + "source=" + doc_type + "&keyWords=" + URLEncoder.encode(doc_keywords.toString(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return sb.toString();
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

				//在更新之前先把所有的tag_tab_layout子View删掉
			
				setTagLayout(hide);
				if(hide)
					other_tag_layout.setVisibility(View.GONE);
				else
					other_tag_layout.setVisibility(View.VISIBLE);
				
				break;

			}
		}
	};

	
	
	

	
	public void setContent(int i){
		
	}
	
	
	
	
	
	private OnClickListener tag_click_listener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int tag_id = (Integer) v.getTag();
			if(tags_state[tag_id] == 0)//表明还未选中
			{
				v.setBackground(getResources().getDrawable(R.drawable.shape_dark_blue));
				tags_state[tag_id] = 1;
			}
			else
			{
				v.setBackground(getResources().getDrawable(R.drawable.shape_light_blue));
				tags_state[tag_id] = 0;				
			}
			
			
			/*更新标签StringBuffer先清空*/
			current_keyword.setLength(0);
			
			
			for(int i = 0;i < tags_state.length;i++)
			{

				if(tags_state[i] == 1){
					
					current_keyword.append(tag_words[i]);
					current_keyword.append("||");
				}
				
			}
			//需要将最后的两个"||"删除掉
			current_keyword.delete(current_keyword.length() - 2, current_keyword.length() - 1);
			
			
			DocumentExpressFragment frag = (DocumentExpressFragment) fragments[curr_fragment_index - DOC_CHI];
			doc_type = DOC_CHI + curr_fragment_index;
			frag.updateDocListFragment(doc_id, doc_type, current_keyword);
			
			

//			doc_keywords = application.keywords[tag_id];
//			if(tags_state[tag_id] == 1)
//			strUrl = Url.composeDocListUrl(doc_id,doc_type,doc_keywords);
//			else
//			strUrl = composeDocListUrl(doc_id,doc_type,new StringBuffer());
			
			setTagLayout(hide);
			
		}
		
	};
	
	
	private EditText other_input_ed ;
	
	private OnClickListener other_tag_click_listener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			other_input_ed = new EditText(DocumentExpressActivity.this);
			other_input_ed.setText(other_tag_str);
			new AlertDialog.Builder(DocumentExpressActivity.this)
			.setTitle("请输入其他标签")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(other_input_ed)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					other_tag_str = other_input_ed.getText().toString();
					other_tag_tv.setText(other_tag_str);
				}
			})
			.setNegativeButton("取消",null)
			.show();


			
			
			
			
			
			
		}
		
	};
	
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if(DataCache.doc_lists != null)
			{
				for(int i = 0;i < DataCache.doc_lists.size();i++){
					DataCache.doc_lists .get(i).clear();
				}
			}
			this.finish();
			return false;
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	


	
	
}
