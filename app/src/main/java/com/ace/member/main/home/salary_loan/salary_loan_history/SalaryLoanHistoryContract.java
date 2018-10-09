package com.ace.member.main.home.salary_loan.salary_loan_history;

import com.ace.member.bean.SalaryLoanFlow;

import java.util.List;

interface SalaryLoanHistoryContract {

	interface View {
		void showData(int nextPage, boolean isHint);

		void showNextPageData(int nextPage);
	}

	interface Presenter {
		void getSalaryLoanHistory(int page);

		List<SalaryLoanFlow> getHistoryData();
	}
}
