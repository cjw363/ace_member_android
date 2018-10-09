package com.ace.member.main.home.salary_loan.salary_loan_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.SalaryLoanFlow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class SalaryLoanDetailActivity extends BaseActivity implements SalaryLoanDetailContract.View {
	@Inject
	SalaryLoanDetailPresenter mPresenter;

	@BindView(R.id.iv_icon)
	AppCompatImageView mIvIcon;
	@BindView(R.id.tv_type)
	TextView mTvType;
	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_detail_title)
	TextView mTvDetailTitle;
	@BindView(R.id.tv_detail_amount)
	MoneyTextView mTvDetailAmount;
	@BindView(R.id.tv_service_charge)
	MoneyTextView mTvServiceCharge;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.ll_service_charge)
	LinearLayout mLlServiceCharge;
	@BindView(R.id.rl_remark)
	RelativeLayout mRlRemark;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;

	private int mId;
	private String mRemark;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSalaryLoanDetailComponent.builder()
			.salaryLoanDetailPresenterModule(new SalaryLoanDetailPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_salary_loan_detail;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.loan_detail).build();
		mId = getIntent().getIntExtra("id", 0);
		mRemark = getIntent().getStringExtra("remark");
		if (TextUtils.isEmpty(mRemark)) mRlRemark.setVisibility(View.GONE);
	}

	public void getData() {
		mPresenter.getSalaryLoanDetail(mId);
	}

	@Override
	public void setDetail(SalaryLoanFlow salaryLoanFlow) {
		try {
			int type = salaryLoanFlow.getType();
			String currency = salaryLoanFlow.getCurrency();
			String amount = salaryLoanFlow.getAmount();
			if (type == AppGlobal.APPLICATION_TYPE_2_DEPOSIT) {
				mIvIcon.setImageResource(R.drawable.ic_salary_loan);
				mTvType.setText(R.string.salary_loan);
				mTvDetailTitle.setText(R.string.loan);
				mLlServiceCharge.setVisibility(View.VISIBLE);
				mTvServiceCharge.setMoney(currency, salaryLoanFlow.getServiceCharge());
			} else if (type == AppGlobal.APPLICATION_TYPE_3_WITHDRAW) {
				mIvIcon.setImageResource(R.drawable.ic_return_salary_loan);
				mTvType.setText(R.string.salary_loan_return);
				mTvDetailTitle.setText(R.string.return_loan);
				mLlServiceCharge.setVisibility(View.GONE);
			}
			mTvAmount.setMoney(currency, amount);
			mTvCurrency.setText(currency);
			mTvDetailAmount.setMoney(currency, amount);
			mTvTime.setText(salaryLoanFlow.getTime());
			mTvRemark.setText(mRemark);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

}
