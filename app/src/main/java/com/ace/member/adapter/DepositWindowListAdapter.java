package com.ace.member.adapter;

import android.content.Context;
import android.view.Gravity;
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



public class DepositWindowListAdapter extends BaseAdapter {

	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;

	public DepositWindowListAdapter(List<Map<String, Object>> data, Context context) {
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
				vh = new ViewHolder();
				view = mInflater.inflate(R.layout.view_window_list_item, null);
				vh.tvAccount = (TextView) view.findViewById(R.id.tv_company);
				vh.llItemLine = (LinearLayout) view.findViewById(R.id.branch_method_item_line);
				vh.llItemLine.setGravity(Gravity.CENTER_VERTICAL);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			vh.tvAccount.setText(mData.get(position).get("name").toString());

			return view;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	public class ViewHolder {
		TextView tvAccount;
		LinearLayout llItemLine;
	}
}
