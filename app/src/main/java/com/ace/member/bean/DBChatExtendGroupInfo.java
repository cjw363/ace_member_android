package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBChatExtendGroupInfo extends BaseBean {
	@SerializedName("chat_id")
	private int chatId;
	@SerializedName("group_name")
	private String groupName;
	@SerializedName("owner_id")
	private int ownerId;
	@SerializedName("flag_verify_invitation")
	private int flagVerifyInvitation;
	@SerializedName("group_notice")
	private String groupNotice;
	private String lmt;

	public String getLmt() {
		return lmt;
	}

	public void setLmt(String lmt) {
		this.lmt = lmt;
	}

	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getFlagVerifyInvitation() {
		return flagVerifyInvitation;
	}

	public void setFlagVerifyInvitation(int flagVerifyInvitation) {
		this.flagVerifyInvitation = flagVerifyInvitation;
	}

	public String getGroupNotice() {
		return groupNotice;
	}

	public void setGroupNotice(String groupNotice) {
		this.groupNotice = groupNotice;
	}
}
