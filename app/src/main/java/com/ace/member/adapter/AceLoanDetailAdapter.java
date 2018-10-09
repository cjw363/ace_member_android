package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ACELoanDetailBean;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;


public class AceLoanDetailAdapter extends BaseAdapter {

	private Context mContext;
	private List<ACELoanDetailBean> mList;

	public AceLoanDetailAdapter(Context context, List<ACELoanDetailBean> list) {
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
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.view_ace_loan_return_loan_item, null);
				viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
			viewHolder.tvTerm = (TextView) convertView.findViewById(R.id.tv_term);
				viewHolder.IvArrowRight = (AppCompatImageView) convertView.findViewById(R.id.iv_arrow_right);
				viewHolder.IvAgree = (AppCompatImageView) convertView.findViewById(R.id.iv_agree);
				viewHolder.vLine = (View) convertView.findViewById(R.id.v_line);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ACELoanDetailBean bean = mList.get(position);
			viewHolder.IvAgree.setSelected(bean.getCheck());
			if (bean.getBatch()) {
				viewHolder.IvArrowRight.setVisibility(View.GONE);
				viewHolder.IvAgree.setVisibility(View.VISIBLE);
			} else {
				viewHolder.IvArrowRight.setVisibility(View.VISIBLE);
				viewHolder.IvAgree.setVisibility(View.GONE);
			}
			String loanTime = bean.getLoanDate();
			viewHolder.tvTime.setText(loanTime);
			String currency = bean.getCurrency();
			double amount = bean.getLoan();
			String amt = Utils.format(amount, currency) + " " + currency;
			viewHolder.tvAmount.setText(amt);
			int term = bean.getTerm();
			String termStr = "";
			if (term > 1) {
				termStr = Utils.getString(R.string.term_x_months);
			} else {
				termStr = Utils.getString(R.string.term_x_month);
			}
			String str = String.format(termStr, term);
			viewHolder.tvTerm.setText(str);
			if (position == getCount() - 1) {
				viewHolder.vLine.setVisibility(View.GONE);
			} else {
				viewHolder.vLine.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvTime;
		TextView tvAmount;
		TextView tvTerm;
		AppCompatImageView IvArrowRight;
		AppCompatImageView IvAgree;
		View vLine;
	}
}
