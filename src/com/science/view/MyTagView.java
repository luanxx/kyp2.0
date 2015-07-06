package com.science.view;
import com.example.science.R;
import com.science.services.MyApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;


public class MyTagView extends LinearLayout  implements OnFocusChangeListener{
	
	private Context context;
	private TableLayout tag_tab_layout;
	private RelativeLayout other_tag_layout;
	private MyImageButton fold_btn;
	private TextView other_tag_tv;
	private EditText other_input_ed ;
	private int rows;
	private int num_cols;//表示一行有几个
	private boolean  hide;//表示折叠按钮是否处于折叠状态
	private int tag_tv_width;
	private int tag_tv_height;
	private int[] tags_state;//记录8个标签的状态
	private String other_tag_str;
    private MyApplication application;
    public static  final boolean HIDE = false;
    public static final boolean SHOW = true;
    private int all_keywords_num;
	private Activity activity;
	
	private String[] tag_words;//记录标签的关键词数组
	private int id;
	private int type;
	private StringBuffer kywd;
	private OnTagChangeListener listener;
	public MyTagView(Context context,AttributeSet attr) {
		super(context,attr);
		this.context = context;
		this.activity = (Activity) context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
		inflater.inflate(R.layout.common_suspend_tag, this);
		this.setOnFocusChangeListener(this);
		this.kywd = new StringBuffer("");
		initMyTagView();	
	}

	
	public void setOnTagChangeListener(OnTagChangeListener l){
		this.listener = l;
	}
	
	private void initMyTagView()
	{
		num_cols = 4;
		hide = true;
		tag_tv_width = 0;
		tag_tv_height = 0;

		application = (MyApplication) this.activity.getApplication();
	
		tags_state = new int[8];
		tag_words = new String[8];
		
		for(int i = 0;i < 8;i++)
		{
			tags_state[i] = 0;
			tag_words[i] = "";
		}
		other_tag_str = "";

		
		all_keywords_num = application.non_null_keywords_list.size();
		
		for(int i = 0;i < all_keywords_num;i++){
			tags_state[i] = 1;
			tag_words[i] = application.non_null_keywords_list.get(i).toString();
		}
	    rows = (all_keywords_num + 3)/4;
	    
	    
	    
	    //init views
	    
		//其他标签
		other_tag_tv = (TextView)findViewById(R.id.tag_input);
		other_tag_tv.setText(other_tag_str);
		other_tag_tv.setOnClickListener(other_tag_click_listener);

		
		fold_btn = (MyImageButton)findViewById(R.id.fold_btn);
		tag_tab_layout = (TableLayout)findViewById(R.id.tag_tab_layout);
		other_tag_layout = (RelativeLayout)findViewById(R.id.other_tag_layout);

		
      fold_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hide = !hide;
				if(hide)
				{
					v.setBackground(getResources().getDrawable(R.drawable.icon_extend));
					other_tag_layout.setVisibility(View.GONE);
				}
				else
				{
				    v.setBackground(getResources().getDrawable(R.drawable.unfold));
				    other_tag_layout.setVisibility(View.VISIBLE);
				}
				
				setTagLayout(hide);
			}
      	
      });
		
      
	 setTagLayout(true);
	}
	
	
	public StringBuffer getTagKeywords(){
		kywd.setLength(0);
		for(int i = 0;i < tags_state.length;i++)
		{

			if(tags_state[i] == 1){
				
				kywd.append(tag_words[i]);
				kywd.append("||");
			}
			
		}
		//需要将最后的两个"||"删除掉
		if(kywd.length() > 2)
		{// Log.i("kywd_length", "" + kywd.length());
		    //kywd.delete(kywd.length() - 1, kywd.length());
		    kywd.deleteCharAt(kywd.length() - 1);
		    kywd.deleteCharAt(kywd.length() - 1);
		   
		}
		else if(kywd.length() <= 0){
			
			kywd.append("-1");
		}
		
		return kywd;
	}
	
	private void setTagLayout(boolean hide)
	{
		if(tag_tab_layout.getChildCount() != 0)
			tag_tab_layout.removeAllViews();
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		fold_btn.measure(w, h);
		if(all_keywords_num != 0)
		tag_tv_width = (activity.getWindowManager().getDefaultDisplay().getWidth() - fold_btn.getMeasuredWidth())/(all_keywords_num < 4?all_keywords_num:4);
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
			TableRow tr = new TableRow(context);
		  for(int i = 0;i < num_cols;i++)
		  {
			  if(j*num_cols + i >= all_keywords_num)
				  break;
			TextView tv = new TextView(context);
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
			
		  }
		  
		  tag_tab_layout.addView(tr);
		}
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
			kywd.setLength(0);
			
			
			
			for(int i = 0;i < tags_state.length;i++)
			{

				if(tags_state[i] == 1){
					
					kywd.append(tag_words[i]);
					kywd.append("||");
				}
				
			}
			//需要将最后的两个"||"删除掉
			if(kywd.length() > 2)
			{ 
			    //kywd.delete(kywd.length() - 1, kywd.length());
			    kywd.deleteCharAt(kywd.length() - 1);
			    kywd.deleteCharAt(kywd.length() - 1);
			   
			}
			else if(kywd.length() <= 0){
				
				kywd.append("-1");
			}
			
			listener.onTagChange(kywd); 
			
			//doc_keywords = application.keywords[tag_id];

			
			setTagLayout(hide);
			
		}
		
	};
	
	
	
	private OnClickListener other_tag_click_listener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			other_input_ed = new EditText(activity);
			other_input_ed.setText(other_tag_str);
			new AlertDialog.Builder(activity)
			.setTitle("请输入其他标签")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(other_input_ed)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					other_tag_str = other_input_ed.getText().toString();
					other_tag_tv.setText(other_tag_str);
					
					
					
					
					
					
					
					
					
					/*更新标签StringBuffer先清空*/
					kywd.setLength(0);
					
					
					
//					for(int i = 0;i < tags_state.length;i++)
//					{
//
//						if(tags_state[i] == 1){
//							
//							kywd.append(tag_words[i]);
//							kywd.append("||");
//						}
//						
//					}
					
					
//					//需要将最后的两个"||"删除掉
//					if(kywd.length() > 2)
//					{ 
//					    //kywd.delete(kywd.length() - 1, kywd.length());
//					    kywd.deleteCharAt(kywd.length() - 1);
//					    kywd.deleteCharAt(kywd.length() - 1);
//					   
//					}

					
					kywd.append(other_tag_str);
					
					if(kywd.length() <= 0){
						
						kywd.append("-1");
					}
					listener.onTagChange(kywd); 
					
				}
			})
			.setNegativeButton("取消",null)
			.show();
		}
		
	};
	
	
	

	
	
	
	//设置焦点改变事件
	@Override
	public void onFocusChange(View arg0, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if(!hasFocus)
		{
			hide = true;
			fold_btn.setBackground(getResources().getDrawable(R.drawable.icon_extend));
			other_tag_layout.setVisibility(View.GONE);
			setTagLayout(hide);
		}
	}
	
	
	

	
	
	public interface OnTagChangeListener{
		
		public void onTagChange(StringBuffer sb);
	}
	
	
}
