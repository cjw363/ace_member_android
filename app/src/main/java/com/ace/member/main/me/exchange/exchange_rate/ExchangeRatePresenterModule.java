package com.ace.member.main.me.exchange.exchange_rate;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ExchangeRatePresenterModule {

	private final ExchangeRateContract.ExchangeRateContractView mView;
	private final Context mContext;


	public ExchangeRatePresenterModule(ExchangeRateContract.ExchangeRateContractView mView, Context mContext) {this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	ExchangeRateContract.ExchangeRateContractView provideExchangeRateContractView(){return mView;}

	@Provides
	Context provideContext(){return mContext;}
}
