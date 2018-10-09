package com.ace.member.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> mFragmentArrayList;

	public CommonFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
		super(fm);
		mFragmentArrayList = fragmentArrayList;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentArrayList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentArrayList!=null?mFragmentArrayList.size():0;
	}
}
