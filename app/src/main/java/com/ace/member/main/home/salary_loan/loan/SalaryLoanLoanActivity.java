package com.ace.member.main.home.salary_loan.loan;

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
import com.ace.member.bean.SalaryLoanBean;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.SnackBarUtil;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SalaryLoanLoanActivity extends BaseActivity implements SalaryLoanLoanContract.View {

	@Inject
	SalaryLoanLoanPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.ll_credit_loan)
	LinearLayout mLlCreditLoan;
	@BindView(R.id.tv_loan)
	MoneyTextView mTvAvailableLoan;
	@BindView(R.id.tv_credit)
	TextView mTvCredit;
	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_charge_title)
	TextView mTvChargeTitle;
	@BindView(R.id.tv_charge)
	MoneyTextView mTvCharge;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSalaryLoanLoanComponent.builder()
			.salaryLoanLoanPresenterModule(new SalaryLoanLoanPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		initListener();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_salary_loan_loan;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.salary_loan).build();
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
				mPresenter.updateServiceCharge(result);
			}
		});
	}

	public void getData() {
		mPresenter.getSalaryLoanConfig();
	}

	@Override
	public void setCreditAndLoan(SalaryLoanBean salaryLoanBean) {
		try {
			String currency = salaryLoanBean.getCurrency();
			double credit = salaryLoanBean.getCredit();
			double loan = salaryLoanBean.getLoan();
			double available = credit - loan;
			mTvAvailableLoan.setMoney(currency, available + "");
			if (credit - loan > 0) {
				setEnableBtnSubmit(true);
			} else {
				setEnableBtnSubmit(false);
			}
			String creditStr = String.format(Utils.getString(R.string.credit_with_currency), Utils.format(credit, 0), currency);
			mTvCredit.setText(creditStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void setEnableServiceCharge(boolean enable) {
		mTvChargeTitle.setEnabled(enable);
		mTvCharge.setEnabled(enable);
	}

	@Override
	public void setServiceCharge(String charge) {
		mTvCharge.setMoney(AppGlobal.USD, charge);
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
				try {
					AppUtils.enableSoftInput(this, false);
					String amount = mEtAmount.getText().toString().replace(",", "");
					if (mPresenter.checkAmount(amount)) {
						mPresenter.checkSubmit(amount);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
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

	@Override
	public void showToast(String msg) {
		SnackBarUtil.show(mLlRoot, msg);
	}
}
