package com.ace.member.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;


public class AboutAdapter extends BaseAdapter {

	private JSONArray mData;

	public AboutAdapter(JSONArray data) {
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.length();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			AboutAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(BaseApplication.getContext(), R.layout.view_about_list_item, null);
				holder = new AboutAdapter.ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (AboutAdapter.ViewHolder) convertView.getTag();
			}
			JSONObject jsonObject = mData.getJSONObject(position);
			String version = jsonObject.getString("version");
			String content = jsonObject.getString("content");
			holder.tvVersion.setText(version);
			holder.tvContent.setText(content);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView tvVersion;
		TextView tvContent;

		ViewHolder(View view) {
			tvVersion = (TextView) view.findViewById(R.id.tv_version);
			tvContent = (TextView) view.findViewById(R.id.tv_about_content);
		}
	}
}
