package com.ace.member.main.detail.deposit_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class DepositDetailPresenterModule {

	private final DepositDetailContract.View mView;
	private final Context mContext;

	public DepositDetailPresenterModule(DepositDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	DepositDetailContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
