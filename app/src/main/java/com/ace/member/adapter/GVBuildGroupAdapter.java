package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ace.member.R;
import com.ace.member.bean.ContactInfo;
import com.ace.member.utils.GlideUtil;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.FileUtils;

import java.util.List;

public class GVBuildGroupAdapter extends BaseAdapter {

	private final Context mContext;
	private List<ContactInfo> mList;
	private final LayoutInflater mInflater;

	public GVBuildGroupAdapter(Context context, List<ContactInfo> contactList) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mList = contactList;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public ContactInfo getItem(int position) {
		return mList.get(position);
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
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.view_gv_build_group_item, parent, false);
				holder.ivHead = (RoundRectImageView) convertView.findViewById(R.id.iv_head);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ContactInfo contact = mList.get(position);
			GlideUtil.loadThumbnailPortrait(mContext, contact.getPortrait(), holder.ivHead);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public void setData(List<ContactInfo> contactList) {
		this.mList = contactList;
		notifyDataSetChanged();
	}

	class ViewHolder {
		RoundRectImageView ivHead;
	}
}
