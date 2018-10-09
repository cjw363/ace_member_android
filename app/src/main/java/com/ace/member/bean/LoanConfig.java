package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class LoanConfig extends BaseBean {
	@SerializedName("partner_id")
	private int partnerId;

	@SerializedName("service_charge_rate")
	private int serviceChargeRate;

	@SerializedName("service_charge_min_amount")
	private int serviceChargeMinAmount;

	@SerializedName("available_loan")
	private int availableLoan;

	@SerializedName("min_loan_credit_per_member")
	private int minLoanCreditPerMember;

	@SerializedName("max_loan_credit_per_member")
	private int maxLoanCreditPerMember;

	@SerializedName("is_over_member_count_limit")
	private boolean isOverMemberCountLimit;

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public int getServiceChargeRate() {
		return serviceChargeRate;
	}

	public void setServiceChargeRate(int serviceChargeRate) {
		this.serviceChargeRate = serviceChargeRate;
	}

	public int getServiceChargeMinAmount() {
		return serviceChargeMinAmount;
	}

	public void setServiceChargeMinAmount(int serviceChargeMinAmount) {
		this.serviceChargeMinAmount = serviceChargeMinAmount;
	}


	public int getAvailableLoan() {
		return availableLoan;
	}

	public void setAvailableLoan(int availableLoan) {
		this.availableLoan = availableLoan;
	}

	public int getMinLoanCreditPerMember() {
		return minLoanCreditPerMember;
	}

	public void setMinLoanCreditPerMember(int minLoanCreditPerMember) {
		this.minLoanCreditPerMember = minLoanCreditPerMember;
	}

	public int getMaxLoanCreditPerMember() {
		return maxLoanCreditPerMember;
	}

	public void setMaxLoanCreditPerMember(int maxLoanCreditPerMember) {
		this.maxLoanCreditPerMember = maxLoanCreditPerMember;
	}

	public boolean isOverMemberCountLimit() {
		return isOverMemberCountLimit;
	}

	public void setOverMemberCountLimit(boolean overMemberCountLimit) {
		isOverMemberCountLimit = overMemberCountLimit;
	}
}
