package com.ace.member.main.home.ace_loan.repay_plan_list;

import com.ace.member.bean.ACELoanRepayBean;

import java.util.List;

public interface AceRepayPlanListContract {
	interface View{
		void initListView(List<ACELoanRepayBean> list);
	}
	interface Presenter{
		void getRepayPlanList();
	}
}
