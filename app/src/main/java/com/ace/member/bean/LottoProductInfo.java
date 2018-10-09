package com.ace.member.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LottoProductInfo extends LoanBean {
	private Product product;
	@SerializedName("balance")
	private List<Balance> balance;
	@SerializedName("bet_amount")
	private double betAmount;
	@SerializedName("max_number")
	private int maxNumber;
	@SerializedName("max_times")
	private int maxTimes;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Balance> getBalance() {
		return balance;
	}

	public void setBalance(List<Balance> balance) {
		this.balance = balance;
	}

	public double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(double betAmount) {
		this.betAmount = betAmount;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	public int getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}
}
