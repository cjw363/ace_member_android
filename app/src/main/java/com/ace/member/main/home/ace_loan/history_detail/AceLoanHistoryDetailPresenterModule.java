package com.ace.member.main.home.ace_loan.history_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceLoanHistoryDetailPresenterModule {
	private final AceLoanHistoryDetailContract.View mView;
	private final Context mContext;

	public AceLoanHistoryDetailPresenterModule(AceLoanHistoryDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	AceLoanHistoryDetailContract.View providesView(){
		return mView;
	}

	@Provides
	Context providesContext(){
		return mContext;
	}
}
