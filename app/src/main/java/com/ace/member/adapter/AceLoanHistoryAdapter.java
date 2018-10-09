package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.utils.ColorUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

public class AceLoanHistoryAdapter extends BaseAdapter {

	private Context mContext;
	private List<ACELoanDetailBean> mList;
	private final static int LOAN_STATUS_1_RUNNING=1;
	public AceLoanHistoryAdapter(Context context, List<ACELoanDetailBean> list){
		mContext=context;
		mList=list;
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.view_loan_history_item, null);
			viewHolder.tvTerm = (TextView) convertView.findViewById(R.id.tv_term);
				viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
				viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			ACELoanDetailBean bean=mList.get(position);
			int term=bean.getTerm();
			String termStr="";
			if(term>1){
				termStr=Utils.getString(R.string.term_x_months);
			}else{
				termStr=Utils.getString(R.string.term_x_month);
			}
			String str = String.format(termStr,term);
			viewHolder.tvTerm.setText(str);
			viewHolder.tvTime.setText(bean.getLoanDate());
			int status=bean.getStatus();
			double amount=0;
			String statusStr="";
			if(status==LOAN_STATUS_1_RUNNING){
				statusStr=Utils.getString(R.string.running);
			}else{
				statusStr=Utils.getString(R.string.paid_off);
			}
			viewHolder.tvAmount.setText(Utils.format(bean.getLoan(),2)+" "+bean.getCurrency());

			viewHolder.tvStatus.setTextColor(ColorUtil.getStatusColor(status));

			viewHolder.tvStatus.setText(statusStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	private class ViewHolder{
		TextView tvTerm;
		TextView tvTime;
		TextView tvStatus;
		TextView tvAmount;
	}
}
