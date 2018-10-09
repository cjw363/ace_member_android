package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MemberLoanPartnerBill extends BaseBean {
	@SerializedName("partner_id")
	private int partnerId;
	private String date;
	@SerializedName("due_day")
	private int dueDay;
	private double amount;

	@SerializedName("paid_amount")
	private double paidAmount;

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDueDay() {
		return dueDay;
	}

	public void setDueDay(int dueDay) {
		this.dueDay = dueDay;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getDueDate() {
		return date.substring(0, date.length() - 2) + "0" + dueDay;
	}

	public boolean isOverDue() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		try {
			return now.after(dateFormat.parse(getDueDate()));
		} catch (ParseException e) {
			e.printStackTrace();
			return true;
		}
	}

	public boolean isPayUp(){
		return paidAmount>=amount;
	}
}
