package com.ace.member.main.me.password.trading_password;

import dagger.Component;

@Component(modules = TradingPasswordPresenterModule.class)
public interface TradingPasswordComponent {
	void inject(TradingPasswordActivity activity);
}
