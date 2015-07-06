package com.science.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Cooper {

	private Context context;
	private Drawable cooper_drawable;
	private Bitmap  cooper_photo;
	private String  cooper_photo_url;
	private int     cooper_id;
	private String  cooper_name;
	private String  cooper_identity;
	private int     cooper_team_size;
	private String  cooper_organization;
    private int     cooper_res_type;
    private int     cooper_res_title;
    private String  cooper_res_url;
    
    
    
    public static final int PHOTO = 0;
    public static final int ID = 1; 
    public static final int NAME = 2;
    public static final int IDENTITY = 3;
    public static final int INFO = 4;
    public static final int INTRODUCTION = 5;
      
	public Cooper(Context context)
	{
		this.context = context;
	}
	
	
	public Cooper(Context context,int id,String name, String photo_url,String res_url,
			int team_size,int res_type,int res_title,String organization){
		
		this.context = context;
		this.cooper_id = id;
		this.cooper_name = name;
		this.cooper_photo_url = photo_url;
		this.cooper_res_url = res_url;
		this.cooper_team_size = team_size;
		this.cooper_res_type = res_type;
		this.cooper_res_title = res_title;
		this.cooper_organization = organization;
		
		switch(res_title){
		
		case 10101:
			this.cooper_identity = "本科生";
			break;
		case 10102:
			this.cooper_identity = "研究生";
			break;
		case 10201:
			this.cooper_identity = "教授";
			break;
		case 10202:
			this.cooper_identity = "副教授";
			break;
		case 10203:
			this.cooper_identity = "研究员";
			break;
		}
		

	}
	
	public void setCooperInformation(Bitmap photo,int id,String name,String info ,
			String introduction,int res_type,int res_title){
		
		this.cooper_photo = photo;
		this.cooper_id = id;
		this.cooper_name = name;
		this.cooper_organization = introduction;
		this.cooper_res_type = res_type;
		this.cooper_res_title = res_title;
		
		
		switch(res_title){
		
		case 10101:
			this.cooper_identity = "本科生";
			break;
		case 10102:
			this.cooper_identity = "研究生";
			break;
		case 10201:
			this.cooper_identity = "教授";
			break;
		case 10202:
			this.cooper_identity = "副教授";
			break;
		case 10203:
			this.cooper_identity = "研究员";
			break;
		}
		
	}
	
	
   public Object getCooperProperty(int type)
   {
	   Object obj = null;
	   switch(type)
	   {
	   case PHOTO:
		   obj = this.cooper_photo;
		   break;
	   case ID:
		   obj = this.cooper_id;
		   break;
	   case NAME:
		   obj = this.cooper_name;
		   break;
	   case IDENTITY:
		   obj = this.cooper_identity;
		   break;
	   case INFO:
		   obj = this.cooper_team_size;
		   break;
	   case INTRODUCTION:
		   obj = this.cooper_organization;
		   break;
	   }
	   return obj;
	   
   }
	
   
   
   public int getCooperId(){
	   return this.cooper_id;
   }
   
   public String getCooperName(){
	   return this.cooper_name;
   }
   
   
   public String getCooperPhotoUrl(){
	   return this.cooper_photo_url;
   }
   
   public String getCooperResUrl(){
	   return this.cooper_res_url;
   }
   
   public int getCooperResTitle(){
	   
	   return this.cooper_res_title;
   }
   
   public int getCooperResType(){
	   return this.cooper_res_type;
   }
   
   public String getCooperIdentity(){
	   return this.cooper_identity;
   }
   
   public int getCooperTeamSize(){
	   return this.cooper_team_size;
   }
   
   public String getCooperOrganization(){
	   return this.cooper_organization;
   }
  
   
   
   
   public void putCooperPhotoDrawable(Drawable drawable){
	   this.cooper_drawable = drawable;
	   
   }
   
   public Drawable getCooperPhotoDrawable(){
	   return this.cooper_drawable;
   }
}
