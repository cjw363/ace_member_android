package com.ace.member.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ace.member.R;
import com.ace.member.bean.LogBean;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class LogAdapter extends BaseAdapter {

	private List<LogBean> mList;
//	private HashMap<String,String> mIPAddress;

	public LogAdapter(List<LogBean> list){
		mList = list;
//		mIPAddress = hashMap;
	}

	@Override
	public int getCount() {
		return mList.size();
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
				LogAdapter.ViewHolder holder;
				if(convertView==null){
					convertView=View.inflate(BaseApplication.getContext(),R.layout.view_log_list_item,null);
					holder=new LogAdapter.ViewHolder(convertView);
					convertView.setTag(holder);
				}else {
					holder= (LogAdapter.ViewHolder) convertView.getTag();
				}
//				JSONObject jsonObject = mData.getJSONObject(position);
				LogBean bean=mList.get(position);
				String date = bean.getDate();
				String time = bean.getTime();
				boolean flagSameDate = bean.getHead();
				if(flagSameDate){
					holder.llDate.setVisibility(View.GONE);
				}else{
					holder.llDate.setVisibility(View.VISIBLE);
				}

				String ip = bean.getIp();
				String remark = bean.getRemark();
//				String city = bmIPAddress.get(ip);
				//if(city != null) ip =  ip+", "+city;
				holder.tvDate.setText(date);
				holder.tvRemark.setText(remark);
				holder.tvIP.setText(ip);
				holder.tvTime.setText(time);
			}catch (Exception e){
				FileUtils.addErrorLog(e);
			}
			return convertView;
		}

	public class ViewHolder{
		TextView tvTime;
		TextView tvDate;
		TextView tvRemark;
		TextView tvIP;
		LinearLayout llDate;

		ViewHolder(View view){
			tvTime = (TextView) view.findViewById(R.id.tv_log_time);
			tvDate = (TextView) view.findViewById(R.id.tv_log_date);
			tvRemark = (TextView) view.findViewById(R.id.tv_log_remark);
			tvIP = (TextView) view.findViewById(R.id.tv_log_ip);
			llDate = (LinearLayout) view.findViewById(R.id.ll_log_date);
		}
	}

}

