package com.ace.member.adapter;


import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

public class GVMenuAdapter extends BaseAdapter {
	private int[] mMenuDrawable;
	private int[] mMenuTitle;
	private LayoutInflater mInflater;

	public GVMenuAdapter(int[] menuDrawable, int[] menuTitle) {
		this.mMenuDrawable = menuDrawable;
		this.mMenuTitle = menuTitle;
		mInflater = LayoutInflater.from(BaseApplication.getContext());
	}

	@Override
	public int getCount() {
		return mMenuTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_gv_menu, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.imageView.setImageResource(mMenuDrawable[position]);
			holder.textView.setText(Utils.getString(mMenuTitle[position]));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	private class ViewHolder {
		AppCompatImageView imageView;
		TextView textView;

		ViewHolder(View view) {
			imageView = (AppCompatImageView) view.findViewById(R.id.iv_menu);
			textView = (TextView) view.findViewById(R.id.tv_menu_title);
		}
	}
}
