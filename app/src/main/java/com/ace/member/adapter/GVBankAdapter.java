package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BankAccount;
import com.ace.member.utils.BankUtil;
import com.og.utils.FileUtils;

import java.util.List;


public class GVBankAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<BankAccount> mBankAccounts;

	public GVBankAdapter(Context context, List<BankAccount> bankAccounts) {
		mInflater = LayoutInflater.from(context);
		mBankAccounts = bankAccounts;
	}

	@Override
	public int getCount() {
		return mBankAccounts.size();
	}

	@Override
	public BankAccount getItem(int position) {
		return mBankAccounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_gv_bank, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BankAccount account=getItem(position);
			holder.mIvBank.setImageResource(BankUtil.getBankImageResourceByBankCode(account.getCode()));
			holder.mTvBank.setText(account.getName());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	private class ViewHolder {
		AppCompatImageView mIvBank;
		TextView mTvBank;

		ViewHolder(View view) {
			mIvBank = (AppCompatImageView) view.findViewById(R.id.iv_bank);
			mTvBank = (TextView) view.findViewById(R.id.tv_bank);
		}
	}
}
