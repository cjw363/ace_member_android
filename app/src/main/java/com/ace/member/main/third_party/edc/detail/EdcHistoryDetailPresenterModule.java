package com.ace.member.main.third_party.edc.detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class EdcHistoryDetailPresenterModule {

	private final EdcHistoryDetailContract.View mView;
	private final Context mContext;

	public EdcHistoryDetailPresenterModule(EdcHistoryDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	EdcHistoryDetailContract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
