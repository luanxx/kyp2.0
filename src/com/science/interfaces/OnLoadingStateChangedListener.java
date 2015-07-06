package com.science.interfaces;

public interface OnLoadingStateChangedListener {

	public void setOnLoadingStateChangedListener(OnLoadingStateChangedListener l);
	public void onLoadingSucceed();
	public void onLoadingFail();
	public void onLoadingBegin();
}
