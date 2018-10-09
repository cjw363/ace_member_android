package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class BillerBean extends BaseBean {
	private int id;
	@SerializedName("partner_id")
	private int partnerId;
	private String code;
	private String name;
	private String phone;
	private String title;
	private int type;
	private String currency;
	@SerializedName("max_amount")
	private int amount;
	@SerializedName("bill_id_title")
	private String billIDTitle;
	@SerializedName("bill_id_length")
	private String billLength;

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public void setPartnerID(int partner_id) {
		this.partnerId = partner_id;
	}

	public int getPartnerID() {
		return this.partnerId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setAmount(int amount) {
		amount = amount;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setBillLength(String value){
		this.billLength=value;
	}
	public String getBillLength(){
		return this.billLength;
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
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setBillTitle(String title){
		this.billIDTitle=title;
	}
	public String getBillTitle(){
		return billIDTitle;
	}
}
