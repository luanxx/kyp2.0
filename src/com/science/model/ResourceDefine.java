package com.science.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;


public class ResourceDefine {
	
	private String abbr;//英文简称
	private int    code;        //数字编码
	private String value;       //表示的值    
	
	public static DocumentBuilderFactory factory;
	public static List<ResourceDefine> defined_area_resource ;

	
	
	public static List<Map<String,List<ResourceDefine>>> defined_subjects_resource;
	public static List<Subject> defined_resource_subject_list;
	public static List<Resource> defined_resource_proj_src_list;
	public static List<Resource> defined_resource_proj_type_list;
	public static List<Resource> defined_resource_coop_area_list;
	public static List<Resource> defined_resource_coop_all_list;
	public ResourceDefine(String abbr,int code,String value){
		
		this.abbr = abbr;
		this.code = code;
		this.value = value;
	}
	
	public static void deriveResourceDefine(Context context){
		
		if(defined_area_resource == null)
			defined_area_resource = new ArrayList<ResourceDefine>();
	    
		factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
//			InputStream is = context.getAssets().open("ChinaAreaResource.xml");
//			Document doc = builder.parse(is);
//			Element root = doc.getDocumentElement();
//			/*解析省市*/
//			NodeList links = root.getElementsByTagName("province");
//			for(int i = 0;i < links.getLength();i++)
//			{
//				Element link  = (Element) links.item(i);
//				NodeList child_links = link.getChildNodes();
//				String abbr = "";
//				int code = 0;
//				String value = "";
//				for(int j = 0;j < child_links.getLength();j++)
//				{
//					Node node = child_links.item(j);//判断是否为元素类型
//					if(node.getNodeType() == Node.ELEMENT_NODE){
//						
//						Element child_node = (Element)node;
//
//						if("abbr".equals(child_node.getNodeName())){
//							abbr = child_node.getFirstChild().getNodeValue();
//						}
//						
//						if("code".equals(child_node.getNodeName()))
//						{
//							code = Integer.parseInt(child_node.getFirstChild().getNodeValue());
//						}
//						
//						if("value".equals(child_node.getNodeName()))
//						{
//							value = child_node.getFirstChild().getNodeValue();
//						}
//	
//					}
//				}
//				
//				
//				ResourceDefine res_def = new ResourceDefine(abbr,code,value);
//				defined_area_resource.add(res_def);
//				//String abbr = link.getElementsByTagName("abbr").item(0).getFirstChild().getNodeValue();
//				//int code = Integer.parseInt(link.getElementsByTagName("code").item(0).getFirstChild().getNodeValue());
//				//String value = link.getElementsByTagName("value").item(0).getFirstChild().getNodeName();
//
//			}
//			
//			is.close();
			
			/*获取学科的名称*/
			
			InputStream is = context.getAssets().open("SubjectResource.xml");
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList links = root.getElementsByTagName("subjects");
			defined_subjects_resource = new ArrayList<Map<String,List<ResourceDefine>>>();
			//定义一级学科列表
			defined_resource_subject_list = new ArrayList<Subject>();
			for(int i = 0;i < links.getLength();i++){
				
				Element subjects_node = (Element) links.item(i);
				String subjects_name = subjects_node.getAttribute("name");
				int subjects_id = Integer.parseInt(subjects_node.getAttribute("id"));
				//定义二级学科列表
				List<Subject> second_subject_list = new ArrayList<Subject>();
				
				NodeList subject_links = subjects_node.getElementsByTagName("subject");
				List<ResourceDefine> subject_list = new ArrayList<ResourceDefine>();
				for(int j = 0;j < subject_links.getLength();j++){
					Element subject_node = (Element) subject_links.item(j);
					NodeList child_links = subject_node.getChildNodes();
					String abbr = "";
					int code = 0;
					String value = "";
					for(int  k = 0;k < child_links.getLength();k++){
						
						Node node = child_links.item(k);
						if(node.getNodeType() == Node.ELEMENT_NODE){
							
							if("abbr".equals(node.getNodeName())){
								abbr = node.getFirstChild().getNodeValue();
							}
							if("code".equals(node.getNodeName())){
								code = Integer.parseInt(node.getFirstChild().getNodeValue());
							}
							if("value".equals(node.getNodeName())){
								value = node.getFirstChild().getNodeValue();
							}
						}
					}
					
					//定义一个二级学科
					Subject second_subject = new Subject(value,code,abbr);
					second_subject_list.add(second_subject);
					ResourceDefine res_def = new ResourceDefine(abbr,code,value);
					subject_list.add(res_def);
				}
	
				//定义一个一级学科并加入到defined_resource_subject_list中
				Subject first_subject = new Subject(subjects_name,subjects_id,"",second_subject_list) ;
				defined_resource_subject_list.add(first_subject);
				
				Map<String,List<ResourceDefine>> map = new HashMap<String,List<ResourceDefine>>();
				map.put(subjects_name, subject_list);
				defined_subjects_resource.add(map);
			}
			
			is.close();
			
			
			
			
			
			
			
			
			
			/*定义项目来源*/
			is = context.getAssets().open("ProjectSourceResource.xml");
			doc = builder.parse(is);
			root = doc.getDocumentElement();
			links = root.getElementsByTagName("sources");
			defined_resource_proj_src_list = new ArrayList<Resource>();
			
			for(int i = 0;i < links.getLength();i++){
				
				Element node = (Element) links.item(i);
				NodeList sub_links = node.getElementsByTagName("source");
				String name = node.getAttribute("name");
				int id = Integer.parseInt(node.getAttribute("id"));
				List<Resource> sub_proj_src_list = new ArrayList<Resource>();
				for(int j = 0;j < sub_links.getLength();j++){
					
					Element sub_node = (Element) sub_links.item(j);
					NodeList sub_sub_links = sub_node.getChildNodes();
					String abbr = "";
					String value = "";
					int code = 0;
					for(int k = 0;k < sub_sub_links.getLength();k++){
						
						Node sub_sub_node =  sub_sub_links.item(k);
						if(sub_sub_node.getNodeType() == Node.ELEMENT_NODE){
						if("abbr".equals(sub_sub_node.getNodeName())){
							abbr = sub_sub_node.getFirstChild().getNodeValue();
						}
						
						if("value".equals(sub_sub_node.getNodeName())){
							value = sub_sub_node.getFirstChild().getNodeValue();
						}
						
						if("code".equals(sub_sub_node.getNodeName())){
							code = Integer.parseInt(sub_sub_node.getFirstChild().getNodeValue());
						}
						}
					}
					Resource res = new Resource(value,abbr,code);
					sub_proj_src_list.add(res);
				}
				
				
				Resource res = new Resource(name,"",id,sub_proj_src_list);
				defined_resource_proj_src_list.add(res);
				/*合作研究中地域来源归结于此*/
				
			}
			
			is.close();
			
			defined_resource_coop_area_list =  defined_resource_proj_src_list.get(links.getLength() - 1).getSubResList();
			
			
			
			
			
			
			/*定义项目类型*/
			is = context.getAssets().open("ProjectTypeResource.xml");
			doc = builder.parse(is);
			root = doc.getDocumentElement();
			links = root.getElementsByTagName("type");
			defined_resource_proj_type_list = new ArrayList<Resource>();
			for(int i = 0;i < links.getLength();i++){
				
				Element node = (Element) links.item(i);
				String abbr = "";
				String value = "";
				int code = 0;
				NodeList sub_links = node.getChildNodes();
				for(int j = 0;j < sub_links.getLength();j++){
					Node sub_node = sub_links.item(j);
					if(sub_node.getNodeType() == Node.ELEMENT_NODE){
						if("abbr".equals(sub_node.getNodeName())){
							abbr = sub_node.getFirstChild().getNodeValue();
						}
						if("code".equals(sub_node.getNodeName())){
							code = Integer.parseInt(sub_node.getFirstChild().getNodeValue());
						}
						if("value".equals(sub_node.getNodeName())){
							value = sub_node.getFirstChild().getNodeValue();
						}
					}
				}
				
				Resource res = new Resource(value,abbr,code);
				defined_resource_proj_type_list.add(res);
			}
			
			
			is.close();
			
			
			
			
			
			/*定义合作研究的全部来源*/
			is = context.getAssets().open("CoopAllResource.xml");
			doc = builder.parse(is);
			root = doc.getDocumentElement();
			links = root.getElementsByTagName("resources");
			defined_resource_coop_all_list = new ArrayList<Resource>();
			for(int i = 0;i < links.getLength();i++){
				
				Element node = (Element) links.item(i);
				String name = node.getAttribute("name");
				NodeList sub_links = node.getElementsByTagName("resource");
				List<Resource> sub_list = new ArrayList<Resource>();
				int id = Integer.parseInt(node.getAttribute("id"));
				
				for(int j = 0;j < sub_links.getLength();j++){
					
					Element sub_node = (Element) sub_links.item(j);
					NodeList sub_sub_links = sub_node.getChildNodes();
					String abbr = "";
					String value = "";
					int code = 0;
					for(int k = 0;k < sub_sub_links.getLength();k++){
						
						Node sub_sub_node = sub_sub_links.item(k);
						if(sub_sub_node.getNodeType() == Node.ELEMENT_NODE){
							
							if("abbr".equals(sub_sub_node.getNodeName())){
								abbr = sub_sub_node.getFirstChild().getNodeValue();
							}
							
							if("value".equals(sub_sub_node.getNodeName())){
								value = sub_sub_node.getFirstChild().getNodeValue();
							}
							
							if("code".equals(sub_sub_node.getNodeName())){
								code = Integer.parseInt(sub_sub_node.getFirstChild().getNodeValue());
							}
						}
						
					}
					
					
					Resource res = new Resource(value,abbr,code);
					sub_list.add(res);
				}
				
				Resource res = new Resource(name,"",id,sub_list);
				defined_resource_coop_all_list.add(res);
			
			}
			
			
			is.close();
			
			
			
			
			
			
			
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	

	
	
	
	
	
	public String getAbbr(){
		return this.abbr;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getValue(){
		return this.value;
	}
}
