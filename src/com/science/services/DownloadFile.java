package com.science.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadFile {

	
	private final int UPDATE_PROGRESS = 0;
	private final int DOWNLOAD_APK_OK =1;
	private ProgressDialog pd;
	private Context context;
	public DownloadFile(Context context){
		
		this.context = context;
		pd = new ProgressDialog(context);
        pd.setTitle("正在下载");
        pd.setMessage("请稍后。。。");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch(msg.what){
            
            case DOWNLOAD_APK_OK:
                pd.cancel();
                update();
            	break;
            case UPDATE_PROGRESS:
            	
            	int progress = (Integer) msg.obj;
            	Log.i("progress", "" + progress);
            	pd.setProgress(progress);
            	pd.setMessage("请稍候: " + progress + "%");
            	break;
            }

            
            
        }
    };
	
	
	
	   public void downFile(final String url){
		   pd.show();
	        new Thread(){
	            public void run(){
	                HttpClient client = new DefaultHttpClient();
	                HttpGet get = new HttpGet(url);
	                HttpResponse response;
	                try {
	                    response = client.execute(get);
	                    HttpEntity entity = response.getEntity();
	                    long length = entity.getContentLength();
	                    InputStream is =  entity.getContent();
	                    FileOutputStream fileOutputStream = null;
	                    if(is != null){
	                        File file = new File(Environment.getExternalStorageDirectory(),"science_pie.apk");
	                        fileOutputStream = new FileOutputStream(file);
	                        byte[] b = new byte[1024];
	                        int charb = -1;
	                        int count = 0;
	                        while((charb = is.read(b))!=-1){
	                            fileOutputStream.write(b, 0, charb);
	                            count += charb;
	                            int progress = (int) ((double)count/length * 100);
	                            Message msg = handler.obtainMessage();
	                            msg.what = UPDATE_PROGRESS;
	                            msg.obj = progress;
	                            handler.sendMessage(msg);
	                        }
	                    }
	                    fileOutputStream.flush();
	                    if(fileOutputStream!=null){
	                        fileOutputStream.close();
	                    }
	                    down();
	                 
	                }  catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }.start();
	    }
	
	   
	   
	   
	    /**
	     * 下载完成，通过handler将下载对话框取消
	     */
	    public void down(){
	        new Thread(){
	            public void run(){
	                Message message = handler.obtainMessage();
	                message.what = DOWNLOAD_APK_OK; 
	                handler.sendMessage(message);
	            }
	        }.start();
	    }
	   
	   
	   
	   
	   /**
	     * 安装应用
	     */
	    public void update(){
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"science_pie.apk"))
	                , "application/vnd.android.package-archive");
	        context.startActivity(intent);
	    }
}
