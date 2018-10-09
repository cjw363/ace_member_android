package com.ace.member.main.me.exchange;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Currency;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExchangeBean extends BaseBean{
	@SerializedName("balance")
	private List<Balance> list;
	@SerializedName("currency")
	private List<Currency> list1;
	@SerializedName("function")
	private boolean isRunning;
	private void setBalanceList(List<Balance> list){
		this.list=list;
	}
	public List<Balance> getBalanceList(){
		return list;
	}
	public void setExchangeList(List<Currency> list){
		this.list1=list;
	}
	public List<Currency> getExchangeList(){
		return list1;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
	}
}
