package com.ace.member.main.me.exchange.recent;

import dagger.Component;

@Component(modules = ExchangeHistoryPresenterModule.class)
public interface ExchangeHistoryComponent {
	void inject(ExchangeHistoryActivity activity);
}
