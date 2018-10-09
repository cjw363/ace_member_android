package com.ace.member.main.home.money.withdraw_cash;

import com.ace.member.base.BaseBean;

public class WithdrawCashResultDetail extends BaseBean {

	private String currency;

	private double amount;

	private double fee;

	private String time;

	private String id;

	private int status;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return Math.abs(amount);
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getFee() {
		return Math.abs(fee);
	}

	public void setFee(double fee) {
		this.fee = fee;
	}
}
