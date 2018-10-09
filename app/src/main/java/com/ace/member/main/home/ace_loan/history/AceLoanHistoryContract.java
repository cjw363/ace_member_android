package com.ace.member.main.home.ace_loan.history;

import com.ace.member.bean.ACELoanDetailBean;

import java.util.List;

public interface AceLoanHistoryContract {
	interface View{
		void initView(List<ACELoanDetailBean> list,int nexPage,boolean canLoadMore);
		void nextPage(List<ACELoanDetailBean> list,int nextPage);
	}
	interface Presenter{
		void getLoadDetailList(String from,String to,int page);
	}
}
