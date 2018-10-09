package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class PartnerBiller extends BaseBean {
	private String id;
	@SerializedName("partner_id")
	private String partnerId;
	@SerializedName("partner_name")
	private String name;
	private String title;
	private String type;
	private String currency;
	@SerializedName("max_amount")
	private int amount;

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	public void setPartnerID(String partner_id) {
		this.partnerId = partner_id;
	}

	public String getPartnerID() {
		return this.partnerId;
	}

	public void setPartnerName(String name) {
		this.name = name;
	}

	public String getPartnerName() {
		return this.name;
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

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setMaxAmount(int amount) {
		amount = amount;
	}

	public int getMaxAmount() {
		return this.amount;
	}
}
