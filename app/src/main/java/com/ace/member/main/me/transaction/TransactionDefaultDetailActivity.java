package com.ace.member.main.me.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.Transaction;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.TransactionType;
import com.og.utils.Utils;

import butterknife.BindView;


public class TransactionDefaultDetailActivity extends BaseActivity {

	@BindView(R.id.iv_icon)
	AppCompatImageView mIvIcon;
	@BindView(R.id.tv_type_title)
	TextView mTvTypeTitle;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;
	@BindView(R.id.rl_remark)
	RelativeLayout mRlRemark;

	private Transaction mTransaction;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
		setData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transaction_default_detail;
	}

	private void initData() {
		mTransaction = (Transaction) getIntent().getSerializableExtra("transaction_data");
	}

	private void initView() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.transaction_detail)
			.setBackListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			})
			.build();
	}

	private void setData() {
		int transactionTypeIconID = TransactionType.getSubTypeIcon(Integer.valueOf(mTransaction.getSubType()));
		String transactionType = TransactionType.getSubTypeName(mContext, Integer.valueOf(mTransaction.getSubType()));
		mIvIcon.setImageResource(transactionTypeIconID);
		mTvTypeTitle.setText(transactionType);

		double amount = Math.abs(Utils.strToDouble(mTransaction.getAmount()));
		String currency = mTransaction.getCurrency();
		mTvAmount.setText(AppUtils.simplifyAmount(currency, String.valueOf(amount)) + " " + currency);
		mTvTime.setText(mTransaction.getTime());
		String remark = mTransaction.getRemark();
		if (TextUtils.isEmpty(remark)) {
			mRlRemark.setVisibility(View.GONE);
		} else {
			mTvRemark.setText(remark);
		}
	}
}
