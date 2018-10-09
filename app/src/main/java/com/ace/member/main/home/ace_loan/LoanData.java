package com.ace.member.main.home.ace_loan;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.ACELoanBean;
import com.ace.member.bean.ACELoanDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoanData extends BaseBean {

	@SerializedName("loan_ace")
	private ACELoanBean loanBean;
	@SerializedName("loan_ace_list")
	private List<ACELoanDetailBean> list;

	public void setLoanBean(ACELoanBean bean) {
		this.loanBean = bean;
	}

	public ACELoanBean getLoanBean() {
		return loanBean;
	}

	public void setLoanList(List<ACELoanDetailBean> list) {
		this.list = list;
	}

	public List<ACELoanDetailBean> getLoanList() {
		return list;
	}
}
