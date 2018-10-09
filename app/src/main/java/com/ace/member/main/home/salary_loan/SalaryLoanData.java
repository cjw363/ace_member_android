package com.ace.member.main.home.salary_loan;


import com.ace.member.base.BaseBean;
import com.ace.member.bean.SalaryLoanBean;
import com.ace.member.bean.SalaryLoanConfigBean;
import com.ace.member.bean.SalaryLoanFlow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalaryLoanData extends BaseBean {

	@SerializedName("loan_config")
	private SalaryLoanConfigBean loanConfig;

	@SerializedName("credit_loan")
	private SalaryLoanBean creditLoan;

	@SerializedName("loan_flow")
	private List<SalaryLoanFlow> loanFlowList;

	@SerializedName("pending_amount")
	private double pendingAmount;

	@SerializedName("total_balance")
	private double totalBalance;

	@SerializedName("max_balance")
	private double maxBalance;

	@SerializedName("usd_balance")
	private double usdBalance;

	public SalaryLoanConfigBean getLoanConfig() {
		return loanConfig;
	}

	public void setLoanConfig(SalaryLoanConfigBean loanConfig) {
		this.loanConfig = loanConfig;
	}

	public SalaryLoanBean getCreditLoan() {
		return creditLoan;
	}

	public void setCreditLoan(SalaryLoanBean creditLoan) {
		this.creditLoan = creditLoan;
	}

	public List<SalaryLoanFlow> getLoanFlowList() {
		return loanFlowList;
	}

	public void setLoanFlowList(List<SalaryLoanFlow> loanFlowList) {
		this.loanFlowList = loanFlowList;
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

	public double getUsdBalance() {
		return usdBalance;
	}

	public void setUsdBalance(double usdBalance) {
		this.usdBalance = usdBalance;
	}
}
