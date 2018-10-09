package com.ace.member.main.home.ace_loan.ace_loan_prepayment;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceLoanPrepaymentPresenterModule {
	private final AceLoanPrepaymentContract.View mView;
	private final Context mContext;

	public AceLoanPrepaymentPresenterModule(AceLoanPrepaymentContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	AceLoanPrepaymentContract.View providesView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
