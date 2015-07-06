package com.science.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {

	FragmentManager fm ;
	Fragment[] fragments;
	private int currentIndex = 0;
    //boolean[] fragments_update_flag;
	public CommonFragmentPagerAdapter(FragmentManager fm,Fragment[] fragments ) {
		super(fm);
		this.fm = fm;
		
		this.fragments = fragments;
		//this.fragments_update_flag = fragments_update_flag;
	
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments[position%this.fragments.length];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fragments.length;
	}
	
	
	@Override
	public int getItemPosition(Object object){
		
		return POSITION_NONE;
	}

	
//	@Override
//	public Object instantiateItem(ViewGroup container,int position){
//		
//		Fragment fragment = (Fragment) super.instantiateItem(container, position);
//	    if(!fragment.isAdded()){//如果fragment还没有added
//	    	
//	    	FragmentTransaction ft = fm.beginTransaction();
//	    	ft.add(fragment, fragment.getClass().getSimpleName());
//	    	ft.commit();
//	    	fm.executePendingTransactions();
//	    }
//		
//	    if(fragment.getView().getParent() == null){
//	    	container.addView(fragment.getView());
//	    }
//
//		
//		return fragment;
//	}
	
	
	

	
	
	
}
