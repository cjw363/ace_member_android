package com.ace.member.main.home.salary_loan.return_loan;

import dagger.Component;

@Component(modules = SalaryLoanReturnPresenterModule.class)
interface SalaryLoanReturnComponent {
	void inject(SalaryLoanReturnActivity activity);
}
