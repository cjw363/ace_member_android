package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class Currency extends BaseBean {
	private String currency;
	private double exchange;
	@SerializedName("exchange_fee")
	private double exchangeFee;
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public String getCurrency(){
		return currency;
	}
	public void setExchange(double exchange){
		this.exchange=exchange;
	}
	public double getExchange(){
		return this.exchange;
	}
	public void setExchangeFee(double exchange){
		this.exchangeFee=exchange;
	}
	public double getExchangeFee(){
		return this.exchangeFee;
	}
}
