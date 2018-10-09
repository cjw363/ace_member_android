package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BalanceFlow;
import com.ace.member.bean.Currency;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;


public class LVBalanceFlowAdapter extends BaseAdapter {
	private List<BalanceFlow> mList;
	private LayoutInflater mInflater;
	private String mCurrency;
	private double mRunningBalance;

	public LVBalanceFlowAdapter(Context context, List<BalanceFlow> list,String currency) {
		mInflater = LayoutInflater.from(context);
		mList = list;
		mCurrency=currency;
		mRunningBalance=AppUtils.getBalance(mCurrency);
	}

	public void setList(List<BalanceFlow> list) {
		mList = list;
		mRunningBalance=AppUtils.getBalance(mCurrency);
		notifyDataSetChanged();
	}

	public void addList(List<BalanceFlow> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public BalanceFlow getItem(int position) {
		return mList == null ? null : mList.get(position);
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
				convertView = mInflater.inflate(R.layout.view_flow_item, parent,false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BalanceFlow loanFlow = getItem(position);

			int type = loanFlow.getType();
			double amount = loanFlow.getAmount();

			if (type == AppGlobal.FLOW_TYPE_1_BEGINNING || type==AppGlobal.FLOW_TYPE_2_DEPOSIT) {
				amount = Math.abs(amount);
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.black));
			} else if (type == AppGlobal.FLOW_TYPE_3_WITHDRAW) {
				amount = -Math.abs(amount);
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
			}

			if(position==0){
				loanFlow.setRunningBalance(amount);
				holder.mTvDateTitle.setVisibility(View.VISIBLE);
				holder.mTvDateTitle.setText(DateUtils.getYearMonth(loanFlow.getTime()));
			}else {
				loanFlow.setRunningBalance(mList.get(position-1).getRunningBalance()+amount);
				String date1=getItem(position-1).getTime();
				String date2=loanFlow.getTime();
				if(DateUtils.compareMonth(date1,date2)){
					holder.mTvDateTitle.setVisibility(View.GONE);
				}else {
					holder.mTvDateTitle.setVisibility(View.VISIBLE);
					holder.mTvDateTitle.setText(DateUtils.getYearMonth(loanFlow.getTime()));
				}
			}
			holder.mTvAmount.setText(Utils.format(amount, mCurrency));
			holder.mTvTime.setText(Utils.formatDateToMonthMinute(loanFlow.getTime()));
			holder.mTvBalance.setText(Utils.format(loanFlow.getRunningBalance(), mCurrency));

			holder.vDivider.setVisibility((position==getCount()-1 || !DateUtils.compareMonth(loanFlow.getTime(),getItem(position+1).getTime())?View.GONE:View.VISIBLE));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public static class ViewHolder {
		TextView mTvDateTitle;
		TextView mTvTime;
		TextView mTvAmount;
		TextView mTvBalance;
		View vDivider;

		public ViewHolder(View view) {
			mTvDateTitle= (TextView) view.findViewById(R.id.tv_date_title);
			mTvTime = (TextView) view.findViewById(R.id.tv_date);
			mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
			mTvBalance = (TextView) view.findViewById(R.id.tv_balance);
			vDivider= view.findViewById(R.id.v_divider);
		}
	}
}
