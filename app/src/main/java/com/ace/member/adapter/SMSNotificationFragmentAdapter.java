package com.ace.member.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.ace.member.base.BaseFragment;
import com.ace.member.sms_notification.first_step.FirstStepFragment;
import com.ace.member.sms_notification.second_step.SecondStepFragment;
import com.ace.member.sms_notification.third_step.ThirdStepFragment;


public class SMSNotificationFragmentAdapter extends FragmentPagerAdapter {
	private SparseArrayCompat<Fragment> mSparseArrayCompat;
	private BaseFragment mCurrentFragment;

	public SMSNotificationFragmentAdapter(FragmentManager fm) {
		super(fm);
		mSparseArrayCompat = new SparseArrayCompat<>(3);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = mSparseArrayCompat.get(position);
		if (fragment == null) {
			switch (position) {
				case 0:
					fragment = new FirstStepFragment();
					break;
				case 1:
					fragment = new SecondStepFragment();
					break;
				case 2:
					fragment = new ThirdStepFragment();
					break;
				default:
					fragment = new FirstStepFragment();
					break;
			}
			mSparseArrayCompat.put(position, fragment);
		}
		return fragment;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		mCurrentFragment = (BaseFragment) object;
	}

	public BaseFragment getPrimaryItem() {
		return mCurrentFragment;
	}

	@Override
	public int getCount() {
		return 3;
	}
}
