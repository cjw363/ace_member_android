package com.ace.member.main.third_party.samrithisak_loan.loan;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.MemberLoanPartner;

public interface LoanContract {
	interface View {

		void setCreditAndLoan(MemberLoanPartner memberLoanPartner);

		void setEnableServiceCharge(boolean enable);

		void setServiceCharge(String charge);

		void setEnableBtnSubmit(boolean enable);

		void showLoanFail();

		void showLoanSuccess();

		AppCompatActivity getActivity();
	}

	interface Presenter {
		void start();

		void updateServiceCharge(String amount);

		boolean checkAmount(String amount);
		void checkSubmit(String amount);
	}
}
