package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ACELoanDetailBean extends BaseBean {

	private int id;
	private String time;
	private String currency;
	private double loan;
	@SerializedName("loan_date")
	private String loanDate;
	private int term;
	private int status;
	private boolean isBatch = false;
	private boolean isCheck = false;
	@SerializedName("interest_amount")
	private double interest;

	@SerializedName("actual_interest_amount")
	private double actualInterestAmount;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setLoan(double loan) {
		this.loan = loan;
	}

	public double getLoan() {
		return loan;
	}

	public void setLoanDate(String date) {
		this.loanDate = date;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getTerm() {
		return term;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setBatch(boolean batch) {
		this.isBatch = batch;
	}

	public boolean getBatch() {
		return isBatch;
	}

	public void setCheck(boolean check) {
		isCheck = check;
	}

	public boolean getCheck() {
		return isCheck;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getInterest() {
		return this.interest;
	}

	public void setActualInterestAmount(double interest){
		actualInterestAmount=interest;
	}
	public double getActualInterestAmount(){
		return actualInterestAmount;
	}

}
