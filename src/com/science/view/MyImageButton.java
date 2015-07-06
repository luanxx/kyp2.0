package com.science.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {

	private boolean pressed = false;
	private Drawable state_normal;
	private Drawable state_pressed;
	private    int   itag;
	private   String stag;
	public MyImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyImageButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setDrawable(Drawable state_normal,Drawable state_pressed)
	{
		this.state_normal = state_normal;
		this.state_pressed = state_pressed;
	}
	
	public void updateButtonState(boolean pressed)
	{
		this.pressed = pressed;
		if(pressed)
			this.setBackground(state_pressed);
		else
			this.setBackground(state_normal);
	}
	
	public boolean isPressed()
	{
		return this.pressed;
	}
	
	public void setItag(int itag)
	{
		this.itag = itag;
	}
	
	public void setStag(String stag)
	{
		this.stag =  stag;
	}
	
	public int getItag()
	{
		return this.itag;
	}
	
	public String getStag()
	{
		return this.stag;
	}
}
