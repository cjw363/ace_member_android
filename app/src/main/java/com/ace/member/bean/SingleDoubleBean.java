package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SingleDoubleBean extends BaseBean {

	@SerializedName("amount")
	private double amount;
	public void setAmount(double amount){
		this.amount=amount;
	}
	public double getAmount(){
		return  amount;
	}
}
