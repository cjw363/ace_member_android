package com.ace.member.main.home.salary_loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SalaryLoanPresenterModule {

	private final SalaryLoanContract.SalaryLoanView mSalaryLoanView;
	private final Context mContext;


	public SalaryLoanPresenterModule(SalaryLoanContract.SalaryLoanView salaryLoanView, Context context) {
		mSalaryLoanView = salaryLoanView;
		mContext = context;
	}

	@Provides
	SalaryLoanContract.SalaryLoanView provideSalaryLoanContractView() {return mSalaryLoanView;}

	@Provides
	Context providesContext() {
		return mContext;
	}

}
