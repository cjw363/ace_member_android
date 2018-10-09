package com.ace.member.main.home.ace_loan.loan;

import com.ace.member.bean.ACELoanRepayBean;
import com.ace.member.bean.TermBean;
import com.ace.member.main.bottom_dialog.ControllerBean;

import java.util.List;

public interface MemberLoanAceContract {
	interface View{
		void setCurrency(String currency);
		void initListView(List<TermBean> list, List<ControllerBean> list2);
		void initDayInterest(double rate);
		void initLoanAanCredit(double loan,double credit,String currency);
		void saveSucceed();
		String getAmount();
		void initNextMonthRepayment(ACELoanRepayBean bean);
	}
	interface Presenter{
		void getLoanData();
		void initTermListView();
		void submit(int number);
	}
}
