package com.science.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCache {
    public static Map<String,Object> cache = new HashMap<String,Object>();
    public static List<Map<String,Object>> hot_project_list = null;// = new ArrayList<Map<String,Object>>();
    public static List<Map<String,Object>> usual_project_list = null;
    public static List<Map<String,Object>> expire_project_list = null;
    
    
    
    public static List<Map<String,Object>> chinese_doc_list = null;
    public static List<Map<String,Object>> english_doc_list = null;
    public static List<Map<String,Object>> work_doc_list = null;
    public static List<Map<String,Object>> nsf_doc_list = null;
    
    public static List<List<Map<String,Object>>>  doc_lists;// = new ArrayList<List<Map<String,Object>>>();
  
}
