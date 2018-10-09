package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class ChatMsg extends BaseBean {
	private int id;
	private String content;
	private String name;
	private int memberId;
	private int contentType;
	private int transferId;
	private String portrait;
	private String currency;
	private double amount;
	private String time;

	public ChatMsg() {}

	public ChatMsg(int id, String content, int memberId, int contentType, String time) {
		this.id = id;
		this.content = content;
		this.memberId = memberId;
		this.contentType = contentType;
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ChatMsg{" + "id=" + id + ", content='" + content + '\'' + ", name='" + name + '\'' + ", memberId=" + memberId + ", contentType=" + contentType + ", transferId=" + transferId + ", portrait='" + portrait + '\'' + ", currency='" + currency + '\'' + ", amount=" + amount + '}';
	}
}
