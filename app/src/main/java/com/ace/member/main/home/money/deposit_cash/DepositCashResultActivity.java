package com.ace.member.main.home.money.deposit_cash;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.Balance;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;


public class DepositCashResultActivity extends BaseActivity {

	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.time)
	TextView mTvTime;

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
		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
		builder.setTvTitleRes(R.string.deposit_cash).build();
		try {
			DepositCashResult depositCashResult = (DepositCashResult) getIntent().getSerializableExtra("result");
			if (depositCashResult != null) {
				Balance b = depositCashResult.getData();
				assert b != null;
				String currency = b.getCurrency();
				String amount = String.valueOf(b.getAmount());
				String time = depositCashResult.getTime();

				mTvCurrency.setText(currency);
				mTvAmount.setText(Utils.format(amount, 2));
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
