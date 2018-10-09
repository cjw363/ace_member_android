package com.ace.member.main.home.salary_loan;

import dagger.Component;

@Component(modules = SalaryLoanPresenterModule.class)
public interface SalaryLoanComponent {
	void inject(SalaryLoanActivity activity);
}
