package com.ace.member.main.currency.deposit;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class DepositPresenterModule {

	private final DepositContract.DepositView mDepositView;
	private final Context mContext;

	public DepositPresenterModule(DepositContract.DepositView depositView, Context context) {
		mDepositView = depositView;
		mContext = context;
	}

	@Provides
	DepositContract.DepositView provideDepositContractView() {
		return mDepositView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
