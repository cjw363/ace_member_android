package com.ace.member.main.detail.exchange_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.CurrencyExchangeBean;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.view.MoneyTextView;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class ExchangeDetailActivity extends BaseActivity implements ExchangeDetailContract.View {
	@Inject
	ExchangeDetailPresenter mPresenter;

	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_exchange_rate)
	TextView mTvExchangeRate;
	@BindView(R.id.tv_source_currency)
	TextView mTvSourceCurrency;
	@BindView(R.id.tv_source_amount)
	TextView mTvSourceAmount;
	@BindView(R.id.tv_target_currency)
	TextView mTvTargetCurrency;
	@BindView(R.id.tv_target_amount)
	TextView mTvTargetAmount;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;

	private int mId;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerExchangeDetailComponent.builder()
			.exchangeDetailPresenterModule(new ExchangeDetailPresenterModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_exchange_detail;
	}

	private void initData() {
		mId = getIntent().getIntExtra("id", 0);
		String currency = getIntent().getStringExtra("currency");
		String amount = getIntent().getStringExtra("amount");
		mTvAmount.setMoney(currency, amount);
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.exchange_detail).build();
	}

	public void getData() {
		mPresenter.getData(mId);
	}

	@Override
	public void setData(CurrencyExchangeBean data) {
		if (data != null) {
			mTvExchangeRate.setText(data.getExchangeRate());
			mTvSourceCurrency.setText(data.getSourceCurrency());
			mTvSourceAmount.setText(data.getSourceAmount());
			mTvTargetCurrency.setText(data.getTargetCurrency());
			mTvTargetAmount.setText(data.getTargetAmount());
			mTvTime.setText(data.getTime());
			mTvRemark.setText(data.getRemark());
		}
	}


}
