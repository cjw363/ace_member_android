package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SalaryLoanBean extends BaseBean {

	/**
	 * currency : USD
	 * credit : 1000
	 * loan : 0
	 * due_date_initial : 0000-00-00
	 * due_date : 0000-00-00
	 */

	private String currency;
	private double credit;
	private double loan;
	@SerializedName("due_date_initial")
	private String dueDateInitial;
	@SerializedName("due_date")
	private String dueDate;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getLoan() {
		return loan;
	}

	public void setLoan(double loan) {
		this.loan = loan;
	}

	public String getDueDateInitial() {
		return dueDateInitial;
	}

	public void setDueDateInitial(String dueDateInitial) {
		this.dueDateInitial = dueDateInitial;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
}
