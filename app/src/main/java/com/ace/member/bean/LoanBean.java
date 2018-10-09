package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class LoanBean extends BaseBean {
	private String currency;
	private double credit;
	private double loan;

	public void setCurrency(String currency){
		this.currency=currency;
	}
	public String getCurrency(){
		return currency;
	}
	public void setCredit(double credit){
		this.credit=credit;
	}
	public double getCredit(){
		return credit;
	}
	public void setLoan(double loan){
		this.loan=loan;
	}
	public double getLoan(){
		return  loan;
	}
}
