package com.ace.member.main.currency;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrencyPresenterModule {

	private final CurrencyContract.View mView;
	private final Context mContext;

	public CurrencyPresenterModule(CurrencyContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	CurrencyContract.View provideCurrencyContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
