package com.ace.member.main.home.money.deposit_cash;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class DepositCashPresenterModule {

	private final DepositCashContract.DepositCashView mDepositCashView;
	private final Context mContext;

	public DepositCashPresenterModule(DepositCashContract.DepositCashView mDepositCashView, Context mContext) {this.mDepositCashView = mDepositCashView;
		this.mContext = mContext;
	}

	@Provides
	DepositCashContract.DepositCashView provideDepositCashContractView(){return mDepositCashView;}

	@Provides
	Context provideContext(){return mContext;}
}
