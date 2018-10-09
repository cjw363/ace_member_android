package com.ace.member.main.home.transfer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class TransferResultActivity extends BaseActivity {

	@BindView(R.id.tv_accept_code)
	TextView mTvAcceptCode;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_phone)
	TextView mTvPhone;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_adapt_to_member)
	TextView mTvAdaptToMember;
	@BindView(R.id.tv_accept_code_title)
	TextView mTvAcceptCodeTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.transfer).build();

		initResultView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transfer_result;
	}

	private void initResultView() {
		try {
			Bundle b = getIntent().getExtras();
			if (b != null) {
				String currency = b.getString("currency");
				String phone = b.getString("phone");
				String amount = Utils.format(b.getString("amount"),2);
				String fee = Utils.format(b.getString("fee"),2);
				String time = b.getString("time");
				if (b.getString("from") != null){
					mTvAdaptToMember.setVisibility(View.VISIBLE);
					mTvAcceptCode.setVisibility(View.INVISIBLE);
					mTvAcceptCodeTitle.setVisibility(View.GONE);
				}else {
					mTvAdaptToMember.setVisibility(View.GONE);
					mTvAcceptCode.setVisibility(View.VISIBLE);
					mTvAcceptCodeTitle.setVisibility(View.VISIBLE);
					String acceptCode = b.getString("accept_code");
					mTvAcceptCode.setText(acceptCode);
				}
				mTvCurrency.setText(currency);
				mTvAmount.setText(AppUtils.simplifyAmount(currency, amount));
				mTvPhone.setText(phone);
				mTvFee.setText(AppUtils.simplifyAmount(currency, fee));
				mTvTime.setText(time);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick({R.id.btn_done})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_done:
				finish();
				break;
		}
	}
}
