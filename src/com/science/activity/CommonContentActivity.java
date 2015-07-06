package com.science.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.science.R;
import com.science.services.DataCache;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.services.ToastProxy;
import com.science.util.DefaultUtil;
import com.science.util.ShoucangUtil;
import com.science.util.Url;
import com.science.view.MyHeaderView;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class CommonContentActivity extends Activity{
	private View main=null;
	ProgressBar bar;
	private WebView webView=null;
	private MyImageButton go_back_btn;
	private MyImageButton shoucang_btn;
	private MyImageButton comment_btn;
	private MyImageButton share_btn;
	private MyImageButton like_btn;
	private MyImageButton email_btn;
	private boolean shoucang;//true表示已收藏，false表示未收藏
	private boolean like;//true表示点赞，false取消点赞
    private String  act_class;
    private String title;
    private Intent intent;
    private String theme;
    private int article_type1;
    private int article_type2;
    private String article_type = "";
    private int article_id;
    private String url;
    //function fm
    private MyApplication application;
    private FunctionManage fm;
    private ShoucangUtil shoucang_util;
    private WebSettings settings;
    private boolean enter_from_push;//表明是不是从push入口处进来
    /*一些状态信息*/
    private final int  SEND_MAIL_OK = 0;
    private final int  SEND_MAIL_FAIL = 1;
    private final int  LIKE_CONTENT_OK = 2;
    private final int  LIKE_CONTENT_FAIL = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = getLayoutInflater();  
		main=(LinearLayout)inflater.inflate(R.layout.common_content, null); 
		
		intent = this.getIntent();
		url = intent.getStringExtra("url");
		
		act_class = intent.getStringExtra("act_class");
		theme = intent.getStringExtra("theme");
//		article_type1 = intent.getIntExtra("articleType1", DefaultUtil.INAVAILABLE);
//		article_type2 = intent.getIntExtra("articleType2", DefaultUtil.INAVAILABLE);
		article_type = intent.getStringExtra("articleType");
		article_id = intent.getIntExtra("id", DefaultUtil.INAVAILABLE);
		enter_from_push = intent.getBooleanExtra("enterFromPush", false);
		application = MyApplication.getInstance();
		fm = new FunctionManage(this);
		setContentView(main);
		initView();
		setListener();

				

		setWebView();
		
		webView.loadUrl(url);
		
		//初始化未收藏
		shoucang_util = new ShoucangUtil(this);
		shoucang = false;
		//shoucang_btn.updateButtonState(shoucang);
//		Log.i("article_type", article_type);
//		Log.i("article_id", ""+article_id);
//        Log.i("url",url);
//        Log.i("theme",theme);
//        
	}
	
	private void initView()
	{
		
		webView=(WebView)main.findViewById(R.id.documentcontentwebview);
		
		bar = (ProgressBar)main.findViewById(R.id.documentcontentmyProgressBar);
		
		MyHeaderView header_view = (MyHeaderView) main.findViewById(R.id.item_header);
		
		
		
		if(act_class.equals("document"))
			header_view.SetHeaderText("文献速递");
		else if(act_class.equals("project"))
			header_view.SetHeaderText("项目申报");
		else if(act_class.equals("热点新闻"))
		{
			title=intent.getStringExtra("title");
			header_view.SetHeaderText(title);
		}
		else if(act_class.equals("关键词分析")){
			//title = intent.getStringExtra("title");
			header_view.SetHeaderText("关键词分析");
		}
		else if(act_class.equals("业界动态")){
			header_view.SetHeaderText("业界动态");
		}else if(act_class.equals("我的收藏")){
			header_view.SetHeaderText("我的收藏");
		}
		
		
		header_view.SetHeaderText(act_class);
		go_back_btn = (MyImageButton)findViewById(R.id.go_back);
		shoucang_btn = (MyImageButton)findViewById(R.id.shoucang);
		comment_btn = (MyImageButton)findViewById(R.id.common_comment_btn);
		shoucang_btn.setDrawable(getResources().getDrawable(R.drawable.icon_shoucang_a), getResources().getDrawable(R.drawable.icon_shoucang_b));
		share_btn = (MyImageButton)findViewById(R.id.share);
		like_btn = (MyImageButton) findViewById(R.id.common_like_btn);
		email_btn = (MyImageButton) findViewById(R.id.common_email_btn);

		LinearLayout bottom_layout = (LinearLayout) findViewById(R.id.common_bottom_layout);
		if(act_class.equals("关键词分析")){
			bottom_layout.setVisibility(View.INVISIBLE);
			shoucang_btn.setVisibility(View.INVISIBLE);
			share_btn.setVisibility(View.INVISIBLE);
		}
		
		//LayoutInflater inflater = getLayoutInflater();
	}
	
	private void setListener()
	{
		go_back_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonContentActivity.this.finish();
			}
			
		});
		
		shoucang_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				final MyApplication application = MyApplication.getInstance();
				if(!application.IsLogin())
				{
					fm.Login();
				}
				else
				{
				

				ShoucangUtil.OnShoucangListener listener = new ShoucangUtil.OnShoucangListener() {
					
					@Override
					public void onShoucang(int result) {
						// TODO Auto-generated method stub
						switch(result){
						case ShoucangUtil.RESULT_ADD_OK:
							shoucang = true;
							shoucang_btn.updateButtonState(true);
							//收藏至本地
							//shoucang_util.addToLocalShoucang(application.user_name,Integer.parseInt(article_type), article_id, theme, "四天前", url);
							//toastAtBottom("收藏成功");
							Toast.makeText(CommonContentActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
							break;
						case ShoucangUtil.RESULT_ADD_FAIL:
							//toastAtBottom("收藏失败");
							break;
						case ShoucangUtil.RESULT_DELETE_OK:
							shoucang = false;
							shoucang_btn.updateButtonState(false);
							//shoucang_util.dropFromLocalShoucang(application.user_name,Integer.parseInt(article_type), article_id);
							//toastAtBottom("取消收藏");
							break;
						case ShoucangUtil.RESULT_DELETE_FAIL:
							//toastAtBottom("无法取消收藏");
							break;
						}
					}
				};
				shoucang_util.setOnShoucangListener(listener);
				//shoucang_util.addShoucang(article_type, article_id, url, theme);
				if(!shoucang)
			    {
			    	
					shoucang_util.addShoucang(article_type, article_id, url, theme);
			    }
			    else
			    {
			    	shoucang_util.deleteShoucang(article_type, article_id);
			    	//shoucang = true;
			    }
			    
			   
			    	
			  }
			
			}
		});
		
		
		//点击喜欢按钮出来悬浮窗
		like_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!application.IsLogin())
				{
					fm.Login();
				}
				else
				{
				String url = Url.composeLikeContentUrl(article_type, article_id);
				ContentLikeThread thread = new ContentLikeThread(url);
				new Thread(thread).start();
				}
                

			}
			
		});
		
		
	    comment_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("theme",theme);
				intent.putExtra("articleType", article_type);
				//Log.i("article_type", "" + article_type1);
				intent.putExtra("article_id", article_id);
				intent.setClass(CommonContentActivity.this, CommentDetailActivity.class);
				startActivity(intent);
			}
	    	
	    });
	    
	    
	    
	    share_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//FunctionManage fm = new FunctionManage(CommonContentActivity.this);
				fm.showShare(theme,theme,url);
				//fm.share2PlatForm(theme, theme, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), url);
			}
	    	
	    });
	    
	    
	    
	    
	    email_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				
				
				if(!application.IsLogin())
				{
					fm.Login();
					
				}
				
				else{
				
				
				LinearLayout layout = new LinearLayout(CommonContentActivity.this);
				layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				layout.setOrientation(LinearLayout.VERTICAL);
				final EditText email_edit_box = new EditText(CommonContentActivity.this);
				email_edit_box.setText(application.user_name);
				
				
				final EditText title_edit_box = new EditText(CommonContentActivity.this);
				title_edit_box.setText(theme);
				
				
				final EditText content_edit_box = new EditText(CommonContentActivity.this);
				content_edit_box.setText("点击链接" + url + "   "+ "进入文章");
				
				layout.addView(email_edit_box);
				layout.addView(title_edit_box);
				layout.addView(content_edit_box);
				
				new AlertDialog.Builder(CommonContentActivity.this)
				.setTitle("发送文章到邮箱")
				.setIcon(R.drawable.icon_email)
				.setView(layout)
				.setPositiveButton("发送", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
				
						String str_url = Url.composeSendToEmailUrl(email_edit_box.getText().toString(), 
								title_edit_box.getText().toString(), content_edit_box.getText().toString());
						SendEmailThread thread = new SendEmailThread(str_url);
						new Thread(thread).start();
					}
				})
				.setNegativeButton("取消",null)
				.show();
			}
		}
			
			
	    });
	    
	}
	
	
	public boolean checkShoucang()
	{
		return shoucang_util.containInShoucang(article_type1, article_id);
	}
	
	
	
	
	private void setWebView()
	{
		//支持javascript
		webView.getSettings().setJavaScriptEnabled(true); 
		// 设置可以支持缩放 
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(false);
		//扩大比例的缩放
		//webView.getSettings().setUseWideViewPort(false);
		//自适应屏幕
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(false);
		
		
		//将图片调整到适合webview的大小
		webView.getSettings().setUseWideViewPort(false);
		webView.requestFocusFromTouch();
		webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		webView.setWebChromeClient(new WebChromeClient() {
	        @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	            if (newProgress == 100) {
	                bar.setVisibility(View.INVISIBLE);
	            } else {
	                if (View.INVISIBLE == bar.getVisibility()) {
	                    bar.setVisibility(View.VISIBLE);
	                }
	                bar.setProgress(newProgress);
	            }
	            super.onProgressChanged(view, newProgress);
	        }
	        
	    });
	}
	
	
	public void toastAtBottom(CharSequence info){
		
		View v = findViewById(R.id.common_bottom_layout);
		v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
				  View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		int height = v.getMeasuredHeight();
		ToastProxy toast = new ToastProxy(CommonContentActivity.this);
		toast.setGravity(Gravity.BOTTOM|Gravity.LEFT, 0, height);
		toast.setToastView(CommonContentActivity.this, info, ToastProxy.LENGTH_SHORT);
		toast.show();
	}
	
	
	
	
	/**
	 * 
	 * @author ming
	 *发送文章到邮箱的线程
	 */
	private class SendEmailThread implements Runnable{

		private String str_url;
		public SendEmailThread(String url){
			
			this.str_url = url;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			URL url;
			try {
				url = new URL(str_url);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				InputStreamReader reader = new InputStreamReader(is,"UTF-8");
				BufferedReader buf_reader = new BufferedReader(reader);
				StringBuffer sb = new StringBuffer();
				String str;
				while((str = buf_reader.readLine()) != null)
				{
					sb.append(str);
				}
				
				String str_temp = sb.toString();
				JSONObject obj = new JSONObject(str_temp);
				int a = str_temp.indexOf("{");
				str_temp = str_temp.substring(a);
				int code = Integer.parseInt(obj.getString("code"));
				if(code == 200)
					handler.sendEmptyMessage(SEND_MAIL_OK);
				else
					handler.sendEmptyMessage(SEND_MAIL_FAIL);
					
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
	};
	
	
	
	
	/**
	 * 
	 * @author ming
	 *点赞文章的线程
	 */
	private class ContentLikeThread implements Runnable{

		private String str_url;
		public ContentLikeThread(String url){
			
			this.str_url = url;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			URL url;
			try {
				url = new URL(str_url);
				URLConnection conn = url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				InputStreamReader reader = new InputStreamReader(is,"UTF-8");
				BufferedReader buf_reader = new BufferedReader(reader);
				StringBuffer sb = new StringBuffer();
				String str;
				while((str = buf_reader.readLine()) != null)
				{
					sb.append(str);
				}
				
				String str_temp = sb.toString();
				JSONObject obj = new JSONObject(str_temp);
				int a = str_temp.indexOf("{");
				str_temp = str_temp.substring(a);
				int code = Integer.parseInt(obj.getString("code"));

				if(code == 200)
					handler.sendEmptyMessage(LIKE_CONTENT_OK);
				else
					handler.sendEmptyMessage(LIKE_CONTENT_FAIL);
					
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
	};
	
	private Handler handler = new Handler(){
	   @Override
	   public void handleMessage(Message msg){
		   
		   switch(msg.what){
		   
		   case SEND_MAIL_OK:
			   Toast.makeText(CommonContentActivity.this, "成功发送至邮箱", Toast.LENGTH_SHORT).show();
			   break;
			   
		   case SEND_MAIL_FAIL:
			   Toast.makeText(CommonContentActivity.this, "发送失败，请检查邮箱是否正确或者网络连接", Toast.LENGTH_SHORT).show();
			   break;
		   case LIKE_CONTENT_OK:
			   Toast.makeText(CommonContentActivity.this, "喜欢 + 1", Toast.LENGTH_SHORT).show();
			   //toastAtBottom("喜欢    +  1");
			   break;
		   case LIKE_CONTENT_FAIL:
			   break;
		   }
	   }
	};
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
               if(enter_from_push && !MainActivity.hasLauncher){
            	   
            	   Intent intent = new Intent(this,MainActivity.class);
            	   startActivity(intent);
               }
			
		     this.finish();
		     return false;
		}
		else{
			return super.dispatchKeyEvent(event);
		}
	}
	
	
}
