package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SocketServers extends BaseBean {

	@SerializedName("receive_money")
	private String receiveMoney;
	@SerializedName("cash_deposit")
	private String cashDeposit;
	@SerializedName("cash_withdraw")
	private String cashWithdraw;


	@SerializedName("im")
	private String im;//

	public String getReceiveMoney() {
		return receiveMoney;
	}

	public void setReceiveMoney(String receiveMoney) {
		this.receiveMoney = receiveMoney;
	}

	public String getCashDeposit() {
		return cashDeposit;
	}

	public void setCashDeposit(String cashDeposit) {
		this.cashDeposit = cashDeposit;
	}

	public String getCashWithdraw() {
		return cashWithdraw;
	}

	public void setCashWithdraw(String cashWithdraw) {
		this.cashWithdraw = cashWithdraw;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}
}
