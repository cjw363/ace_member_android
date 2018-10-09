package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.Coupon;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;


public class LVCouponAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Coupon> mList;

	public LVCouponAdapter(Context context, List<Coupon> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setList(List<Coupon> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void addList(List<Coupon> list) {
		if (Utils.isEmptyList(list)) return;
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Coupon getItem(int position) {
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
				convertView = mInflater.inflate(R.layout.view_coupon_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Coupon coupon = getItem(position);
			if (DateUtils.isExpired(coupon.getDateExpire())) {
				holder.mTvTitle.setTextColor(Utils.getColor(R.color.clr_primary_separator_title2));
			} else {
				holder.mTvTitle.setTextColor(Utils.getColor(R.color.black));
			}
			holder.mTvTimeToTime.setText(String.format("%s ~ %s", DateUtils.getYearMonthDay(coupon.getTime()), coupon.getDateExpire()));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public static class ViewHolder {
		TextView mTvTitle;
		TextView mTvTimeToTime;

		public ViewHolder(View view) {
			mTvTitle = (TextView) view.findViewById(R.id.tv_title);
			mTvTimeToTime = (TextView) view.findViewById(R.id.tv_time_to_time);
		}
	}

	public boolean checkUsable(int position) {
		if (position < 0 || position > getCount()) return false;
		Coupon coupon = getItem(position);
		int status = coupon.getStatus();
		return (status == M.CouponStatus.STATUS_0_DEFAULT && !DateUtils.isExpired(coupon.getDateExpire()));
	}

}
