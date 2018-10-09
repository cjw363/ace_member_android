package com.ace.member.main.home.salary_loan.salary_loan_history;

import com.ace.member.main.home.salary_loan.salary_loan_detail.SalaryLoanDetailActivity;

import dagger.Component;

@Component(modules = SalaryLoanHistoryPresenterModule.class)
interface SalaryLoanHistoryComponent {
	void inject(SalaryLoanHistoryActivity activity);
}

