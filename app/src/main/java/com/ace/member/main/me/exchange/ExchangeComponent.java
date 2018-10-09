package com.ace.member.main.me.exchange;

import dagger.Component;

@Component(modules = ExchangePresenterModule.class)
public interface ExchangeComponent {
	void inject(ExchangeActivity activity);
}
