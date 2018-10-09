package com.ace.member.main.home.receive_to_acct.history;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ace.member.R;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;


public class R2AReceiveToAcctAdapter extends BaseAdapter {

	private JSONArray mData;
	private Context context;

	public R2AReceiveToAcctAdapter(JSONArray data,Context context){
		mData = data;
		this.context = context;
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(BaseApplication.getContext(), R.layout.view_receive_to_acct_list_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			JSONObject jsonObject = mData.getJSONObject(position);
			String date = jsonObject.getString("date");
			String time = " " + jsonObject.getString("only_time");
			String sourceType = jsonObject.getString("source_type");
			String source = jsonObject.getString("source");
			final String phone = jsonObject.getString("phone_source");
			String currency = jsonObject.getString("currency");
			String amount = jsonObject.getString("amount");
			boolean flagSameDate = jsonObject.getBoolean("flagSameDate");

			if (flagSameDate) {
				holder.tvDate.setVisibility(View.GONE);
			} else {
				holder.tvDate.setVisibility(View.VISIBLE);
			}

			holder.tvDate.setText(date);
			holder.tvName.setText(source);
			if (Integer.parseInt(sourceType) == 99) {
				holder.tvPhone.setText("[System]");
			} else {
				SpannableString spanStr = new SpannableString(phone);
				spanStr.setSpan(new UnderlineSpan(), 0, phone.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				holder.tvPhone.setText(spanStr);
				holder.tvPhone.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				});
			}

			holder.tvAmount.setText(currency + " " + AppUtils.simplifyAmount(currency, amount));
			holder.tvTime.setText(time);

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		@BindView(R.id.tv_date)
		TextView tvDate;

		@BindView(R.id.tv_name)
		TextView tvName;

		@BindView(R.id.tv_phone)
		TextView tvPhone;

		@BindView(R.id.tv_amount)
		TextView tvAmount;

		@BindView(R.id.tv_time)
		TextView tvTime;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}

}

