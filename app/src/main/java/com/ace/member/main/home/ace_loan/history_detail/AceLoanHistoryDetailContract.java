package com.ace.member.main.home.ace_loan.history_detail;

import com.ace.member.bean.ACELoanRepayBean;

import java.util.List;

public interface AceLoanHistoryDetailContract {
	interface View{
		void initLoanDetail(List<ACELoanRepayBean> list);
	}
	interface Presenter{
		void getLoanDetail(int id);
	}
}
