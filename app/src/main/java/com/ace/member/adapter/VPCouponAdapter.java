package com.ace.member.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.main.currency.coupon.available.AvailableCouponFragment;
import com.ace.member.main.currency.coupon.unvailable.UnAvailableCouponFragment;
import com.og.utils.Utils;


public class VPCouponAdapter extends FragmentPagerAdapter {

	private BaseFragment[] mFragments;
	private String[] mTitles;

	public VPCouponAdapter(FragmentManager fm) {
		super(fm);

		mFragments = new BaseFragment[]{new AvailableCouponFragment(), new UnAvailableCouponFragment()};
		mTitles = Utils.getStringArray(R.array.coupon_titles);

	}


	@Override
	public int getCount() {
		return mFragments.length;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments[position];
	}


	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
}
