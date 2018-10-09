package com.ace.member.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;

public class MeAdapter extends BaseAdapter{

	private Context mContext;
	private int[] mIconList;
	private int[] mIconNameList;

	public MeAdapter(Context context, int[] icon, int[] iconName) {
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
		return  mIconList[i];
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
				view = LayoutInflater.from(mContext).inflate(R.layout.view_me_small_icon, null);
				viewHolder = new ViewHolder();
				viewHolder.iconView = (ImageView) view.findViewById(R.id.iv_icon);
				viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			Drawable icon = ContextCompat.getDrawable(mContext, mIconList[i]);
			String name=mContext.getResources().getString(mIconNameList[i]);
			if (viewHolder != null) {
				viewHolder.iconView.setImageDrawable(icon);
				viewHolder.tvName.setText(name);
			}
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
		}
		return view;
	}

	private class ViewHolder {
		ImageView iconView;
		TextView tvName;
	}
}
