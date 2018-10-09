package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BillPaymentBean;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class BillPaymentHistoryAdapter extends BaseAdapter {
	private List<BillPaymentBean> mList;
	private LayoutInflater mInflater;

	public BillPaymentHistoryAdapter(Context context, List<BillPaymentBean> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<BillPaymentBean> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public BillPaymentBean getBillPaymentBean(int position) {
		return mList.get(position);
	}

	public void addList(List<BillPaymentBean> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_bill_payment_history_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
				viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			BillPaymentBean bean = mList.get(position);
			viewHolder.tvNumber.setText(bean.getNumber());
			viewHolder.tvTime.setText(bean.getTime());
			double total = bean.getAmount();
			viewHolder.tvAmount.setText("-" + Utils.format(total, 2) + " " + bean.getCurrency());
			return convertView;

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	protected class ViewHolder {
		TextView tvNumber;
		TextView tvTime;
		TextView tvAmount;
	}
}
