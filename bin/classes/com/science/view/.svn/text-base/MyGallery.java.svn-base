package cn.sciencenet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery {

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 重写onFling方法，将Gallery的滑动速度降低
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return super.onFling(e1, e2, velocityX / 3, velocityY);
	}
}
