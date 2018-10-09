package com.ace.member.main.home.ace_loan.loan_detail;

import com.ace.member.bean.ACELoanDetailBean;

public interface AceLoanDetailContract {
	interface View{
		void initView(ACELoanDetailBean bean);
	}
	interface Presenter{
		void getLoanPlanDetail(int id);
	}
}
