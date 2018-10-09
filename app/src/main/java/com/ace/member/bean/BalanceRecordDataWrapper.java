package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class BalanceRecordDataWrapper extends BaseBean {
	@SerializedName("balance")
	private Balance balance;

	@SerializedName("record_list")
	private List<BalanceRecord> recordList;

	@SerializedName("function_deposit")
	private boolean isFunctionDeposit;

	@SerializedName("function_withdraw")
	private boolean isFunctionWithdraw;

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public List<BalanceRecord> getRecordList() {
		return recordList==null?new ArrayList<BalanceRecord>():recordList;
	}

	public void setRecordList(List<BalanceRecord> recordList) {
		this.recordList = recordList;
	}

	public boolean isFunctionDeposit() {
		return isFunctionDeposit;
	}

	public void setFunctionDeposit(boolean functionDeposit) {
		isFunctionDeposit = functionDeposit;
	}

	public boolean isFunctionWithdraw() {
		return isFunctionWithdraw;
	}

	public void setFunctionWithdraw(boolean functionWithdraw) {
		isFunctionWithdraw = functionWithdraw;
	}
}
