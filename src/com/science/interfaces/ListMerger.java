package com.science.interfaces;

import java.util.ArrayList;
import java.util.List;

public  interface ListMerger<T> {

	//在尾部插入
	public  boolean mergeTwoListBackward(List<T> list1,List<T> list2);
	
	//在前部插入
	public  boolean mergeTwoListForward(List<T> list1,List<T> list2);
	
	/**
	 * 
	 * @param list1
	 * @param list2
	 * @param direction   true表示在后面追加，false表示在前面追加
	 * @return
	 */
	public boolean mergeTwoList(List<T> list1,List<T>list2,boolean direction);
}
