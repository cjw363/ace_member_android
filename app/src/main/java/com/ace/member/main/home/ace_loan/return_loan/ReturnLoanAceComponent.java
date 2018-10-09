package com.ace.member.main.home.ace_loan.return_loan;

import dagger.Component;

@Component(modules = ReturnLoanAcePresenterModule.class)
public interface ReturnLoanAceComponent {
	void inject(ReturnLoanAceActivity aceActivity);
}
