package com.science.model;

import java.util.List;

public class Resource {

	private String name;//资源名称
	private String abbr;//资源缩写
	private int id;//资源id
	private List<Resource> sub_res_list;
	public Resource(String name,String abbr,int id){
		
		this.name = name;
		this.abbr = abbr;
		this.id = id;
	}
	
	
	public Resource(String name,String abbr,int id,List<Resource>sub_res_list){
		
		this.name = name;
		this.abbr = abbr;
		this.id = id;
		this.sub_res_list = sub_res_list;
	}
	
	
	
	public String getResName(){
		
		return this.name;
		
	}
	
	public String getResAbbr(){
		return this.abbr;
	}
	
	public int getResId(){
		return this.id;
	}
	
	
	public List<Resource>getSubResList(){
		return this.sub_res_list;
	}
	
}
