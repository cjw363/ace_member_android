package com.ace.member.main.home.ace_loan.repay_plan;

import dagger.Component;

@Component(modules = AceLoanRepayPlanPresenterModule.class)
public interface AceLoanRepayPlanComponent {
	void inject(AceLoanRepayPlanActivity aceLoanRepayPlanActivity);
}
