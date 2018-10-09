package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ACELoanFail extends BaseBean {

	@SerializedName("amount")
	public double amount;
	@SerializedName("max_amount")
	public double maxAmount;

	public void setAmount(double amount){
		this.amount=amount;
	}
	public double getAmount(){
		return amount;
	}
	public void setMaxAmount(double amount){
		this.maxAmount=amount;
	}
	public double getMaxAmount(){
		return maxAmount;
	}
}
