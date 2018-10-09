package com.ace.member.main.third_party.bill_payment.history_detail;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class BillPaymentDetailBean extends BaseBean {
//	private  int type;
	private String title;
	private String code;
	private String name;
	private String time;
	private String phone;
	@SerializedName("partner_bill_id")
	private String billID;
	@SerializedName("bill_id_title")
	private String billIDTitle;
	private String currency;
	private double amount;
	private double fee;
	private String remark;
//	public void setType(int type){
//		this.type=type;
//	}
//	public int getType(){
//		return type;
//	}
	public void setTitle(String title){
		this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setCode(String code){
		this.code=code;
	}
	public String getCode(){
		return code;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return time;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public String getCurrency(){
		return currency;
	}
	public void setAmount(double amount){
		this.amount=amount;
	}
	public double getAmount(){
		return amount;
	}
	public void setFee(double fee){
		this.fee=fee;
	}
	public double getFee(){
		return fee;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
	public void setBillID(String billID){
		this.billID=billID;
	}
	public String getBillID(){
		return billID;
	}
	public void setBillIDTitle(String title){
		this.billIDTitle=title;
	}
	public String getBillIDTitle(){
		return billIDTitle;
	}
}
