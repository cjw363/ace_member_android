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
import com.ace.member.bean.Statement;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatementAdapter extends BaseAdapter {

	private final Context mContext;
	private List<Statement> mData;
	private LayoutInflater mInflater;

	public StatementAdapter(List<Statement> data, Context context) {
		mData = data;
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
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
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		try {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_statement_item, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Statement statementData = mData.get(position);
			String subType = statementData.getSubType();
			String currency = statementData.getCurrency();
			String amount = AppUtils.simplifyAmount(currency, statementData.getAmount());
			String date = statementData.getDate();
			String subTypeName = TransactionType.getSubTypeName(mContext, Integer.valueOf(subType));
			int typeImage = TransactionType.getSubTypeIcon(Integer.valueOf(subType));
			String balance = AppUtils.simplifyAmount(currency, statementData.getBalance());
			viewHolder.mDate.setText(date);
			viewHolder.mSubType.setText(subTypeName);
			viewHolder.mIvType.setImageResource(typeImage);
			viewHolder.mAmount.setMoney(currency, amount);
			viewHolder.mBalance.setMoney(currency, balance);
			if (Utils.strToDouble(amount) < 0) {
				viewHolder.mAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
			} else {
				viewHolder.mAmount.setTextColor(Utils.getColor(R.color.clr_common_content));
			}
			if (statementData.isDateTitle()) {
				viewHolder.mRlBalance.setVisibility(View.GONE);
			} else {
				viewHolder.mRlBalance.setVisibility(View.VISIBLE);
			}
			if (statementData.isFlagSameDate()) {
				viewHolder.mLlDate.setVisibility(View.GONE);
			} else {
				viewHolder.mLlDate.setVisibility(View.VISIBLE);
			}
			if (statementData.isDateEnd()) {
				viewHolder.mRlBalance.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mRlBalance.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public class ViewHolder {
		@BindView(R.id.tv_date)
		TextView mDate;

		@BindView(R.id.tv_sub_type)
		TextView mSubType;

		@BindView(R.id.tv_amount)
		MoneyTextView mAmount;

		@BindView(R.id.tv_balance)
		MoneyTextView mBalance;

		@BindView(R.id.rl_balance)
		RelativeLayout mRlBalance;

		@BindView(R.id.ll_date)
		LinearLayout mLlDate;

		@BindView(R.id.iv_type)
		AppCompatImageView mIvType;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}

	public void setData(List<Statement> data) {
		this.mData = data;
		notifyDataSetChanged();
	}
}
