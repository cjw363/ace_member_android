package com.ace.member.main.home.transfer.to_non_member;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.TransferCashConfig;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransferCashConfigList extends BaseBean{
	private String currency;
	@SerializedName("config")
	private List<TransferCashConfig> list;

	public void setCurrency(String currency){
		this.currency=currency;
	}
	public String getCurrency(){
		return currency;
	}
	public void setList(List<TransferCashConfig> list){
		this.list=list;
	}
	public List<TransferCashConfig> getList(){
		return list;
	}
}
