package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class PaymentHistoryBean extends BaseBean {
	private int id;
	private String time;
	private String date;
	private double amount;
	private double fee;
	private String currency;
	private int status;
	private int type;
	private boolean isHeader=false;

	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return id;
	}
	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return this.time;
	}
	public void setAmount(double amount){
		this.amount=amount;
	}
	public double getAmount(){
		return  amount;
	}
	public void setType(int type){
		this.type=type;
	}
	public int getType(){
		return this.type;
	}
	public void setFee(double fee){
		this.fee=fee;
	}
	public double getFee(){
		return this.fee;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public String getCurrency(){
		return this.currency;
	}
	public void setStatus(int status){
		this.status=status;
	}
	public int getStatus(){
		return status;
	}

	public void setDate(String date){
		this.date=date;
	}
	public String getDate(){
		return this.date;
	}
	public void setHeader(boolean isHeader){
		this.isHeader=isHeader;
	}
	public boolean getHeader(){
		return this.isHeader;
	}
}
