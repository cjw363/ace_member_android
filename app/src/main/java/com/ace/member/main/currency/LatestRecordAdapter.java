package com.ace.member.main.currency;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BalanceRecord;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.TransactionType;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LatestRecordAdapter extends BaseAdapter {
	private List<BalanceRecord> mList;
	private String mCurrency;
	private SparseArray<String> mTypeArray;
	private SparseArray<String> mStatusArray;

	public LatestRecordAdapter(List<BalanceRecord> list, String currency) {
		mList = list;
		mCurrency = currency;
		mTypeArray = new SparseArray<>(2);
		mStatusArray = new SparseArray<>(4);

		mTypeArray.put(AppGlobal.APPLICATION_TYPE_2_DEPOSIT, Utils.getString(R.string.deposit));
		mTypeArray.put(AppGlobal.APPLICATION_TYPE_3_WITHDRAW, Utils.getString(R.string.withdraw));

		mStatusArray.put(AppGlobal.STATUS_1_PENDING, Utils.getString(R.string.pending));
		mStatusArray.put(AppGlobal.STATUS_3_COMPLETED, Utils.getString(R.string.completed));
		mStatusArray.put(AppGlobal.STATUS_4_CANCELLED, Utils.getString(R.string.cancelled));
	}

	public void setList(List<BalanceRecord> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public BalanceRecord getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getType();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(BaseApplication.getContext(), R.layout.view_balance_record, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BalanceRecord record=getItem(position);
			int type = record.getType();
			holder.mTvType.setText(mTypeArray.get(type));

			int status = record.getStatus();
			holder.mTvStatus.setText(mStatusArray.get(status));
			holder.mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
			holder.mTvBankTransactionTime.setText(record.getTime());


			String amount = Utils.format(record.getAmount(), mCurrency);
			if (status == AppGlobal.STATUS_4_CANCELLED) {
				int cancelColor=Utils.getColor(R.color.color_canceled);
				holder.mTvType.setTextColor(cancelColor);
				holder.mTvAmount.setText(amount.replaceFirst("-", "") + " " + mCurrency);
				holder.mTvAmount.setTextColor(cancelColor);
				holder.mTvBankTransactionTime.setTextColor(cancelColor);
			} else {
				holder.mTvType.setTextColor(Utils.getColor(R.color.black));
				holder.mTvBankTransactionTime.setTextColor(Utils.getColor(R.color.gray));
				if (type == AppGlobal.APPLICATION_TYPE_2_DEPOSIT) {
					holder.mTvAmount.setText(amount + " " + mCurrency);
					holder.mTvAmount.setTextColor(Utils.getColor(R.color.black));
				} else if (type == AppGlobal.APPLICATION_TYPE_3_WITHDRAW) {
					holder.mTvAmount.setText(amount + " " + mCurrency);
					holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	class ViewHolder {
		@BindView(R.id.tv_bank_transaction_time)
		TextView mTvBankTransactionTime;

		@BindView(R.id.tv_status)
		TextView mTvStatus;

		@BindView(R.id.tv_type)
		TextView mTvType;

		@BindView(R.id.tv_amount)
		TextView mTvAmount;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}
}
