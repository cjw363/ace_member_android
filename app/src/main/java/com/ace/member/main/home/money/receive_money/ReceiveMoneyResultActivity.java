package com.ace.member.main.home.money.receive_money;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import butterknife.BindView;


public class ReceiveMoneyResultActivity extends BaseActivity {

	@BindView(R.id.tv_name)
	TextView mTvName;
	@BindView(R.id.tv_phone)
	TextView mTvPhone;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.btn_confirm)
	Button mBtnConfirm;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_receive_money_result;
	}

	private void init() {
		new ToolBarConfig.Builder(this,null).setTvTitleRes(R.string.receive_money).build();

		mBtnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ReceiveMoneyResult result= (ReceiveMoneyResult) getIntent().getSerializableExtra("result");
		if(result!=null) setResult(result);
	}

	private void setResult(ReceiveMoneyResult result){
		try {
			mTvName.setText(result.getName());
			mTvPhone.setText(result.getPhone());
			mTvAmount.setText(Utils.format(result.getAmount(),result.getCurrency()));
			mTvCurrency.setText(result.getCurrency());
			mTvTime.setText(result.getTime());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
