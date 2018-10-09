package com.ace.member.main.home.money.withdraw_cash.confirm;

import dagger.Component;

@Component(modules = WithdrawCashConfirmPresenterModule.class)
public interface WithdrawCashConfirmComponent {
	void inject(WithdrawCashConfirmActivity withdrawCashConfirmActivity);
}
