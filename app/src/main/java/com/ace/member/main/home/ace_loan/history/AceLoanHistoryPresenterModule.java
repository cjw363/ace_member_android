package com.ace.member.main.home.ace_loan.history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceLoanHistoryPresenterModule {

	private final AceLoanHistoryContract.View mView;
	private final Context mContext;

	public AceLoanHistoryPresenterModule(AceLoanHistoryContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	AceLoanHistoryContract.View providesView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
