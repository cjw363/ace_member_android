package com.ace.member.main.home.ace_loan.return_loan;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.ACELoanRepayBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AceLoanReturnData extends BaseBean {

	@SerializedName("capital_amount")
	private double amount;
	@SerializedName("plan_interest_amount")
	private double interest;
	@SerializedName("list_detail")
	private List<ACELoanDetailBean> listDetail;

	public void setListDetail(List<ACELoanDetailBean> list) {
		this.listDetail = list;
	}

	public List<ACELoanDetailBean> getListDetail() {
		return this.listDetail;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setInterest(double amount) {
		this.interest = amount;
	}

	public double getInterest() {
		return interest;
	}
}
