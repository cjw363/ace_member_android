package com.ace.member.main.home.salary_loan.return_loan;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SalaryLoanReturnWrapper extends BaseBean {
	@SerializedName("usd_balance")
	private double usdBalance;
	@SerializedName("current_loan")
	private double currentLoan;

	public double getUsdBalance() {
		return usdBalance;
	}

	public void setUsdBalance(double usdBalance) {
		this.usdBalance = usdBalance;
	}

	public double getCurrentLoan() {
		return currentLoan;
	}

	public void setCurrentLoan(double currentLoan) {
		this.currentLoan = currentLoan;
	}
}
