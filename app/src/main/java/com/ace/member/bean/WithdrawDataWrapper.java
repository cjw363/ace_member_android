package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class WithdrawDataWrapper extends BaseBean {

	@SerializedName("company_bank_list")
	private List<BankAccount> companyBanks;

	@SerializedName("member_bank_list")
	private List<BankAccount> memberBanks;

	@SerializedName("bank_config_list")
	private List<BankConfig> mBankConfigs;

	@SerializedName("bank_withdraw_amount_list")
	private List<BankWithdrawAmount> bankWithdrawAmounts;

	@SerializedName("withdraw_config")
	private WithdrawConfig withdrawConfig;

	@SerializedName("exchange_info")
	private Currency exchangeInfo;

	@SerializedName("day_amount")
	private double dayAmount;

	@SerializedName("function_withdraw")
	private boolean isFunctionWithdraw;

	public List<BankAccount> getCompanyBanks() {
		return companyBanks == null ? new ArrayList<BankAccount>() : companyBanks;
	}

	public void setCompanyBanks(List<BankAccount> companyBanks) {
		this.companyBanks = companyBanks;
	}

	public List<BankAccount> getMemberBanks() {
		return memberBanks == null ? new ArrayList<BankAccount>() : memberBanks;
	}

	public void setMemberBanks(List<BankAccount> memberBanks) {
		this.memberBanks = memberBanks;
	}

	public List<BankConfig> getBankConfigs() {
		return mBankConfigs == null ? new ArrayList<BankConfig>() : mBankConfigs;
	}

	public void setBankConfigs(List<BankConfig> bankConfigs) {
		this.mBankConfigs = bankConfigs;
	}

	public List<BankWithdrawAmount> getBankWithdrawAmounts() {
		return bankWithdrawAmounts==null?new ArrayList<BankWithdrawAmount>():bankWithdrawAmounts;
	}

	public void setBankWithdrawAmounts(List<BankWithdrawAmount> bankWithdrawAmounts) {
		this.bankWithdrawAmounts = bankWithdrawAmounts;
	}

	public WithdrawConfig getWithdrawConfig() {
		return withdrawConfig == null ? new WithdrawConfig() : withdrawConfig;
	}

	public void setWithdrawConfig(WithdrawConfig withdrawConfig) {
		this.withdrawConfig = withdrawConfig;
	}

	public Currency getExchangeInfo() {
		return exchangeInfo == null ? new Currency() : exchangeInfo;
	}

	public void setExchangeInfo(Currency exchangeInfo) {
		this.exchangeInfo = exchangeInfo;
	}

	public double getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(double dayAmount) {
		this.dayAmount = dayAmount;
	}

	public boolean isFunctionWithdraw() {
		return isFunctionWithdraw;
	}

	public void setFunctionWithdraw(boolean functionWithdraw) {
		isFunctionWithdraw = functionWithdraw;
	}
}
