package com.ace.member.main.third_party.samrithisak_loan.return_loan;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.MemberLoanPartner;

public interface ReturnLoanContract {
	interface View {

		void setCreditAndLoan(MemberLoanPartner memberLoanPartner);


		void setEnableBtnSubmit(boolean enable);

		void showLoanFail();

		void showReturnLoanSuccess();

		AppCompatActivity getActivity();
	}

	interface Presenter {
		void start();

		boolean checkAmount(String amount);

		void checkSubmit(String amount);

		int getCurrentLoan();
	}
}
