package com.ace.member.main.detail.withdraw_via_agent_detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.CashApplication;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.view.MoneyTextView;
import com.og.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;

public class WithdrawViaAgentDetailActivity extends BaseActivity implements WithdrawViaAgentDetailContract.WithdrawViaAgentDetailView {

	@Inject
	WithdrawViaAgentDetailPresenter mPresenter;

	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_ticket_id)
	TextView mTvTicketID;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_detail_amount)
	MoneyTextView mTvDetailAmount;
	@BindView(R.id.tv_fee)
	MoneyTextView mTvFee;
	@BindView(R.id.tv_source)
	TextView mTvSource;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;

	private int mApplicationID;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerWithdrawViaAgentDetailComponent.builder()
			.withdrawViaAgentDetailModule(new WithdrawViaAgentDetailModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_withdraw_via_agent_detail;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mApplicationID = bundle.getInt("id");
		}
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.withdraw_detail).build();
	}

	public void getData() {
		HashMap<String, String> params = new HashMap<>();
		params.put("application_id", mApplicationID + "");
		mPresenter.getData(params);
	}

	@Override
	public void setData(CashApplication data) {
		String currency = data.getCurrency();
		String amount = data.getAmount();
		String fee = data.getTransactionFee();
		int mStatus = data.getStatus();
		String source = AppUtils.getUserType(data.getSourceType()) + " " + data.getSourcePhone();

		mTvAmount.setMoney(currency,amount);
		mTvTicketID.setText(String.valueOf(mApplicationID));
		mTvTime.setText(data.getTime());
		mTvCurrency.setText(currency);
		mTvDetailAmount.setMoney(currency, amount);
		mTvFee.setMoney(currency, fee);
		mTvSource.setText(source);
		mTvStatus.setText(AppUtils.getStatus(mStatus));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(mStatus));
		mTvRemark.setText(data.getRemark());
	}
}
