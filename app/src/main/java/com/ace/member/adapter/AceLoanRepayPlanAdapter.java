package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ACELoanRepayBean;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class AceLoanRepayPlanAdapter extends BaseAdapter {
	private List<ACELoanRepayBean> mList;
	private Context mContext;

	public AceLoanRepayPlanAdapter(Context context, List<ACELoanRepayBean> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.view_ace_loan_return_loan_item, null);
			viewHolder.mTvTerm = (TextView) convertView.findViewById(R.id.tv_term);
				viewHolder.mTvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
				viewHolder.mIvAgree = (AppCompatImageView) convertView.findViewById(R.id.iv_agree);
				viewHolder.mIvArrowRight = (AppCompatImageView) convertView.findViewById(R.id.iv_arrow_right);
				viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.mVLine = (View) convertView.findViewById(R.id.v_line);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mTvTime.setVisibility(View.GONE);
			ACELoanRepayBean bean = mList.get(position);
			viewHolder.mTvTerm.setTextColor(Utils.getColor(R.color.clr_tv_primary));
			viewHolder.mTvTerm.setVisibility(View.VISIBLE);
			viewHolder.mTvTerm.setText(bean.getPlanDate());
			String currency = bean.getCurrency();
			double amount = bean.getCapitalAmount() + bean.getPlanAmount();
			viewHolder.mTvAmount.setText(Utils.format(amount, currency) + " " + currency);
			viewHolder.mIvAgree.setVisibility(View.GONE);
			viewHolder.mIvArrowRight.setVisibility(View.GONE);
			int status = bean.getStatus();
			if (status == 2) {
				viewHolder.mTvTime.setTextColor(Utils.getColor(R.color.clr_tv_hint));
				viewHolder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_tv_hint));
			}
			if (position == getCount() - 1) {
				viewHolder.mVLine.setVisibility(View.GONE);
			} else {
				viewHolder.mVLine.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		TextView mTvTerm;
		TextView mTvAmount;
		//		TextView mTvInterest;
		AppCompatImageView mIvAgree;
		AppCompatImageView mIvArrowRight;
		TextView mTvTime;
		View mVLine;
	}
}
