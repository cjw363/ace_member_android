package com.ace.member.listener;


import android.view.View;

public interface IViewLayoutListener {
	void onLayout(View view);
	void onLayout(int width,int height);

	class SimpleViewLayoutListenerAdapter implements IViewLayoutListener{

		@Override
		public void onLayout(View view) {

		}

		@Override
		public void onLayout(int width, int height) {

		}
	}
}
