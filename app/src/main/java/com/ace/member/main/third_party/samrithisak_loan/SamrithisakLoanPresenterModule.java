package com.ace.member.main.third_party.samrithisak_loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SamrithisakLoanPresenterModule {

	private final SamrithisakLoanContract.View mView;
	private final Context mContext;

	public SamrithisakLoanPresenterModule(SamrithisakLoanContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SamrithisakLoanContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
