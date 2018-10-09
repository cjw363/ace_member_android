package com.ace.member.main.home.receive_to_acct.history;

import dagger.Component;

@Component(modules = R2ARecentPresenterModule.class)
public interface R2ARecentComponent {
	void inject(R2ARecentActivity activity);
}
