package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.Transaction;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TransactionListAdapter extends BaseAdapter {

	private final Context mContext;
	private List<Transaction> mData;
	private LayoutInflater mInflater;
	private IMyViewOnClickListener mClickListener;

	public TransactionListAdapter(List<Transaction> data, Context context, IMyViewOnClickListener listener) {
		mData = data;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mClickListener = listener;
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
				convertView = mInflater.inflate(R.layout.view_transaction_list_item, null);
				holder = new TransactionListAdapter.ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Transaction transactionData = mData.get(position);
			holder.mTvDate.setText(transactionData.getDate().substring(0, 7));
			if (transactionData.isFlagSameDate()) {
				holder.mLlTotal.setVisibility(View.GONE);
				holder.mLine.setVisibility(View.VISIBLE);
			} else {
				holder.mLine.setVisibility(View.GONE);
				holder.mLlTotal.setVisibility(View.VISIBLE);
				String total = transactionData.getTotal();
				holder.mTvTotal.setMoney(AppGlobal.USD, total);
				if (Utils.strToDouble(total) < 0){
					holder.mTvTotal.setTextColor(Utils.getColor(R.color.clr_amount_red));
				}else {
					holder.mTvTotal.setTextColor(Utils.getColor(R.color.clr_primary_separator_title));
				}
			}
			int subType = transactionData.getSubType();
			int subTypeIcon = TransactionType.getSubTypeIcon(subType);
			holder.mIvIcon.setImageResource(subTypeIcon);
			String subTypeName = TransactionType.getSubTypeName(mContext, subType);
			holder.mTvType.setText(subTypeName);
			String currency = transactionData.getCurrency();
			String amount = transactionData.getAmount();
			if (Utils.strToDouble(amount) < 0) {
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
			} else {
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_tv_balance));
			}
			holder.mTvAmount.setMoney(currency, amount);
			holder.mRlContent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mClickListener.onItemClick(mData, v, position);
				}
			});
			holder.mTvTime.setText(transactionData.getTime().substring(5, 16));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public class ViewHolder {

		@BindView(R.id.horizontal_line)
		View mLine;

		@BindView(R.id.ll_total)
		LinearLayout mLlTotal;

		@BindView(R.id.rl_content)
		RelativeLayout mRlContent;

		@BindView(R.id.tv_date)
		TextView mTvDate;

		@BindView(R.id.tv_total)
		MoneyTextView mTvTotal;

		@BindView(R.id.tv_type)
		TextView mTvType;

		@BindView(R.id.tv_amount)
		MoneyTextView mTvAmount;

		@BindView(R.id.tv_time)
		TextView mTvTime;

		@BindView(R.id.iv_icon)
		AppCompatImageView mIvIcon;

		@BindView(R.id.rl_remark)
		RelativeLayout mRlRemark;

		@BindView(R.id.tv_remark)
		TextView mTvRemark;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}
}
