package com.ace.member.main.detail.exchange_detail;

import dagger.Component;

@Component(modules = ExchangeDetailPresenterModule.class)
public interface ExchangeDetailComponent {
	void inject(ExchangeDetailActivity activity);
}

