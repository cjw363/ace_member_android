package com.ace.member.adapter;


import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.GlideUtil;
import com.og.utils.FileUtils;

import java.util.List;

public class GVChatInfoAdapter extends BaseAdapter {
	private List<String> mPortraitList;
	private List<String> mNameList;
	private LayoutInflater mInflater;

	public GVChatInfoAdapter(List<String> portraitList, List<String> nameList) {
		mInflater = LayoutInflater.from(BaseApplication.getContext());
		this.mPortraitList = portraitList;
		this.mNameList = nameList;
	}

	@Override
	public int getCount() {
		return mNameList.size() + 1;
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
				convertView = mInflater.inflate(R.layout.view_gv_chat_info, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == getCount() - 1){
				holder.imageView.setImageResource(R.drawable.ic_friends_chat_add);
			}else {
				GlideUtil.loadNormalPortrait(BaseApplication.getContext(), mPortraitList.get(position), holder.imageView);
			}
			if (!TextUtils.isEmpty(mNameList.get(position))) {
				holder.textView.setText(mNameList.get(position));
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	private class ViewHolder {
		AppCompatImageView imageView;
		TextView textView;

		ViewHolder(View view) {
			imageView = (AppCompatImageView) view.findViewById(R.id.iv_icon);
			textView = (TextView) view.findViewById(R.id.tv_name);
		}
	}

	public void setData(List<String> portraitList, List<String> nameList) {
		this.mPortraitList = portraitList;
		this.mNameList = nameList;
		notifyDataSetChanged();
	}
}
