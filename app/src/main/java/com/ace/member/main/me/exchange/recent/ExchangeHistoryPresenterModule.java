package com.ace.member.main.me.exchange.recent;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ExchangeHistoryPresenterModule {

	private final ExchangeHistoryContract.HistoryView mExchangeHistoryView;
	private final Context mContext;

	public ExchangeHistoryPresenterModule(ExchangeHistoryContract.HistoryView mExchangeHistoryView, Context mContext) {
		this.mExchangeHistoryView = mExchangeHistoryView;
		this.mContext = mContext;
	}

	@Provides
	ExchangeHistoryContract.HistoryView providesExchangeHistoryContractView() {return mExchangeHistoryView;}

	@Provides
	Context providesContext() {return mContext;}
}
