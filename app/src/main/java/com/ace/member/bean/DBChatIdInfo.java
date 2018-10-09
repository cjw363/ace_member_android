package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBChatIdInfo extends BaseBean {
	private int id;
	private String time;
	@SerializedName("chat_type")
	private int chatType;
	@SerializedName("time_last_member_join")
	private String timeLastMemberJoin;
	@SerializedName("time_last_message")
	private String timeLastMessage;
	private String lmt;

	public int getChatType() {
		return chatType;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

	public String getLmt() {
		return lmt;
	}

	public void setLmt(String lmt) {
		this.lmt = lmt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeLastMemberJoin() {
		return timeLastMemberJoin;
	}

	public void setTimeLastMemberJoin(String timeLastMemberJoin) {
		this.timeLastMemberJoin = timeLastMemberJoin;
	}

	public String getTimeLastMessage() {
		return timeLastMessage;
	}

	public void setTimeLastMessage(String timeLastMessage) {
		this.timeLastMessage = timeLastMessage;
	}
}
