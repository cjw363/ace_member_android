package com.ace.member.main.currency;

import dagger.Component;

@Component(modules = CurrencyPresenterModule.class)
public interface CurrencyComponent {
	void inject(CurrencyActivity activity);
}

