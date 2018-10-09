package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LottoResult extends BaseBean {

	private String currency;
	private double accepted;
	@SerializedName("balance")
	private List<Balance> balance;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAccepted() {
		return accepted;
	}

	public void setAccepted(double accepted) {
		this.accepted = accepted;
	}

	public List<Balance> getBalance() {
		return balance;
	}

	public void setBalance(List<Balance> balance) {
		this.balance = balance;
	}
}
