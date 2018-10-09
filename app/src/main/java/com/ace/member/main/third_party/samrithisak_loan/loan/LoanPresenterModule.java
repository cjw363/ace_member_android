package com.ace.member.main.third_party.samrithisak_loan.loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class LoanPresenterModule {

	private final LoanContract.View mView;
	private final Context mContext;

	public LoanPresenterModule(LoanContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	LoanContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
