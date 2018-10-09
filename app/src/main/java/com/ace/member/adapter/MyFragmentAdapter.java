package com.ace.member.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;

import com.ace.member.main.friends.FriendsFragment;
import com.ace.member.main.home.HomeFragment;
import com.ace.member.main.me.MeFragment;
import com.ace.member.main.third_party.ThirdPartyFragment;


public class MyFragmentAdapter extends FragmentPagerAdapter {
	private SparseArrayCompat<Fragment> mFragmentSparseArrayCompat;
	public MyFragmentAdapter(FragmentManager fm) {
		super(fm);
		mFragmentSparseArrayCompat=new SparseArrayCompat<>(4);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = mFragmentSparseArrayCompat.get(position);
		if (fragment == null) {
			switch (position) {
				case 0:
					fragment = new HomeFragment();
					break;
				case 1:
					fragment = new FriendsFragment();
					break;
				case 2:
					fragment = new ThirdPartyFragment();
					break;
				case 3:
					fragment = new MeFragment();
					break;
			}
			mFragmentSparseArrayCompat.put(position, fragment);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 4;
	}
}
