package com.ace.member.main.home.ace_loan.loan;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.ACELoanBean;
import com.ace.member.bean.ACELoanRepayBean;
import com.google.gson.annotations.SerializedName;

public class MemberLoanAceData extends BaseBean {
	@SerializedName("loan_ace")
	private ACELoanBean loan;
	@SerializedName("repayment")
	private ACELoanRepayBean repayBean;

	public void setRepayBean(ACELoanRepayBean bean) {
		repayBean = bean;
	}

	public ACELoanRepayBean getRepayBean() {
		return repayBean;
	}

	public void setLoan(ACELoanBean bean) {
		loan = bean;
	}

	public ACELoanBean getLoan() {
		return loan;
	}
}
