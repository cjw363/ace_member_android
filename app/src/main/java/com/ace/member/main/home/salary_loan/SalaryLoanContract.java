package com.ace.member.main.home.salary_loan;


class SalaryLoanContract {
	interface SalaryLoanView{
		void setData(SalaryLoanData data);

		void setBtnLoanEnable(boolean enable);

		void setBtnReturnLoanEnable(boolean enable);

		void showRefreshStatus(boolean isRefreshing);

		void hideInfo();

		void showToast(String msg);
	}

	interface SalaryLoanPresenter{
		void getSalaryLoanData(boolean isLoading);
	}
}
