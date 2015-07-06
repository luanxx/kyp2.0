package com.science.model;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.science.interfaces.ListMerger;
import com.science.interfaces.OnShowMoreListener;
import com.science.util.DefaultUtil;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class Comment implements Comparable{

	
	public static final String commentid = "commentid";
	public static final String customerid = "customerid";
	public static final String thir_commentname = "thir_commentname";
	public static final String thir_content = "thir_content";
	public static final String sec_commentname = "sec_commentname";
	public static final String sec_content = "sec_content";
	public static final String customername = "customername";
	public static final String content = "content";
	public static final String time = "time";
	public static final String parentname = "parentname";
	public static final String parentcontent = "parentcontent";
	public static final String likenum = "likenum";
	public static final int MAX_COMMENT_WORDS_NUM = 10;
	
	
	 
	public SoftReference<Drawable> customer_avatar ;
	public String comment_avatar_url;
	public String customer_name = DefaultUtil.NULL;
	public String comment_content = DefaultUtil.EMPTY ;
	public Date comment_date = null;
	public int comment_id = DefaultUtil.INAVAILABLE;
	public int customer_id = DefaultUtil.INAVAILABLE;
	public String comment_time = DefaultUtil.NULL;
	public SpannableString span_comment_content = new SpannableString(comment_content);
	public Comment fir_comment = null;
	public Comment sec_comment = null;
	public Comment thr_comment = null;
	public TextView tv = null;
	public int mark;//标志当前是几级评论
	public int comment_like_num = DefaultUtil.ZERO;
	public String comment_parent_name = DefaultUtil.NULL;
	public String comment_parent_content = DefaultUtil.NULL;
	public boolean like = false;//标志是否已经喜欢了
	private int pos1 = DefaultUtil.INAVAILABLE,
			    pos2 = DefaultUtil.INAVAILABLE,
			    pos3 = DefaultUtil.INAVAILABLE,
			    pos4 = DefaultUtil.INAVAILABLE,
			    pos5 = DefaultUtil.INAVAILABLE,
			    pos6 = DefaultUtil.INAVAILABLE;
	
	private int showed_words_len = 0;
	private int total_words_len = 0;  
	private String temp_comment_content;//用以存储临时字符串
	private OnShowMoreListener listener;
	/*
	 * 
	 * hello//@yanan:good!//@ming:ok.....  显示更多
	 *      |       |     |      |         |     |
	 *      pos1   pos2  pos3   pos4       pos5  pos6 
	 * 
	 */
	
	/***
	 * 
	 * @param obj接受JSONObject参数
	 */
	public Comment(JSONObject obj){
		
		if(null == obj)
			return;
		try {
			this.comment_id = Integer.parseInt(obj.getString(commentid));
			this.customer_id = Integer.parseInt(obj.getString(customerid));
			this.customer_name = obj.getString(customername);
			this.comment_time = obj.getString(time);
			this.comment_content = obj.getString(content);
			this.comment_like_num = Integer.parseInt(obj.getString(likenum));
			this.comment_parent_name = obj.getString(parentname);
			this.comment_parent_content = obj.getString(parentcontent);
			this.comment_avatar_url = obj.getString("customerimage");
			
			
			//设置评论的回复样式
			if(!this.comment_parent_name.equals(DefaultUtil.NULL))
			{
				this.comment_content += "//@" + this.comment_parent_name + ":" + this.comment_parent_content;
			}
			
			Log.i("comment_content", this.comment_content);
			setCommentText();
			//获取时间的值
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMMM-dddd HH:mm:ss");
			try {
				this.comment_date = sdf.parse(this.comment_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		
	}
	
	
	
	
	public Comment(int comment_id,int root_id,int article_type,int article_id,String comment_content){
		
		
	}
	
	
	
	
	
	
	
	
	public Comment(int comment_id,int customer_id,String customer_name,
			            String comment_time,String comment_content,int comment_like_num,
			            String comment_parent_name,String comment_parent_content,String comment_avatar_url)
	{
		this.comment_id = comment_id;
		this.customer_id = customer_id;
		this.customer_name = customer_name;
		this.comment_time = comment_time;
		this.comment_content = comment_content;
		this.comment_like_num = comment_like_num;
		this.comment_parent_name = comment_parent_name;
		this.comment_parent_content = comment_parent_content;
		this.comment_avatar_url = comment_avatar_url;
		
	}
	
	public Comment()
	{
	}
	
	
	
	
	
	public Comment(Comment fir_comment,Comment sec_comment,Comment thr_comment)
	{
		this.fir_comment = fir_comment;
		this.sec_comment = sec_comment;
		this.thr_comment = thr_comment;
		
		
		if(this.thr_comment.customer_name.equals(DefaultUtil.NULL))
			this.thr_comment = null;
		
		
		if(this.sec_comment.customer_name.equals(DefaultUtil.NULL))
			this.sec_comment = null;
		
		if(this.fir_comment.customer_name.equals(DefaultUtil.NULL))
			this.fir_comment = null;
		
		if(this.thr_comment != null)//说明第三级有内容
		{
			this.comment_time = this.thr_comment.comment_time;
			this.customer_name = this.thr_comment.customer_name;
			this.comment_content += this.thr_comment.comment_content;
			this.mark = 3;
		}
		
		
		
		
		if(this.sec_comment != null)//说明第二级不为空
		{
			if(this.thr_comment == null)
			{
				this.comment_time = this.sec_comment.comment_time;
				this.customer_name = this.sec_comment.customer_name;
				this.comment_content +=   this.sec_comment.comment_content;
				this.mark = 2;
			}
			else
			{
				pos1 = this.comment_content.length();
				this.comment_content += "//@" + this.sec_comment.customer_name + ":" ;
				pos2 = this.comment_content.length() - 1;
			    this.comment_content += this.sec_comment.comment_content;
			}
			
			
		}
		
		if(this.fir_comment != null)
		{
			if(this.sec_comment == null && this.thr_comment == null)//第三级、第二级评论都为空
			{
				this.comment_time = this.fir_comment.comment_time;
				this.customer_name = this.fir_comment.customer_name;
				this.comment_content = this.fir_comment.comment_content;
				this.mark = 1;
			}
			else
			{
				pos3 = this.comment_content.length();
				this.comment_content += "//@" + this.fir_comment.customer_name + ":";
				pos4 = this.comment_content.length() - 1;
			    this.comment_content += this.fir_comment.comment_content;
			}
			
			this.comment_id = this.fir_comment.comment_id;
			this.customer_id = this.fir_comment.customer_id;
		}
		
		

		total_words_len = this.comment_content.length();
		setCommentText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMMM-dddd HH:mm:ss");
		try {
			this.comment_date = sdf.parse(this.comment_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Log.i("year", ""+this.comment_date.getYear());
	}

	
	private OnClickListener l = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i("show_more", ""+span_comment_content.toString());
			setCommentText();
			if(tv != null)
			{
				tv.setText(span_comment_content);
				listener.showMore();
			}
		}
		
	};
	
	
	
	public void setCommentText()
	{
		int start = 0,end = 0;
		if(this.total_words_len - showed_words_len > MAX_COMMENT_WORDS_NUM)
		{
			
			this.temp_comment_content = this.comment_content.substring(0,showed_words_len + MAX_COMMENT_WORDS_NUM);
			showed_words_len += MAX_COMMENT_WORDS_NUM;
			this.temp_comment_content += "......";
			start += this.temp_comment_content.length();
			this.temp_comment_content += "显示更多";
			end = this.temp_comment_content.length();
			this.span_comment_content = new SpannableString(this.temp_comment_content);
			//this.span_comment_content.setSpan(new ForegroundColorSpan(Color.GRAY),start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			this.span_comment_content.setSpan(new MyClickable(l),start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//this.comment_content += "......";
			//pos5 = this.comment_content.length();
			//this.comment_content += "显示更多";
			//pos6 = pos5 +  new String("显示更多").length();
		}
		else
		{
			this.temp_comment_content = this.comment_content;
			showed_words_len = this.comment_content.length();
			this.span_comment_content = new SpannableString(this.temp_comment_content);
			if(tv != null)
				tv.setText(span_comment_content);
			
		}
		
		
		
	}
	
	public void setSpannableCommentContent(TextView tv,OnShowMoreListener listener)
	{
 
		
		if(tv == null)
			return ;

		this.tv = tv;
		//setCommentText();
		this.listener = listener;
		tv.setText(this.span_comment_content );
		//setCommentText();
		//setCommentText();
		
		
//		span_comment_content = new SpannableString(this.comment_content);
//		if(pos1 > 0 && pos2 > 0 && pos1 < pos2)
//			span_comment_content.setSpan(new ForegroundColorSpan(Color.BLUE), pos1, pos2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		
//		if(pos3 > 0&& pos3 > 0 && pos3 < pos4)
//			span_comment_content.setSpan(new ForegroundColorSpan(Color.BLUE), pos3, pos4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	
	
	class MyClickable extends ClickableSpan implements OnClickListener{
		private final View.OnClickListener mListener;

		public MyClickable(View.OnClickListener l){
		mListener = l;
		}

		@Override
		public void onClick(View v){
		mListener.onClick(v);
		}
	
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Comment other = (Comment)o;
		if(this.comment_date == null || other.comment_date == null)
			return 0;
		
		Calendar c = Calendar.getInstance();
		Time t1 = new Time(),t2 = new Time();
		c.setTime(this.comment_date);
		t1.year = c.get(Calendar.YEAR);
		t1.month = c.get(Calendar.MONTH);
		t1.day = c.get(Calendar.DAY_OF_MONTH);
		t1.hour = c.get(Calendar.HOUR_OF_DAY);
		t1.minute = c.get(Calendar.MINUTE);
		t1.second = c.get(Calendar.SECOND);
		
		c.setTime(other.comment_date);
		t2.year = c.get(Calendar.YEAR);
		t2.month = c.get(Calendar.MONTH);
		t2.day = c.get(Calendar.DAY_OF_MONTH);
		t2.hour = c.get(Calendar.HOUR_OF_DAY);
		t2.minute = c.get(Calendar.MINUTE);
		t2.second = c.get(Calendar.SECOND);
		
		int d_year = t1.year - t2.year;
		if(d_year > 0)
			return -1;
		else if(d_year < 0)
			return 1;
		
		int d_month = t1.month - t2.month;
		if(d_month > 0)
			return -1;
		else if(d_month < 0)
			return 1;
		
		int d_day = t1.day - t2.day;
		if(d_day > 0)
			return -1;
		else if(d_day < 0)
			return 1;
		
		int d_hour = t1.hour - t2.hour;
		if(d_hour > 0)
			return -1;
		else if(d_hour < 0)
			return 1;
		
		int d_min = t1.minute - t2.minute;
		if(d_min > 0)
			return -1;
		else if(d_min < 0)
			return 1;
		
		int d_sec = t1.second - t2.second;
		if(d_sec > 0)
			return -1;
		else if(d_sec < 0)
			return 1;
		
		return 0;
	}
	
	
	

	private class Time{
		public int year,month,day,hour,minute,second;
	}

	
	
	
  public static boolean mergeCommentList(List<Comment> dst,List<Comment> src){
	  
	  ListMerger<Comment> merger = new ListMerger<Comment>(){

		@Override
		public boolean mergeTwoListBackward(List<Comment> list1,
				List<Comment> list2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mergeTwoListForward(List<Comment> dst,
				List<Comment> src) {
			// TODO Auto-generated method stub
			boolean merged = false;
			if(dst == null || src == null)
				return false;
			
			for(int i = 0;i < src.size();i++){

				int id2 = (Integer) src.get(i).comment_id;
				int j = 0;
				for(j = 0;j < dst.size();j++){
					int id1 = (Integer) dst.get(j).comment_id;
					if(id1 == id2)
					{
						break;
					}
				}
				
				if(j >= dst.size())
				{
					dst.add(0,src.get(i));
					merged = true;
				}
			}
			
			
			return merged;
		}

		@Override
		public boolean mergeTwoList(List<Comment> list1, List<Comment> list2,
				boolean direction) {
			// TODO Auto-generated method stub
			return false;
		}
		  
	  };
	  
	  
	  
	return  merger.mergeTwoListForward(dst, src);
	  
	  
  }
	
	
	

	
	
}
