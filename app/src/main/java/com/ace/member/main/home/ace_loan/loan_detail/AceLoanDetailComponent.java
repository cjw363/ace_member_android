package com.ace.member.main.home.ace_loan.loan_detail;

import dagger.Component;

@Component(modules = AceLoanDetailPresenterModule.class)
public interface AceLoanDetailComponent {
	void inject(AceLoanDetailActivity aceLoanRepayPlanDetailActivity);
}
