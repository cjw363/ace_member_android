package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;

import java.util.List;
import java.util.Map;

public class DialogListViewAdapter extends BaseAdapter{

	private List<Map<String, String>> mData;
	private LayoutInflater mInflater;

	public DialogListViewAdapter(Context context,  List<Map<String, String>> data){
		mInflater = LayoutInflater.from(context);
		mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			Holder holder;
			if(convertView==null){
				convertView=mInflater.inflate(R.layout.view_blend_dialog_list_item, null);
				holder=new Holder();
				holder.tvDialogListItem=(TextView) convertView.findViewById(R.id.tv_blend_dialog_list_item);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			holder.tvDialogListItem.setText(mData.get(position).get("target_phone"));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}
	class Holder{
		TextView tvDialogListItem;
	}

}
