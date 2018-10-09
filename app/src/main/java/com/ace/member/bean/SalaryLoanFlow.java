package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SalaryLoanFlow extends BaseBean {

	/**
	 * id : 1
	 * time : 2017-09-28 16:30:24
	 * type : 2
	 * currency : USD
	 * amount : 100
	 */

	private int id;
	private String time;
	private int type;
	private String currency;
	private String amount;
	private String BalanceDue;
	@SerializedName("service_charge")
	private String serviceCharge;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() { return time;}

	public void setTime(String time) { this.time = time;}

	public int getType() { return type;}

	public void setType(int type) { this.type = type;}

	public String getCurrency() { return currency;}

	public void setCurrency(String currency) { this.currency = currency;}

	public String getAmount() { return amount;}

	public void setAmount(String amount) { this.amount = amount;}

	public String getBalanceDue() {
		return BalanceDue;
	}

	public void setBalanceDue(String balanceDue) {
		BalanceDue = balanceDue;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
}
