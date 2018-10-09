package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class BillerConfig extends BaseBean {
	private String currency;
	@SerializedName("split_amount")
	private double amount;
	@SerializedName("type_fee_and_commission")
	private int type;
	@SerializedName("fee_percent")
	private double percent;
	private double fee;

	public void setCurrency(String currency){
		this.currency=currency;
	}
	public String getCurrency(){
		return currency;
	}
	public void setAmount(double amount){
		this.amount=amount;
	}
	public double getAmount(){
		return this.amount;
	}
	public void setType(int type){
		this.type=type;
	}
	public int getType(){
		return this.type;
	}
	public void setPercent(double percent){
		this.percent=percent;
	}
	public double getPercent(){
		return this.percent;
	}
	public void setFee(double fee){
		this.fee=fee;
	}
	public double getFee(){
		return this.fee;
	}
}
