package com.science.util;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import com.science.http.HttpUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	public HashMap<String, SoftReference<Drawable>> imageCache;
	public String defaultFolder;
	public int reqImgWidth;
	public int reqImgHeight;

	public AsyncImageLoader(int reqImgWidth, int reqImgHeight) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		defaultFolder = DataUrlKeys.NEWS_LIST_IMG_CACHE_FOLDER; // 默认缓存路径是新闻列表的图片缓存路径
		this.reqImgWidth = reqImgWidth;
		this.reqImgHeight = reqImgHeight;
		Log.i("create Loader", "" + imageCache.size());
	}

	public AsyncImageLoader(String SDcardCacheFolder, int reqImgWidth,
			int reqImgHeight) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		this.defaultFolder = SDcardCacheFolder; // 设置图片缓存路径
		this.reqImgWidth = reqImgWidth;
		this.reqImgHeight = reqImgHeight;
	}

	/**
	 * 下载到本地并缓存到SD卡
	 * 
	 * @param imageUrl
	 * @param imageCallback
	 * @return
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		final String folder = defaultFolder;
//		String outFileName = folder
//				+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
//		File file = new File(outFileName);
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			} //else {
//				if (file.exists()) {
//					Bitmap bitmap = FileAccess.decodeSampledBitmapFromResource(
//							outFileName, reqImgWidth, reqImgHeight);
//					//Bitmap bitmap = BitmapFactory.decodeFile(outFileName);
//					Drawable tmpDrawable = new BitmapDrawable(bitmap);
//					imageCache.put(imageUrl, new SoftReference<Drawable>(
//							tmpDrawable)); // cache里面的软引用已经被系统收回，重新把图片从SD卡里面调入内存
//					System.gc();
//					return tmpDrawable;
//				}
			//}
		} //else if (file.exists()) {
//			Bitmap bitmap = FileAccess.decodeSampledBitmapFromResource(
//					outFileName, reqImgWidth, reqImgHeight);
//			// Bitmap bitmap = BitmapFactory.decodeFile(outFileName);
//			Drawable drawable = new BitmapDrawable(bitmap);
//			imageCache.put(imageUrl, new SoftReference<Drawable>(drawable)); // 将SD卡里面的图片调入cache
//			System.gc();
//			return drawable;
//		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};

		new Thread() {
			@Override
			public void run() {
				 Bitmap mBitmap=null;
		        try {
		        	
					URL url = new URL(imageUrl);  
			        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
			        conn.setConnectTimeout(10 * 1000);  
					conn.setRequestMethod("GET");
			        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
			            //return conn.getInputStream(); 
			            mBitmap = BitmapFactory.decodeStream(conn.getInputStream());
			            
			        } 
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        @SuppressWarnings("deprecation")
				Drawable drawable =new BitmapDrawable(mBitmap);
		        imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
		        //Drawable d = Drawable.createFromStream(is, src_name);
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
