package com.ace.member.main.home.salary_loan.loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class SalaryLoanLoanPresenterModule {

	private final SalaryLoanLoanContract.View mView;
	private final Context mContext;

	SalaryLoanLoanPresenterModule(SalaryLoanLoanContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SalaryLoanLoanContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
