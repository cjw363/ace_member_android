package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class TowResultBean extends BaseBean {
	@SerializedName("min_amount")
	private double minAmount;
	@SerializedName("max_amount")
	private double maxAmount;

	public void setMinAmount(double amount){
		minAmount=amount;
	}
	public double getMinAmount(){
		return minAmount;
	}
	public void setMaxAmount(double amount){
		maxAmount=amount;
	}
	public double getMaxAmount(){
		return maxAmount;
	}
}
