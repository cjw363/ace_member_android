package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BillPaymentBean;
import com.ace.member.bean.BillerConfig;
import com.og.LibApplication;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class BillPaymentRuleAdapter extends BaseAdapter {

	private List<BillerConfig> mList;

	public BillPaymentRuleAdapter(List<BillerConfig> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		try {
			ViewHolder viewHolder = null;
			if (view == null) {
				view = LayoutInflater.from(LibApplication.getContext())
						.inflate(R.layout.view_bill_payment_rule_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvSplit = (TextView) view.findViewById(R.id.tv_split);
				viewHolder.tvFee = (TextView) view.findViewById(R.id.tv_fee);
				viewHolder.vBottom = (View) view.findViewById(R.id.v_bottom);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			BillerConfig b = mList.get(i);
			String currency = b.getCurrency();
			int type = b.getType();
			viewHolder.tvSplit.setText(Utils.format(b.getAmount(), currency) + " " + currency);
			if (type == 2) {
				viewHolder.tvFee.setText(Utils.format(b.getFee(), currency) + " " + currency);
			} else {
				viewHolder.tvFee.setText(Utils.format(b.getPercent(), 2) + "%");
			}
			if (i == getCount()) {
				viewHolder.vBottom.setVisibility(View.GONE);
			} else {
				viewHolder.vBottom.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return view;
	}

	private class ViewHolder {
		TextView tvSplit;
		TextView tvFee;
		View vBottom;
	}

}
