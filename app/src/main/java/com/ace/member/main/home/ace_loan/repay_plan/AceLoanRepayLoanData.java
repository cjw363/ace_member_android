package com.ace.member.main.home.ace_loan.repay_plan;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.ACELoanRepayBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AceLoanRepayLoanData extends BaseBean {
	@SerializedName("plan_interest_amount")
	private double planInterestAmount;
	@SerializedName("capital_amount")
	private double capitalAmount;
	@SerializedName("list_repay")
	private List<ACELoanRepayBean> list;
	@SerializedName("interest_rate")
	private double interestRate;

	public void setPlanInterestAmount(double amount) {
		planInterestAmount = amount;
	}

	public double getPlanInterestAmount() {
		return planInterestAmount;
	}

	public void setCapitalAmount(double amount) {
		capitalAmount = amount;
	}

	public double getCapitalAmount() {
		return capitalAmount;
	}

	public void setList(List<ACELoanRepayBean> list) {
		this.list = list;
	}

	public List<ACELoanRepayBean> getList() {
		return list;
	}

	public void setInterestRate(double rate){
		interestRate=rate;
	}
	public double getInterestRate(){
		return interestRate;
	}
}
