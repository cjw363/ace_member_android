package com.ace.member.main.home.salary_loan.loan;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.bean.SalaryLoanBean;
import com.ace.member.bean.SalaryLoanConfigBean;

interface SalaryLoanLoanContract {
	interface View {

		void setCreditAndLoan(SalaryLoanBean salaryLoanBean);

		void setEnableServiceCharge(boolean enable);

		void setServiceCharge(String charge);

		void setEnableBtnSubmit(boolean enable);

		void showLoanFail();

		void showLoanSuccess();

		AppCompatActivity getActivity();

		void showToast(String msg);
	}

	interface Presenter {
		void getSalaryLoanConfig();

		void updateServiceCharge(String amount);

		boolean checkAmount(String amount);

		void checkSubmit(String amount);

		void submit(String amount);
	}
}
