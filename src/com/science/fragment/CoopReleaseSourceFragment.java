package com.science.fragment;

import com.example.science.R;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CoopReleaseSourceFragment extends Fragment {

	private View view;
	private Activity activity;
	
	
	private  int width_user_name;
	private  int height_user_name;
	private  int width_user_college;
	private  int height_user_college;
	private  int width_user_identity;
	private  int height_user_identity;
	private  int width_user_job;
	private  int height_user_job;
	private  int width_user_area;
	private  int height_user_area;
	private  int width_user_major;
	private  int height_user_major;
	private  int width_user_mail;
	private  int height_user_mail;
	private  int width_user_experience;
	private  int height_user_experience;
	private  int width_team_members;
	private  int height_team_members;
	
	private  int width_device_name;
	private  int height_device_name;
	private  int width_device_type;
	private  int height_device_type;
	private  int width_device_version;
	private  int height_device_version;
	private  int width_device_num;
	private  int height_device_num;
	private  int width_upload_pic;
	private  int height_upload_pic;
	
	/*定义所有的控件*/
	
	private ImageView user_photo_iv;
	private EditText  user_name_et;
	private EditText  user_college_et;
	private Spinner   identity_spinner;
	private Spinner   job_spinner;
	private Spinner   area_spinner;
	private EditText  major_et;
	private EditText  contact_et;
	private EditText  experience_et;
	private MyImageButton  upload_pic_btn;
	private CheckBox  check_has_team_cb;
	private Spinner   people_num_spinner;
	private CheckBox  check_has_device_cb;
	private Spinner   device_name_spinner;
	private EditText  device_type_et;
	private EditText  device_version_et;
	private Spinner   device_number_spinner;
	private ImageButton device_upload_picture_btn;
	private TextView  continue_tv;
	private MyImageButton finish_btn;
	
	private String   name = "";
	private String   college = "";
	private String   identity = "";
	private String   job = "";
	private String   area = "";
	private String   major = "";
	private String   contact = "";
	private String   experience = "";
	private String   people_num = "";
	private String   device_name = "";
	private String   device_type = "";
	private String   device_version = "";
	private String   device_number = "";
	
	private static  String[] strShenFen={"学者","学生"};
	private static  String[] strZhiWu={"本科生","硕士生","博士生"};
	private static  String[] strDiYu={"北京","上海","山东","安徽","四川","广东","河北"};
	private static  String[] strRenShu={"1至5人","5至30人","30人及以上"};
	
	private ArrayAdapter<String> adapterShenFen;
	private ArrayAdapter<String> adapterZhiWu;
	private ArrayAdapter<String> adapterDiYu;
	private ArrayAdapter<String> adapterRenShu;
	
	public CoopReleaseSourceFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		
		this.view = inflater.inflate(R.layout.coop_fragment_release_source, null);
		this.activity = this.getActivity();
		initViews();
		return this.view;
	}
	
	
//	public int px2dip(int px){
//		
//		int dip = 0;
//		Display display = activity.getWindowManager().getDefaultDisplay();
//		DisplayMetrics metrics = new DisplayMetrics();
//		display.getMetrics(metrics);
//		float scale = metrics.density;
//		dip = (int) (px/scale + 0.5f);
//		return dip;
//	}
//	
//	public int normalize(int px)
//	{
//		int norm_px = 0;
//		float scale = (float)activity.getWindowManager().getDefaultDisplay().getWidth()/720.0f;
//		norm_px = (int) (px*scale);
//		return norm_px;
//	}
//	
//	
	
	
	public void initViews(){
		
		user_photo_iv = (ImageView) view.findViewById(R.id.release_resource_user_photo);
		user_name_et = (EditText) view.findViewById(R.id.release_resource_user_name);
		user_college_et = (EditText) view.findViewById(R.id.release_resource_user_college);
		
//		/*身份选择框获取*/
//		user_identity_select_view = view.findViewById(R.id.release_resource_user_identity);
//		user_identity_select_tv = (TextView) user_identity_select_view.findViewById(R.id.release_select_text);
//		user_identity_select_btn = (MyImageButton) user_identity_select_view.findViewById(R.id.release_select_btn);
//		/*职业选择框获取*/
//		user_job_select_view =  view.findViewById(R.id.release_resource_user_job);
//		user_job_select_tv = (TextView) user_job_select_view.findViewById(R.id.release_select_text);
//		user_job_select_btn = (MyImageButton) user_job_select_view.findViewById(R.id.release_select_btn);
//		/*地域选择框获取*/
//		user_job_select_view = view.findViewById(R.id.release_resource_user_area);
//		
//		user_area_select_view =  view.findViewById(R.id.release_resource_user_area);
//		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		//params.width = width_user_name;
		//params.height = height_user_name;
//		user_name_et.setLayoutParams(params);
//		
//		user_name_et.setWidth(normalize(width_user_name));
//		user_name_et.setHeight(normalize(height_user_name));
//		
//		
//		//params.width = width_user_college;
//		//params.height = height_user_college;
//		user_college_et.setLayoutParams(params);
//		user_college_et.setWidth(normalize(width_user_college));
//		user_college_et.setHeight(normalize(height_user_college));
//		
//		
//		
//		
//		//user_identity_select_view.setLayoutParams(params);
//		/*设置身份*/
//		user_identity_select_view.setMinimumWidth(normalize(width_user_identity));
//		user_identity_select_view.setMinimumHeight(normalize(height_user_identity));
//		user_identity_select_tv.setText("身份");
//		
//		
//		/*设置职业*/
//		user_job_select_view.setMinimumWidth(normalize(width_user_job));
//		user_job_select_view.setMinimumHeight(normalize(height_user_job));
//		user_job_select_tv.setText("职业");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
