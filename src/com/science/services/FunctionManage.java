package com.science.services;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.login.LoginException;


import cn.sharesdk.douban.Douban;
import cn.sharesdk.dropbox.Dropbox;
import cn.sharesdk.evernote.Evernote;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.facebookmessenger.FacebookMessenger;
import cn.sharesdk.flickr.Flickr;
import cn.sharesdk.foursquare.FourSquare;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.instagram.Instagram;
import cn.sharesdk.instapaper.Instapaper;
import cn.sharesdk.kaixin.KaiXin;
import cn.sharesdk.kakao.story.KakaoStory;
import cn.sharesdk.kakao.talk.KakaoTalk;
import cn.sharesdk.line.Line;
import cn.sharesdk.linkedin.LinkedIn;
import cn.sharesdk.mingdao.Mingdao;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.pinterest.Pinterest;
import cn.sharesdk.pocket.Pocket;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sohu.suishenkan.SohuSuishenkan;
import cn.sharesdk.system.bluetooth.Bluetooth;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.tumblr.Tumblr;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.vkontakte.VKontakte;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.whatsapp.WhatsApp;
import cn.sharesdk.yixin.friends.Yixin;
import cn.sharesdk.yixin.moments.YixinMoments;
import cn.sharesdk.youdao.YouDao;

import com.example.science.R;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.science.json.JsonCommonResponseStateHandler;
import com.science.json.JsonDownLoadsKeywords;
import com.science.json.JsonUpdateGeTuiTagsHandler;
import com.science.util.FileAccess;
import com.science.util.Url;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class FunctionManage {
	
	public MyApplication myApplication;
	public Context context;
	public Map<String,String> keywordsMap=null;
	public JsonDownLoadsKeywords jsonDownLoadsKeywords;
	public JsonUpdateGeTuiTagsHandler jsonUpdateGeTuiTagsHandler=null;
	public String name=null;
	public String pass=null;
	public OnekeyShare oks=null;
    public ShareParams share = null;

	public FunctionManage(Context con)
	{
		myApplication=MyApplication.getInstance();
		context=con;
		jsonDownLoadsKeywords=new JsonDownLoadsKeywords();
		jsonUpdateGeTuiTagsHandler=new JsonUpdateGeTuiTagsHandler();
		ShareSDK.initSDK(context);
		oks=new OnekeyShare();
	}
	
	public  void Login()
	{
		//GetLoginInfo();
		//if (name==null||pass==null) {
		if(MyApplication.sidString == null)
			MyApplication.sidString = "";
		if(MyApplication.sidString.isEmpty()){
			Intent intent=new Intent();
			intent.setAction("com.science.intent.action.LOGIN");
			context.startActivity(intent);
		}
		else {
			MyThreadLogin myThreadLogin=new MyThreadLogin();
			new Thread(myThreadLogin).start();
		}		
		//UpdateCid();
	}
	
	
	
	public int Logout(){
		
		
		class LogoutThread extends Thread{
			
			int code = 0;
			@Override
			public void run(){
				String str_url = Url.composeLogoutUrl();
				URL url;
				
				try {
					url = new URL(str_url);
					URLConnection conn = url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					JsonCommonResponseStateHandler json = new JsonCommonResponseStateHandler();
				    code = json.getResponseStateCode(is);
				
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			public int getCode(){
				return this.code;
			}
			
		}
		
		
		LogoutThread thread = new LogoutThread();
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		
		return thread.getCode();
	}
	
	
	
	
	
	
	
	
	public void showShare(String title,String content,String url) {

		 //关闭sso授权
		 try {
			FileAccess.saveBitmapToFile(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher), "/sdcard/science_pie/ic_launcher.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 oks.disableSSOWhenAuthorize(); 
		 
		// 分享时Notification的图标和文字
		 oks.setNotification(R.drawable.ic_launcher, context.getResources().getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(title);
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(url);
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText(content);
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImagePath("/sdcard/science_pie/ic_launcher.png");//确保SDcard下面存在此张图片
		 //oks.setImagePath(imagePath)
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl(url);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 //oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 //oks.setImageUrl("http://i3.sinaimg.cn/travel/2015/0413/U10580P704DT20150413102025.jpg");
		 oks.setSite(context.getString(R.string.app_name));
		 oks.setSiteUrl(url);
         
		// 启动分享GUI
		 oks.show(context);
		 hideUselessPlatform();

}

	
	
	
	public void share2PlatForm(String title,String content,Bitmap bitmap,String url){
		
		// oks.disableSSOWhenAuthorize(); 
		// oks.setPlatform(Wechat.NAME);
		// oks.show(context);
		// hideUselessPlatform();
		 share = new ShareParams();
		 share.setTitle(title);
		 share.setText(content);
		 share.setImageData(bitmap);
		 share.setSiteUrl(url);
		 //setWechatPlatform();
		// setQZonePlatform();
		 setWeiboPlatform();

		 
	}
	
	

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	PlatformActionListener platformListener = new PlatformActionListener(){

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onComplete(Platform platform, int action,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = MSG_ACTION_CCALLBACK;
			msg.arg1 = 1;
			msg.arg2 = action;
			msg.obj = platform;
			UIHandler.sendMessage(msg, callback);
		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			// TODO Auto-generated method stub
			t.printStackTrace();
			//错误监听,handle the error msg
			Message msg = new Message();
			msg.what = MSG_ACTION_CCALLBACK;
			msg.arg1 = 2;
			msg.arg2 = action;
			msg.obj = t;
			UIHandler.sendMessage(msg,callback);
		}
		
	};
	
	
	
	
	
	
	Callback callback = new Callback(){

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what) {
			case MSG_TOAST: {
				String text = String.valueOf(msg.obj);
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
			break;
			case MSG_ACTION_CCALLBACK: {
				switch (msg.arg1) {
					case 1: { // 成功提示, successful notification
						//showNotification(2000,getString(R.string.share_completed));
					}
					break;
					case 2: { // 失败提示, fail notification
						String expName = msg.obj.getClass().getSimpleName();
						if ("WechatClientNotExistException".equals(expName)
								|| "WechatTimelineNotSupportedException".equals(expName)) {
							showNotification(2000, context.getString(R.string.wechat_client_inavailable));
						}
						else if ("GooglePlusClientNotExistException".equals(expName)) {
							showNotification(2000, context.getString(R.string.google_plus_client_inavailable));
						}
						else if ("QQClientNotExistException".equals(expName)) {
							showNotification(2000, context.getString(R.string.qq_client_inavailable));
						}
						else {
							showNotification(2000, context.getString(R.string.share_failed));
						}
					}
					break;
					case 3: { // 取消提示, cancel notification
						showNotification(2000, context.getString(R.string.share_canceled));
					}
					break;
				}
			}
			break;
			case MSG_CANCEL_NOTIFY: {
				NotificationManager nm = (NotificationManager) msg.obj;
				if (nm != null) {
					nm.cancel(msg.arg1);
				}
			}
			break;
		}
			return false;
		}
		
	};
	
	
	
	
	
	
	private void setWechatPlatform(){

       Wechat.ShareParams sp = new Wechat.ShareParams();
       sp.shareType = Platform.SHARE_WEBPAGE;
       sp.title = "ShareSDK wechat share title";
       //sp.imageData = share.getImageData();
       sp.imageUrl = "http://i3.sinaimg.cn/travel/2015/0413/U10580P704DT20150413102025.jpg";
       sp.text = "ShareSDK wechat share title";
       sp.url = "http://www.sina.com.cn/";
       Platform wechat = ShareSDK.getPlatform(context,Wechat.NAME);
       wechat.setPlatformActionListener(platformListener);
       ShareParams ss = sp;
       wechat.share(ss);
	}
	
	
	private void setQZonePlatform(){
	   QZone.ShareParams sp = new QZone.ShareParams();
	   sp.title = "ShareSDK wechat share title";
	       //sp.imageData = share.getImageData();
	   sp.imageUrl = "http://i3.sinaimg.cn/travel/2015/0413/U10580P704DT20150413102025.jpg";
	   sp.text = "ShareSDK wechat share title";
	   sp.siteUrl = "http://www.sina.com.cn/";
	   Platform qzone = ShareSDK.getPlatform(context,QZone.NAME);
	   qzone.setPlatformActionListener(platformListener);
	   ShareParams ss = sp;
	   qzone.share(ss);
	}
	
	
	private void setWeiboPlatform(){
		SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		sp.setText("ShareSDK wechat share title");
		sp.setImageUrl("http://i3.sinaimg.cn/travel/2015/0413/U10580P704DT20150413102025.jpg");
		sp.setTitle("ShareSDK wechat share title");
		sp.setSiteUrl("http://www.sina.com.cn/");
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.share(sp);
	}
	
	private void  hideUselessPlatform(){
	    
		
		oks.addHiddenPlatform(TencentWeibo.NAME);
		oks.addHiddenPlatform(WechatFavorite.NAME);
		oks.addHiddenPlatform(Facebook.NAME);
		oks.addHiddenPlatform(Twitter.NAME);
		oks.addHiddenPlatform(KaiXin.NAME);
		oks.addHiddenPlatform(Email.NAME);
		oks.addHiddenPlatform(ShortMessage.NAME);
		oks.addHiddenPlatform(Douban.NAME);
		oks.addHiddenPlatform(YouDao.NAME);
		oks.addHiddenPlatform(Evernote.NAME);
		oks.addHiddenPlatform(LinkedIn.NAME);
		oks.addHiddenPlatform(GooglePlus.NAME);
		oks.addHiddenPlatform(SohuSuishenkan.NAME);
		oks.addHiddenPlatform(Flickr.NAME);
		oks.addHiddenPlatform(Pinterest.NAME);
		oks.addHiddenPlatform(Tumblr.NAME);
		oks.addHiddenPlatform(Dropbox.NAME);
		oks.addHiddenPlatform(VKontakte.NAME);
		oks.addHiddenPlatform(Yixin.NAME);
		oks.addHiddenPlatform(YixinMoments.NAME);
		oks.addHiddenPlatform(Mingdao.NAME);
		oks.addHiddenPlatform(Line.NAME);
		oks.addHiddenPlatform(KakaoTalk.NAME);
		oks.addHiddenPlatform(KakaoStory.NAME);
		oks.addHiddenPlatform(FacebookMessenger.NAME);
		oks.addHiddenPlatform(WhatsApp.NAME);
		oks.addHiddenPlatform(Pocket.NAME);
		oks.addHiddenPlatform(FourSquare.NAME);
		oks.addHiddenPlatform(Instagram.NAME);
		oks.addHiddenPlatform(Instapaper.NAME);
		oks.addHiddenPlatform(Bluetooth.NAME);
		oks.addHiddenPlatform(Dropbox.NAME);
		
	}
	
	
	public void SaveLoginInfo(String userName,String userPass)
	{
		//实例化SharedPreferences对象（第一步） 
		SharedPreferences mySharedPreferences= myApplication.getSharedPreferences("LoginInfo", Activity.MODE_PRIVATE); 
		//实例化SharedPreferences.Editor对象（第二步） 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//用putString的方法保存数据 
		editor.putString("name", userName); 
		editor.putString("pass", userPass); 
		//editor.putString("", value)
		//提交当前数据 
		editor.commit(); 
	}
	
	public void GetLoginInfo()
	{
		//实例化SharedPreferences对象（第一步） 
		SharedPreferences mySharedPreferences= myApplication.getSharedPreferences("LoginInfo", Activity.MODE_PRIVATE); 
	
		name=mySharedPreferences.getString("name",null);
		pass=mySharedPreferences.getString("pass", null);
	}
	
	public  void DownLoadKeywords()
	{
		MyThreadUpdateKeywords myThreadUpdateKeywords=new MyThreadUpdateKeywords();
		new Thread(myThreadUpdateKeywords).start();
	}
	
	public void UpdateCid()
	{
		MyThreadUpdateCid myThreadUpdateCid=new MyThreadUpdateCid();
		new Thread(myThreadUpdateCid).start();
		
	}
	
	public void UpdataTags()
	{
		MyThreadUpdateTags myThreadUpdateTags=new MyThreadUpdateTags();
		new Thread(myThreadUpdateTags).start();
	}
	
	private class MyThreadUpdateKeywords implements Runnable
    {
		@Override
		public void run() {
			// TODO Auto-generated method stub
						URL url;
						try {
				    		String temp="";
				    		for(int i=0;i<8;i++)
				    		{
				    			temp+=myApplication.keywords[i];
				    			temp+=" ";
				    		}
				    		temp=URLEncoder.encode(temp, "utf8");
							url = new URL(myApplication.ComposeToken(Url.UpdateKeywords+temp));
							URLConnection con = url.openConnection();
							con.connect();
							InputStream input = con.getInputStream();
							keywordsMap=jsonDownLoadsKeywords.getListItems(input);
							if (keywordsMap!=null) {
//								handler.sendEmptyMessage(2);
								//更新Application关键词数组
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		}
    	
    }

	private class MyThreadLogin implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			 myApplication.Login(name, pass);
			 
			 UpdataTags();
		}
		
	}
	
	
	
	
	private class MyThreadLogout implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	
	

	public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

		public void onShare(Platform platform, ShareParams paramsToShare) {

//			String text = platform.getContext().getString(R.string.share_title);
//			if ("WechatMoments".equals(platform.getName())) {
//				// 改写twitter分享内容中的text字段，否则会超长，
//				// 因为twitter会将图片地址当作文本的一部分去计算长度
//				text += platform.getContext().getString(R.string.share_to_wechatmoment);
//				paramsToShare.setText(text);
//			}else if("SinaWeibo".equals(platform.getName())){
//				text += platform.getContext().getString(R.string.share_to_sina);
//				paramsToShare.setText(text);			
//			}else if("TencentWeibo".equals(platform.getName())){
//				text += platform.getContext().getString(R.string.share_to_tencent);
//				paramsToShare.setText(text);			
//			}else if("ShortMessage".equals(platform.getName())){
//				text += platform.getContext().getString(R.string.share_to_sms);
//				paramsToShare.setText(text);
				
			}
		}
	
	private class MyThreadUpdateCid implements Runnable
	{

		@Override
		public void run() {
			
			try {
				URL url;
				String cidString=PushManager.getInstance().getClientid(context);
				url = new URL(myApplication.ComposeToken(Url.updateCid+PushManager.getInstance().getClientid(context)));
				//Log.i("cid",PushManager.getInstance().getClientid(context));
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				
				//UpdataTags();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class MyThreadUpdateTags implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url;
				url = new URL(myApplication.ComposeToken(Url.updateTags));
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				Map<String, String> map=jsonUpdateGeTuiTagsHandler.getListItems(input);
				if(map!=null)
				{
					String tag=map.get("pushtags");
					String [] tags=tag.split(",");
					Tag [] Tags=new Tag[tags.length];
					for (int i = 0; i < tags.length; i++) {
						Tag tempTag=new Tag();
						tempTag.setName(tags[i]);
						Tags[i]=tempTag;
					}
					
					Integer errorcode=PushManager.getInstance().setTag(context, Tags);
					Log.v("FunctionManage", "bbb"+errorcode.toString());
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = context.getApplicationContext();
			NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.ic_launcher, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, "sharesdk test", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, callback);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
