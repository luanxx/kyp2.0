package com.science.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;


import cn.sharesdk.wechat.friends.Wechat;


import com.example.science.R;

import com.science.json.JsonLoginHandler;
import com.science.services.DataCache;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.util.RegisterUtil;
import com.science.util.Url;
import com.science.view.MyImageButton;

//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
//import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
//import com.umeng.socialize.exception.SocializeException;






import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Android_DialogActivity extends Activity {
	
		static final int MSG_SHOW_COMPLETE=4;
		static final int MSG_SHOW_ERROR=5;
		static final int MSG_SHOW_CANCEL=6;
		static final int SEND_MAIL_OK=7;
		static final int SEND_MAIL_FAIL=8;
	
		
		public ProgressDialog p_dialog;  
		public MyHandler myHandler=null;
		public String name=null;
		public String pass=null;
		public static String loginState=null;
		public static String sidString=null;
		public static String result=null;
		public static JsonLoginHandler jsonLoginHandler=new JsonLoginHandler(); 
		
		public EditText editTextUserName=null;
		public EditText editTextPassWord=null;
		private EditText editText;
		
		public TextView tvRegister=null;
		public TextView tvForgetPassWord=null;
		
		public ImageButton btnLogin=null;
		public ImageButton btnQQ=null;
		public ImageButton btnWeiBo=null;
		public ImageButton btnWeiXin=null;
		
		public MyApplication myApplication=null;
		public FunctionManage functionManage=null;
		//private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
	    
		
		
		@Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        
	        myHandler=new MyHandler();
	        
	        myApplication=(MyApplication)this.getApplication();
	        functionManage=new FunctionManage(Android_DialogActivity.this);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	        ShareSDK.initSDK(this);
	        setContentView(R.layout.logindialog);
	        
	        InitView();
	        SetOnClickeListener();
	        
	       // configPlatforms();
	      //  setShareContent();
	    }  
		
		
		/**
		 * 配置分享平台参数
		 */
//		private void configPlatforms() {
//			// 添加新浪sso授权
//			mController.getConfig().setSsoHandler(new SinaSsoHandler());
//			// 添加腾讯微博SSO授权
//			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//			// 添加人人网SSO授权
//			//RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(
//			//		LoginActivity.this, "201874",
//			//		"28401c0964f04a72a14c812d6132fcef",
//			//		"3bf66e42db1e4fa9829b955cc300b737");
//		//	mController.getConfig().setSsoHandler(renrenSsoHandler);
//
//			// 添加QQ、QZone平台
//			//addQQQZonePlatform();
//
//			// 添加微信、微信朋友圈平台
//		//	addWXPlatform();
//
//		}	
		
		
		
		/**
		 * 根据不同的平台设置不同的分享内容</br>
		 */
//		private void setShareContent() {
//
//			// 配置SSO
//			mController.getConfig().setSsoHandler(new SinaSsoHandler());
//			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//
//			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
//					this, "100424468",
//					"c7394704798a158208a74ab60104f0ba");
//			qZoneSsoHandler.addToSocialSDK();
//			mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能");
//
//			// APP ID：201874, API
//			// * KEY：28401c0964f04a72a14c812d6132fcef, Secret
//			// * Key：3bf66e42db1e4fa9829b955cc300b737.
//
//
//			UMImage localImage = new UMImage(this, R.drawable.ic_launcher);
//			UMImage urlImage = new UMImage(this,
//					"http://www.umeng.com/images/pic/social/integrated_3.png");
//			// UMImage resImage = new UMImage(LoginActivity.this, R.drawable.icon);
//
//			// 视频分享
//			UMVideo video = new UMVideo(
//					"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//			// vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//			video.setTitle("友盟社会化组件视频");
//			video.setThumb(urlImage);
//
//			UMusic uMusic = new UMusic(
//					"http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//			uMusic.setAuthor("umeng");
//			uMusic.setTitle("天籁之音");
//			uMusic.setThumb(urlImage);
//			// uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");
//
//			// 设置renren分享内容
//			RenrenShareContent renrenShareContent = new RenrenShareContent();
//			renrenShareContent.setShareContent("人人分享内容");
//			UMImage image = new UMImage(this,
//					BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//			image.setTitle("thumb title");
//			image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");
//			renrenShareContent.setShareImage(image);
//			renrenShareContent.setAppWebSite("http://www.umeng.com/social");
//			mController.setShareMedia(renrenShareContent);
//
//			UMImage qzoneImage = new UMImage(this,
//					"http://www.umeng.com/images/pic/social/integrated_3.png");
//			qzoneImage
//					.setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");
//
//			// 设置QQ空间分享内容
//			QZoneShareContent qzone = new QZoneShareContent();
//			qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
//			qzone.setTargetUrl("http://www.umeng.com/social");
//			qzone.setTitle("QZone title");
//			qzone.setShareImage(urlImage);
//			mController.setShareMedia(qzone);
//
//			video.setThumb(new UMImage(this, BitmapFactory
//					.decodeResource(getResources(), R.drawable.ic_launcher)));
//
//			QQShareContent qqShareContent = new QQShareContent();
//			qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
//			qqShareContent.setTitle("hello, title");
//			qqShareContent.setShareImage(urlImage);
//			// qqShareContent.setShareMusic(uMusic);
//			// qqShareContent.setShareVideo(video);
//			qqShareContent.setTargetUrl("http://www.umeng.com/social");
//			mController.setShareMedia(qqShareContent);
//
//			// 视频分享
//			UMVideo umVideo = new UMVideo(
//					"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//			umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//			umVideo.setTitle("友盟社会化组件视频");
//
//			TencentWbShareContent tencent = new TencentWbShareContent();
//			tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
//			// 设置tencent分享内容
//			mController.setShareMedia(tencent);
//
//
//
//
//
//			SinaShareContent sinaContent = new SinaShareContent(urlImage);
//			sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，新浪微博");
//			mController.setShareMedia(sinaContent);
//
//			TwitterShareContent twitterShareContent = new TwitterShareContent();
//			twitterShareContent
//					.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，TWITTER");
//			twitterShareContent.setShareMedia(localImage);
//			mController.setShareMedia(twitterShareContent);
//
//			GooglePlusShareContent googlePlusShareContent = new GooglePlusShareContent();
//			googlePlusShareContent
//					.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，G+");
//			googlePlusShareContent.setShareMedia(localImage);
//			mController.setShareMedia(googlePlusShareContent);
//
//
//
//
//
//		}
		
		
		
		
		
		
		
//		private void addQQQZonePlatform() {
//			String appId = "100424468";
//			String appKey = "c7394704798a158208a74ab60104f0ba";
//			// 添加QQ支持, 并且设置QQ分享内容的target url
//			UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
//					appId, appKey);
//			qqSsoHandler.setTargetUrl("http://www.umeng.com");
//			qqSsoHandler.addToSocialSDK();
//
//			// 添加QZone平台
////			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
////					this, appId, appKey);
////			qZoneSsoHandler.addToSocialSDK();
//		}
//		
//		private void login(final SHARE_MEDIA platform) {
//			mController.doOauthVerify(this, platform,
//					new UMAuthListener() {
//
//						@Override
//						public void onStart(SHARE_MEDIA platform) {
//							Toast.makeText(Android_DialogActivity.this, "授权开始",
//									Toast.LENGTH_SHORT).show();
//						}
//
//						@Override
//						public void onError(SocializeException e,
//								SHARE_MEDIA platform) {
//							Toast.makeText(Android_DialogActivity.this, "授权失败",
//									Toast.LENGTH_SHORT).show();
//						}
//
//						@Override
//						public void onComplete(Bundle value, SHARE_MEDIA platform) {
//							// 获取uid
//							String uid = value.getString("uid");
//							if (!TextUtils.isEmpty(uid)) {
//								// uid不为空，获取用户信息
//								getUserInfo(platform);
//							} else {
//								Toast.makeText(Android_DialogActivity.this, "授权失败...",
//										Toast.LENGTH_LONG).show();
//							}
//						}
//
//						@Override
//						public void onCancel(SHARE_MEDIA platform) {
//							Toast.makeText(Android_DialogActivity.this, "授权取消",
//									Toast.LENGTH_SHORT).show();
//						}
//					});
//		}
//
//		
//		
//		/**
//		 * 获取用户信息
//		 * 
//		 * @param platform
//		 */
//		private void getUserInfo(SHARE_MEDIA platform) {
//			mController.getPlatformInfo(this, platform,
//					new UMDataListener() {
//
//						@Override
//						public void onStart() {
//
//						}
//
//						@Override
//						public void onComplete(int status, Map<String, Object> info) {
//							// String showText = "";
//							// if (status == StatusCode.ST_CODE_SUCCESSED) {
//							// showText = "用户名：" +
//							// info.get("screen_name").toString();
//							// Log.d("#########", "##########" + info.toString());
//							// } else {
//							// showText = "获取用户信息失败";
//							// }
//
//							if (info != null) {
//								Toast.makeText(Android_DialogActivity.this, info.toString(),
//										Toast.LENGTH_SHORT).show();
//							}
//						}
//					});
//		}		
//		
//		
		
		
		
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			ShareSDK.stopSDK(this);
			super.onStop();
		}
	    public void InitView()
	    {
	          editTextUserName=(EditText)findViewById(R.id.AccountEditText);
	          editTextPassWord=(EditText)findViewById(R.id.PasswordEidtText);
	          
	          editTextUserName.setText(MyApplication.user_name);
	          editTextPassWord.setText(MyApplication.password);
	          
	          tvRegister=(TextView)findViewById(R.id.logindialogregister);
	          tvForgetPassWord=(TextView)findViewById(R.id.logindialogfogetpassword);
	          
	          btnLogin=(ImageButton)findViewById(R.id.logindialoglogin);
	          btnQQ=(ImageButton)findViewById(R.id.logindialogqqlogin);
	          btnWeiBo=(ImageButton)findViewById(R.id.logindialogweibologin);
	          btnWeiXin=(ImageButton)findViewById(R.id.logindialogweixinlogin);
	    }
	    
	    public void SetOnClickeListener()
	    {
	    	tvForgetPassWord.setOnClickListener(onClickListener);
	    	tvRegister.setOnClickListener(onClickListener);
	    	btnLogin.setOnClickListener(onClickListener);
	    	btnQQ.setOnClickListener(onClickListener);
	    	btnWeiBo.setOnClickListener(onClickListener);
	    	btnWeiXin.setOnClickListener(onClickListener);
	    }
	    
	    private PlatformActionListener platformActionListener=new PlatformActionListener() {
			
	    	//授权失败
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				System.out.println("授权失败");
				Message msg=new Message();
				msg.what=MSG_SHOW_ERROR;
				myHandler.sendMessage(msg);
			}

			//授权成功
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				arg0.removeAccount();
				System.out.println("授权成功");
				Message msg=new Message();
				msg.what=MSG_SHOW_COMPLETE;
				
				String id=arg2.get("id").toString();
				String name=arg2.get("name").toString();
	  			String description=arg2.get("description").toString();
	  			String profile_image_url=arg2.get("profile_image_url").toString();
	  			String str="ID: "+id+";\n"+
	  						"用户名： "+name+";\n"+
	  						"描述："+description+";\n"+
	  						"用户头像地址："+profile_image_url;
	  			Toast.makeText(Android_DialogActivity.this, "用户名" + name, Toast.LENGTH_SHORT).show();
	  			System.out.println("用户资料: "+str);
	  			Bundle data=new Bundle();//----map
				data.putString("information", str);
				msg.setData(data);
				//发送消息
				myHandler.sendMessage(msg);
			}

			//授权取消
			public void onCancel(Platform arg0, int arg1) {
				System.out.println("取消授权");
				Message msg=new Message();
				msg.what=MSG_SHOW_CANCEL;
				myHandler.sendMessage(msg);
			}
		};
	    
	    private View.OnClickListener onClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.logindialogregister:
					//ShareContent();
					//functionManage.showShare();
					//Intent intent=new Intent(Android_DialogActivity.this, RegisterActivity.class);
					//startActivity(intent);
					
					AlertDialog.Builder builder;
					builder = new AlertDialog.Builder(Android_DialogActivity.this);
					LayoutInflater inflater = getLayoutInflater().from(Android_DialogActivity.this);
					final View layout = inflater.inflate(R.layout.register_dialog, null);
					builder.setView(layout);
					final AlertDialog dialog = builder.create();
					dialog.show();
					
					MyImageButton cancel_btn = (MyImageButton) layout.findViewById(R.id.register_cacel);
					cancel_btn.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
						
					});
					
					
					MyImageButton finish_register_btn = (MyImageButton) layout.findViewById(R.id.finish_register_btn);
					
	
					finish_register_btn.setOnClickListener(new OnClickListener(){

						RegisterUtil reg = RegisterUtil.getInstance();
						
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String account = ((EditText)layout.findViewById(R.id.account_input_box)).getText().toString();
							String password1 = ((EditText)layout.findViewById(R.id.password_input_box)).getText().toString();
							String password2 = ((EditText)layout.findViewById(R.id.ensure_password_input_box)).getText().toString();
							String email = ((EditText)layout.findViewById(R.id.email_input_box)).getText().toString();
							
							
//							Log.i("testtttttttttt","email:" + email + "account:" + account + "password1:" + password1 + "password2" + password2);
							
							RegisterUtil.OnRegisterListener listener = new RegisterUtil.OnRegisterListener(){

								@Override
								public void onRegister(int result) {
									// TODO Auto-generated method stub
									if(result == RegisterUtil.RESULT_OK)
									{
										dialog.dismiss();
									}
								}
								
							};
							reg.register(Android_DialogActivity.this,account,password1,password2,email,listener);
						    
							
						}
						
					});
					break;
				case R.id.logindialogfogetpassword:
					editText=new EditText(Android_DialogActivity.this);
					AlertDialog builder_forgetpassword =new AlertDialog.Builder(Android_DialogActivity.this).setTitle("找回密码").setView(editText).setMessage("若忘记密码，请填写您的注册邮箱，点击确认后前往注册邮箱，查收找回密码的邮件并修改密码。").setPositiveButton("确定", listener).setNegativeButton("取消", null).show(); 
					
					
					break;
				case R.id.logindialoglogin:
					MyThread myThread=new MyThread();
					new Thread(myThread).start();
                  p_dialog = ProgressDialog  
                  .show(Android_DialogActivity.this,  
                          "请等待",  
                          "正在为您登录...",  
                          true);
					break;
				case R.id.logindialogqqlogin:
					
					Toast.makeText(Android_DialogActivity.this,"很抱歉，暂不支持第三方登陆",Toast.LENGTH_SHORT).show();
					
					//login(SHARE_MEDIA.QQ);
					//ThirdPartyLogin tpl = new ThirdPartyLogin();
					
					//tpl.show(Android_DialogActivity.this);
					
					
					
					
					//TestThirdPartDemoActivity tpl = new TestThirdPartDemoActivity();
					//tpl.show(Android_DialogActivity.this);
					//Platform qq=ShareSDK.getPlatform(QQ.NAME);
					//authorize(qq);
					//禁用SSO授权（禁用客户端授权）
					//qq.SSOSetting(true);
					//qq.setPlatformActionListener(platformActionListener);
					//移除授权，以达到每次都可以打开授权登录页面
					//qq.removeAccount();
					//登录
					//qq.showUser(null);
					break;
				case R.id.logindialogweibologin:
					Toast.makeText(Android_DialogActivity.this,"很抱歉，暂不支持第三方登陆",Toast.LENGTH_SHORT).show();
					//login(SHARE_MEDIA.SINA);
//					Platform weibo=ShareSDK.getPlatform(SinaWeibo.NAME);
//					//禁用SSO授权（禁用客户端授权）
//					weibo.SSOSetting(true);
//					weibo.setPlatformActionListener(platformActionListener);
//					//移除授权，以达到每次都可以打开授权登录页面
//					weibo.removeAccount();
//					//登录
//					weibo.showUser(null);
					break;
				case R.id.logindialogweixinlogin:
					Toast.makeText(Android_DialogActivity.this,"很抱歉，暂不支持第三方登陆",Toast.LENGTH_SHORT).show();
					//Platform weixin=ShareSDK.getPlatform(Wechat.NAME);
					//禁用SSO授权（禁用客户端授权）
//					weixin.SSOSetting(true);
//					weixin.setPlatformActionListener(platformActionListener);
//					//移除授权，以达到每次都可以打开授权登录页面
//					weixin.removeAccount();
//					//登录
//					weixin.showUser(null);
					//authorize(weixin);
					//req.appid = "";
					break;
				default:
					break;
				}
			}
		};
		//找回密码的listener
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  

	    {  

	        public void onClick(DialogInterface dialog, int which)  

	        {  

	            switch (which)  

	            {  

	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  

	            	String email_edit_box= editText.getText().toString();
					email_edit_box = email_edit_box.replaceAll("@","$");
					String content ="";
					String str_url = Url.composeFindPasswordUrl(editText.getText().toString(), 
							content);
					FindPasswordThread thread = new FindPasswordThread(str_url);
					new Thread(thread).start();
	                break;  

	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  

	                break;  

	            default:  

	                break;  

	            }  

	        }  

	    };    
	    
	    private class MyHandler extends Handler
	    {

			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				super.dispatchMessage(msg);
			}


			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what==1) {
					Intent data=new Intent();  
		            data.putExtra("result", "success");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(200, data);
		            //保存用户名和密码
		            if(name!=null&&pass!=null)
		            {
		            	//functionManage.SaveLoginInfo(name,pass);	
		            	
		            	//Toast.makeText(Android_DialogActivity.this, "正在下载用户信息", Toast.LENGTH_SHORT).show();
		            	
		                  p_dialog = ProgressDialog  
		                          .show(Android_DialogActivity.this,  
		                                  "请等待",  
		                                  "正在加载用户信息...",  
		                                  true);
		            	
		            	MyApplication.getInstance().initUserInfoFromRemote(this,9);
		            	//MyApplication.getInstance().changeUserInfo();
		            }
		            functionManage.UpdataTags();
		            //关闭掉这个Activity
		            //Toast.makeText(Android_DialogActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
		           // MyApplication.changeUserInfo();
		            
				}
				else if (msg.what==2) {
					Intent data=new Intent();  
		            data.putExtra("result", "faile");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(400, data);  
		            //关闭掉这个Activity  
		            //finish();
		            Toast.makeText(Android_DialogActivity.this, "登录失败，请检查网络连接以及用户名密码是否正确", Toast.LENGTH_LONG).show();
				    p_dialog.dismiss();
				}
				else if(msg.what== MSG_SHOW_ERROR) {
				Toast.makeText(Android_DialogActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
				}
			
				else if(msg.what==MSG_SHOW_COMPLETE){
					Intent data=new Intent();  
		            data.putExtra("result", "success");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(200, data);  
		            //关闭掉这个Activity
		            Toast.makeText(Android_DialogActivity.this, "success", Toast.LENGTH_LONG).show();
		              
					Bundle userData=msg.getData();
					String informa=userData.getString("information");
					finish();
				}
				else if(msg.what== MSG_SHOW_CANCEL){
				Toast.makeText(Android_DialogActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
				}
				else if(msg.what== 7){
				   Toast.makeText(Android_DialogActivity.this, "成功发送至注册邮箱", Toast.LENGTH_SHORT).show();
				}
				else if(msg.what== 8){
					
				   Toast.makeText(Android_DialogActivity.this, "发送失败，请检查注册邮箱是否填写正确或者网络是否已连接", Toast.LENGTH_SHORT).show();
				}
				else if(msg.what == 9){
					finish();  
				}
			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				// TODO Auto-generated method stub
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
	    	
	    }


		/**
		 * 
		 * @author ming
		 *密码找回的线程
		 */
		private class FindPasswordThread implements Runnable{

			private String str_url;
			public  FindPasswordThread(String url){
				
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
					Log.i("status send email", str_temp);
					if(code == 200)
						myHandler.sendEmptyMessage(7);
					else
						myHandler.sendEmptyMessage(8);
						
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
	    
	    
	    public class MyThread implements Runnable
	    {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try { 
					//Log.v("Login", "start");
					name=editTextUserName.getText().toString();
					pass=editTextPassWord.getText().toString();
                    String result=myApplication.Login(name,pass);
//                    Log.v("Login", result);
//                    Log.v("Login", "faile");
                    if (result==null) {
						myHandler.sendEmptyMessage(2);
					}
                    else{
						myHandler.sendEmptyMessage(1);
					}
                } catch (Exception e) {  
                    e.printStackTrace();  
                } finally {  
                	if(p_dialog!=null)
                	{
                		p_dialog.dismiss();
                	}
                    //  
                }  
            }  
			
	    	
	    }

	    public  String Login()
		{
			if (name==null||pass==null) {
				return null;
			}
			else {
				String strUrlString=Url.LOGINURL;
				strUrlString+="name=";
				strUrlString+=name;
				strUrlString+="&";
				strUrlString+="pass=";
				strUrlString+=pass;

				try {
					URL url = new URL(strUrlString);
//					Log.i("NewsListUrl", DataUrlKeys.NEWS_LIST_URL + pass);
					URLConnection con;
					con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					sidString=jsonLoginHandler.getListItems(input);
					myApplication.sidString=sidString;
					return sidString;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					return sidString;
				}

			}
		}
	    
	    public void ShareContent()
	    {
	    	ShareSDK.initSDK(this);
	    	 OnekeyShare oks = new OnekeyShare();
	    	 //关闭sso授权
	    	 oks.disableSSOWhenAuthorize(); 
	    	 
	    	// 分享时Notification的图标和文字
	    	 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
	    	 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	    	 oks.setTitle(getString(R.string.share));
	    	 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	    	 oks.setTitleUrl("http://sharesdk.cn");
	    	 // text是分享文本，所有平台都需要这个字段
	    	 oks.setText("我是分享文本");
	    	 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	    	 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
	    	 // url仅在微信（包括好友和朋友圈）中使用
	    	 oks.setUrl("http://sharesdk.cn");
	    	 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	    	 oks.setComment("我是测试评论文本");
	    	 // site是分享此内容的网站名称，仅在QQ空间使用
	    	 oks.setSite(getString(R.string.app_name));
	    	 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	    	 oks.setSiteUrl("http://sharesdk.cn");
	    	 
	    	// 启动分享GUI
	    	 oks.show(this);
	    }
	    
	    
	    
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if(p_dialog != null)
					p_dialog.dismiss();
				finish();
//	         if(p_dialog != null){
//	        	 p_dialog.cancel();
//    		        p_dialog.dismiss();
//    		     Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();   
//    		}
//    		if(p_dialog == null){
//    			finish();
//    		}
			return false;
			} else {
				return super.dispatchKeyEvent(event);
			}
		}   
	    
		
		
		
		
		private void authorize(Platform plat){
			if(plat == null){
				return;
			}
			
			plat.setPlatformActionListener(platformActionListener);
			plat.SSOSetting(true);
			plat.showUser(null);
		}
		
		
		
		public class Constants {
		    
		    public static final String DESCRIPTOR = "com.umeng.share";
			
			private static final String TIPS = "请移步官方网站 ";
			private static final String END_TIPS = ", 查看相关说明.";
			public static final String TENCENT_OPEN_URL = TIPS + "http://wiki.connect.qq.com/android_sdk使用说明" 
															+ END_TIPS;
			public static final String PERMISSION_URL = TIPS + "http://wiki.connect.qq.com/openapi权限申请" 
															+ END_TIPS;
			
			public static final String SOCIAL_LINK = "http://www.umeng.com/social";
			public static final String SOCIAL_TITLE = "友盟社会化组件帮助应用快速整合分享功能";
			public static final String SOCIAL_IMAGE = "http://www.umeng.com/images/pic/banner_module_social.png";
			
			public static final String SOCIAL_CONTENT = "友盟社会化组件（SDK）让移动应用快速整合社交分享功能，我们简化了社交平台的接入，为开发者提供坚实的基础服务：（一）支持各大主流社交平台，" + 
														"（二）支持图片、文字、gif动图、音频、视频；@好友，关注官方微博等功能" +
														"（三）提供详尽的后台用户社交行为分析。http://www.umeng.com/social";
			
		}
	    
//	    @Override
//	    public boolean onKeyDown(int keyCode, KeyEvent event){
//	    	
//	    	if(keyCode == KeyEvent.KEYCODE_BACK){
//	    		if(p_dialog != null){
//	    			if(p_dialog.isShowing())
//	    		        p_dialog.dismiss();
//	    			else
//	    				finish();
//	    		}
//	    		if(p_dialog == null){
//	    			finish();
//	    		}
//	    		return true;
//	    	}
//	    	
//	    	return super.onKeyDown(keyCode, event);
//	    }
}