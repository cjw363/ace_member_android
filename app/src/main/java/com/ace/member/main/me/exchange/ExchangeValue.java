package com.ace.member.main.me.exchange;


import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExchangeValue extends BaseBean {

	@SerializedName("detail")
	private ExchangeResultBead detail;

	@SerializedName("balance")
	private List<Balance> balanceList;

	private void setDetail(ExchangeResultBead bead){
		detail=bead;
	}
	public ExchangeResultBead getDetail(){
		return detail;
	}

	private void setBalanceList(List<Balance> list){
		this.balanceList=list;
	}
	public List<Balance> getBalanceList(){
		return balanceList;
	}
}
