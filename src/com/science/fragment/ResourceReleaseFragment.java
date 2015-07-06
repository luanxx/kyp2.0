package com.science.fragment;

import com.example.science.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResourceReleaseFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.coop_fragment_release_source, null);
		
		return view;
	}
}
