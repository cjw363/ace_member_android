package com.ace.member.main.home.ace_loan.loan;

import dagger.Component;

@Component(modules = MemberLoanAcePresenterModule.class)
public interface MemberLoanAceComponent {
	void inject(MemberLoanAceActivity aceActivity);
}
