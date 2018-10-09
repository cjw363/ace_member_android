package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class MemberLoanConfigWrapper extends BaseBean {

	@SerializedName("credit_loan")
	private MemberLoanPartner loanPartner;

	@SerializedName("loan_config")
	private LoanConfig loanConfig;

	@SerializedName("pending_amount")
	private double pendingAmount;

	@SerializedName("total_balance")
	private double totalBalance;

	@SerializedName("max_balance")
	private double maxBalance;

	@SerializedName("max_loan")
	private double maxLoan;

	public MemberLoanPartner getLoanPartner() {
		return loanPartner;
	}

	public void setLoanPartner(MemberLoanPartner loanPartner) {
		this.loanPartner = loanPartner;
	}

	public LoanConfig getLoanConfig() {
		return loanConfig;
	}

	public void setLoanConfig(LoanConfig loanConfig) {
		this.loanConfig = loanConfig;
	}

	public double getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(double pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public double getMaxBalance() {
		return maxBalance;
	}

	public void setMaxBalance(double maxBalance) {
		this.maxBalance = maxBalance;
	}

	public double getMaxLoan() {
		return maxLoan;
	}

	public void setMaxLoan(double maxLoan) {
		this.maxLoan = maxLoan;
	}

	public double getRealMaxLoan(){
		return Math.min(Math.min(maxLoan,loanConfig.getMaxLoanCreditPerMember()),loanConfig.getAvailableLoan());
	}
}
