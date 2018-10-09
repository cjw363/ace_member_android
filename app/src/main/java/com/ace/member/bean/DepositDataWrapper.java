package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class DepositDataWrapper extends BaseBean {

	@SerializedName("company_bank_list")
	private List<BankAccount> companyBanks;

	@SerializedName("member_bank_list")
	private List<BankAccount> memberBanks;

	@SerializedName("day_amount")
	private double dayAmount;

	@SerializedName("total_balance")
	private double totalBalance;

	@SerializedName("pending_amount")
	private double pendingAmount;

	@SerializedName("function_deposit")
	private boolean isFunctionDeposit;

	public List<BankAccount> getCompanyBanks() {
		return companyBanks==null?new ArrayList<BankAccount>():companyBanks;
	}

	public void setCompanyBanks(List<BankAccount> companyBanks) {
		this.companyBanks = companyBanks;
	}

	public List<BankAccount> getMemberBanks() {
		return memberBanks==null?new ArrayList<BankAccount>():memberBanks;
	}

	public void setMemberBanks(List<BankAccount> memberBanks) {
		this.memberBanks = memberBanks;
	}

	public double getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(double dayAmount) {
		this.dayAmount = dayAmount;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public double getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(double pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public boolean isFunctionDeposit() {
		return isFunctionDeposit;
	}

	public void setFunctionDeposit(boolean functionDeposit) {
		isFunctionDeposit = functionDeposit;
	}
}
