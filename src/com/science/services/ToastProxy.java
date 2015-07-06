package com.science.services;

import com.example.science.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ToastProxy extends Toast{

	public ToastProxy(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static Toast makeText(Context context,int resId,int duration)throws Resources.NotFoundException{
		return makeText(context,context.getResources().getText(resId),duration);
		
	}

	
    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast toast = new ToastProxy(context);

        View view = View.inflate(context, R.layout.float_view, null);
        TextView message = (TextView) view.findViewById(R.id.toast_info);
        message.setText(text);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        params.width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        view.setLayoutParams(params);
        toast.setView(view);
        toast.setDuration(duration);
        
        //updateToastAnim(toast);

        return toast;
}
    
    public void setToastView(Context context, CharSequence text, int duration)
    {
        //Toast toast = new ToastProxy(context);

        View view = View.inflate(context, R.layout.float_view, null);
        TextView message = (TextView) view.findViewById(R.id.toast_info);
        message.setText(text);
        
        this.setView(view);
        this.setDuration(duration);
    }
	
	
	



    
    
    
    
}
