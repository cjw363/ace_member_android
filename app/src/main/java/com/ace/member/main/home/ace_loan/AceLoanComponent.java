package com.ace.member.main.home.ace_loan;

import dagger.Component;

@Component(modules = AceLoanPresenterModule.class)
public interface AceLoanComponent {
	void inject(AceLoanActivity aceLoanActivity);
}
