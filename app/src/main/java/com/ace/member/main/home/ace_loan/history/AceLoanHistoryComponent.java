package com.ace.member.main.home.ace_loan.history;

import dagger.Component;

@Component(modules = AceLoanHistoryPresenterModule.class)
public interface AceLoanHistoryComponent {
	void inject(AceLoanHistoryActivity aceLoanHistoryActivity);
}
