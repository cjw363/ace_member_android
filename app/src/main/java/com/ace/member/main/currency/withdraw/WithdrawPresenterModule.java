package com.ace.member.main.currency.withdraw;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WithdrawPresenterModule {

	private final WithdrawContract.WithdrawView mWithdrawView;
	private final Context mContext;

	public WithdrawPresenterModule(WithdrawContract.WithdrawView withdrawView, Context context) {
		mWithdrawView = withdrawView;
		mContext = context;
	}

	@Provides
	WithdrawContract.WithdrawView provideWithdrawContractView() {
		return mWithdrawView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
