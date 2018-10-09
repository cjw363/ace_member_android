package com.ace.member.main.home.transfer.to_member;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ToMemberData extends BaseBean {
	private String time;

	@SerializedName("transfer_id")
	private int transferID;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTransferID() {
		return transferID;
	}

	public void setTransferID(int transferID) {
		this.transferID = transferID;
	}
}
