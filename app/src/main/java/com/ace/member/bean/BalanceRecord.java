package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class BalanceRecord extends BaseBean {
	private int id;
	private int type;
	private String amount;
	private String currency;

	@SerializedName("transaction_fee")
	private String transactionFee;

	@SerializedName("bank_fee")
	private String bankFee;

	@SerializedName("bank_code")
	private String bankCode;

	@SerializedName("bank_account_no")
	private String bankAccountNo;

	@SerializedName("bank_account_name")
	private String bankAccountName;

	@SerializedName("company_bank_account_no")
	private String companyBankAccountNo;

	@SerializedName("remark_applicant")
	private String remark;

	private String time;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(String transactionFee) {
		this.transactionFee = transactionFee;
	}

	public String getBankFee() {
		return bankFee;
	}

	public void setBankFee(String bankFee) {
		this.bankFee = bankFee;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getCompanyBankAccountNo() {
		return companyBankAccountNo;
	}

	public void setCompanyBankAccountNo(String companyBankAccountNo) {
		this.companyBankAccountNo = companyBankAccountNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
