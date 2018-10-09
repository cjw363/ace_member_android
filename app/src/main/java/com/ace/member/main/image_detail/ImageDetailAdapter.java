package com.ace.member.main.image_detail;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;

import java.util.List;

public class ImageDetailAdapter extends FragmentStatePagerAdapter {
	private List<String> mList;

	public ImageDetailAdapter(FragmentManager fm, List<String> list) {
		super(fm);
		mList = list;
	}

	public void setList(List<String> list) {
		this.mList = list;
	}

	public List<String> getList() {
		return mList;
	}

	@Override
	public PhotoViewFragment getItem(int position) {
		PhotoViewFragment photoViewFragment = new PhotoViewFragment();
		photoViewFragment.getArguments().putString(M.ImagePicker.IMAGE, mList.get(position));
		return photoViewFragment;
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
