package com.ace.member.main.home.salary_loan.loan;

import dagger.Component;

@Component(modules = SalaryLoanLoanPresenterModule.class)
interface SalaryLoanLoanComponent {
	void inject(SalaryLoanLoanActivity activity);
}
