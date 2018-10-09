package com.ace.member.main.detail.deposit_detail;

import dagger.Component;

@Component(modules = DepositDetailPresenterModule.class)
public interface DepositDetailComponent {
	void inject(DepositDetailActivity activity);
}

