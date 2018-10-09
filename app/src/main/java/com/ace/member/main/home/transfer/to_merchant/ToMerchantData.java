package com.ace.member.main.home.transfer.to_merchant;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Merchant;
import com.ace.member.bean.WithdrawConfig;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ToMerchantData extends BaseBean {

	@SerializedName("balance_list")
	private List<Balance> mBalance;

	@SerializedName("function_member_to_merchant")
	private boolean functionToMerchantRunning;

	@SerializedName("withdraw_config")
	private WithdrawConfig withdrawConfig;

	@SerializedName("exchange_fee")
	private JsonObject exchangeFee;

	@SerializedName("merchant_info")
	private Merchant mMerchant;

	public List<Balance> getBalance() {
		return mBalance;
	}

	public void setBalance(List<Balance> mBalance) {
		this.mBalance = mBalance;
	}

	public boolean isFunctionToMerchantRunning() {
		return functionToMerchantRunning;
	}

	public void setFunctionToMerchantRunning(boolean functionMerchantRunning) {
		this.functionToMerchantRunning = functionMerchantRunning;
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

	public Merchant getMerchant() {
		return mMerchant;
	}

	public void setMerchant(Merchant mMerchant) {
		this.mMerchant = mMerchant;
	}
}
