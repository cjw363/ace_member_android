package com.ace.member.bean;


import com.google.gson.annotations.SerializedName;

public class WithdrawLimitConfig extends LimitConfig {
	@SerializedName("max_withdraw_per_time")
	private int maxWithdrawPerTime;

	@SerializedName("max_withdraw_per_day")
	private int maxWithdrawPerDay;

	public int getMaxWithdrawPerTime() {
		return maxWithdrawPerTime;
	}

	public void setMaxWithdrawPerTime(int maxWithdrawPerTime) {
		this.maxWithdrawPerTime = maxWithdrawPerTime;
	}

	public int getMaxWithdrawPerDay() {
		return maxWithdrawPerDay;
	}

	public void setMaxWithdrawPerDay(int maxWithdrawPerDay) {
		this.maxWithdrawPerDay = maxWithdrawPerDay;
	}
}
