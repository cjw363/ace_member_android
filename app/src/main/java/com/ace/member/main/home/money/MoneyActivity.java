package com.ace.member.main.home.money;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.home.money.deposit_cash.DepositCashActivity;
import com.ace.member.main.home.money.receive_money.ReceiveMoneyActivity;
import com.ace.member.main.home.money.withdraw_cash.WithdrawCashActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.Utils;

import butterknife.BindView;

public class MoneyActivity extends BaseActivity implements View.OnClickListener {


	@BindView(R.id.btn_receive_money)
	LinearLayout mBtnReceiveMoney;
	@BindView(R.id.btn_deposit_cash)
	LinearLayout mBtnDepositCash;
	@BindView(R.id.btn_withdraw_cash)
	LinearLayout mBtnWithdrawCash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
		setEvent();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_monery;
	}

	public void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.money).build();
	}

	private void setEvent() {
		mBtnReceiveMoney.setOnClickListener(this);
		mBtnDepositCash.setOnClickListener(this);
		mBtnWithdrawCash.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_receive_money:
				Utils.toActivity(MoneyActivity.this, ReceiveMoneyActivity.class);
				break;
			case R.id.btn_deposit_cash:
				intent = new Intent(this, DepositCashActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_withdraw_cash:
				intent = new Intent(this, WithdrawCashActivity.class);
				startActivity(intent);
				break;
		}
	}

}
