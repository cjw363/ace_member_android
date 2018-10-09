package com.ace.member.bean;


import com.google.gson.annotations.SerializedName;

public class TransferLimitConfig extends LimitConfig {
	@SerializedName("max_transfer_per_time")
	private int maxTransferPerTime;

	@SerializedName("max_transfer_per_day")
	private double maxTransferPerDay;

	@SerializedName("day_amount")
	private double dayAmount;

	public int getmaxTransferPerTime() {
		return maxTransferPerTime;
	}

	public void setmaxTransferPerTime(int maxTransferPerTime) {
		this.maxTransferPerTime = maxTransferPerTime;
	}

	public double getmaxTransferPerDay() {
		return maxTransferPerDay;
	}

	public void setmaxTransferPerDay(int maxTransferPerDay) {
		this.maxTransferPerDay = maxTransferPerDay;
	}

	public double getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(double dayAmount) {
		this.dayAmount = dayAmount;
	}
}
