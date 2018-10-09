package com.ace.member.main.third_party.samrithisak_loan.loan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.SnackBarUtil;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class LoanActivity extends BaseActivity implements LoanContract.View {

	@Inject
	LoanPresenter mPresenter;

	@BindView(R.id.ll_credit_loan)
	LinearLayout mLlCreditLoan;
	@BindView(R.id.tv_credit)
	TextView mTvCredit;
	@BindView(R.id.tv_available_credit)
	TextView mTvAvailableCredit;

	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;

	@BindView(R.id.tv_charge_title)
	TextView mTvChargeTitle;
	@BindView(R.id.tv_charge)
	TextView mTvCharge;

	@BindView(R.id.btn_submit)
	Button mBtnSubmit;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerLoanComponent.builder()
			.loanPresenterModule(new LoanPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_loan;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.loan).build();
		mEtAmount.setHint(Utils.getString(R.string.amount_usd));
		mEtAmount.addTextChangedListener(new SimpleTextWatcher.MoneyTextWatcher(mEtAmount, 0) {
			@Override
			public void afterTextChanged(Editable s) {
				String result = mEtAmount.getText().toString().replace(",", "");
				if (TextUtils.isEmpty(result)) {
					mEtAmount.setHint(Utils.getString(R.string.amount_usd));
					mTvCurrency.setText(null);
				} else {
					mEtAmount.setHint(null);
					mTvCurrency.setText(R.string.usd);
				}
				mPresenter.updateServiceCharge(result);
			}
		});
		mLlCreditLoan.setFocusable(true);
		mLlCreditLoan.setFocusableInTouchMode(true);
		mLlCreditLoan.requestFocus();
		mPresenter.start();
	}

	@Override
	public void setCreditAndLoan(MemberLoanPartner memberLoanPartner) {
		int credit = memberLoanPartner.getCredit();
		int available = credit - memberLoanPartner.getLoan();
		mTvCredit.setText(Utils.format(credit, 2));
		mTvAvailableCredit.setText(String.format(Utils.getString(R.string.format_twice), Utils.format(available, 2), AppGlobal.USD));
	}

	@Override
	public void setEnableServiceCharge(boolean enable) {
		mTvChargeTitle.setEnabled(enable);
		mTvCharge.setEnabled(enable);
	}

	@Override
	public void setServiceCharge(String charge) {
		mTvCharge.setText(String.format(Utils.getString(R.string.format_twice), Utils.format(charge, 2), AppGlobal.USD));
		setEnableServiceCharge(!Utils.checkEmptyAmount(charge));
	}

	@Override
	public void setEnableBtnSubmit(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}

	@OnClick({R.id.tv_currency, R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.tv_currency:
				mEtAmount.requestFocus();
				AppUtils.enableSoftInput(mEtAmount, true);
				break;
			case R.id.btn_submit:
				AppUtils.enableSoftInput(this, false);
				String amount = mEtAmount.getText().toString().replace(",", "");
				if (mPresenter.checkAmount(amount)) mPresenter.checkSubmit(amount);
				break;
		}
	}

	@Override
	public void showLoanFail() {
		SnackBarUtil.show(getWindow().findViewById(android.R.id.content), R.string.fail, Snackbar.LENGTH_SHORT, new Snackbar.Callback() {
			@Override
			public void onDismissed(Snackbar transientBottomBar, int event) {
				finish();
			}
		});
	}

	@Override
	public void showLoanSuccess() {
		SnackBarUtil.show(getWindow().findViewById(android.R.id.content), R.string.success, Snackbar.LENGTH_SHORT, new Snackbar.Callback() {
			@Override
			public void onDismissed(Snackbar transientBottomBar, int event) {
				finish();
			}
		});
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}
}
