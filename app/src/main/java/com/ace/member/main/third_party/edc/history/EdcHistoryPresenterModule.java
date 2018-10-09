package com.ace.member.main.third_party.edc.history;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class EdcHistoryPresenterModule {

	private final EdcHistoryContract.View mView;
	private final Context mContext;

	public EdcHistoryPresenterModule(EdcHistoryContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	EdcHistoryContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
