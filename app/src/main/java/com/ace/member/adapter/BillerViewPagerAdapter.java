package com.ace.member.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ace.member.main.third_party.bill_payment.fragment.BillerFragment;

import java.util.List;

public class BillerViewPagerAdapter extends FragmentPagerAdapter {

	private String[] titles;
	public BillerViewPagerAdapter(FragmentManager fm, String[] titles) {
		super(fm);
		this.titles=titles;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return   this.titles[position];
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f=null;
		switch (position){
			case 0:
				f= BillerFragment.newInstance(1);
				break;
			case 1:
				f= BillerFragment.newInstance(2);
				break;
			case 2:
				f= BillerFragment.newInstance(3);
				break;
			case 3:
				f= BillerFragment.newInstance(4);
				break;
		}
		return f;
	}

	@Override
	public int getCount() {
		return this.titles.length;
	}
}
