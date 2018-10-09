package com.ace.member.bean;

import com.google.gson.annotations.SerializedName;

public class ACELoanBean extends LoanBean {
	@SerializedName("repay_day")
	private int repayDay;
	@SerializedName("day_interest_rate")
	private double rate;

	public void setRepayDay(int day){
		repayDay=day;
	}
	public int getRepayDay(){
		return repayDay;
	}
	public void setRate(double rate){
		this.rate=rate;
	}
	public double getRate(){
		return rate;
	}
}
