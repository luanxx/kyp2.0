package com.science.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 获取当前日期
	 * 
	 * @return iphone_yyyy_MM_dd的形式
	 */
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_MM_dd");
		Date curDate = new Date(System.currentTimeMillis());
		return "iphone" + formatter.format(curDate);
	}
	
	/**
	 * 比较日期，判断是否在今天以前
	 * @param date1
	 * @return
	 */
	public static boolean isDateBefore(String date1, String currentTime) {

		java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = df.parse(date1);
			d2 = df.parse(currentTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d1.after(d2); // true date1鏃╀簬date2
	}
}
