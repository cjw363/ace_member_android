package com.ace.member.main.home.money.withdraw_cash;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;


public class WithdrawCashResultActivity extends BaseActivity {

	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.ll_fee)
	LinearLayout mLlFee;
	@BindView(R.id.time)
	TextView mTvTime;
	@BindView(R.id.title1)
	TextView mTvTitle1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initResultView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_deposit_cash_result;
	}

	private void initResultView() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.withdraw_cash).build();
		mTvTitle1.setText(Utils.getString(R.string.withdraw_cash));
		mLlFee.setVisibility(View.VISIBLE);
		try {
			WithdrawCashResultDetail withdrawCashResultDetail = (WithdrawCashResultDetail) getIntent().getSerializableExtra("result");
			if (withdrawCashResultDetail != null) {
				String currency = withdrawCashResultDetail.getCurrency();
				double amount = withdrawCashResultDetail.getAmount();
				double fee = withdrawCashResultDetail.getFee();
				String time = withdrawCashResultDetail.getTime();

				mTvCurrency.setText(currency);
				mTvAmount.setText(Utils.format(amount, currency));
				mTvFee.setText(Utils.format(fee, currency));
				mTvTime.setText(time);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@OnClick({R.id.btn_confirm, R.id.ll_toolbar_menu})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_confirm:
				finish();
				break;
		}
	}

}
