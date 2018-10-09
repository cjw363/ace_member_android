package com.ace.member.bean;

import com.ace.member.base.BaseBean;


public class Balance extends BaseBean {

	/**
	 * currency : KHR
	 * amount : 99.99
	 */

	private String currency;
	private double amount;

	public String getCurrency() { return currency;}

	public void setCurrency(String currency) { this.currency = currency;}

	public double getAmount() { return amount;}

	public void setAmount(double amount) { this.amount = amount;}

	@Override
	public String toString() {
		return "Balance{" + "currency='" + currency + '\'' + ", amount=" + amount + '}';
	}
}
