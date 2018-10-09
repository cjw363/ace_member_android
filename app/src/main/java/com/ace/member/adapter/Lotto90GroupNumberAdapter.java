package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.LottoBetBillItem;

import java.text.DecimalFormat;
import java.util.List;

public class Lotto90GroupNumberAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<LottoBetBillItem> mList;
	private DecimalFormat mDecimalFormat;

	public Lotto90GroupNumberAdapter(Context context, List<LottoBetBillItem> list) {
		inflater = LayoutInflater.from(context);
		mList = list;
		mDecimalFormat = new DecimalFormat("00");
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int i) {
		return mList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.view_lotto90_single, null);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			holder.tvBall_1 = (TextView) convertView.findViewById(R.id.tv_ball_1);
			holder.tvBall_2 = (TextView) convertView.findViewById(R.id.tv_ball_2);
			holder.tvBall_3 = (TextView) convertView.findViewById(R.id.tv_ball_3);
			holder.tvBall_4 = (TextView) convertView.findViewById(R.id.tv_ball_4);
			holder.tvBall_5 = (TextView) convertView.findViewById(R.id.tv_ball_5);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LottoBetBillItem item = mList.get(i);
		String number = item.getNumber();
		String[] str = number.split(",");
		String n = String.valueOf(i + 1);
		holder.tvNumber.setText(n);
		holder.tvBall_1.setText(mDecimalFormat.format(Integer.valueOf(str[0])));
		holder.tvBall_2.setText(mDecimalFormat.format(Integer.valueOf(str[1])));
		holder.tvBall_3.setText(mDecimalFormat.format(Integer.valueOf(str[2])));
		holder.tvBall_4.setText(mDecimalFormat.format(Integer.valueOf(str[3])));
		holder.tvBall_5.setText(mDecimalFormat.format(Integer.valueOf(str[4])));
		return convertView;
	}

	class ViewHolder {
		TextView tvNumber;
		TextView tvBall_1;
		TextView tvBall_2;
		TextView tvBall_3;
		TextView tvBall_4;
		TextView tvBall_5;
	}
}
