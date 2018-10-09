package com.ace.member.main.home.money.withdraw_cash;

import dagger.Component;

@Component(modules = WithdrawCashPresenterModule.class)
public interface WithdrawCashComponent {
	void inject(WithdrawCashActivity WithdrawCashActivity);
}
