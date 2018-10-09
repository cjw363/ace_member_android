package com.ace.member.main.third_party.samrithisak_loan.loan;

import dagger.Component;

@Component(modules = LoanPresenterModule.class)
public interface LoanComponent {
	void inject(LoanActivity activity);
}
