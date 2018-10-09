package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SalaryLoanConfigBean extends BaseBean {

	/**
	 * currency : USD
	 * min_amount : 100
	 * max_amount : 500
	 * total_amount : 1000
	 * loan_amount_unit : 10
	 * service_charge_unit : 100
	 * service_charge_amount : 5
	 */
	private String currency;
	@SerializedName("min_loan_credit_per_member")
	private double minAmount;
	@SerializedName("max_loan_credit_per_member")
	private double maxAmount;
	@SerializedName("member_total_loan_limit")
	private double totalAmount;
	@SerializedName("loan_amount_unit")
	private int loanAmountUnit;
	@SerializedName("service_charge_rate")
	private double serviceChargeRate;
	@SerializedName("service_charge_min_amount")
	private double serviceChargeMinAmount;
	@SerializedName("available_loan")
	private double availableLoan;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getLoanAmountUnit() {
		return loanAmountUnit;
	}

	public void setLoanAmountUnit(int loanAmountUnit) {
		this.loanAmountUnit = loanAmountUnit;
	}

	public double getServiceChargeRate() {
		return serviceChargeRate;
	}

	public void setServiceChargeRate(double serviceChargeRate) {
		this.serviceChargeRate = serviceChargeRate;
	}

	public double getServiceChargeMinAmount() {
		return serviceChargeMinAmount;
	}

	public void setServiceChargeMinAmount(double serviceChargeMinAmount) {
		this.serviceChargeMinAmount = serviceChargeMinAmount;
	}

	public double getAvailableLoan() {
		return availableLoan;
	}

	public void setAvailableLoan(double availableLoan) {
		this.availableLoan = availableLoan;
	}
}
