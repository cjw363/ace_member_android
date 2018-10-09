package com.ace.member.main.home.transfer.to_partner;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Partner;
import com.ace.member.bean.WithdrawConfig;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ToPartnerData extends BaseBean {

	@SerializedName("balance_list")
	private List<Balance> mBalance;

	@SerializedName("function_member_to_partner")
	private boolean functionToPartnerRunning;

	@SerializedName("withdraw_config")
	private WithdrawConfig withdrawConfig;

	@SerializedName("exchange_fee")
	private JsonObject exchangeFee;

	@SerializedName("partner_info")
	private Partner mPartner;

	public List<Balance> getBalance() {
		return mBalance;
	}

	public void setBalance(List<Balance> mBalance) {
		this.mBalance = mBalance;
	}

	public boolean isFunctionToPartnerRunning() {
		return functionToPartnerRunning;
	}

	public void setFunctionToPartnerRunning(boolean functionPartnerRunning) {
		this.functionToPartnerRunning = functionPartnerRunning;
	}

	public WithdrawConfig getWithdrawConfig() {
		return withdrawConfig == null ? new WithdrawConfig() : withdrawConfig;
	}

	public void setWithdrawConfig(WithdrawConfig withdrawConfig) {
		this.withdrawConfig = withdrawConfig;
	}

	public JsonObject getExchangeFee() {
		return exchangeFee;
	}

	public void setExchangeFee(JsonObject exchangeFee) {
		this.exchangeFee = exchangeFee;
	}

	public Partner getPartner() {
		return mPartner;
	}

	public void setPartner(Partner mPartner) {
		this.mPartner = mPartner;
	}
}
