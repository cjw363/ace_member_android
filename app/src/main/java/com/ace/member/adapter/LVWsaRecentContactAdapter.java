package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.EdcBill;
import com.ace.member.bean.WsaBill;
import com.og.utils.FileUtils;

import java.util.List;


public class LVWsaRecentContactAdapter extends BaseAdapter {
	private List<WsaBill> mList;
	private LayoutInflater mInflater;

	public LVWsaRecentContactAdapter(Context context, List<WsaBill> list) {
		mList = list;
		mInflater = LayoutInflater.from(context);
	}

	public void setList(List<WsaBill> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public WsaBill getItem(int position) {
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
		TextView mTvName;
		TextView mTvPhone;

		public ViewHolder(View view) {
			mTvName = (TextView) view.findViewById(R.id.tv_name);
			mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
		}
	}
}
