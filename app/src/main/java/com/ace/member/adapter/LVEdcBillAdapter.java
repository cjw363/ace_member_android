package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.EdcBill;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;


public class LVEdcBillAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<EdcBill> mList;

	public LVEdcBillAdapter(Context context, List<EdcBill> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<EdcBill> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void addList(List<EdcBill> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public EdcBill getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList==null?0:mList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_edc_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			EdcBill bill=getItem(position);
			int status=bill.getStatus();
			holder.mTvPhone.setText(bill.getPhone());
			holder.mTvTime.setText(Utils.formatDateToYearMonthDay(bill.getTime()));
			holder.mTvAmount.setText("-" + Utils.format(bill.getTotal()) + " " + bill.getCurrency());
			holder.mTvStatus.setText(AppUtils.getStatus(status));
			if(status== AppGlobal.STATUS_4_CANCELLED){
				int color=ColorUtil.getStatusColor(AppGlobal.STATUS_4_CANCELLED);
				holder.mTvPhone.setTextColor(color);
				holder.mTvAmount.setTextColor(color);
				holder.mTvStatus.setTextColor(color);
				holder.mTvTime.setTextColor(color);
			}else {
				holder.mTvAmount.setTextColor(Utils.getColor(R.color.clr_amount_red));
				holder.mTvPhone.setTextColor(Utils.getColor(R.color.black));
				holder.mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
				holder.mTvTime.setTextColor(Utils.getColor(R.color.gray));
			}

			if(position==0){
				holder.mTvDateTitle.setVisibility(View.VISIBLE);
				holder.mTvDateTitle.setText(DateUtils.getYearMonth(bill.getTime()));
			}else {
				String date1=getItem(position-1).getTime();
				String date2= bill.getTime();
				if(DateUtils.compareMonth(date1,date2)){
					holder.mTvDateTitle.setVisibility(View.GONE);
				}else {
					holder.mTvDateTitle.setVisibility(View.VISIBLE);
					holder.mTvDateTitle.setText(DateUtils.getYearMonth(bill.getTime()));
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	class ViewHolder {
		TextView mTvDateTitle;
		TextView mTvPhone;
		TextView mTvTime;
		TextView mTvAmount;
		TextView mTvStatus;

		ViewHolder(View view) {
			mTvDateTitle= (TextView) view.findViewById(R.id.tv_date_title);
			mTvPhone= (TextView) view.findViewById(R.id.tv_phone);
			mTvTime = (TextView) view.findViewById(R.id.tv_time);
			mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
			mTvStatus = (TextView) view.findViewById(R.id.tv_status);
		}
	}
}
