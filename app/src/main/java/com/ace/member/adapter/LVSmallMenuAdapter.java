package com.ace.member.adapter;


import android.support.annotation.BoolRes;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.Map;

public class LVSmallMenuAdapter extends BaseAdapter{
	private Integer [] mMenuDrawable;
	private Integer[] mMenuTitle;
	private SparseArrayCompat<Boolean> mArrayCompat;

	public LVSmallMenuAdapter(Integer[] menuDrawable,Integer []menuTitle){
		this.mMenuDrawable=menuDrawable;
		this.mMenuTitle=menuTitle;
		this.mArrayCompat=new SparseArrayCompat<>();
	}

	public void enableDot(int position,boolean enable){
		mArrayCompat.put(position,enable);
	}

	public void enableDot(int[]positions,boolean []enables){
		if(positions!=null && enables !=null && positions.length>0 && enables.length>0 && positions.length==enables.length){
			for(int i=0;i<positions.length;i++){
				mArrayCompat.put(positions[i],enables[i]);
			}
		}
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
				convertView=View.inflate(BaseApplication.getContext(),R.layout.view_lv_menu_small,null);
				holder=new ViewHolder(convertView);
				convertView.setTag(holder);
			}else {
				holder= (ViewHolder) convertView.getTag();
			}

			holder.imageView.setImageResource(mMenuDrawable[position]);
			holder.textView.setText(Utils.getString(mMenuTitle[position]));
			holder.vDot.setVisibility(mArrayCompat.get(position)!=null && mArrayCompat.get(position)?View.VISIBLE:View.GONE);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public class ViewHolder{
		AppCompatImageView imageView;
		View vDot;
		TextView textView;
		ViewHolder(View view){
			imageView= (AppCompatImageView) view.findViewById(R.id.iv_menu);
			vDot= view.findViewById(R.id.v_dot);
			textView= (TextView) view.findViewById(R.id.tv_menu_title);
		}
	}
}
