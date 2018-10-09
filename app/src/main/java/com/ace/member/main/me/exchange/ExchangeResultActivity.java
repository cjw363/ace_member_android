package com.ace.member.main.me.exchange;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;


public class ExchangeResultActivity extends BaseActivity {

	@BindView(R.id.tv_source_currency)
	TextView mTvSourceCurrency;
	@BindView(R.id.tv_source_amount)
	TextView mTvSourceAmount;
	@BindView(R.id.tv_target_currency)
	TextView mTvTargetCurrency;
	@BindView(R.id.tv_target_amount)
	TextView mTvTargetAmount;
	@BindView(R.id.time)
	TextView mTvTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initResultView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_exchange_result;
	}

	private void initResultView() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.exchange).build();
		try {
			Bundle b = getIntent().getExtras();
			if (b != null) {
				ExchangeResultBead exchangeValue = (ExchangeResultBead) b.getSerializable("value");
				if (exchangeValue == null) return;
				String sourceCurrency = exchangeValue.getSourceCurrency();
				double sourceAmount = exchangeValue.getSourceAmount();
				String targetCurrency = exchangeValue.getTargetCurrency();
				double targetAmount = exchangeValue.getTargetAmount();
				String time = exchangeValue.getTime();

				mTvSourceCurrency.setText(sourceCurrency);
				mTvSourceAmount.setText(Utils.format(sourceAmount, 2));
				mTvTargetCurrency.setText(targetCurrency);
				mTvTargetAmount.setText(Utils.format(targetAmount, 2));
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
