package com.ace.member.bean;


import com.google.gson.annotations.SerializedName;

public class DepositLimitConfig extends LimitConfig{
	@SerializedName("max_deposit_per_time")
	private int maxDepositPerTime;

	@SerializedName("max_deposit_per_day")
	private int maxDepositPerDay;

	public int getMaxDepositPerTime() {
		return maxDepositPerTime;
	}

	public void setMaxDepositPerTime(int maxDepositPerTime) {
		this.maxDepositPerTime = maxDepositPerTime;
	}

	public int getMaxDepositPerDay() {
		return maxDepositPerDay;
	}

	public void setMaxDepositPerDay(int maxDepositPerDay) {
		this.maxDepositPerDay = maxDepositPerDay;
	}
}
