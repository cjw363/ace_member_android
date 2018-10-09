package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class CashApplication extends BaseBean {

	/**
	 * id : 1
	 * time : 2017-07-12 16:30:54
	 * user_type : 1
	 * user_id : 14
	 * user_input_type : 2
	 * type : 3
	 * currency : USD
	 * amount : 520.00
	 * transaction_fee : 1.00
	 * agent_commission : 1.00
	 * source_phone : +86-31243243324
	 * status : 1
	 * user_name : user_name
	 * user_phone : +855-12312455
	 * remark : test
	 */

	private int id;
	private String time;
	@SerializedName("user_type")
	private int userType;
	@SerializedName("user_id")
	private int userId;
	@SerializedName("user_input_type")
	private int userInputType;
	private int type;
	private String currency;
	private String amount;
	@SerializedName("transaction_fee")
	private String transactionFee;
	@SerializedName(value = "agent_commission", alternate = {"commission"})
	private String agentCommission;
	@SerializedName("source_type")
	private int sourceType;
	@SerializedName("source_id")
	private String sourceId;
	private int status;
	@SerializedName("user_name")
	private String userName;
	@SerializedName("user_phone")
	private String userPhone;
	private String remark;
	@SerializedName("source_phone")
	private String sourcePhone;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserInputType() {
		return userInputType;
	}

	public void setUserInputType(int userInputType) {
		this.userInputType = userInputType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(String transactionFee) {
		this.transactionFee = transactionFee;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAgentCommission() {
		return agentCommission;
	}

	public void setAgentCommission(String agentCommission) {
		this.agentCommission = agentCommission;
	}

	public String getSourcePhone() {
		return sourcePhone;
	}

	public void setSourcePhone(String sourcePhone) {
		this.sourcePhone = sourcePhone;
	}
}
