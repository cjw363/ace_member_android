package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ACELoanPrepaymentBean extends BaseBean {

	private double amount;
	private double interest;
	private String currency;
	@SerializedName("loan_date")
	private String loanDate;

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getInterest() {
		return interest;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setLoanDate(String date){
		loanDate=date;
	}
	public String getLoanDate(){
		return loanDate;
	}
}
