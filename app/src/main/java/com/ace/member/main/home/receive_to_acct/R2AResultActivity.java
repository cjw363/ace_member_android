package com.ace.member.main.home.receive_to_acct;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;


public class R2AResultActivity extends BaseActivity {

	@BindView(R.id.r2a_rs_currency)
	TextView mTvCurrency;
	@BindView(R.id.r2a_rs_phone)
	TextView mTvPhone;
	@BindView(R.id.r2a_rs_amount)
	TextView mTvAmount;
	@BindView(R.id.r2a_rs_time)
	TextView mTvTime;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.detail).build();

		initResultView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_r2a_result;
	}

	private void initResultView() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			String currency = b.getString("currency", "");
			String sender = b.getString("sender", "");
			String amount = b.getString("amount", "");
			String time = b.getString("time", "");

			mTvCurrency.setText(currency);
			mTvAmount.setText(amount);
			mTvPhone.setText(sender);
			mTvTime.setText(time);
		}
	}

	@OnClick({R.id.btn_confirm})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_confirm:
				finish();
				break;
		}
	}

}
