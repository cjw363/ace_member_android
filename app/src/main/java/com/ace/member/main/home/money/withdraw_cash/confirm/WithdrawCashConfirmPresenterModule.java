package com.ace.member.main.home.money.withdraw_cash.confirm;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class WithdrawCashConfirmPresenterModule {

	private final WithdrawCashConfirmContract.WithdrawCashConfirmView mWithdrawCashConfirmView;
	private final Context mContext;

	public WithdrawCashConfirmPresenterModule(WithdrawCashConfirmContract.WithdrawCashConfirmView mWithdrawCashConfirmView, Context mContext) {
		this.mWithdrawCashConfirmView = mWithdrawCashConfirmView;
		this.mContext = mContext;
	}

	@Provides
	WithdrawCashConfirmContract.WithdrawCashConfirmView provideWithdrawCashConfirmContractView() {return mWithdrawCashConfirmView;}

	@Provides
	Context provideContext() {return mContext;}
}
