package com.ace.member.main.home.ace_loan.repay_plan;

import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.ACELoanRepayBean;

import java.util.List;

public interface AceLoanRepayPlanContract {
	interface View {
		void initView(double amount, double amount2,double rate, List<ACELoanRepayBean> list);
	}

	interface Presenter {
		void getData(int id);
	}
}
