package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class CreditBean extends BaseBean {
	private String time;
	private double amount;
	private double interest;
	private double total_amount;

	public void setAmount(double amount) {
		this.amount = amount;
	}

	private double getAmount() {
		return amount;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getInterest() {
		return this.interest;
	}

	public void setTotalAmount(double amount) {
		total_amount = amount;
	}

	public double getTotalAmount() {
		return total_amount;
	}

	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return time;
	}
}
