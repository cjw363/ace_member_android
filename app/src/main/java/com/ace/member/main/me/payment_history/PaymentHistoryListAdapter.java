package com.ace.member.main.me.payment_history;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ace.member.R;
import com.ace.member.bean.PaymentHistoryBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.ColorUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;
import org.json.JSONObject;

import java.util.List;


public class PaymentHistoryListAdapter extends BaseAdapter {

	private List<PaymentHistoryBean> mList;

	private JSONObject mType = new JSONObject();

	public PaymentHistoryListAdapter(List<PaymentHistoryBean> list) {
		mList = list;
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
			PaymentHistoryListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(BaseApplication.getContext(), R.layout.view_payment_history_list_item, null);
				holder = new PaymentHistoryListAdapter.ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (PaymentHistoryListAdapter.ViewHolder) convertView.getTag();
			}
			PaymentHistoryBean bean=mList.get(position);
			String date = bean.getDate();
			String time = bean.getTime();
			boolean flagSameDate = bean.getHeader();;
			if (flagSameDate) {
				holder.mLLDate.setVisibility(View.GONE);
			} else {
				holder.mLLDate.setVisibility(View.VISIBLE);
			}

			double amount = bean.getAmount();
			double fee = bean.getFee();
			String currency = bean.getCurrency();
			int status = bean.getStatus();
			int type = bean.getType();

			holder.mTVDate.setText(date);
			holder.mTVTime.setText(time);
			holder.mTVAmount.setText("-" + Utils.format(String.valueOf(amount+fee), 2) + " " + currency);
			holder.mTVStatus.setText(AppUtils.getStatus(status));
			holder.mTVStatus.setTextColor(ColorUtil.getStatusColor(status));
			holder.mIVIcon.setImageResource(getImg(type));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	private int getImg(int type){
		if(type == AppGlobal.PAYMENT_TYPE_1_EDC) return R.drawable.ic_electricity;
		else if(type == AppGlobal.PAYMENT_TYPE_2_WSA) return R.drawable.ic_water;
		return 0;
	}

	public class ViewHolder {
		@BindView(R.id.ph_ll_date)
		LinearLayout mLLDate;

		@BindView(R.id.ph_tv_date)
		TextView mTVDate;

		@BindView(R.id.ph_iv_icon)
		AppCompatImageView mIVIcon;

		@BindView(R.id.ph_tv_time)
		TextView mTVTime;

		@BindView(R.id.ph_tv_amount)
		TextView mTVAmount;

		@BindView(R.id.ph_tv_status)
		TextView mTVStatus;

		ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
