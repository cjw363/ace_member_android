package com.ace.member.main.home.salary_loan.salary_loan_detail;

import dagger.Component;

@Component(modules = SalaryLoanDetailPresenterModule.class)
interface SalaryLoanDetailComponent {
	void inject(SalaryLoanDetailActivity activity);
}

