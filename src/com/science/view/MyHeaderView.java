package com.science.view;

import java.net.ContentHandler;

import com.example.science.R;
import com.science.activity.MainActivity;
import com.science.activity.SettingManageActivity;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyHeaderView extends LinearLayout {

	public TextView tv=null;
	public LinearLayout headerContentView=null;
	public String [] headcontent=null;
	public Button[] headButtons=null;
	public View.OnClickListener[] onClickListeners=null;
	public ImageButton home_btn;
	public ImageButton setting;
	private Context headContent;

	private MyApplication application;
	private FunctionManage fm;
	
	private Drawable corner_left_side_fill;
	private Drawable corner_left_side_null;
	private Drawable corner_right_side_fill;
	private Drawable corner_right_side_null;
	private Drawable rect_middle_fill;
	private Drawable rect_middle_null;
	
	private float header_all_btn_prop;//所有按钮的比重
	private float header_btn_ratio;//按钮的比率
	private int screen_width;
	private int header_btn_all_width;

	public MyHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.header, this);  
        application = MyApplication.getInstance();
        headContent=context;

        InitView();
        fm = new FunctionManage(context);

	}


	/*隐藏主页按钮*/
	public void hideHeaderView(String name){
		
		if("home".equals(name)){
			home_btn.setVisibility(View.INVISIBLE);
		}
		
	}
	
	
	
	
	public MyHeaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
//        imageView=(ImageView) findViewById(R.id.imageView1);
//        textView=(TextView)findViewById(R.id.textView1);


        headContent=context;

       InitView();
       application = MyApplication.getInstance();
       fm = new FunctionManage(context);
	}

	public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
//        imageView=(ImageView) findViewById(R.id.imageView1);
//        textView=(TextView)findViewById(R.id.textView1);
        InitView();
        application = MyApplication.getInstance();
        fm = new FunctionManage(context);
	}
	
	
	
	
	
	
	private void InitView()
	{
		header_all_btn_prop = 500.0f/720.f;
		WindowManager wm = (WindowManager) headContent.getSystemService(Context.WINDOW_SERVICE);
		screen_width = wm.getDefaultDisplay().getWidth();
		header_btn_all_width = (int) (screen_width*header_all_btn_prop);
		
		

		
		tv=(TextView)this.findViewById(R.id.headertitle);
		headerContentView=(LinearLayout)this.findViewById(R.id.headcontent);
		home_btn = (ImageButton) this.findViewById(R.id.doc_home_btn);
		home_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getContext().startActivity(new Intent(getContext(),MainActivity.class));
				((Activity) getContext()).finish();
			}
			
		});
		
		setting=(ImageButton)this.findViewById(R.id.myheadersettingmanage);
		setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//if(application.IsLogin())		
					headContent.startActivity(new Intent(headContent,SettingManageActivity.class));
				//else
				//	fm.Login();

				//((Activity) headContent).finish();
			}
		});
		
	}
	
	public Boolean SetHeaderText(String str)
	{
		if (tv!=null) {
			tv.setText(str);
		}
		return true;
	}
	
	//调用这个函数表示header中需要设置按钮
	
	public Boolean SetHeaderButtons(String [] str)
	{
		int length = str.length;
		if(length <= 2)
		{	
			corner_left_side_fill = getResources().getDrawable(R.drawable.corner_left_side_white);
			corner_left_side_null = getResources().getDrawable(R.drawable.corner_left_side_transparent);
			corner_right_side_fill = getResources().getDrawable(R.drawable.corner_right_side_white);
			corner_right_side_null = getResources().getDrawable(R.drawable.corner_right_side_transparent);
			rect_middle_fill = getResources().getDrawable(R.drawable.rect_middle_white);	
			rect_middle_null = getResources().getDrawable(R.drawable.rect_middle_transparent);
		}
		else{
			corner_left_side_fill = getResources().getDrawable(R.drawable.corner_left_side_white_small);
			corner_left_side_null = getResources().getDrawable(R.drawable.corner_left_side_transparent_small);
			corner_right_side_fill = getResources().getDrawable(R.drawable.corner_right_side_white_small);
			corner_right_side_null = getResources().getDrawable(R.drawable.corner_right_side_transparent_small);
			rect_middle_fill = getResources().getDrawable(R.drawable.rect_middle_white_small);
			rect_middle_null = getResources().getDrawable(R.drawable.rect_middle_transparent_small);

		}
		
		headerContentView.setPadding(0, 0, 0, 20);
		
		if (str!=null&&str.length>1) {
			
			headButtons=new Button[str.length];
			
			onClickListeners=new OnClickListener[str.length];
			
			for(int i=0;i<str.length;i++) {
				headButtons[i]=new Button(headerContentView.getContext());
				int head_btn_width = header_btn_all_width/length;
                //int head_btn_height = (int) (header_btn_ratio*head_btn_width);
				headButtons[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				headButtons[i].setTextColor(Color.WHITE);
				headButtons[i].setText(str[i]);
				headButtons[i].setTextSize(15);
				headButtons[i].setGravity(Gravity.CENTER);
				headButtons[i].setPadding(0, 3, 0, 3);
				headButtons[i].setMinWidth(0);
				headButtons[i].setMinHeight(0);
				headButtons[i].setMinimumWidth(0);
				headButtons[i].setMinimumHeight(0);
                if(i == 0)
                	headButtons[i].setBackground(corner_left_side_null);
                else if(i == str.length - 1)
                	headButtons[i].setBackground(corner_right_side_null);
                else
                	headButtons[i].setBackground(rect_middle_null);
                //headButtions[i].setBackground(background)
				//headButtions[i].setLayoutParams(lParams);
				headButtons[i].setId(i);
				headerContentView.addView(headButtons[i]);
				headButtons[i].setOnClickListener(onClickListener);
			} 
		}

		return true;
	}
	
	 
	
	
	
	public Boolean SetOnHeadButtonClickListener(View.OnClickListener onClickListener,int index)
	{
		if(onClickListener!=null)
		{
			onClickListeners[index] = onClickListener;
		}

		return true;
	}
	
	
	
	public Boolean SetSelected(int index)
	{
		for(int i=0;i<headButtons.length;i++)
		{
			if(i == 0)
			    headButtons[i].setBackground(corner_left_side_null);
			else if(i == headButtons.length - 1)
				headButtons[i].setBackground(corner_right_side_null);
			else
				headButtons[i].setBackground(rect_middle_null);
		   
			headButtons[i].setTextColor(Color.WHITE);
		}
		headButtons[index].setBackgroundColor(getResources().getColor(R.color.white));
		
		headButtons[index].setTextColor(getResources().getColor(R.color.light_blue));
		
		if(index == 0)
			headButtons[index].setBackground(corner_left_side_fill);
		else if(index == headButtons.length - 1)
			headButtons[index].setBackground(corner_right_side_fill);
		else
			headButtons[index].setBackground(rect_middle_fill);
		
		return true;
	}
	private View.OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(headButtons!=null)
			{
				for(int i=0;i<headButtons.length;i++)
				{
					if(i == 0)
					    headButtons[i].setBackground(corner_left_side_null);
					else if(i == headButtons.length - 1)
						headButtons[i].setBackground(corner_right_side_null);
					else
						headButtons[i].setBackground(rect_middle_null);
				   
					headButtons[i].setTextColor(Color.WHITE);
				}
			}
			
			headButtons[v.getId()].setTextColor(getResources().getColor(R.color.light_blue));
			
			if(v.getId() == 0)
				headButtons[v.getId()].setBackground(corner_left_side_fill);
			else if(v.getId() == headButtons.length - 1)
				headButtons[v.getId()].setBackground(corner_right_side_fill);
			else
				headButtons[v.getId()].setBackground(rect_middle_fill);
			
			
			if (onClickListeners!=null) {
				onClickListeners[v.getId()].onClick(v);
			}
		}
	};
	

}
