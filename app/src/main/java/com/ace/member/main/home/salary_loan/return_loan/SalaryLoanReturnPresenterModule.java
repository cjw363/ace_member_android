package com.ace.member.main.home.salary_loan.return_loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class SalaryLoanReturnPresenterModule {

	private final SalaryLoanReturnContract.View mView;
	private final Context mContext;

	SalaryLoanReturnPresenterModule(SalaryLoanReturnContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SalaryLoanReturnContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
