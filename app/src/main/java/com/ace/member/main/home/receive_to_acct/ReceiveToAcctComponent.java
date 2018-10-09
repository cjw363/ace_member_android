package com.ace.member.main.home.receive_to_acct;

import dagger.Component;

@Component(modules = ReceiveToAcctPresenterModule.class)
public interface ReceiveToAcctComponent {
	void inject(ReceiveToAcctActivity activity);
}

