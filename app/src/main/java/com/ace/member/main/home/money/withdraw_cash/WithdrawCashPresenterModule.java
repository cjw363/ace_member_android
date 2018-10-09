package com.ace.member.main.home.money.withdraw_cash;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WithdrawCashPresenterModule {

	private final WithdrawCashContract.WithdrawCashView mWithdrawCashView;
	private final Context mContext;

	public WithdrawCashPresenterModule(WithdrawCashContract.WithdrawCashView mWithdrawCashView, Context mContext) {this.mWithdrawCashView = mWithdrawCashView;
		this.mContext = mContext;
	}

	@Provides
	WithdrawCashContract.WithdrawCashView provideWithdrawCashContractView(){return mWithdrawCashView;}

	@Provides
	Context provideContext(){return mContext;}
}
