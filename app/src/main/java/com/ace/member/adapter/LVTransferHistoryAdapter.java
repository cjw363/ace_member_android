package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.TransferRecent;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class LVTransferHistoryAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<TransferRecent> mList;
	private int mMemberID;

	public LVTransferHistoryAdapter(Context context, List<TransferRecent> list, int memberID) {
		mInflater = LayoutInflater.from(context);
		mList = list;
		mMemberID = memberID;
	}

	public void setList(List<TransferRecent> list) {
		mList = list;
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
				convertView = mInflater.inflate(R.layout.view_transfer_history, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			TransferRecent history = getItem(position);
			double amount = history.getAmount();
			String amountStr;
			if (history.getUserID() == mMemberID) {
				holder.mTvPhone.setText(history.getSourcePhone());
				amountStr = "+" + AppUtils.simplifyAmount(history.getCurrency(), String.valueOf(amount)) + " " + history
					.getCurrency();
				holder.mTvAmount.setText(amountStr);
			} else {
				holder.mTvPhone.setText(history.getTargetPhone());
				amountStr = "-" + AppUtils.simplifyAmount(history.getCurrency(), String.valueOf(amount)) + " " + history
					.getCurrency();
				holder.mTvAmount.setText(amountStr);
			}
			holder.mTvDate.setText(Utils.formatDateToYearMonthDay(history.getTime()));
			holder.mTvStatus.setText(AppUtils.getStatus(history.getStatus()));
			holder.mTvStatus.setTextColor(ColorUtil.getStatusColor(history.getStatus()));
			if (history.getStatus() == AppGlobal.STATUS_4_CANCELLED) {
				holder.mTvPhone.setTextColor(Utils.getColor(R.color.color_canceled));
				holder.mTvDate.setTextColor(Utils.getColor(R.color.color_canceled));
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.color_canceled));
			} else {
				holder.mTvPhone.setTextColor(Utils.getColor(R.color.clr_common_title));
				holder.mTvDate.setTextColor(Utils.getColor(R.color.clr_list_time));
				if (history.getUserID() == mMemberID) {
					holder.mTvAmount.setTextColor(Utils.getColor(R.color.black));
				} else {
					holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
				}
			}

			if (history.isFlagSameDate()) {
				holder.mTvYearMonth.setVisibility(View.GONE);
			} else {
				holder.mTvYearMonth.setText(Utils.formatDateToYearMonth(history.getTime()));
				holder.mTvYearMonth.setVisibility(View.VISIBLE);
			}
			if (history.isDateEnd()) {
				holder.mDivider.setVisibility(View.GONE);
			} else {
				holder.mDivider.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	public static class ViewHolder {
		TextView mTvYearMonth;
		TextView mTvPhone;
		TextView mTvDate;
		TextView mTvAmount;
		TextView mTvStatus;
		View mDivider;

		public ViewHolder(View view) {
			mTvYearMonth = (TextView) view.findViewById(R.id.tv_year_month);
			mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
			mTvDate = (TextView) view.findViewById(R.id.tv_date);
			mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
			mTvStatus = (TextView) view.findViewById(R.id.tv_status);
			mDivider = view.findViewById(R.id.v_divider);
		}
	}
}
