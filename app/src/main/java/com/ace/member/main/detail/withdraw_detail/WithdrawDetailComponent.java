package com.ace.member.main.detail.withdraw_detail;

import dagger.Component;

@Component(modules = WithdrawDetailPresenterModule.class)
public interface WithdrawDetailComponent {
	void inject(WithdrawDetailActivity activity);
}

