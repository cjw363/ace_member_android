package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BillerBean;
import com.og.utils.FileUtils;

import java.util.List;

public class SelectBillerAdapter extends BaseAdapter {
	private Context mContext;
	private List<BillerBean> mList;

	public SelectBillerAdapter(Context context, List<BillerBean> list) {
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
		ViewHolder viewHolder = null;
		try {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.view_gv_biller_company, null);
				viewHolder = new ViewHolder();
				viewHolder.mIVBiller = (AppCompatImageView) convertView.findViewById(R.id.iv_biller);
				viewHolder.mTvBillerName = (TextView) convertView.findViewById(R.id.tv_biller_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			BillerBean item = mList.get(position);
			viewHolder.mIVBiller.setImageResource(R.drawable.ic_partner_common);
			String name = item.getName();
			String code = item.getCode();
			String title = item.getTitle();
			String str = code + " " + name;
			if (!TextUtils.isEmpty(title)) str += " (" + title + ")";
			viewHolder.mTvBillerName.setText(str);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	protected class ViewHolder {
		AppCompatImageView mIVBiller;
		TextView mTvBillerName;
	}

}
