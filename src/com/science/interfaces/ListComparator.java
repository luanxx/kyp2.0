package com.science.interfaces;

import java.util.Comparator;
import java.util.Map;

import com.science.model.Cooper;

/*从大到小排序*/
public class ListComparator implements Comparator<Map<String,Object>>{



	@Override
	public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		int i1 = (Integer) arg0.get("id");
		int i2 = (Integer) arg1.get("id");
		if(i1 < i2)
			return 1;
		else if(i1 > i2)
			return -1;
		return 0;
	}

}
