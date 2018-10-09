package com.ace.member.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;
import com.og.utils.Utils;


public class LVRecentTransactionAdapter extends BaseAdapter {
	private Integer[] mMenuTitle;
	private SparseArrayCompat<Boolean> mArrayCompat;

	public LVRecentTransactionAdapter(Integer []menuTitle){
		this.mMenuTitle=menuTitle;
		this.mArrayCompat=new SparseArrayCompat<>();
	}

	@Override
	public int getCount() {
		return mMenuTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder;
			if(convertView==null){
				convertView=View.inflate(BaseApplication.getContext(), R.layout.view_lv_currency,null);
				holder=new ViewHolder(convertView);
				convertView.setTag(holder);
			}else {
				holder= (ViewHolder) convertView.getTag();
			}

			holder.textView.setText(Utils.getString(mMenuTitle[position]));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public class ViewHolder{
		TextView textView;
		ViewHolder(View view){
			textView= (TextView) view.findViewById(R.id.tv_menu_title);
		}
	}
}
