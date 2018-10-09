package com.ace.member.main.home.salary_loan.salary_loan_history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class SalaryLoanHistoryPresenterModule {

	private final SalaryLoanHistoryContract.View mView;
	private final Context mContext;

	SalaryLoanHistoryPresenterModule(SalaryLoanHistoryContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SalaryLoanHistoryContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
