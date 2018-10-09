package com.ace.member.main.home.ace_loan.loan_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceLoanDetailPresenterModule {
	private final AceLoanDetailContract.View mView;
	private final Context mContext;

	public AceLoanDetailPresenterModule(AceLoanDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	AceLoanDetailContract.View providesView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
