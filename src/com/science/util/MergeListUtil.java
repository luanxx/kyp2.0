package com.science.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.science.interfaces.ListMerger;

public class  MergeListUtil implements ListMerger<Map<String,Object>>{

	@Override
	public boolean mergeTwoListBackward(List<Map<String, Object>> list1,
			List<Map<String, Object>> list2) {
		// TODO Auto-generated method stub	
		boolean merged = false;
		if(list1 == null || list2 == null)
			return false;
		
		for(int i = 0;i < list2.size();i++){
			if(!list2.get(i).containsKey("id"))
				return false;
			int id2 = (Integer) list2.get(i).get("id");
			int j = 0;
			for(j = 0;j < list1.size();j++){
				if(!list1.get(j).containsKey("id"))
					return false;
				int id1 = (Integer) list1.get(j).get("id");
				if(id1 == id2)
				{
					break;
				}
			}
			
			if(j >= list1.size())
			{
				list1.add(list2.get(i));
				merged = true;
			}
		}
		
		return merged;
	}

	@Override
	public boolean mergeTwoListForward(List<Map<String, Object>> list1,
			List<Map<String, Object>> list2) {
		// TODO Auto-generated method stub	
		boolean merged = false;
		if(list1 == null || list2 == null)
			return false;
		
		for(int i = 0;i < list2.size();i++){
			if(!list2.get(i).containsKey("id"))
				return false;
			int id2 = (Integer) list2.get(i).get("id");
			int j = 0;
			for(j = 0;j < list1.size();j++){
				if(!list1.get(j).containsKey("id"))
					return false;
				int id1 = (Integer) list1.get(j).get("id");
				if(id1 == id2)
				{
					break;
				}
			}
			
			if(j >= list1.size())
			{
				list1.add(0,list2.get(i));
				merged = true;
			}
		}
		
		return merged;
	}

	@Override
	public boolean mergeTwoList(List<Map<String, Object>> list1,
			List<Map<String, Object>> list2, boolean direction) {
		// TODO Auto-generated method stub
		/**
		 * 在后面追加
		 */
		if(direction)
		{
			return mergeTwoListBackward(list1,list2);
		}
		else
		{
			return mergeTwoListForward(list1,list2);
		}
		
		
	}

	
	

}
