package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.TransferRecent;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class LVTransferRecentAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<TransferRecent> mList;

	public LVTransferRecentAdapter(Context context, List<TransferRecent> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<TransferRecent> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void addList(List<TransferRecent> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public TransferRecent getItem(int position) {
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
				convertView = mInflater.inflate(R.layout.view_transfer_recent, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			TransferRecent recent=getItem(position);
			double amount = recent.getAmount();
			holder.mTvAmount.setMoney(recent.getCurrency(), String.valueOf(amount));
			holder.mTvAmount.setTextColor(Utils.getColor(amount < 0 ? R.color.clr_amount_red : R.color.black));
			holder.mTvPhone.setText(recent.getPhone());
			holder.mTvTime.setText(Utils.formatDateToYearMonthDay(recent.getTime()));
			holder.mTvStatus.setText(AppUtils.getStatus(recent.getStatus()));
			holder.mTvStatus.setTextColor(ColorUtil.getStatusColor(recent.getStatus()));
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	public static class ViewHolder {
		TextView mTvTime;
		TextView mTvPhone;
		MoneyTextView mTvAmount;
		TextView mTvStatus;

		public ViewHolder(View view) {
			mTvTime = (TextView) view.findViewById(R.id.tv_time);
			mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
			mTvAmount = (MoneyTextView) view.findViewById(R.id.tv_amount);
			mTvStatus = (TextView) view.findViewById(R.id.tv_status);
		}
	}
}
