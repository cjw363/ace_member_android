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
import com.ace.member.main.home.transfer.TransferBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.BaseApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferRecentAdapter extends BaseAdapter {

	private List<TransferBean> mList;
	private LayoutInflater mInflater;

	public TransferRecentAdapter(List<TransferBean> list, Context context) {
		mList = list;
		mInflater = LayoutInflater.from(context);
	}

	public TransferBean getTransferBean(int position){
		return mList.get(position);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		try {
			TransferRecentAdapter.ViewHolder holder;
			if (view == null) {
				view = View.inflate(BaseApplication.getContext(), R.layout.view_transfer_recent_list_item, null);
				holder = new TransferRecentAdapter.ViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (TransferRecentAdapter.ViewHolder) view.getTag();
			}
			TransferBean bean = mList.get(position);
			String phone = bean.getPhone();
			String name = bean.getName();
			int type = bean.getType();
			holder.mTvPhone.setText(phone);
			if (!TextUtils.isEmpty(name)) {
				holder.mTvName.setVisibility(View.VISIBLE);
				holder.mTvName.setText(name);
				if (type == AppGlobal.USER_TYPE_1_MEMBER){
					holder.mIvHead.setBackgroundResource(R.drawable.ic_transfer_member);
				}else if (type == AppGlobal.USER_TYPE_3_PARTNER){
					holder.mIvHead.setBackgroundResource(R.drawable.ic_transfer_partner);
					holder.mTvPhone.setVisibility(View.GONE);
				}else {
					holder.mIvHead.setBackgroundResource(R.drawable.ic_transfer_merchant);
					holder.mTvPhone.setVisibility(View.GONE);
				}

			} else {
				holder.mTvName.setVisibility(View.GONE);
				holder.mIvHead.setBackgroundResource(R.drawable.ic_transfer_nonmember);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	public class ViewHolder {

		@BindView(R.id.tv_member_name)
		TextView mTvName;

		@BindView(R.id.tv_phone)
		TextView mTvPhone;

		@BindView(R.id.iv_head)
		AppCompatImageView mIvHead;

		ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}
	}
}
