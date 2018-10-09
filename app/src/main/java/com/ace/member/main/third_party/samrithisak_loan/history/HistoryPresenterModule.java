package com.ace.member.main.third_party.samrithisak_loan.history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class HistoryPresenterModule {

	private final HistoryContract.View mView;
	private final Context mContext;

	public HistoryPresenterModule(HistoryContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	HistoryContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
