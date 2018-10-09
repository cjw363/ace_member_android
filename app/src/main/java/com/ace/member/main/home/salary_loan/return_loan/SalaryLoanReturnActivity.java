package com.ace.member.main.home.salary_loan.return_loan;

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
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.SnackBarUtil;
import com.ace.member.view.MoneyTextView;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SalaryLoanReturnActivity extends BaseActivity implements SalaryLoanReturnContract.View {

	@Inject
	SalaryLoanReturnPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.ll_credit_loan)
	LinearLayout mLlCreditLoan;
	@BindView(R.id.tv_loan)
	MoneyTextView mTvCurrentLoan;
	@BindView(R.id.tv_balance)
	TextView mTvBalance;
	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_return_all)
	TextView mTvReturnAll;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;

	private double mLoan;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSalaryLoanReturnComponent.builder()
			.salaryLoanReturnPresenterModule(new SalaryLoanReturnPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		initListener();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_salary_loan_return;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.salary_loan_return).build();
		mEtAmount.setHint(Utils.getString(R.string.amount_usd));
		mLlCreditLoan.requestFocus();
	}

	private void initListener() {
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
				if (TextUtils.isEmpty(result) || Utils.strToDouble(result) > mLoan) {
					setEnableBtnSubmit(false);
				} else {
					setEnableBtnSubmit(true);
				}
			}
		});
	}

	public void getData() {
		mPresenter.getSalaryLoanReturnConfig();
	}

	@Override
	public void setData(double currentLoan, double balance) {
		mLoan = currentLoan;
		mTvCurrentLoan.setMoney(AppGlobal.USD, mLoan + "");
		String str = Utils.getString(R.string.balance) + " " +Utils.format(balance, 2) + " " + Utils.getString(R.string.usd);
		mTvBalance.setText(str);
	}

	@OnClick({R.id.tv_currency, R.id.btn_submit, R.id.tv_return_all})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.tv_currency:
				mEtAmount.requestFocus();
				AppUtils.enableSoftInput(mEtAmount, true);
				break;
			case R.id.tv_return_all:
				mEtAmount.setText(Utils.format(mLoan, 0));
				break;
			case R.id.btn_submit:
				AppUtils.enableSoftInput(this, false);
				String amount = mEtAmount.getText().toString().replace(",", "");
				if (mPresenter.checkAmount(amount)) {
					mPresenter.checkSubmit(amount);
				}
				break;
		}
	}

	@Override
	public void setEnableBtnSubmit(boolean enable) {
		mBtnSubmit.setEnabled(enable);
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

	@Override
	public void showToast(String msg) {
		SnackBarUtil.show(mLlRoot, msg);
	}
}
