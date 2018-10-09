package com.ace.member.main.home.ace_loan.ace_loan_prepayment;

import com.ace.member.bean.ACELoanPrepaymentBean;

import java.util.List;

public interface AceLoanPrepaymentContract {
	interface View {
		void initListView(List<ACELoanPrepaymentBean> list);

		void closeActivity(boolean bool);
	}

	interface Presenter {
		void getPrepayment(String str);

		void prepayment(String listStr);
	}
}
