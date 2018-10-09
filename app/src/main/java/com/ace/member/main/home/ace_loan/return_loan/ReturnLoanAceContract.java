package com.ace.member.main.home.ace_loan.return_loan;

import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.ACELoanRepayBean;

import java.util.List;

public interface ReturnLoanAceContract {
	interface View {
		void initLoanDetail(List<ACELoanDetailBean> list);
//		void initLoanRepay(List<ACELoanRepayBean> list);
		void initView(double amount,double interest);
		void showRefreshStatus(boolean isRefreshing);
	}

	interface Presenter {
		void getLoanDetailList(String time,String time2,boolean loading);
	}
}
