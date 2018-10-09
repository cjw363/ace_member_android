package com.ace.member.main.bottom_dialog;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.ace.member.listener.IMyViewOnClickListener;

public class BottomController {
	private Context mContext;
	private CharSequence mTvTitle;
	private int[] mIvRes;
	private int mTvContentColorRes;
	private int[] mTvContent1;
	private int mTvContent2Weight;
	private CharSequence[] mTvContent2;
	private int mBottomHeight;
	private IMyViewOnClickListener mClickListener;
	private FragmentManager mFragmentManager;


	public BottomController() {}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public CharSequence getTvTitle() {
		return mTvTitle;
	}

	public void setTvTitle(CharSequence tvTitle) {
		mTvTitle = tvTitle;
	}

	public int[] getIvRes() {
		return mIvRes;
	}

	public void setIvRes(int[] ivRes) {
		mIvRes = ivRes;
	}

	public int getTvContentColorRes() {
		return mTvContentColorRes;
	}

	public void setTvContentColorRes(int tvContentColorRes) {
		mTvContentColorRes = tvContentColorRes;
	}

	public int[] getTvContent1() {
		return mTvContent1;
	}

	public void setTvContent1(int[] tvContent1) {
		mTvContent1 = tvContent1;
	}

	public int getTvContent2Weight() {
		return mTvContent2Weight;
	}

	public void setTvContent2Weight(int tvContent2Weight) {
		mTvContent2Weight = tvContent2Weight;
	}

	public CharSequence[] getTvContent2() {
		return mTvContent2;
	}

	public void setTvContent2(CharSequence[] tvContent2) {
		mTvContent2 = tvContent2;
	}

	public int getBottomHeight() {
		return mBottomHeight;
	}

	public void setBottomHeight(int bottomHeight) {
		mBottomHeight = bottomHeight;
	}

	public IMyViewOnClickListener getClickListener() {
		return mClickListener;
	}

	public void setClickListener(IMyViewOnClickListener clickListener) {
		mClickListener = clickListener;
	}

	public FragmentManager getFragmentManager() {
		return mFragmentManager;
	}

	public void setFragmentManager(FragmentManager fragmentManager) {
		mFragmentManager = fragmentManager;
	}

	public static class BottomParams {
		public Context mContext;
		public CharSequence mTvTitle;
		public int[] mIvRes;
		public int mTvContentColorRes;
		public int[] mTvContent1;
		public int mTvContent2Weight;
		public CharSequence[] mTvContent2;
		public int mBottomHeight;
		public IMyViewOnClickListener mClickListener;
		public FragmentManager mFragmentManager;

		public BottomParams(FragmentActivity activity) {
			mContext = activity;
			mFragmentManager = activity.getSupportFragmentManager();
		}

		public void apply(BottomController controller) {
			controller.mTvTitle = mTvTitle;
			controller.mIvRes = mIvRes;
			controller.mTvContentColorRes = mTvContentColorRes;
			controller.mTvContent1 = mTvContent1;
			controller.mTvContent2Weight = mTvContent2Weight;
			controller.mTvContent2 = mTvContent2;
			controller.mBottomHeight=mBottomHeight;
			controller.mClickListener = mClickListener;
			controller.mFragmentManager = mFragmentManager;
		}
	}
}
