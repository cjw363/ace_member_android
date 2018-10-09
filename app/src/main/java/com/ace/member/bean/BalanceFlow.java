package com.ace.member.bean;

import com.ace.member.base.BaseBean;


public class BalanceFlow extends BaseBean {
	private int type;
	private String time;
	private double amount;
	private double runningBalance;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getRunningBalance() {
		return runningBalance;
	}

	public void setRunningBalance(double runningBalance) {
		this.runningBalance = runningBalance;
	}
}
