package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class InfoForTransferToMember extends BaseBean {

	@SerializedName("function_member_to_member")
	private boolean functionToMemberRunning;

	@SerializedName("balance_list")
	private List<Balance> mBalance;

	public boolean isFunctionToMemberRunning() {
		return functionToMemberRunning;
	}

	public void setFunctionToMemberRunning(boolean functionToMemberRunning) {
		this.functionToMemberRunning = functionToMemberRunning;
	}

	public List<Balance> getBalance() {
		return mBalance;
	}

	public void setBalance(List<Balance> balance) {
		mBalance = balance;
	}
}
