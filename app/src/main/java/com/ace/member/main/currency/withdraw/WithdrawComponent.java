package com.ace.member.main.currency.withdraw;

import dagger.Component;

@Component(modules = WithdrawPresenterModule.class)
public interface WithdrawComponent {
	void inject(WithdrawActivity withdrawActivity);
}
