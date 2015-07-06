package com.science.view;

import java.util.zip.Inflater;

import com.example.science.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MySelectProjectApplication extends LinearLayout{

	public MySelectProjectApplication(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.myselectprojectapplication, this);
	}

}
