package com.ace.member.main.currency.deposit;

import dagger.Component;

@Component(modules = DepositPresenterModule.class)
public interface DepositComponent {
	void inject(DepositActivity depositActivity);
}
