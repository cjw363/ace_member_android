package com.ace.member.main.third_party.samrithisak_loan.return_loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ReturnLoanPresenterModule {

	private final ReturnLoanContract.View mView;
	private final Context mContext;

	public ReturnLoanPresenterModule(ReturnLoanContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ReturnLoanContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
