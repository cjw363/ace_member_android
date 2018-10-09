package com.ace.member.main.me.exchange;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ExchangePresenterModule {

	private final ExchangeContract.ExchangeContractView mView;
	private final Context mContext;


	public ExchangePresenterModule(ExchangeContract.ExchangeContractView mView, Context mContext) {this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	ExchangeContract.ExchangeContractView provideExchangeContractView(){return mView;}

	@Provides
	Context provideContext(){return mContext;}
}
