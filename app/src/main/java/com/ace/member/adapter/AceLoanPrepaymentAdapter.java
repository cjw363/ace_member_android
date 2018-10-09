package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ACELoanPrepaymentBean;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class AceLoanPrepaymentAdapter extends BaseAdapter {

	private Context mContext;
	private List<ACELoanPrepaymentBean> mList;

	public AceLoanPrepaymentAdapter(Context context, List<ACELoanPrepaymentBean> list) {
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
						.inflate(R.layout.view_ace_loan_prepayment_item, null);
				viewHolder.tvLoanDate = (TextView) convertView.findViewById(R.id.tv_loan_date);
				viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
				viewHolder.tvInterest = (TextView) convertView.findViewById(R.id.tv_interest);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ACELoanPrepaymentBean bean = mList.get(position);
			viewHolder.tvLoanDate.setText(bean.getLoanDate());
			String currency=bean.getCurrency();
			viewHolder.tvAmount.setText(Utils.format(bean.getAmount(), currency)+" "+currency);
			viewHolder.tvInterest.setText(Utils.format(bean.getInterest(), currency)+" "+currency);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvLoanDate;
		TextView tvAmount;
		TextView tvInterest;
	}
}
