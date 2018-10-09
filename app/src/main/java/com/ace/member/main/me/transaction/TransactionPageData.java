package com.ace.member.main.me.transaction;


import com.ace.member.bean.PageBaseBean;
import com.ace.member.bean.Transaction;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class TransactionPageData extends PageBaseBean<Transaction> {

	@SerializedName("total_amount")
	private Map<String,String> totalAmountObj;

	public Map<String,String> getTotalAmountObj() {
		return totalAmountObj;
	}

	public void setTotalAmountObj(Map<String,String> totalAmountObj) {
		this.totalAmountObj = totalAmountObj;
	}
}
