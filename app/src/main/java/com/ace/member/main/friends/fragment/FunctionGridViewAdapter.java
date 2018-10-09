package com.ace.member.main.friends.fragment;


import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;

public class FunctionGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private int[] mIconList;
	private int[] mIconNameList;

	FunctionGridViewAdapter(Context context, int[] icon, int[] iconName) {
		mContext = context;
		mIconList = icon;
		mIconNameList = iconName;
	}

	@Override
	public int getCount() {
		return mIconList.length;
	}

	@Override
	public Object getItem(int i) {
		return mIconList[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		try {
			ViewHolder viewHolder;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.view_chat_function, null);
				viewHolder = new ViewHolder();
				viewHolder.iconView = (AppCompatImageView) view.findViewById(R.id.iv_icon);
				viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			if (viewHolder != null) {
				viewHolder.iconView.setImageResource(mIconList[i]);
				viewHolder.tvName.setText(mIconNameList[i]);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return view;
	}

	private class ViewHolder {
		AppCompatImageView iconView;
		TextView tvName;
	}
}
