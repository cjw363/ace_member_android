package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class BillPaymentBean  extends BaseBean{
	private int id;
	@SerializedName("partner_bill_id")
	private String number;
	private String currency;
	private double amount;
	private double fee;
//	private int status;
	private String time;

	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return this.id;
	}
	public void setNumber(String number){
		this.number=number;
	}
	public String getNumber(){
		return number;
	}
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
		return amount;
	}
	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return time;
	}
	public void setFee(double fee){
		this.fee=fee;
	}
	public double getFee(){
		return fee;
	}
}
