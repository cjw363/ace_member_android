package com.ace.member.main.home.money.deposit_cash;

import dagger.Component;

@Component(modules = DepositCashPresenterModule.class)
public interface DepositCashComponent {
	void inject(DepositCashActivity DepositCashActivity);
}
