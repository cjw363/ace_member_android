package com.ace.member.main.home.transfer.to_non_member;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToNonMemberData {

	@SerializedName("function_member_to_non_member")
	private boolean functionToNonMemberRunning;

	@SerializedName("config_list")
	private List<TransferCashConfigList> list;

	public boolean isFunctionToNonMemberRunning() {
		return functionToNonMemberRunning;
	}

	public void setFunctionToNonMemberRunning(boolean functionToNonMemberRunning) {
		this.functionToNonMemberRunning = functionToNonMemberRunning;
	}
	public void setConfigList(List<TransferCashConfigList> list){
		this.list=list;
	}

	public List<TransferCashConfigList> getConfigList() {
		return list;
	}

}
