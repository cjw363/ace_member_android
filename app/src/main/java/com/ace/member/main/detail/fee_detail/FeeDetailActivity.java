package com.ace.member.main.detail.fee_detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.Fee;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;

public class FeeDetailActivity extends BaseActivity implements FeeDetailContract.FeeDetailView {

	@Inject
	FeeDetailPresenter mPresenter;

	@BindView(R.id.iv_icon)
	AppCompatImageView mIvIcon;
	@BindView(R.id.tv_type_title)
	TextView mTvType;
	@BindView(R.id.tv_fee)
	MoneyTextView mTvFee;
	@BindView(R.id.tv_related_type)
	TextView mTvRelatedType;
	@BindView(R.id.ll_bank)
	LinearLayout mLlBank;
	@BindView(R.id.tv_related_bank)
	TextView mTvRelatedBank;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_fee2)
	MoneyTextView mTvFee2;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;

	private int mSubType;
	private int mMainSubType;
	private int mApplicationID;
	private String mRemark;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerFeeDetailComponent.builder()
			.feeDetailModule(new FeeDetailModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_fee_detail;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mSubType = bundle.getInt("sub_type");
			mMainSubType = bundle.getInt("main_sub_type");
			mApplicationID = bundle.getInt("id");
			mRemark = bundle.getString("remark");
		}
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.withdraw_fee).build();

		mIvIcon.setImageResource(TransactionType.getSubTypeIcon(mSubType));
		mTvType.setText(TransactionType.getSubTypeName(mContext, mSubType));
	}

	public void getData() {
		HashMap<String, String> params = new HashMap<>();
		params.put("application_id", mApplicationID + "");
		switch (mSubType) {
			case TransactionType.TRANSACTION_SUB_TYPE_215_WITHDRAW_FEE:
				mPresenter.getWithdrawFeeData(params, mMainSubType);
				break;
			case TransactionType.TRANSACTION_SUB_TYPE_217_BANK_FEE:
				mPresenter.getBankFeeData(params);
				break;
		}
	}

	@Override
	public void setWithdrawFeeData(Fee data) {
		String currency = data.getCurrency();
		String fee = data.getFee();
		String amount = data.getAmount();
		int mStatus = data.getStatus();

		mTvFee.setMoney(currency, fee);
		mTvRelatedType.setText(TransactionType.getSubTypeName(this, mMainSubType));
		mTvCurrency.setText(currency);
		mTvAmount.setMoney(currency, amount);
		mTvFee2.setMoney(currency, fee);
		mTvTime.setText(data.getTime());
		mTvStatus.setText(AppUtils.getStatus(mStatus));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(mStatus));
		mTvRemark.setText(mRemark);
	}

	@Override
	public void setBankFeeData(Fee data) {
		String currency = data.getCurrency();
		String fee = data.getFee();
		String amount = data.getAmount();
		int mStatus = data.getStatus();

		mTvFee.setMoney(currency, fee);
		mTvRelatedType.setText(TransactionType.getSubTypeName(this, mMainSubType));
		mLlBank.setVisibility(View.VISIBLE);
		mTvRelatedBank.setText(data.getBankName());
		mTvCurrency.setText(currency);
		mTvAmount.setMoney(currency, amount);
		mTvFee2.setMoney(currency, fee);
		mTvTime.setText(data.getTime());
		mTvStatus.setText(AppUtils.getStatus(mStatus));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(mStatus));
		mTvRemark.setText(mRemark);
	}
}
