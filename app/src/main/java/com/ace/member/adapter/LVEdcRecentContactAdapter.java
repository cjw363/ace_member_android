package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.EdcBill;
import com.og.utils.FileUtils;

import java.util.List;


public class LVEdcRecentContactAdapter extends BaseAdapter {
	private List<EdcBill> mList;
	private LayoutInflater mInflater;

	public LVEdcRecentContactAdapter(Context context, List<EdcBill> list) {
		mList = list;
		mInflater = LayoutInflater.from(context);
	}

	public void setList(List<EdcBill> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public EdcBill getItem(int position) {
		return mList != null ? mList.get(position) : null;
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
				convertView = mInflater.inflate(R.layout.view_edcwsa_recent_contact_item, parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mTvPhone.setText(getItem(position).getPhone());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		TextView mTvPhone;

		public ViewHolder(View view) {
			mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
		}
	}
}
