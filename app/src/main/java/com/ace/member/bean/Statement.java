package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class Statement extends BaseBean {

	/**
	 * date : 2017-05-26
	 * agent_id : 9
	 * type : 3
	 * currency : USD
	 * amount : 6000.00
	 */
	private String date;
	@SerializedName("agent_id")
	private String agentID;
	private String type;
	@SerializedName("sub_type")
	private String subType;
	private String currency;
	private String amount;
	private String balance;
	private boolean flagSameDate;
	private boolean isDateTitle = false;
	private boolean isDateEnd = false;

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public boolean isDateTitle() {
		return isDateTitle;
	}

	public void setDateTitle(boolean dateTitle) {
		isDateTitle = dateTitle;
	}

	public boolean isFlagSameDate() {
		return flagSameDate;
	}

	public void setFlagSameDate(boolean flagSameDate) {
		this.flagSameDate = flagSameDate;
	}

	public String getDate() { return date;}

	public void setDate(String date) { this.date = date;}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public String getType() { return type;}

	public void setType(String type) { this.type = type;}

	public String getCurrency() { return currency;}

	public void setCurrency(String currency) { this.currency = currency;}

	public String getAmount() { return amount;}

	public void setAmount(String amount) { this.amount = amount;}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public boolean isDateEnd() {
		return isDateEnd;
	}

	public void setDateEnd(boolean dateEnd) {
		isDateEnd = dateEnd;
	}

}
