package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class TermBean extends BaseBean {
	private int term;
	private double amount;
	private String currency;
	private double interest;
//	private boolean isCheck = false;

	public void setTerm(int term) {
		this.term = term;
	}

	public int getTerm() {
		return term;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getInterest() {
		return interest;
	}

//	public void setCheck(boolean check) {
//		isCheck = check;
//	}

//	public boolean getCheck() {
//		return isCheck;
//	}

}
