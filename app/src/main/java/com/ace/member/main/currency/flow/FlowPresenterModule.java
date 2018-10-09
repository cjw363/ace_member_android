package com.ace.member.main.currency.flow;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class FlowPresenterModule {

	private final FlowContract.View mView;
	private final Context mContext;

	public FlowPresenterModule(FlowContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	FlowContract.View provideCurrencyContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
