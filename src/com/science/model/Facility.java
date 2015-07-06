package com.science.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Facility  {

	private String faci_model;
	private String faci_owner;
	private String faci_res_url;
	private String faci_name;
	private int    faci_res_type;
	private int    faci_number;
	private int    faci_id;
	private List<String> faci_img_url_list;
	private Drawable     faci_drawable;
    private List<Drawable> faci_drawable_list = new ArrayList<Drawable>();
	
	public Facility(){
		
	}
	

	public Facility(String name,String res_url,int res_type,
			        String model,int number,String owner,int id,
			        List<String> img_url_list){
		
		this.faci_name = name;
		this.faci_res_url = res_url;
		this.faci_res_type = res_type;
		this.faci_model = model;
		this.faci_number = number;
		this.faci_owner = owner;
		this.faci_id = id;
		this.faci_img_url_list = img_url_list;
	}
	
	
	
	public String getFaciName(){
		return this.faci_name;
	}
	
	public String getFaciResUrl(){
		return this.faci_res_url;
	}
	
	public int getFaciResType(){
		return this.faci_res_type;
	}
	
	public String getFaciModel(){
		return this.faci_model;
	}
	
	public int getFaciNumber(){
		return this.faci_number;
	}
	
	public String getFaciOwner(){
		return this.faci_owner;
	}
	
	public int getFaciId(){
		return this.faci_id;
	}
	
	
	public List<String>getFaciImgUrlList(){
		return this.faci_img_url_list;
	}
	
	public List<Drawable>getFaciDrawableList()
	{
		return this.faci_drawable_list;
	}
	
	public void putFaciDrawableList(Drawable drawable){
		
		this.faci_drawable_list.add(drawable);
	}
	
	public Drawable getFaciDrawable(){
		return this.faci_drawable;
	}
	
	public void putFaciDrawable(Drawable drawable){
		this.faci_drawable = drawable;
	}
}
