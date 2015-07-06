package com.science.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

//import cn.smssdk.gui.CommonDialog;

import com.example.science.R;
import com.science.json.JsonCommonResponseStateHandler;
import com.science.json.JsonGetMyInfoHandler;
import com.science.model.ResourceDefine;
import com.science.services.MyApplication;
import com.science.util.FileAccess;
import com.science.util.Url;
import com.science.view.CircularImage;
import com.science.view.MyImageButton;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Activity{
	private MyApplication myApplication=null;
	private EditText nickname_edit=null;
	private EditText organization_edit = null;
	private EditText email_edit = null;
	private Spinner area_spinner=null;
	private Spinner identity_spinner=null;
	private String   nickname = "";
	private String   identity = "";
	private String   organization = "";
	private String   area = "";
	private String   email = "";
	private MyImageButton  save_btn;
//	private EditText userName=null;
//	private EditText password=null;
//	private EditText repassword=null;
    private String  edit_info_url = "";
	private static  String[] strDiYu={"河北","山东","海南","河南","山西"};
	private static  String[] strZhiYe={"学生","学者","科研人员"};
	private ArrayAdapter<String> adapterDiYu;
	private ArrayAdapter<String> adapterZhiYe;
	
	private ImageButton headerback=null;
	private TextView headertitle;
	private Dialog pd;
	private CircularImage avatar;
	private static final int IMAGE_REQUEST_CODE = 2;
	private static final int RESULT_REQUEST_CODE = 3;
	private static final int IMAGE2_REQUEST_CODE = 100;
	
	private final int EDIT_MY_INFO_OK = 0;
	private final int GET_MY_INFO_OK = 1;
	private Map<String,Object> my_info;
	
	private File sdcardTempFile;
	private int crop = 300;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myApplication=(MyApplication)this.getApplication();
		
		// 去掉顶部灰条
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myinfo);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		
		sdcardTempFile = new File("/mnt/sdcard/science_pie/", "tmp_pic_" + ".jpg");

		
		InitVariable();
		InitViews();
		InitData();
	}
	
	private void InitVariable()
	{
		strDiYu = new String[ResourceDefine.defined_resource_coop_area_list.size() - 1];
		for(int i = 1;i < ResourceDefine.defined_resource_coop_area_list.size();i++){
			
			strDiYu[i - 1] = ResourceDefine.defined_resource_coop_area_list.get(i).getResName();
		}
	}
	
	private void InitViews()
	{
		headerback=(ImageButton)findViewById(R.id.settingback);
		headertitle=(TextView)findViewById(R.id.settingheadertitle);
		nickname_edit = (EditText) findViewById(R.id.setting_info_nickname_edit);
		
		area_spinner=(Spinner)findViewById(R.id.setting_info_area_spinner);
		identity_spinner=(Spinner)findViewById(R.id.setting_info_identity_spinner);
		save_btn = (MyImageButton) findViewById(R.id.setting_info_save);
		organization_edit = (EditText) findViewById(R.id.setting_info_organization_edit);
		email_edit = (EditText) findViewById(R.id.setting_info_email_edit);
		avatar = (CircularImage) findViewById(R.id.avatar);
		
		avatar.setOnClickListener(onClickListener);
		
		save_btn.setOnClickListener(onClickListener);
		if(MyApplication.user.userAvatar != null){
			if(MyApplication.user.userAvatar.get() != null){
				avatar.setImageBitmap(MyApplication.user.userAvatar.get());
			}
		}
		
	}
	
	private void InitData()
	{
		

		
		
		
		adapterDiYu=new ArrayAdapter<String>(this,R.layout.my_spinner,strDiYu);
		adapterZhiYe=new ArrayAdapter<String>(this,R.layout.my_spinner,strZhiYe);
		adapterDiYu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterZhiYe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		area_spinner.setAdapter(adapterDiYu);
		identity_spinner.setAdapter(adapterZhiYe);
		
		headertitle.setText("我的资料");
		headerback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		handler.sendEmptyMessage(GET_MY_INFO_OK);
		
		
		
		
		
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				URL url;
//				String my_info_str_url = Url.composeGetMyInfoUrl();
//				try {
//					url = new URL(my_info_str_url);
//					URLConnection conn = url.openConnection();
//					conn.connect();
//					InputStream is = conn.getInputStream();
//					JsonGetMyInfoHandler json = new JsonGetMyInfoHandler();
//					List<Map<String,Object>> temp = json.getListItems(is);
//					if(temp != null){
//						my_info = temp.get(0);
//						handler.sendEmptyMessage(GET_MY_INFO_OK);
//					}
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//			
//		}).start();
		
		
	}
	
	
	
	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.avatar:
				 Intent intent = new Intent("android.intent.action.PICK");
                 intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                 intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                 intent.putExtra("crop", "true");
                 intent.putExtra("aspectX", 1);// 裁剪框比例
                 intent.putExtra("aspectY", 1);
                 intent.putExtra("outputX", crop);// 输出图片大小
                 intent.putExtra("outputY", crop);
                 startActivityForResult(intent, IMAGE2_REQUEST_CODE);
				
				
//				Intent intentFromGallery = new Intent();
//				intentFromGallery.setType("image/*"); // 设置文件类型
//				intentFromGallery
//						.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(intentFromGallery,
//						IMAGE_REQUEST_CODE);
				break;
				
			
			case R.id.setting_info_save:
				
				nickname = nickname_edit.getText().toString();
				identity = identity_spinner.getSelectedItem().toString();
				organization = organization_edit.getText().toString();
				area = area_spinner.getSelectedItem().toString();
				email = email_edit.getText().toString();
                final Map<String,File> fileMap = new HashMap<String,File>();
                
                fileMap.put(sdcardTempFile.getAbsolutePath(), sdcardTempFile);
			    Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
				try {
					FileAccess.saveBitmapToFile(bmp, "/sdcard/science_pie/" + MyApplication.user_name + ".jpg");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    MyApplication.user.userAvatar = new SoftReference<Bitmap>(bmp);
                new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
	
		                	//uploadAvatar(Url.composeUploadMyAvatar(),sdcardTempFile.getAbsolutePath());
							try {
								String url = Url.composeUploadMyAvatar();
							    post(url,fileMap);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					}
                	
                }).start();
			   // pd = CommonDialog.ProgressDialog(MyInfoActivity.this);
			   // pd.show();
				if(nickname.length() <= 0){
					
					Toast.makeText(MyInfoActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				    break;
				}
				
				if(identity.isEmpty())
				{
					Toast.makeText(MyInfoActivity.this, "身份不能为空", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if(organization.isEmpty() ){
					Toast.makeText(MyInfoActivity.this, "单位不能为空", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if(area.isEmpty()){
					Toast.makeText(MyInfoActivity.this, "地域不能为空", Toast.LENGTH_SHORT).show();
					break;
				}
				
				
				if(!email.contains("@")){
					Toast.makeText(MyInfoActivity.this, "邮箱不合法", Toast.LENGTH_SHORT).show();
					break;
				}
				
				 MyApplication.getInstance().user.userNickName = nickname;
				 MyApplication.getInstance().user.userIdentity = identity;
				 MyApplication.getInstance().user.userOrganization = organization;
				 MyApplication.getInstance().user.userArea = area;
				 MyApplication.getInstance().user.userEmail = email;
				
				edit_info_url = Url.composeEditMyInfoUrl(nickname, identity, organization, area, email);
				
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						URL url;
						try {
							url = new URL(edit_info_url);
							URLConnection conn = url.openConnection();
							conn.connect();
							InputStream is = conn.getInputStream();
							JsonCommonResponseStateHandler json = new JsonCommonResponseStateHandler();
							int code = json.getResponseStateCode(is);
							if(code == 200){
								handler.sendEmptyMessage(EDIT_MY_INFO_OK);
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}).start();
				
				break;
			}
		}
		
	};
	
	
	
	
	Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			
			switch(msg.what){
			case EDIT_MY_INFO_OK:
				if(pd != null){
					if(pd.isShowing())
						pd.dismiss();
				}
				Toast.makeText(MyInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
				myApplication.changeUserInfo();
				finish();
				break;
			case GET_MY_INFO_OK:
				nickname_edit.setText(MyApplication.getInstance().user.userNickName);
				int pos = 0;
				for(int i = 0;i < identity_spinner.getCount();i++){
					
					if(identity_spinner.getItemAtPosition(i).toString().equals(MyApplication.getInstance().user.userIdentity)){
						pos = i;
						break;
					}
				}
				identity_spinner.setSelection(pos);
				pos = 0;
				
				String organization = MyApplication.getInstance().user.userOrganization;
				if(organization == null)
					organization = "";
				organization_edit.setText(organization);
				
				for(int i = 0;i < area_spinner.getCount();i++){
					
					if(area_spinner.getItemAtPosition(i).toString().equals(MyApplication.getInstance().user.userArea)){
						pos = i;
						break;
					}
				}
				
				area_spinner.setSelection(pos);
				pos = 0;
				
				email_edit.setText(MyApplication.getInstance().user.userEmail);
				
				
				break;
			default:
				break;
			}
				
		}
	};
	

	
	private void uploadAvatar(String uploadUrl,String srcPath){
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter( CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httpPost = new HttpPost(uploadUrl);
	    MultipartEntity entity = new MultipartEntity();  
        
        File file = new File(srcPath);  
        FileBody fileBody = new FileBody(file);  
        entity.addPart("imagefile", fileBody);  
              
        httpPost.setEntity(entity);  
        HttpResponse response;
		try {
			response = httpClient.execute(httpPost);
	        HttpEntity resEntity = response.getEntity();  
	        if (resEntity != null) {        
	        	
	            Toast.makeText(this, EntityUtils.toString(resEntity), Toast.LENGTH_LONG).show();  
	        }  
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
          

  
        httpClient.getConnectionManager().shutdown();  
	}
	
	
	
	
	public static String post(String actionUrl, 
	        Map<String, File> files) throws IOException {  
		 Log.i("filehere0", "filehere0");
	    String BOUNDARY = java.util.UUID.randomUUID().toString();  
	    String PREFIX = "--", LINEND = "\r\n";  
	    String MULTIPART_FROM_DATA = "multipart/form-data";  
	    String CHARSET = "UTF-8";  
	    URL uri = new URL(actionUrl);  
	    HttpURLConnection conn = (HttpURLConnection) uri.openConnection();  
	    conn.setReadTimeout(5 * 1000);  
	    conn.setDoInput(true);// 允许输入  
	    conn.setDoOutput(true);// 允许输出  
	    conn.setUseCaches(false);  
	    conn.setRequestMethod("POST"); // Post方式  
	    conn.setRequestProperty("connection", "keep-alive");  
	    conn.setRequestProperty("Charsert", "UTF-8");  
	    conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA  
	            + ";boundary=" + BOUNDARY);  
	  
	    Log.i("filehere1", "filehere1");
	    // 首先组拼文本类型的参数  
//	    StringBuilder sb = new StringBuilder();  
//	    for (Map.Entry<String, String> entry : params.entrySet()) {  
//	        sb.append(PREFIX);  
//	        sb.append(BOUNDARY);  
//	        sb.append(LINEND);  
//	        sb.append("Content-Disposition: form-data; name=\""  
//	                + entry.getKey() + "\"" + LINEND);  
//	        sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);  
//	        sb.append("Content-Transfer-Encoding: 8bit" + LINEND);  
//	        sb.append(LINEND);  
//	        sb.append(entry.getValue());  
//	        sb.append(LINEND);  
//	    }  
	  
	    DataOutputStream outStream = new DataOutputStream(conn  
	            .getOutputStream());  
	//    outStream.write(sb.toString().getBytes());  
	  
	    // 发送文件数据  
	    if (files != null)  
	        for (Map.Entry<String, File> file : files.entrySet()) {  
	            StringBuilder sb1 = new StringBuilder();  
	            sb1.append(PREFIX);  
	            sb1.append(BOUNDARY);  
	            sb1.append(LINEND);  
	            sb1  
	                    .append("Content-Disposition: form-data; name=\"imagefile\"; filename=\""  
	                            + file.getKey() + "\"" + LINEND);  
	            sb1.append("Content-Type: application/octet-stream; charset="  
	                    + CHARSET + LINEND);  
	            sb1.append(LINEND);  
	            outStream.write(sb1.toString().getBytes());  
	            InputStream is = new FileInputStream(file.getValue());  
	            byte[] buffer = new byte[1024];  
	            int len = 0;  
	            while ((len = is.read(buffer)) != -1) {  
	                outStream.write(buffer, 0, len);  
	            }  
	  
	            is.close();  
	            outStream.write(LINEND.getBytes());  
	        }  
	    Log.i("filehere2", "filehere2");
	    // 请求结束标志  
	    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();  
	    outStream.write(end_data);  
	    outStream.flush();  
	  
	    // 得到响应码  
	    int res = conn.getResponseCode();  
	    InputStream in = conn.getInputStream();  
	    InputStreamReader isReader = new InputStreamReader(in);  
	    BufferedReader bufReader = new BufferedReader(isReader);  
	    String line = null;  
	    String data = "OK";  
	  
	    while ((line = bufReader.readLine()) == null)  
	        data += line;  
	  
	    if (res == 200) {  
	        int ch;  
	        StringBuilder sb2 = new StringBuilder();  
	        while ((ch = in.read()) != -1) {  
	            sb2.append((char) ch);  
	        }  
	        
	        
	        Log.i("upload OK", "upload OK");
	        
	    }  
	    outStream.close();  
	    conn.disconnect();  
	    return in.toString();  
	}  
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE2_REQUEST_CODE:
                Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
                avatar.setImageBitmap(bmp);
               // uploadAvatar(Url.composeUploadMyAvatar(),sdcardTempFile.getAbsolutePath());


                break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			avatar.setImageDrawable(drawable);
			Log.i("saveAvatar", "true");
		}
		Log.i("saveAvatar", "false");
	}	

	
}
