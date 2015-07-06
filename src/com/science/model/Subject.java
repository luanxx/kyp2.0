package com.science.model;

import java.util.List;

public class Subject {

	private String sbj_name;//学科名称
	private int    sbj_code;//学科代号
	private String sbj_abbr;//学科缩写
	private List<Subject> sub_subjects;//该学科下的下一级学科
	
	
	
	public Subject(String sbj_name,int sbj_code,String sbj_abbr)
	{
		this.sbj_name = sbj_name;
		this.sbj_code = sbj_code;
		this.sbj_abbr = sbj_abbr;
	}
	public Subject(String sbj_name,int sbj_code,String sbj_abbr,List<Subject> sbj_subjects)
	{
		this.sbj_name = sbj_name;
		this.sbj_code = sbj_code;
		this.sbj_abbr = sbj_abbr;
		this.sub_subjects = sbj_subjects;
	}
	
	public String getSbjName(){
		return this.sbj_name;
	}

	public int  getSbjCode(){
		return this.sbj_code;
	}
	
	public String getSbjAbbr(){
		return this.sbj_abbr;
	}
	
	public List<Subject> getSubSbj(){
		return this.sub_subjects;
	}
}
