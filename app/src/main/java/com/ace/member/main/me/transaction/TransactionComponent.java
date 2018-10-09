package com.ace.member.main.me.transaction;

import dagger.Component;

@Component(modules = TransactionPresenterModule.class)
public interface TransactionComponent {
	void inject(TransactionActivity transactionActivity);
}
