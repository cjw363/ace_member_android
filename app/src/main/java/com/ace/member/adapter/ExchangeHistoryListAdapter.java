package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.main.me.exchange.recent.ExchangeHistory;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExchangeHistoryListAdapter extends BaseAdapter {

	private final Context mContext;
	private List<ExchangeHistory> mData;
	private LayoutInflater mInflater;

	public ExchangeHistoryListAdapter(List<ExchangeHistory> data, Context context) {
		mData = data;
		mContext = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.activity_exchange_history_list_item, null);
				holder = new ExchangeHistoryListAdapter.ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ExchangeHistory historyData = mData.get(position);
			holder.mTvDate.setText(historyData.getTime().substring(0, 7));
			if (historyData.isFlagSameDate()) {
				holder.mLlDate.setVisibility(View.GONE);
			} else {
				holder.mLlDate.setVisibility(View.VISIBLE);
			}
			String sourceAmountStr = Utils.format(historyData.getAmountSource(), 2) + " " + historyData
				.getCurrencySource();
			String targetAmountStr = Utils.format(historyData.getAmountTarget(), 2) + " " + historyData
				.getCurrencyTarget();
			holder.mTvSourceAmount.setText(sourceAmountStr);
			holder.mTvTargetAmount.setText(targetAmountStr);
			holder.mTvTime.setText(historyData.getTime().substring(5, 16));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public class ViewHolder {

		@BindView(R.id.ll_date)
		LinearLayout mLlDate;

		@BindView(R.id.ll_content_1)
		LinearLayout mLlContent1;

		@BindView(R.id.ll_content_2)
		LinearLayout mLlContent2;

		@BindView(R.id.tv_date)
		TextView mTvDate;

		@BindView(R.id.tv_target_amount)
		TextView mTvTargetAmount;

		@BindView(R.id.tv_source_amount)
		TextView mTvSourceAmount;

		@BindView(R.id.tv_exchange_history_list_item_time)
		TextView mTvTime;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}
}
