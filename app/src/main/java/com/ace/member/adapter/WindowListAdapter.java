package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;

import java.util.List;
import java.util.Map;


public class WindowListAdapter extends BaseAdapter {

	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;

	public WindowListAdapter(List<Map<String, Object>> data, Context context) {
		mData = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		try {
			ViewHolder vh;
			if (view == null) {
				view = mInflater.inflate(R.layout.view_deposit_window_list_item, null);
				vh = new ViewHolder(view);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			vh.tvAccount.setText(mData.get(position).get("name").toString());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return view;
	}

	public class ViewHolder {
		TextView tvAccount;
		LinearLayout llItemLine;

		ViewHolder(View view) {
			tvAccount = (TextView) view.findViewById(R.id.tv_company);
			llItemLine = (LinearLayout) view.findViewById(R.id.branch_method_item_line);
		}
	}
}
