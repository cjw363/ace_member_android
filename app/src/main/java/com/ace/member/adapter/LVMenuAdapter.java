package com.ace.member.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

public class LVMenuAdapter extends BaseAdapter{
	private int [] mMenuDrawable;
	private int[] mMenuTitle;

	public LVMenuAdapter(int[] menuDrawable,int []menuTitle){
		this.mMenuDrawable=menuDrawable;
		this.mMenuTitle=menuTitle;
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
				convertView=View.inflate(BaseApplication.getContext(), R.layout.view_lv_menu,null);
				holder=new ViewHolder(convertView);
				convertView.setTag(holder);
			}else {
				holder= (ViewHolder) convertView.getTag();
			}

			holder.imageView.setImageResource(mMenuDrawable[position]);
			holder.textView.setText(Utils.getString(mMenuTitle[position]));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	private class ViewHolder{
		ImageView imageView;
		TextView textView;
		ViewHolder(View view){
			imageView= (ImageView) view.findViewById(R.id.iv_menu_icon);
			textView= (TextView) view.findViewById(R.id.tv_menu_title);
		}
	}
}
