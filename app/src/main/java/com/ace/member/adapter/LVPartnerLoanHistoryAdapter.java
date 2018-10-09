package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.PartnerLoanFlow;
import com.ace.member.utils.AppGlobal;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;


public class LVPartnerLoanHistoryAdapter extends BaseAdapter {

	private List<PartnerLoanFlow> mList;
	private LayoutInflater mInflater;

	public LVPartnerLoanHistoryAdapter(Context context, List<PartnerLoanFlow> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<PartnerLoanFlow> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void addList(List<PartnerLoanFlow> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public PartnerLoanFlow getItem(int position) {
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
				convertView = mInflater.inflate(R.layout.view_partner_loan_history, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PartnerLoanFlow loanFlow = getItem(position);

			int type = loanFlow.getType();
			int amount = loanFlow.getAmount();

			if (type == AppGlobal.APPLICATION_TYPE_2_DEPOSIT) {
				amount = Math.abs(amount);
				holder.mTvType.setText(Utils.getString(R.string.loan));
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.black));
			} else if (type == AppGlobal.APPLICATION_TYPE_3_WITHDRAW) {
				amount = -Math.abs(amount);
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
				holder.mTvType.setText(Utils.getString(R.string.return_loan));
			} else {
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.black));
			}
			holder.mTvAmount.setText(String.format(Utils.format(amount, 2), AppGlobal.USD));
			holder.mTvTime.setText(Utils.formatDateToYearMonthDay(loanFlow.getTime()));

			if(position==0){
				holder.mTvDateTitle.setVisibility(View.VISIBLE);
				holder.mTvDateTitle.setText(DateUtils.getYearMonth(loanFlow.getTime()));
			}else {
				String date1=getItem(position-1).getTime();
				String date2= loanFlow.getTime();
				if(DateUtils.compareMonth(date1,date2)){
					holder.mTvDateTitle.setVisibility(View.GONE);
				}else {
					holder.mTvDateTitle.setVisibility(View.VISIBLE);
					holder.mTvDateTitle.setText(DateUtils.getYearMonth(loanFlow.getTime()));
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public static class ViewHolder {
		TextView mTvDateTitle;
		TextView mTvType;
		TextView mTvTime;
		TextView mTvAmount;

		public ViewHolder(View view) {
			mTvDateTitle= (TextView) view.findViewById(R.id.tv_date_title);
			mTvType = (TextView) view.findViewById(R.id.tv_type);
			mTvTime = (TextView) view.findViewById(R.id.tv_time);
			mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
		}
	}
}
