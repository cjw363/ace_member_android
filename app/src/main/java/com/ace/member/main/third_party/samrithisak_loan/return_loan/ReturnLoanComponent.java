package com.ace.member.main.third_party.samrithisak_loan.return_loan;

import dagger.Component;

@Component(modules = ReturnLoanPresenterModule.class)
public interface ReturnLoanComponent {
	void inject(ReturnLoanActivity activity);
}
