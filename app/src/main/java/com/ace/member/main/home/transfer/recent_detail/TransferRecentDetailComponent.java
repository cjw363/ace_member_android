package com.ace.member.main.home.transfer.recent_detail;

import dagger.Component;

@Component(modules = TransferRecentDetailPresenterModule.class)
public interface TransferRecentDetailComponent {
	void inject(TransferRecentDetailActivity transferRecentDetailActivity);
}
