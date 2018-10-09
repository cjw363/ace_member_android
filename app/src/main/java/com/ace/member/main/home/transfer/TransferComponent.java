package com.ace.member.main.home.transfer;

import dagger.Component;

@Component(modules = TransferPresenterModule.class)
public interface TransferComponent {
	void inject(TransferActivity transferActivity);
}
