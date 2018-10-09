package com.ace.member.main.third_party.samrithisak_loan.history;

import dagger.Component;

@Component(modules = HistoryPresenterModule.class)
public interface HistoryComponent {
	void inject(HistoryActivity activity);
}
