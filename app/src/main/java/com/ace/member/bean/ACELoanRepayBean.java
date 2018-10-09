package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ACELoanRepayBean extends BaseBean {
	private int id;
	@SerializedName("plan_date")
	private String planDate;
	private String currency;
	@SerializedName("capital_amount")
	private double capitalAmount;
	@SerializedName("plan_interest_amount")
	private double planAmount;
	@SerializedName("actual_date")
	private String actualDate;
	@SerializedName("actual_interest_amount")
	private double actualInterestAmount;
	@SerializedName("actual_penalty_amount")
	private double actualPenaltyAmount;
	private int status;

	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return id;
	}

	public void setActualDate(String date) {
		actualDate = date;
	}

	public String getActualDate() {
		return actualDate;
	}

	public void setActualInterestAmount(double amount) {
		actualInterestAmount = amount;
	}

	public double getActualInterestAmount() {
		return actualInterestAmount;
	}

	public void setActualPenaltyAmount(double amount) {
		actualPenaltyAmount = amount;
	}

	public double getActualPenaltyAmount() {
		return actualPenaltyAmount;
	}

	public void setPlanDate(String date) {
		planDate = date;
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCapitalAmount(double amount) {
		this.capitalAmount = amount;
	}

	public double getCapitalAmount() {
		return capitalAmount;
	}

	public void setPlanAmount(double amount) {
		planAmount = amount;
	}

	public double getPlanAmount() {
		return planAmount;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
