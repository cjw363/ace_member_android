package com.ace.member.main.detail.exchange_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class ExchangeDetailPresenterModule {

	private final ExchangeDetailContract.View mView;
	private final Context mContext;

	ExchangeDetailPresenterModule(ExchangeDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ExchangeDetailContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
