package com.ace.member.view;

import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.Utils;

public class TabItem {

	private View mView;

	private int mSelectorRes;

	private int mTitleRes;

	private SpannableString mTitleString;

	private Class<? extends Fragment> mFragmentClass;

	private Bundle mBundle;

	private View mVDot;


	private TabItem(Builder builder) {
		mSelectorRes = builder.mSelectorRes;
		mTitleRes = builder.mTitleRes;
		mFragmentClass = builder.mFragmentClass;
		mTitleString = builder.mTitleString;
		initBundle();
		initView();
	}

	private void initBundle() {
		if (this.mBundle == null) {
			mBundle = new Bundle();
			mBundle.putString("TAG", mTitleRes == 0 ? null : Utils.getString(mTitleRes));
		}
	}

	private void initView() {
		mView = Utils.inflate(R.layout.view_tab_item);
		AppCompatImageView ivTabItem = (AppCompatImageView) mView.findViewById(R.id.iv_tab_item);
		TextView tvTabItem = (TextView) mView.findViewById(R.id.tv_tab_item);
		mVDot = mView.findViewById(R.id.v_dot);
		if (mSelectorRes != 0) ivTabItem.setBackgroundResource(mSelectorRes);

		if (mTitleRes != 0) tvTabItem.setText(mTitleRes);
		if (mTitleString != null) tvTabItem.setText(mTitleString);
	}

	public void setEnableDot(boolean enableDot) {
		mVDot.setVisibility(enableDot ? View.VISIBLE : View.GONE);
	}

	public Class<? extends Fragment> getFragmentClass() {
		return mFragmentClass;
	}

	public String getTitle() {
		return mTitleRes == 0 ? mTitleString.toString() : Utils.getString(mTitleRes);
	}

	public View getView() {
		return mView;
	}

	public Bundle getBundle() {
		return mBundle;
	}

	public static class Builder {
		int mSelectorRes;

		int mTitleRes;

		SpannableString mTitleString;

		Class<? extends Fragment> mFragmentClass;

		public Builder(Class<? extends Fragment> fragmentClass) {
			mFragmentClass = fragmentClass;
		}

		public Builder setSelectorRes(int selectorRes) {
			mSelectorRes = selectorRes;
			return this;
		}

		public Builder setTitleRes(int titleRes) {
			mTitleRes = titleRes;
			return this;
		}

		public Builder setTitle(SpannableString string) {
			mTitleString = string;
			return this;
		}

		public TabItem build() {
			return new TabItem(this);
		}
	}
}
