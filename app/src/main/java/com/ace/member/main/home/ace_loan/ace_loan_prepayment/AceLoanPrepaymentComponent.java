package com.ace.member.main.home.ace_loan.ace_loan_prepayment;

import dagger.Component;

@Component(modules = AceLoanPrepaymentPresenterModule.class)
public interface AceLoanPrepaymentComponent {
	void inject(AceLoanPrepaymentActivity aceLoanPrepaymentActivity);
}
