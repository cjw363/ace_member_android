package com.ace.member.main.me.password;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class PasswordConfigBean extends BaseBean {
	@SerializedName("status_trading_password")
	private int tradingPasswordStatus;
	@SerializedName("status")
	private int userVerifyStatus;
	private String remark;

	public void setTradingPasswordStatus(int status){
		this.tradingPasswordStatus=status;
	}
	public int getTradingPasswordStatus(){
		return tradingPasswordStatus;
	}
	public void setUserVerifyStatus(int status){
		this.userVerifyStatus=status;
	}
	public int getUserVerifyStatus(){
		return userVerifyStatus;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}

}
