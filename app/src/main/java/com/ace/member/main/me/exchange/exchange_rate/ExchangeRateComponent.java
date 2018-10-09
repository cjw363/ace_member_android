package com.ace.member.main.me.exchange.exchange_rate;


import dagger.Component;

@Component(modules = ExchangeRatePresenterModule.class)
public interface ExchangeRateComponent {
	void inject(ExchangeRateActivity activity);
}
