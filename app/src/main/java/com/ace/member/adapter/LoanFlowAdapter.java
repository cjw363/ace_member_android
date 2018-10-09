package com.ace.member.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.SalaryLoanFlow;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoanFlowAdapter extends BaseAdapter {
	private Context mContext;
	private List<SalaryLoanFlow> mList;
	private SparseArray<Integer> mTypeArray;
	private boolean mFlagSmallRecord = true;

	public LoanFlowAdapter(Context context, List<SalaryLoanFlow> list) {
		mContext = context;
		mList = list;
		mTypeArray = new SparseArray<>(2);
		mTypeArray.put(AppGlobal.APPLICATION_TYPE_2_DEPOSIT, TransactionType.TRANSACTION_SUB_TYPE_240_SALARY_LOAN);
		mTypeArray.put(AppGlobal.APPLICATION_TYPE_3_WITHDRAW, TransactionType.TRANSACTION_SUB_TYPE_241_RETURN_SALARY_LOAN);
	}

	public void setList(List<SalaryLoanFlow> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public SalaryLoanFlow getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return mTypeArray.get(getItem(position).getType());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder;
			int layout = R.layout.view_loan_flow_record_small;
			if (!mFlagSmallRecord) layout = R.layout.view_loan_flow_record;
			if (convertView == null) {
				convertView = View.inflate(BaseApplication.getContext(), layout, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SalaryLoanFlow record = getItem(position);
			int type = record.getType();
			String amount = record.getAmount();
			holder.mTvType.setText(TransactionType.getSubTypeName(mContext, mTypeArray.get(type)));
			holder.mTvTime.setText(record.getTime());
			String currency = record.getCurrency();
			holder.mTvAmount.setMoney(currency, amount);
			if (Utils.strToDouble(amount) < 0){
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
			}else {
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.black));
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	class ViewHolder {
		@BindView(R.id.tv_time)
		TextView mTvTime;

		@BindView(R.id.tv_type)
		TextView mTvType;

		@BindView(R.id.tv_amount)
		MoneyTextView mTvAmount;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}

	public void setFlagSmallRecord(boolean flagRecord){
		mFlagSmallRecord = flagRecord;
	}

}
