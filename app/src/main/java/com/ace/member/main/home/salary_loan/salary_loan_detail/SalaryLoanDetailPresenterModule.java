package com.ace.member.main.home.salary_loan.salary_loan_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class SalaryLoanDetailPresenterModule {

	private final SalaryLoanDetailContract.View mView;
	private final Context mContext;

	SalaryLoanDetailPresenterModule(SalaryLoanDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SalaryLoanDetailContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
