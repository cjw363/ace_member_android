package com.ace.member.main.home.salary_loan.salary_loan_detail;

import com.ace.member.bean.SalaryLoanFlow;

interface SalaryLoanDetailContract {

	interface View {
		void setDetail(SalaryLoanFlow salaryLoanFlow);
	}

	interface Presenter {
		void getSalaryLoanDetail(int id);
	}
}
