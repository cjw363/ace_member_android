package com.ace.member.main.home.salary_loan.return_loan;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.SalaryLoanBean;

interface SalaryLoanReturnContract {
	interface View {
		void setData(double currentLoan, double balance);

		void setEnableBtnSubmit(boolean enable);

		void showLoanFail();

		void showLoanSuccess();

		AppCompatActivity getActivity();

		void showToast(String msg);
	}

	interface Presenter {
		void getSalaryLoanReturnConfig();

		boolean checkAmount(String amount);

		void checkSubmit(String amount);

		void submit(String amount);
	}
}
