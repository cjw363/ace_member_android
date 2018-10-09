package com.ace.member.main.home.receive_to_acct.detail;

import dagger.Component;

@Component(modules = R2ADetailPresenterModule.class)
public interface R2ADetailComponent {
	void inject(R2ADetailActivity transferRecentDetailActivity);
}
