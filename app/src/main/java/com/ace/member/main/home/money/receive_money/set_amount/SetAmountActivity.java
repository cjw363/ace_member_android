package com.ace.member.main.home.money.receive_money.set_amount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetAmountActivity extends BaseActivity {

	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_set_amount;
	}

	@Override
	protected void initActivity() {
		new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.set_amount).build();
		String currency = getIntent().getStringExtra("currency");
		String amount = getIntent().getStringExtra("amount");
		if (TextUtils.isEmpty(currency)) currency = AppGlobal.USD;
		final String finalCurrency = currency;
		mEtAmount.setHint(String.format("%s (%s)", Utils.getString(R.string.amount), finalCurrency));
		mEtAmount.addTextChangedListener(new SimpleTextWatcher.MoneyTextWatcher(mEtAmount, finalCurrency) {
			@Override
			public void afterTextChanged(Editable s) {
				try {
					String result = mEtAmount.getText().toString().replace(",", "");
					if (TextUtils.isEmpty(result)) {
						mEtAmount.setHint(String.format("%s (%s)",Utils.getString(R.string.amount),finalCurrency));
						mTvCurrency.setText(null);
					} else {
						mEtAmount.setHint(null);
						mTvCurrency.setText(finalCurrency);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});

		if(!TextUtils.isEmpty(amount) || Utils.strToDouble(amount)>0){
			mEtAmount.setText(amount);
		}
	}

	@OnClick({R.id.tv_currency, R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.tv_currency:
				mEtAmount.requestFocus();
				AppUtils.enableSoftInput(mEtAmount, true);
				break;
			case R.id.btn_submit:
				try {
					Intent intent=new Intent();
					String amount=mEtAmount.getText().toString().replace(",","");
					if(Utils.strToDouble(amount)==0) amount=null;
					intent.putExtra("amount",amount);
					setResult(Activity.RESULT_OK,intent);
					finish();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
				break;
		}
	}
}
