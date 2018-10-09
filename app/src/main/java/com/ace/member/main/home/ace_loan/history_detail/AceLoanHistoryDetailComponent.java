package com.ace.member.main.home.ace_loan.history_detail;

import dagger.Component;

@Component(modules = AceLoanHistoryDetailPresenterModule.class)
public interface AceLoanHistoryDetailComponent {
	void inject(AceLoanHistoryDetailActivity activity);
}
