package com.ace.member.main.home.ace_loan;

import com.ace.member.bean.ACELoanBean;
import com.ace.member.bean.ACELoanDetailBean;

import java.util.List;

public interface AceLoanContract {
	interface View{
		void initView(ACELoanBean bean);
		void initLoanDetail(List<ACELoanDetailBean> list);
		void showRefreshStatus(boolean isRefreshing);
	}
	interface Presenter{
		void getAceLoan(boolean isLoading);
	}
}
