package com.ace.member.main.bottom_dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.ace.member.listener.IMyViewOnClickListener;

import java.util.List;

public class BottomController2 {
	private Context mContext;
	private List<ControllerBean> mList;
	private CharSequence mTvTitle;
	private int mTvContentColorRes;
	private int mTvContent2Weight;
	private int mBottomHeight;
	private int mType;
	private IMyViewOnClickListener mClickListener;
	private FragmentManager mFragmentManager;

	public BottomController2() {
	}

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

	public List<ControllerBean> getList() {
		return mList;
	}

	public void setList(List<ControllerBean> list) {
		mList = list;
	}

	public int getTvContentColorRes() {
		return mTvContentColorRes;
	}

	public void setTvContentColorRes(int tvContentColorRes) {
		mTvContentColorRes = tvContentColorRes;
	}

	public void setType(int type) {
		mType = type;
	}

	public int getType() {
		return mType;
	}

	public int getTvContent2Weight() {
		return mTvContent2Weight;
	}

	public void setTvContent2Weight(int tvContent2Weight) {
		mTvContent2Weight = tvContent2Weight;
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
		public List<ControllerBean> mList;
		public int mTvContentColorRes;
		public int mTvContent2Weight;
		public int mBottomHeight;
		public int mType=0;
		public IMyViewOnClickListener mClickListener;
		public FragmentManager mFragmentManager;

		public BottomParams(FragmentActivity activity) {
			mContext = activity;
			mFragmentManager = activity.getSupportFragmentManager();
		}

		public void apply(BottomController2 controller) {
			controller.mTvTitle = mTvTitle;
			controller.mTvContentColorRes = mTvContentColorRes;
			controller.mList = mList;
			controller.mTvContent2Weight = mTvContent2Weight;
			controller.mBottomHeight = mBottomHeight;
			controller.mClickListener = mClickListener;
			controller.mFragmentManager = mFragmentManager;
			controller.mType=mType;
		}
	}

}
