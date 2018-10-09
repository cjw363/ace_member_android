package com.ace.member.main.home.transfer.recent;

import dagger.Component;

@Component(modules = TransferRecentPresenterModule.class)
public interface TransferRecentComponent {
	void inject(TransferRecentActivity transferRecentActivity);
}
