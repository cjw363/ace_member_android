package com.ace.member.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.TopUpOrder;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.view.GridViewNumber;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;


public class LVTopUpOrderAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<TopUpOrder> mList;

	public LVTopUpOrderAdapter(Context context, List<TopUpOrder> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<TopUpOrder> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void addList(List<TopUpOrder> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public TopUpOrder getItem(int position) {
		return mList.get(position);
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
				convertView = mInflater.inflate(R.layout.view_top_up_order, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TopUpOrder order = getItem(position);

			double price = order.getPrice();
			String currency = order.getCurrency();
			holder.mTvPrice.setText(String.format(Utils.getString(R.string.format_twice), Utils.format(price == 0 ? 0 : -Math.abs(price), currency), currency));
			holder.mTvPrice.setTextColor(Utils.getColor(-price < 0 ? R.color.clr_amount_red : R.color.black));
			String type = AppUtils.getTopUpWay(order.getTopUpType());
			holder.mTvPhoneOrPincode.setText(type);
			holder.mTvTime.setText(Utils.formatDateToMonthMinute(order.getTime()));

			if (position == 0) {
				holder.mTvDateTitle.setVisibility(View.VISIBLE);
				holder.mTvDateTitle.setText(DateUtils.getYearMonth(order.getTime()));
			} else {
				String date1 = getItem(position - 1).getTime();
				String date2 = order.getTime();
				if (DateUtils.compareMonth(date1, date2)) {
					holder.mTvDateTitle.setVisibility(View.GONE);
				} else {
					holder.mTvDateTitle.setVisibility(View.VISIBLE);
					holder.mTvDateTitle.setText(DateUtils.getYearMonth(order.getTime()));
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public static class ViewHolder {
		TextView mTvDateTitle;
		TextView mTvTime;
		TextView mTvPhoneOrPincode;
		TextView mTvPrice;

		public ViewHolder(View view) {
			mTvDateTitle = (TextView) view.findViewById(R.id.tv_date_title);
			mTvTime = (TextView) view.findViewById(R.id.tv_time);
			mTvPhoneOrPincode = (TextView) view.findViewById(R.id.tv_phone_or_pincode);
			mTvPrice = (TextView) view.findViewById(R.id.tv_price);
		}
	}
}
