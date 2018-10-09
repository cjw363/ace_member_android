package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class MemberLoanDateWrapper extends BaseBean {

	@SerializedName("credit_loan")
	private MemberLoanPartner loanPartner;

	@SerializedName("latest_bill")
	private MemberLoanPartnerBill loanPartnerBill;

	@SerializedName("service_charge_rate")
	private double serviceChargeRate;

	@SerializedName("service_charge_min_amount")
	private int serviceChargeMinAmount;

	public MemberLoanPartner getLoanPartner() {
		return loanPartner;
	}

	public void setLoanPartner(MemberLoanPartner loanPartner) {
		this.loanPartner = loanPartner;
	}

	public MemberLoanPartnerBill getLoanPartnerBill() {
		return loanPartnerBill;
	}

	public void setLoanPartnerBill(MemberLoanPartnerBill loanPartnerBill) {
		this.loanPartnerBill = loanPartnerBill;
	}

	public double getServiceChargeRate() {
		return serviceChargeRate;
	}

	public void setServiceChargeRate(double serviceChargeRate) {
		this.serviceChargeRate = serviceChargeRate;
	}

	public int getServiceChargeMinAmount() {
		return serviceChargeMinAmount;
	}

	public void setServiceChargeMinAmount(int serviceChargeMinAmount) {
		this.serviceChargeMinAmount = serviceChargeMinAmount;
	}
}
